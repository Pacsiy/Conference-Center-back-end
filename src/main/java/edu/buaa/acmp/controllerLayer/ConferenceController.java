package edu.buaa.acmp.controllerLayer;

import edu.buaa.acmp.controllerLayer.utils.AESToken;
import edu.buaa.acmp.dataAccessLayer.domain.Conference;
import edu.buaa.acmp.dataAccessLayer.domain.ConferenceRegister;
import edu.buaa.acmp.dataAccessLayer.domain.Contribution;
import edu.buaa.acmp.dataAccessLayer.domain.Subject;
import edu.buaa.acmp.dataAccessLayer.domain.Participant;
import edu.buaa.acmp.serviceLayer.ConferenceService;
import edu.buaa.acmp.serviceLayer.SubjectService;
import edu.buaa.acmp.util.*;
import edu.buaa.acmp.util.JSON;
import edu.buaa.acmp.util.RetData;
import edu.buaa.acmp.controllerLayer.utils.R;
import edu.buaa.acmp.util.TimeConversion;
import org.json.JSONArray;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ConferenceController {
    private final ConferenceService conferenceService;
    private final SubjectService subjectService;

    public ConferenceController(ConferenceService conferenceService, SubjectService subjectService) {
        this.conferenceService = conferenceService;
        this.subjectService = subjectService;
    }

    /**
     * 获得某篇会议的详细信息
     */
    @RequestMapping(value = "conference/{id}")
    public String getConferenceDetail(HttpSession session, @PathVariable("id") String id) {
        RetData retData;
        try {
            Conference conference = conferenceService.getConferenceDetail(id);
            if (conference == null) throw new RuntimeException("not exist");
            JSON data = conference.toJson();
            Subject subject = subjectService.getConferenceSubject(conference.id.toString());
            data.put("field", subject.id);
            retData = new RetData(R.STATE_SUCC, "成功获取会议信息", data);
        } catch (Exception e) {
            e.printStackTrace();
            retData = new RetData(R.STATE_FAIL, "获取会议信息失败", null);
        }
        return retData.toString();
    }

    /**
     * 主办方发布会议
     */
    @RequestMapping(value = "postConference")
    public String postConference(@RequestBody String request, HttpSession session) {
        JSON req = new JSON(request);
        Conference conference = new Conference();
        RetData retData;
        try {
            String token = req.getString("token");
            JSON user = new JSON(new AESToken().decrypt(token));
//            if(user == null) throw new RuntimeException("未登录");
            if(!user.getString("type").equals("principal")) throw new RuntimeException("单位用户未登录");
            BigInteger idx = BigInteger.valueOf(Long.valueOf(user.getString("id")));
            conference.institution_id = BigInteger.valueOf(conferenceService.getInstitutionIDByPID(idx.toString()));
            conference.title = req.getString("title");
            conference.introduction = req.getString("introduction");
            conference.start_date = TimeConversion.toDateTime(req.getString("start_date"));
            conference.end_date = TimeConversion.toDateTime(req.getString("end_date"));
            conference.convening_place = req.getString("convening_place");
            conference.essay_information = req.getString("essay_information");
            conference.essay_instructions = req.getString("essay_instructions");
            conference.paper_ddl = TimeConversion.toDateTime(req.getString("paper_ddl"));
            conference.employ_date = TimeConversion.toDateTime(req.getString("employ_date"));
            conference.register_ddl = TimeConversion.toDateTime(req.getString("register_ddl"));
            conference.schedule = req.getString("schedule");
            conference.paper_template = req.getString("paper_template");
            conference.register_information = req.getString("register_information");
            conference.ATinformation = req.getString("ATinformation");
            conference.contact = req.getString("contact");
            conference.conference_template = req.getBigInteger("conference_template");
            conference.backimg = req.getString("backimg");
            conference.state = TimeConversion.getConferenceState(conference.start_date, conference.end_date,
                    conference.paper_ddl, conference.employ_date, conference.register_ddl);

            BigInteger field = req.getBigInteger("field");

            conferenceService.postConference(conference, field);
            retData = new RetData(R.STATE_SUCC, "成功发布会议", null);
        } catch (Exception e) {
            retData = new RetData(R.STATE_FAIL, "信息不完整，发布会议失败", null);
        }
        return retData.toString();
    }

    /**
     * 获得所有会议
     */
    @RequestMapping("manage/conferences")
    public String getAllConferences(HttpSession session,@RequestBody String req){
        RetData retData;
        try{
//            JSON user = (JSON) session.getAttribute(R.USER_SESSION);
//            if(user == null) throw new RuntimeException("未登录");
//            if(!user.getString("type").equals("principal")) throw new RuntimeException("单位用户未登录");
            String token = new JSON(req).getString("token");
            JSON user = new JSON(new AESToken().decrypt(token));
            Integer insID = conferenceService.getInstitutionIDByPID(user.getString("id"));
            List<Conference> conferences =
                    conferenceService.getAllConferencesByInstutionID(insID.toString());
            JSONArray data = new JSONArray();
            for(Conference conference:conferences){
                JSON temp = new JSON();
                temp.put("id",conference.id);
                temp.put("title",conference.title);
                temp.put("start_date",conference.start_date);
                temp.put("end_date",conference.end_date);
                Integer register = conferenceService.getRegisterNumberByConferenceID(conference.id);
                Integer paper = conferenceService.getPaperNumberByConferenceID(conference.id);
                temp.put("papers",paper);
                temp.put("registers",register);
                temp.put("backimg",conference.backimg);
                data.put(temp);
            }
            retData = new RetData(R.STATE_SUCC,"成功获取所有会议",data);
        }catch (Exception e){
            retData = new RetData(R.STATE_FAIL,"获取所有会议失败",null);
        }
        return retData.toString();
    }

    /**
     * 导出某片会议的投稿情况
     * @param id 会议ID
     */
    @RequestMapping("/conference/{id}/contributions/export")
    public String exportContributions(@PathVariable("id") String id){
        RetData retData;
        try {
            List<Contribution> contributions = conferenceService.getAllContributionByConferID(id);
            String path = ExcelUtils.exportContributions(contributions);
            String data = R.SERVER_ADDR+path;
            retData = new RetData(R.STATE_SUCC,"成功导出会议投稿",data);
        }catch (Exception e){
            e.printStackTrace();
            retData = new RetData(R.STATE_FAIL,"导出会议投稿失败",null);
        }
        return retData.toString();
    }

    /**
     * 导出某会议的注册情况
     */
    @RequestMapping("/conference/{id}/registers/export")
    public String exportRegisters(@PathVariable("id") String id){
        RetData retData;
        try {
            List<ConferenceRegister> registers = conferenceService.getRegisters(id);
            ArrayList<Participant> participants = new ArrayList<>();
            for(ConferenceRegister register:registers){
                participants.addAll(conferenceService.getParticipantsByRegisterID(register.id.toString()));
            }
            String path = ExcelUtils.exportRegisters(registers,participants);
            String data = R.SERVER_ADDR+path;
            retData = new RetData(R.STATE_SUCC,"成功导出注册名单",data);
        }catch (Exception e){
            e.printStackTrace();
            retData = new RetData(R.STATE_FAIL,"导出注册名单失败",null);
        }
        return retData.toString();
    }

    /**
     * 更新会议
     */
    @RequestMapping("conference/modify/{id}")
    public String updateConferInfo(@RequestBody String request, @PathVariable("id") String id){
        JSON req = new JSON(request);
        Conference conference = new Conference();
        RetData retData;
        try {
            String token = req.getString("token");
            JSON user = new JSON(new AESToken().decrypt(token));
            if(!user.getString("type").equals("principal")) throw new RuntimeException("单位用户未登录");
            BigInteger idx = BigInteger.valueOf(Long.valueOf(user.getString("id")));
            conference.id = new BigInteger(id);
            conference.institution_id = BigInteger.valueOf(conferenceService.getInstitutionIDByPID(idx.toString()));
            conference.title = req.getString("title");
            conference.introduction = req.getString("introduction");
            conference.start_date = TimeConversion.toDateTime(req.getString("start_date"));
            conference.end_date = TimeConversion.toDateTime(req.getString("end_date"));
            conference.convening_place = req.getString("convening_place");
            conference.essay_information = req.getString("essay_information");
            conference.essay_instructions = req.getString("essay_instructions");
            conference.paper_ddl = TimeConversion.toDateTime(req.getString("paper_ddl"));
            conference.employ_date = TimeConversion.toDateTime(req.getString("employ_date"));
            conference.register_ddl = TimeConversion.toDateTime(req.getString("register_ddl"));
            conference.schedule = req.getString("schedule");
            conference.paper_template = req.getString("paper_template");
            conference.register_information = req.getString("register_information");
            conference.ATinformation = req.getString("ATinformation");
            conference.contact = req.getString("contact");
            conference.backimg = req.getString("backimg");
            conference.conference_template = req.getBigInteger("conference_template");
            conference.state = TimeConversion.getConferenceState(conference.start_date, conference.end_date,
                    conference.paper_ddl, conference.employ_date, conference.register_ddl);

            BigInteger field = req.getBigInteger("field");

            conferenceService.updateConferInfo(conference, field);
            retData = new RetData(R.STATE_SUCC, "成功更新会议", null);
        } catch (Exception e) {
            e.printStackTrace();
            retData = new RetData(R.STATE_FAIL, "信息不完整，会议更新失败", null);
        }
        return retData.toString();
    }
}
