package edu.buaa.acmp.controllerLayer;

import edu.buaa.acmp.controllerLayer.utils.R;
import edu.buaa.acmp.dataAccessLayer.domain.Conference;
import edu.buaa.acmp.dataAccessLayer.domain.User;
import edu.buaa.acmp.serviceLayer.IndexService;
import edu.buaa.acmp.util.JSON;
import edu.buaa.acmp.util.RetData;
import org.json.JSONArray;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class IndexController {

    private final IndexService indexService;

    public IndexController(IndexService indexService) {
        this.indexService = indexService;
    }

    /**
     * 获取最新的三个会议
     */
    @RequestMapping("home/newest")
    public String getRecentConference(@RequestBody String request){
        RetData retData;
        try{
            JSONArray data = new JSONArray();
            List<Conference> conferences =indexService.getNewConference();
            for(Conference conference:conferences){
                JSON item = new JSON();
                item.put("conference_id",conference.id);
                item.put("title",conference.title);
                item.put("start_date",conference.start_date);
                item.put("convening_place",conference.convening_place);
                item.put("state",conference.state);
                data.put(item);
            }
            retData = new RetData(R.STATE_SUCC,"",data);
        }catch (Exception e){
            e.printStackTrace();
            retData = new RetData(R.STATE_FAIL,e.getMessage(),null);
        }
        return retData.toString();
    }

    /**
     * 获得活跃的5个作者
     */
    @RequestMapping("home/activeScholars")
    public String getTop5Scholars(@RequestBody String request){
        RetData retData;
        try{
            JSONArray data = new JSONArray();
            List<User> users = indexService.getactiveScholars();
            for(User user:users){
                JSON item = new JSON();
                item.put("name",user.name);
                item.put("avator",user.avator);
                item.put("agency",user.agency);
                item.put("introduction",user.profile);
                data.put(item);
            }
            retData = new RetData(R.STATE_SUCC,"",data);
        }catch(Exception e){
            e.printStackTrace();
            retData = new RetData(R.STATE_FAIL,"",null);
        }
        return retData.toString();
    }


    /**
     * 获取了六项近期事项
     */
    @RequestMapping("home/recentlyItem")
    public String getTop6Conference(@RequestBody String request){
        RetData retData;
        try{
            JSONArray data = indexService.getNewIssues();
            retData = new RetData(R.STATE_SUCC,"",data);
        }catch (Exception e){
            e.printStackTrace();
            retData = new RetData(R.STATE_FAIL,"",null);
        }
        return retData.toString();
    }
}
