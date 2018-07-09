package edu.buaa.acmp.controllerLayer;

import edu.buaa.acmp.controllerLayer.utils.AESToken;
import edu.buaa.acmp.controllerLayer.utils.R;
import edu.buaa.acmp.dataAccessLayer.domain.*;
import edu.buaa.acmp.serviceLayer.ConferenceService;
import edu.buaa.acmp.serviceLayer.PrincipalService;
import edu.buaa.acmp.serviceLayer.UserService;
import edu.buaa.acmp.util.JSON;
import edu.buaa.acmp.util.PageDataWrapper;
import edu.buaa.acmp.util.RetData;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final ConferenceService conferenceService;
    private final PrincipalService principalService;

    public UserController(UserService userService, ConferenceService conferenceService, PrincipalService principalService) {
        this.userService = userService;
        this.conferenceService = conferenceService;
        this.principalService = principalService;
    }

    /**
     * 获得Token 并解析Token
     */
    @RequestMapping("user/token")
    public String verify(@RequestBody String request, HttpSession session){
        JSON re = new JSON(request);
        String token = re.getString("token");
        RetData retData;
        try {
            String origin = new AESToken().decrypt(token);
            System.out.println(origin);
            JSON userInfo = new JSON(origin);
            session.setAttribute(R.USER_SESSION,userInfo);
            retData = new RetData(R.STATE_SUCC,"",userInfo);
        }catch (Exception e){
            retData = new RetData(R.STATE_FAIL,e.getMessage(),null);
        }
        return retData.toString();
    }

    /**
     * 注销
     */
    @RequestMapping("user/logout")
    public String logout(HttpSession session){
        RetData retData;
        try{
            if(session.getAttribute(R.USER_SESSION) != null){
                session.removeAttribute(R.USER_SESSION);
            }
            retData = new RetData(R.STATE_SUCC,"",null);
        }catch (Exception e){
            retData = new RetData(R.STATE_FAIL,"",null);
        }
        return retData.toString();
    }

    /**
     * 收藏会议
     */
    @RequestMapping(value = "user/collect")
    public String collectSubject(@RequestBody String request, HttpSession session) {
        JSON req = new JSON(request);
        String conferID = req.getBigInteger("conference_id").toString();
        String token = req.getString("token");
        RetData retData;
        try {
            JSON user = new JSON(new AESToken().decrypt(token));
            if(!user.getString("type").equals("user")) throw new RuntimeException("只有普通用户才可以收藏会议");
            userService.collectConference(user.getString("id"),conferID);
            retData = new RetData(R.STATE_SUCC,"收藏成功",null);
        } catch (Exception e) {
            retData= new RetData(R.STATE_FAIL,"收藏失败",null);
        }
        return retData.toString();
    }

    /**
     * 判断会议是否被用户收藏
     */
    @RequestMapping("conference/iscollect/{id}")
    public String isCollectByUser(@RequestBody String request, @PathVariable("id") String conferID){
        JSON req = new JSON(request);
        String token = req.getString("token");
        RetData retData;
        try{
            JSON user = AESToken.decode(token);
            if(!user.getString("type").equals("user")) throw new RuntimeException("当前用户类型是单位用户");
            Integer data = userService.isCollectByUser(user.getString("id"),conferID);
            retData = new RetData(R.STATE_SUCC,"用户已收藏",data);
        }catch (Exception e){
            e.printStackTrace();
            retData = new RetData(R.STATE_FAIL,"未知错误",-1);
        }
        return retData.toString();
    }

    /**
     * 取消收藏会议
     */
    @RequestMapping("conference/cancel/collect/{id}")
    public String cancelCollect(@RequestBody String request,@PathVariable("id") String conferID){
        JSON req = new JSON(request);
        String token = req.getString("token");
        RetData retData;
        try{
            JSON user = AESToken.decode(token);
            if(!user.getString("type").equals("user")) throw new RuntimeException("当前用户类型是单位用户");
            userService.cancelCollect(user.getString("id"),conferID);
            retData = new RetData(R.STATE_SUCC,"",null);
        }catch (Exception e){
            retData = new RetData(R.STATE_FAIL,"",null);
        }
        return retData.toString();
    }

    /**
     * 获得普通用户信息
     */
    @RequestMapping("user/info")
    public String getUserInfo(@RequestBody String request){
        RetData retData;
        JSON req = new JSON(request);
        try{
            String token = req.getString("token");
            JSON data = new JSON(new AESToken().decrypt(token));
            if(!data.getString("type").equals("user")) throw new RuntimeException("不是普通用户");
            User user = userService.getUserInfoByID(data.getString("id"));
            retData = new RetData(R.STATE_SUCC,"",user.toJSON());
        }catch (Exception e){
            retData = new RetData(R.STATE_FAIL,"",null);
        }
        return retData.toString();
    }

    /**
     * 修改普通用户信息
     */
    @RequestMapping("user/modify")
    public String updateUserInfo(@RequestBody String request){
        RetData retData;
        JSON req = new JSON(request);
        try{
            String token = req.getString("token");
            JSON data = new JSON(new AESToken().decrypt(token));
            if(!data.getString("type").equals("user")) throw new RuntimeException("不是普通用户");
            User user = new User();
            user.id = BigInteger.valueOf(Long.valueOf(data.getString("id")));
            user.avator = req.getString("avator");
            user.name = req.getString("name");
            user.profile = req.getString("profile");
            user.phone = req.getString("phone");
            user.agency = req.getString("agency");
            userService.updateUserInfo(user);
            User ret = userService.getUserInfoByID(user.id.toString());
            retData = new RetData(R.STATE_SUCC,"",ret.toJSON());
        }catch (Exception e){
            retData = new RetData(R.STATE_FAIL,"",null);
        }
        return retData.toString();
    }

    /**
     * 用户修改密码
     */
    @RequestMapping("user/password")
    public String modifyPassword(@RequestBody String request) {
        RetData retData;
        JSON req = new JSON(request);
        String token = req.getString("token");
        String origin = req.getString("origin_pass");
        String newpass = req.getString("new_pass");
        try {
            JSON user = AESToken.decode(token);
            if(!user.getString("type").equals("user")) throw new RuntimeException("用户类型不是普通用户");

            User DBuser = userService.getUserInfoByID(user.getString("id"));
            if(!origin.equals(DBuser.password))
                throw new RuntimeException("原密码输入错误");
            userService.updatePassword(user.getString("id"), newpass);
            retData = new RetData(R.STATE_SUCC,"修改密码成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            retData = new RetData(R.STATE_FAIL,"修改密码失败",null);
        }
        return retData.toString();
    }

    /**
     * 获得用户消息
     */
    @RequestMapping("user/messages")
    public String getUserMessages(@RequestBody String request){
        RetData retData;
        JSON req = new JSON(request);
        String token = req.getString("token");
        Integer index = req.getInt("index");
        Integer size = req.getInt("size");
        String state = req.getBigInteger("state").toString();
        PageDataWrapper pageData = new PageDataWrapper();
        try{
            JSON user = AESToken.decode(token);
            if(!user.getString("type").equals("user")) throw new RuntimeException("用户类型不是普通用户");
            List<Message> messages = userService.getMessages(user.getString("id"),index,size,pageData,state);
            JSONArray messageArr = new JSONArray();
            for(Message message:messages){
                messageArr.put(message.toJSON());
            }
            JSON data = new JSON();
            data.put("total_num",pageData.itemNum);
            data.put("page_num",pageData.pageNum);
            data.put("messages",messageArr);
            retData = new RetData(R.STATE_SUCC,"",data);
        }catch (Exception e){
            e.printStackTrace();
            retData = new RetData(R.STATE_FAIL,"",null);
        }
        return retData.toString();
    }

    /**
     * 将信息标记为已读消息
     */
    @RequestMapping("user/message/{id}")
    public String readMessage(@PathVariable("id") String id,@RequestBody String request){
        RetData retData ;
        JSON req = new JSON(request);
        String token = req.getString("token");
        try{
            JSON user = AESToken.decode(token);
            if(!user.getString("type").equals("user")) throw new RuntimeException("类型不是普通用户");
            userService.readMessage(id);
            retData = new RetData(R.STATE_SUCC,"",null);
        }catch (Exception e){
            retData = new RetData(R.STATE_FAIL,"",null);
        }
        return retData.toString();
    }

    /**
     * 查看投稿状况
     */
    @RequestMapping("user/getContribution")
    public String getUserContributions(@RequestBody String request){
        RetData retData ;
        JSON req = new JSON(request);
        String token = req.getString("token");
        String type = req.getString("type");
        try{
            JSON user = AESToken.decode(token);
            if(!user.getString("type").equals("user")) throw new RuntimeException("类型不是普通用户");
            String userID = user.getString("id");
            List<Contribution> contributions;
            switch (type){
                case "pending":
                    contributions = userService.getPendingContributions(userID);
                    break;
                case "passed":
                    contributions = userService.getApprovedContributions(userID);
                    break;
                case "fixing":
                    contributions = userService.getFixingContributions(userID);
                    break;
                case "rejected":
                    contributions = userService.getRejectedContributions(userID);
                    break;
                default:
                    contributions = userService.getPendingContributions(userID);
            }
            JSONArray conArr = new JSONArray();
            for (Contribution contribution:contributions){
                JSON data = contribution.toJSON();
                Conference conference = conferenceService.getConferenceDetail(contribution.conference_id.toString());
                data.put("conference_title", conference.title);
                conArr.put(data);
            }
            ArrayList<Integer> total = userService.getContributionTotal(userID);
            JSON data = new JSON();
            data.put("total",new JSONArray(total));
            data.put("contributions",conArr);
            retData = new RetData(R.STATE_SUCC,"",data);
        }catch (Exception e){
            e.printStackTrace();
            retData = new RetData(R.STATE_FAIL,"",null);
        }
        return retData.toString();
    }

    /**
     * 查看用户已注册的会议
     */
    @RequestMapping("user/getRegisterConference")
    public String getUserRegisterConference(@RequestBody String request){
        RetData retData ;
        JSON req = new JSON(request);
        String token = req.getString("token");
        String type = req.getString("type");
        try{
            JSON user = AESToken.decode(token);
            if(!user.getString("type").equals("user")) throw new RuntimeException("类型不是普通用户");
            String userID = user.getString("id");
            List<Conference> conferences;
            switch(type){
                case "notOpen":
                    conferences = userService.getNotOpenConference(userID);
                    break;
                case "opened":
                    conferences = userService.getOpeningConference(userID);
                    break;
                case "enden":
                    conferences = userService.getEndedConference(userID);
                    break;
                default:
                    conferences = userService.getNotOpenConference(userID);
            }
            JSONArray conferArr = new JSONArray();
            for(Conference conference:conferences){
                JSON item = conference.searchConferToJson();
                item.put("state",conference.state);
                Institution institution = principalService.getInsInfo(conference.institution_id.toString());
                item.put("institution_name",institution.name);
                conferArr.put(item);
            }
            JSON data = new JSON();
            data.put("result",conferArr);
            ArrayList<Integer> total = userService.getConferenceTotal(userID);
            data.put("total_num",new JSONArray(total));
            retData = new RetData(R.STATE_SUCC,"",data);
        }catch (Exception e){
            retData = new RetData(R.STATE_FAIL,"",null);
        }
        return retData.toString();
    }

    /**
     * 判断用户是否注册某会议
     */
    @RequestMapping("conference/isregister/{id}")
    public String isRegisterByUser(@RequestBody String request, @PathVariable("id") String conferID) {
        JSON req = new JSON(request);
        String token = req.getString("token");
        RetData retData;
        try{
            JSON user =AESToken.decode(token);
            if(!user.getString("type").equals("user")) throw new RuntimeException("当前用户类型不是普通用户");
            Integer data = userService.isRegisterByUser(user.getString("id"),conferID);
            retData = new RetData(R.STATE_SUCC,"",data);
        } catch (Exception e) {
            e.printStackTrace();
            retData = new RetData(R.STATE_FAIL,"",-1);
        }
        return retData.toString();
    }

    /**
     * 用户提交注册的信息
     */
    @RequestMapping("user/register")
    public String registerConference(@RequestBody String request){
        RetData retData;
        JSON req = new JSON(request);
        String token = req.getString("token");
        BigInteger conferID = req.getBigInteger("conference_id");
        String payment = req.getString("payment");
        BigInteger type = req.getBigInteger("type");
        String paperNumber = req.getString("paper_number");
        JSONArray parts = req.getJSONArray("participants");
        try{
            JSON user = AESToken.decode(token);
            if(!user.getString("type").equals("user")) throw new RuntimeException("类型不是普通用户");
            ConferenceRegister conferenceRegister = new ConferenceRegister();
            conferenceRegister.conference_id = conferID;
            conferenceRegister.user_id = BigInteger.valueOf(Long.valueOf(user.getString("id")));
            conferenceRegister.type = type;
            conferenceRegister.paper_number = paperNumber;
            conferenceRegister.payment = payment;
            BigInteger registerID = userService.registerConference(conferenceRegister);
            ArrayList<Participant> participants = new ArrayList<>();
            for(int i = 0;i<parts.length();i++){
                JSONObject item = parts.getJSONObject(i);
                Participant participant = new Participant();
                participant.register_id = registerID;
                participant.sex = item.getString("sex");
                participant.name = item.getString("name");
                participant.contract = item.getString("contract");
                participant.is_book = item.getBigInteger("is_book");
                participant.note = item.getString("note");
                participant.job = item.getString("job");
                participants.add(participant);
            }
            userService.insertParticipant(participants);
            retData = new RetData(R.STATE_SUCC,"",null);
        }catch (Exception e){
            e.printStackTrace();
            retData = new RetData(R.STATE_FAIL,"",null);
        }
        return retData.toString();
    }

    /**
     * 用户填表单时预加载注册信息
     */
    @RequestMapping("user/getRegister")
    public String loadRegisterInfo(@RequestBody String request){
        JSON req = new JSON(request);
        String token = req.getString("token");
        String conferID = req.getBigInteger("conference_id").toString();
        RetData retData;
        try{
            JSON user = AESToken.decode(token);
            if(!user.getString("type").equals("user")) throw new RuntimeException("类型不是普通用户");
            String userID = user.getString("id");
            List<Contribution> contributions = userService.getPassedContributionsByConferID(conferID,userID);
            JSON data = new JSON();
            if(contributions.size() == 0){
                data.put("type",1);
            }else {
                data.put("type",0);
                JSONArray papers = new JSONArray();
                for(Contribution contribution:contributions){
                    JSON tmp = new JSON();
                    tmp.put("title",contribution.title);
                    tmp.put("paper_number",contribution.paper_number);
                    JSONArray item = new JSONArray(contribution.author);
                    tmp.put("authors",item);
                    papers.put(tmp);
                }
                data.put("papers",papers);
            }
            retData = new RetData(R.STATE_SUCC,"",data);
        }catch (Exception e){
            retData = new RetData(R.STATE_FAIL,"",null);
        }
        return retData.toString();
    }

    /**
     * 用户查看收藏的会议
     */
    @RequestMapping("user/getCollectConference")
    public String getUserCollectedConference(@RequestBody String request){
        RetData retData ;
        JSON req = new JSON(request);
        String token = req.getString("token");
        String type = req.getString("type");
        try{
            JSON user = AESToken.decode(token);
            if(!user.getString("type").equals("user")) throw new RuntimeException("类型不是普通用户");
            String userID = user.getString("id");
            List<Conference> conferences;
            switch(type){
                case "notOpen":
                    conferences = userService.getNotOpenConferenceCollected(userID);
                    break;
                case "opened":
                    conferences = userService.getOpeningConferenceCollected(userID);
                    break;
                case "enden":
                    conferences = userService.getEndedConferenceCollected(userID);
                    break;
                default:
                    conferences = userService.getNotOpenConferenceCollected(userID);
            }
            JSONArray conferArr = new JSONArray();
            for(Conference conference:conferences){
                JSON item = conference.searchConferToJson();
                Institution institution = principalService.getInsInfo(conference.institution_id.toString());
                item.put("institution_name",institution.name);
                item.put("state",conference.state);
                conferArr.put(item);
            }
            JSON data = new JSON();
            data.put("result",conferArr);
            ArrayList<Integer> total = userService.getConferenceTotalCollected(userID);
            data.put("total_num",new JSONArray(total));
            retData = new RetData(R.STATE_SUCC,"",data);
        }catch (Exception e){
            retData = new RetData(R.STATE_FAIL,"",null);
        }
        return retData.toString();
    }
}
