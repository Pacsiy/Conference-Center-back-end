package edu.buaa.acmp.controllerLayer;

import com.fasterxml.jackson.databind.node.BigIntegerNode;
import edu.buaa.acmp.controllerLayer.utils.R;
import edu.buaa.acmp.dataAccessLayer.domain.Conference;
import edu.buaa.acmp.dataAccessLayer.domain.Subject;
import edu.buaa.acmp.serviceLayer.SubjectService;
import edu.buaa.acmp.util.JSON;
import edu.buaa.acmp.util.PageDataWrapper;
import edu.buaa.acmp.util.RetData;
import edu.buaa.acmp.util.SubjectBean;
import org.json.JSONArray;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SubjectController {
    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @RequestMapping(value = "subjects")
    public String getAllSubject() {
        RetData retData;
        try {
            List<Subject> tagList = subjectService.getAllSubject();
            JSONArray subjects = new JSONArray();
            for (Subject tag : tagList) {
                JSON item = new JSON();
                item.put("tag_id", tag.id);
                item.put("name", tag.name);
                subjects.put(item);
            }
            retData = new RetData(R.STATE_SUCC, "", subjects);
        } catch (Exception e) {
            retData = new RetData(R.STATE_FAIL, e, null);
        }
        return retData.toString();
    }

    @RequestMapping("search/subject")
    public String getConferenceBySubject(@RequestBody String request){
        RetData retData;
        JSON req = new JSON(request);
        String id = req.getBigInteger("id").toString();
        Integer index = req.getInt("index");
        Integer size = req.getInt("size");
        try{
            PageDataWrapper pageData = new PageDataWrapper();
            List<Conference> conferences = subjectService.getConferenceBySubID(id,index,size,pageData);
            JSONArray conferArr = new JSONArray();
            for(Conference conference:conferences){
                conferArr.put(conference.searchConferToJson());
            }
            JSON data = new JSON();
            data.put("page_num",pageData.pageNum);
            data.put("total_num",pageData.itemNum);
            data.put("result",conferArr);
            retData = new RetData(R.STATE_SUCC,"",data);
        }catch (Exception e){
            e.printStackTrace();
            retData = new RetData(R.STATE_FAIL,e.getMessage(),null);
        }
        return retData.toString();
    }

    @RequestMapping("search/all/subject")
    public String getFieldNumber(@RequestBody String request){
        RetData retData;
        try{
            List<Subject> subjects = subjectService.getAllSubject();
            List<SubjectBean> subjectBeans = subjectService.getConferenceNumberByField();
            JSONArray data = new JSONArray();
            for(Subject subject :subjects){
                JSON item = new JSON();
                item.put("tag_id", subject.id);
                item.put("name", subject.name);
                BigInteger flag = BigInteger.ZERO;
                for(SubjectBean subjectBean:subjectBeans){
                    if(subjectBean.id.toString().equals(subject.id.toString())){
                        flag = subjectBean.num;
                    }
                }
                item.put("num",flag);
                data.put(item);
            }
            retData = new RetData(R.STATE_SUCC,"",data);
        }catch (Exception e){
            e.printStackTrace();
            retData = new RetData(R.STATE_FAIL,e.getMessage(),null);
        }
        return retData.toString();
    }

}
