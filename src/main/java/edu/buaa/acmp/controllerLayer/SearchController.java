package edu.buaa.acmp.controllerLayer;

import edu.buaa.acmp.controllerLayer.utils.R;
import edu.buaa.acmp.dataAccessLayer.domain.Conference;
import edu.buaa.acmp.util.PageDataWrapper;
import edu.buaa.acmp.serviceLayer.SearchService;
import edu.buaa.acmp.util.JSON;
import edu.buaa.acmp.util.RetData;
import edu.buaa.acmp.util.TimeConversion;
import org.json.JSONArray;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping(value = "api")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    /**
     * 关键字搜索
     */
    @RequestMapping("/SearchConferences")
    public String searchConferenceByKeyword(@RequestBody String request){
        JSON re = new JSON(request);
        String keyword = re.getString("keyword");
        int index = re.getInt("index");
        int size = re.getInt("size");
        String type = re.getString("date_type");
        RetData retData;
        PageDataWrapper pageData = new PageDataWrapper();
        try{
            List<Conference> conferences = null;
            if(type.equals("none")) {
                conferences = searchService.searchConferenceByKeyword(keyword, index, size, pageData);
            }else {
                Timestamp time = TimeConversion.toDateTime(re.getString("date"));
                if(keyword.equals("")) {
                    conferences = searchService.searchConferenceByDate(index, size, pageData, time, type);
                }else {
                    conferences = searchService.searchConferenceByKeywordAndDate(keyword,index,size,pageData,time,type);
                }
            }
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

}
