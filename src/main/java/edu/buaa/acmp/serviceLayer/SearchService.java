package edu.buaa.acmp.serviceLayer;

import com.github.pagehelper.PageHelper;
import edu.buaa.acmp.dataAccessLayer.domain.Collect;
import edu.buaa.acmp.dataAccessLayer.domain.Conference;
import edu.buaa.acmp.dataAccessLayer.mapper.SearchMapper;
import edu.buaa.acmp.util.PageDataWrapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

@Service
public class SearchService {

    private final SearchMapper searchMapper;

    public SearchService(SearchMapper searchMapper) {
        this.searchMapper = searchMapper;
    }

    /**
     * 通过关键字检索会议
     * @param keyword 关键词
     * @param index 第几页
     * @param size 大小
     * @return 搜出的会议列表
     */
    @NotNull
    public List<Conference> searchConferenceByKeyword(String keyword, int index, int size, PageDataWrapper pageData){
        try{
            pageData.itemNum = searchMapper.getItemsCount(keyword);
            pageData.pageNum = (int)Math.ceil(pageData.itemNum*1.0/size);
            PageHelper.startPage(index,size);
            return searchMapper.getConferenceItem(keyword);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 根据日期和关键字搜索
     * @param time 时间
     * @param type 时间类型
     * @return 会议
     */
    public List<Conference> searchConferenceByKeywordAndDate(String keyword, int index, int size,
                                                             PageDataWrapper pageData, Timestamp time,String type){
        try{
            switch (type){
                case "paper":
                    pageData.itemNum = searchMapper.getItemsCountByDatePaperKeyword(keyword,time);
                    pageData.pageNum = (int)Math.ceil(pageData.itemNum*1.0/size);
                    PageHelper.startPage(index,size);
                    return searchMapper.getConferenceItemByDatePaperKeyword(keyword,time);
                case "register":
                    pageData.itemNum = searchMapper.getItemsCountByDateRegisterKeyword(keyword,time);
                    pageData.pageNum = (int)Math.ceil(pageData.itemNum*1.0/size);
                    PageHelper.startPage(index,size);
                    return searchMapper.getConferenceItemByDateRegisterKeyword(keyword,time);
                case "start":
                    pageData.itemNum = searchMapper.getItemsCountByDateStartKeyword(keyword,time);
                    pageData.pageNum = (int)Math.ceil(pageData.itemNum*1.0/size);
                    PageHelper.startPage(index,size);
                    return searchMapper.getConferenceItemByDateStartKeyword(keyword,time);
                case "end":
                    pageData.itemNum = searchMapper.getItemsCountByDateEndKeyword(keyword,time);
                    pageData.pageNum = (int)Math.ceil(pageData.itemNum*1.0/size);
                    PageHelper.startPage(index,size);
                    return searchMapper.getConferenceItemByDateEndKeyword(keyword,time);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public List<Conference> searchConferenceByDate(int index, int size, PageDataWrapper pageData, Timestamp time,String type){
        try{
            switch (type){
                case "paper":
                    pageData.itemNum = searchMapper.getItemsCountByDatePaper(time);
                    pageData.pageNum = (int)Math.ceil(pageData.itemNum*1.0/size);
                    PageHelper.startPage(index,size);
                    return searchMapper.getConferenceItemByDatePaper(time);
                case "register":
                    pageData.itemNum = searchMapper.getItemsCountByDateRegister(time);
                    pageData.pageNum = (int)Math.ceil(pageData.itemNum*1.0/size);
                    PageHelper.startPage(index,size);
                    return searchMapper.getConferenceItemByDateRegister(time);
                case "start":
                    pageData.itemNum = searchMapper.getItemsCountByDateStart(time);
                    pageData.pageNum = (int)Math.ceil(pageData.itemNum*1.0/size);
                    PageHelper.startPage(index,size);
                    return searchMapper.getConferenceItemByDateStart(time);
                case "end":
                    pageData.itemNum = searchMapper.getItemsCountByDateEnd(time);
                    pageData.pageNum = (int)Math.ceil(pageData.itemNum*1.0/size);
                    PageHelper.startPage(index,size);
                    return searchMapper.getConferenceItemByDateEnd(time);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

}
