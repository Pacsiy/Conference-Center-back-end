package edu.buaa.acmp.serviceLayer;

import edu.buaa.acmp.dataAccessLayer.domain.Conference;
import edu.buaa.acmp.dataAccessLayer.domain.User;
import edu.buaa.acmp.dataAccessLayer.mapper.IndexMapper;
import edu.buaa.acmp.util.JSON;
import org.json.JSONArray;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class IndexService {
    private final IndexMapper indexMapper;

    public IndexService(IndexMapper indexMapper) {
        this.indexMapper = indexMapper;
    }

    /**
     * 获取最新会议
     * @return 三篇最新会议
     */
    public List<Conference> getNewConference() {
        try {
            return indexMapper.getNewConference();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 获取最热学者
     * @return 5个学者信息
     */
    public List<User> getactiveScholars() {
        try {
            return indexMapper.getactiveScholars();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 获取发生最近事项的六篇论文
     * @return 两个开始征稿，两个停止征稿，两个停止注册，两个即将开始
     */
    public JSONArray getNewIssues() {
        try {
            JSONArray data = new JSONArray();
            List<Conference> conferenceList = indexMapper.getNewIssuesPaperStart();
            for(Conference conference:conferenceList){
                JSON item = new JSON();
                item.put("id",conference.id);
                item.put("name",conference.title);
                item.put("date",conference.start_date);
                item.put("instructions","开始征稿");
                data.put(item);
            }
            List<Conference> conferences = indexMapper.getNewIssuesPaperddl();
            for(Conference conference:conferences){
                JSON item = new JSON();
                item.put("id",conference.id);
                item.put("name",conference.title);
                item.put("date",conference.end_date);
                item.put("instructions","即将截止征稿");
                data.put(item);
            }
            List<Conference> conferences1 = indexMapper.getNewIssuesRegisterddl();
            for(Conference conference:conferences1){
                JSON item = new JSON();
                item.put("id",conference.id);
                item.put("name",conference.title);
                item.put("date",conference.end_date);
                item.put("instructions","即将截止注册");
                data.put(item);
            }
            List<Conference> conferences2 = indexMapper.getNewIssuesStart();
            for(Conference conference:conferences2){
                JSON item = new JSON();
                item.put("id",conference.id);
                item.put("name",conference.title);
                item.put("date",conference.end_date);
                item.put("instructions","即将开幕");
                data.put(item);
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }
}
