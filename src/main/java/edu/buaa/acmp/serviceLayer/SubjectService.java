package edu.buaa.acmp.serviceLayer;

import com.github.pagehelper.PageHelper;
import edu.buaa.acmp.dataAccessLayer.domain.Conference;
import edu.buaa.acmp.dataAccessLayer.domain.Subject;
import edu.buaa.acmp.dataAccessLayer.mapper.SubjectMapper;
import edu.buaa.acmp.util.PageDataWrapper;
import edu.buaa.acmp.util.SubjectBean;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SubjectService {
    private final SubjectMapper subjectMapper;

    public SubjectService(SubjectMapper subjectMapper) {
        this.subjectMapper = subjectMapper;
    }

    /**
     * 获取所有领域
     */
    public List<Subject> getAllSubject() {
        try {
            return subjectMapper.getAll();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * 获取会议的领域
     */
    public Subject getConferenceSubject(String conferID) {
        return subjectMapper.getConferenceSubject(conferID);
    }

    /**
     * 通过领域ID 获得相关会议
     * @param id 主题ID
     */
    public List<Conference> getConferenceBySubID(String id, int index, int size, PageDataWrapper pageData){
        try{
            pageData.itemNum = subjectMapper.getConferenceCountBySubID(id);
            pageData.pageNum = (int)Math.ceil(1.0*pageData.itemNum/size);
            PageHelper.startPage(index,size);
            return subjectMapper.getConferenceBySubID(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 获得每个领域的会议数量
     */
    public List<SubjectBean> getConferenceNumberByField(){
        try{
            return subjectMapper.getTotal();
        }catch (Exception e){
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
