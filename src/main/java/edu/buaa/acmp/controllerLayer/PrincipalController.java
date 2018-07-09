package edu.buaa.acmp.controllerLayer;

import com.sendgrid.SendGrid;
import edu.buaa.acmp.controllerLayer.utils.AESToken;
import edu.buaa.acmp.controllerLayer.utils.R;
import edu.buaa.acmp.dataAccessLayer.domain.*;
import edu.buaa.acmp.serviceLayer.ConferenceService;
import edu.buaa.acmp.serviceLayer.MessageService;
import edu.buaa.acmp.serviceLayer.PrincipalService;
import edu.buaa.acmp.serviceLayer.UserService;
import edu.buaa.acmp.util.JSON;
import edu.buaa.acmp.util.MailServer;
import edu.buaa.acmp.util.PageDataWrapper;
import edu.buaa.acmp.util.RetData;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PrincipalController {

    private final PrincipalService principalService;
    private final ConferenceService conferenceService;
    private final UserService userService;
    private final SendGrid sendGrid;
    private final MessageService messageService;

    public PrincipalController(PrincipalService principalService, ConferenceService conferenceService, UserService userService, SendGrid sendGrid, MessageService messageService) {
        this.principalService = principalService;
        this.conferenceService = conferenceService;
        this.userService = userService;
        this.sendGrid = sendGrid;
        this.messageService = messageService;
    }

    /**
     * 获得主办方详细信息
     */
    @RequestMapping("principal/info")
    public String getPrincipalInfo(@RequestBody String request){
        RetData retData;
        JSON req = new JSON(request);
        String token = req.getString("token");
        try{
            JSON user = AESToken.decode(token);
            if(!user.getString("type").equals("principal")) throw new RuntimeException("只有负责人才可以查看详细信息");
            Principal principal = principalService.getPrincipalInfo(user.getString("id"));
            Institution institution = principalService.getInsInfo(principal.belongIns.toString());
            JSON data = new JSON();
            data.put("principal",principal.toJSON());
            data.put("institution",institution.toJson());
            retData = new RetData(R.STATE_SUCC,"",data);
        }catch (Exception e){
            e.printStackTrace();
            retData = new RetData(R.STATE_FAIL,e.getMessage(),null);
        }
        return retData.toString();
    }


    /**
     * boss修改主办方详细信息
     */
    @RequestMapping("principal/modify")
    public String updatePrincipalInfo(@RequestBody String request){
        RetData retData;
        JSON req = new JSON(request);
        String token = req.getString("token");
        JSONObject prin = req.getJSONObject("principal");
        JSONObject ins = req.getJSONObject("institution");
        try{
            JSON user = AESToken.decode(token);
            if(!user.getString("type").equals("principal")) throw new RuntimeException("不是负责人不能更改信息");
            Principal principal = new Principal();
            principal.avator = prin.getString("avator");
            principal.phone = prin.getString("phone");
            principal.id = BigInteger.valueOf(Long.valueOf(user.getString("id")));
            Institution institution = new Institution();
            institution.id = BigInteger.valueOf(conferenceService.getInstitutionIDByPID(principal.id.toString()));
            institution.backimg = ins.getString("backimg");
            institution.introduction = ins.getString("introduction");
            institution.phone = ins.getString("phone");
            principalService.updatePrincpalInfo(principal);
            principalService.updateInsInfo(institution);
            retData = new RetData(R.STATE_SUCC,"",null);
        }catch (Exception e){
            retData = new RetData(R.STATE_FAIL,e.getMessage(),null);
        }
        return retData.toString();
    }

    /**
     * boss或员工修改密码
     */
    @RequestMapping("principal/password")
    public String modifyPassword(@RequestBody String request) {
        RetData retData;
        JSON req = new JSON(request);
        String token = req.getString("token");
        String origin = req.getString("origin_pass");
        String newpass = req.getString("new_pass");
        try {
            JSON principal = AESToken.decode(token);
            if(!principal.getString("type").equals("principal")) throw new RuntimeException("用户类型不是工作人员");

            Principal DBuser = principalService.getPrincipalInfo(principal.getString("id"));
            if(!origin.equals(DBuser.password))
                throw new RuntimeException("原密码输入错误");
            principalService.updatePassword(principal.getString("id"), newpass);
            retData = new RetData(R.STATE_SUCC,"修改密码成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            retData = new RetData(R.STATE_FAIL,e.getMessage(),null);
        }
        return retData.toString();
    }

    /**
     * 添加个人账户
     */
    @RequestMapping("/manage/addPrincipal")
    public String addCount(@RequestBody String request){
        JSON req = new JSON(request);
        String token = req.getString("token");
        String password = req.getString("password");
        String name = req.getString("name");
        String location = req.getString("location");
        String phone = req.getString("phone");
        String email = req.getString("email");

        RetData retData;
        try{
            JSON user = AESToken.decode(token);
            if(!user.getString("type").equals("principal")) throw new RuntimeException("类型不是单位用户");
            Principal principal = new Principal();
            principal.belongIns =BigInteger.valueOf(conferenceService.getInstitutionIDByPID(user.getString("id")));
            principal.password = password;
            principal.name = name;
            principal.location = location;
            principal.phone = phone;
            principal.email = email;
            principalService.addPrincipal(principal);
            retData = new RetData(R.STATE_SUCC,"",null);
        }catch (Exception e){
            e.printStackTrace();
            retData = new RetData(R.STATE_FAIL,e.getMessage(),null);
        }
        return retData.toString();
    }

    /**
     * 获得principals
     */
    @RequestMapping("/manage/principals")
    public String getPrincipals(@RequestBody String request){
        RetData retData;
        JSON req = new JSON(request);
        String token = req.getString("token");
        try{
            JSON user = AESToken.decode(token);
            if(!user.getString("type").equals("principal")) throw new RuntimeException("类型不是单位用户");
            String insID = conferenceService.getInstitutionIDByPID(user.getString("id")).toString();
            List<Principal> principals = principalService.getAllPrincipal(insID);
            JSONArray prinArr = new JSONArray();
            for(Principal principal:principals){
                prinArr.put(principal.toJSON());
            }
            JSON data = new JSON();
            data.put("principal",prinArr);
            retData = new RetData(R.STATE_SUCC,"",data);
        }catch (Exception e){
            retData = new RetData(R.STATE_FAIL,"",null);
        }
        return retData.toString();
    }

    /**
     * 查看注册名单
     */
    @RequestMapping("manage/registry")
    public String getRegisters(@RequestBody String request){
        JSON req = new JSON(request);
        String token = req.getString("token");
        String conferID = req.getBigInteger("conference_id").toString();
        Integer index = req.getInt("index");
        Integer size = req.getInt("size");
        PageDataWrapper pageData = new PageDataWrapper();
        RetData retData;
        try{
            JSON user = AESToken.decode(token);
            if(!user.getString("type").equals("principal")) throw new RuntimeException("类型不是单位用户");
            List<ConferenceRegister> registers = principalService.getAllRegisters(conferID,index,size,pageData);
            JSONArray data = new JSONArray();
            for(ConferenceRegister register:registers){
                JSON item = register.toJSON();
                User user1 = userService.getUserInfoByID(register.user_id.toString());
                item.put("user_name",user1.name);
                List<Participant> participants = principalService.getAllParticipants(register.id.toString());
                JSONArray partArr = new JSONArray();
                for(Participant participant:participants){
                    partArr.put(participant.toJSON());
                }
                item.put("participant",partArr);
                data.put(item);
            }
            JSON ret = new JSON();
            ret.put("page_num",pageData.pageNum);
            ret.put("registers",data);
            retData = new RetData(R.STATE_SUCC,"",ret);
        }catch (Exception e){
            e.printStackTrace();
            retData = new RetData(R.STATE_FAIL,e.getMessage(),null);
        }
        return retData.toString();
    }

    /**
     * 录入审查结果
     */
    @RequestMapping("manage/review")
    public String enterReviewResult(@RequestBody String request){
        JSON req = new JSON(request);
        String token = req.getString("token");
        String id = req.getBigInteger("id").toString();
        String userID = req.getBigInteger("user_id").toString();
        String action = req.getBigInteger("action").toString();
        String suggestion = req.getString("suggestion");
        RetData retData;
        try{
            JSON user = AESToken.decode(token);
            if(!user.getString("type").equals("principal")) throw new RuntimeException("类型不是单位用户");
            User user1 = userService.getUserInfoByID(userID);
            principalService.updateReview(id, suggestion, action);
            messageService.sendMessage(userID, action,suggestion);
            try {
                MailServer.sendEmail(suggestion, "[稿件审核结果]", user1.email, sendGrid);
            }catch (Exception e){
                e.printStackTrace();
            }
            retData = new RetData(R.STATE_SUCC,"",null);
        }catch (Exception e){
            e.printStackTrace();
            retData = new RetData(R.STATE_FAIL,e.getMessage(),null);
        }
        return retData.toString();
    }
}
