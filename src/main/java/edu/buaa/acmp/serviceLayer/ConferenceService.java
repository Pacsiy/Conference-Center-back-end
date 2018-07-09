package edu.buaa.acmp.serviceLayer;

import edu.buaa.acmp.dataAccessLayer.domain.*;
import edu.buaa.acmp.dataAccessLayer.mapper.ConferenceMapper;
import edu.buaa.acmp.dataAccessLayer.mapper.SubjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

@Service
public class ConferenceService {
    private final ConferenceMapper conferenceMapper;
    private final SubjectMapper subjectMapper;

    public ConferenceService(ConferenceMapper conferenceMapper, SubjectMapper subjectMapper){
        this.conferenceMapper = conferenceMapper;
        this.subjectMapper = subjectMapper;
    }

    /**
     * 获取会议信息
     * @param id 会议ID
     * @return 会议的所有信息
     */
    public Conference getConferenceDetail(String id)throws Exception{
            return conferenceMapper.getConferenceDetail(id);
    }

    /**
     * 发布会议
     * @param conference 会议
     * @param field 领域ID
     */
    public void postConference(Conference conference, BigInteger field) {
        try {
            conferenceMapper.postConference(conference);
            subjectMapper.insertConferenceSubject(conference.id.toString(), field.toString());
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 获得一个机构举办的所有会议
     * @param institutionID 机构ID
     * @return 会议列表
     */
    @NotNull
    public List<Conference> getAllConferencesByInstutionID(String institutionID){
        try {
            return conferenceMapper.getAllConference(institutionID);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * 获取会议注册总数（参会人数）
     * @param conferID 会议ID
     * @return 参会人数
     */
    public Integer getRegisterNumberByConferenceID(BigInteger conferID){
        return conferenceMapper.getRegisterNumber(conferID.toString());
    }

    /**
     * 获取会议投稿总数
     * @param conferID 会议ID
     * @return 投稿总数
     */
    public Integer getPaperNumberByConferenceID(BigInteger conferID){
        return conferenceMapper.getPaperNumber(conferID.toString());
    }

    /**
     * 通过principal获得机构ID
     */
    public Integer getInstitutionIDByPID(String principalID){
        return conferenceMapper.getInstitutionIDByPID(principalID);
    }

    /**
     * 获得某会议的全部投稿信息
     * @param conferID 会议ID
     * @return 投稿列表
     */
    @NotNull
    public List<Contribution> getAllContributionByConferID(String conferID){
        try{
            return conferenceMapper.getAllContributions(conferID);
        }catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * 获得某会议的注册信息
     * @param conferID 会议ID
     */
    @NotNull
    public List<ConferenceRegister> getRegisters(String conferID) {
        try {
            return conferenceMapper.getRegisters(conferID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 根据注册ID获得参加者
     * @param registerID 注册ID
     */
    @NotNull
    public List<Participant> getParticipantsByRegisterID(String registerID){
        try {
            return conferenceMapper.getParticipants(registerID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 会议更新
     */
    public void updateConferInfo(Conference conference, BigInteger fieldID){
        conferenceMapper.updateConferInfo(conference);
        subjectMapper.updateConferenceSubject(conference.id.toString(), fieldID.toString());
    }

}
