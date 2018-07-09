package edu.buaa.acmp.dataAccessLayer.mapper;

import edu.buaa.acmp.dataAccessLayer.domain.*;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserMapper {
    @Select(" SELECT * FROM user WHERE id = #{uid} ")
    User getUserInfo(@Param("uid") String uid);

    @Update(" UPDATE user " +
            " SET name = #{name}, avator = #{avator}, phone = #{phone}, agency = #{agency}, profile = #{profile} " +
            " WHERE id = #{id} ")
    void updateUserInfo(User user);

    @Update(" UPDATE user SET password = #{password} WHERE id = #{userID} ")
    void updatePassword(@Param("userID") String userID, @Param("password") String password);

    //收藏会议
    @Insert( "INSERT INTO collect (user_id, conference_id) values(#{uid}, #{cid})")
    void addCollect(@Param("uid") String uid, @Param("cid") String cid);
    //判断是否收藏
    @Select(" SELECT count(*) FROM collect WHERE user_id = #{uid} and conference_id = #{cid} ")
    Integer isCollect(@Param("uid") String uid, @Param("cid") String cid);
    //取消收藏
    @Delete(" DELETE FROM collect WHERE user_id = #{uid} and conference_id = #{cid} ")
    void cancelCollect(@Param("uid") String uid, @Param("cid") String cid);

    //获取消息
    @Select(" SELECT * FROM message WHERE receiver_id = #{uid} and is_read = 1 ORDER BY sent_time DESC ")
    List<Message> getMessagesRead(@Param("uid") String uid);
    //获取消息数量
    @Select(" SELECT count(*) FROM message WHERE receiver_id = #{uid} and is_read = 1  ")
    Integer getMessagesCountRead(@Param("uid") String uid);

    //获取未读消息
    @Select(" SELECT * FROM message WHERE receiver_id = #{uid} and is_read = 0 ORDER BY sent_time DESC ")
    List<Message> getMessagesNotRead(@Param("uid") String uid);
    //获取未读消息数量
    @Select(" SELECT count(*) FROM message WHERE receiver_id = #{uid} and is_read = 0 ")
    Integer getMessagesCountNotRead(@Param("uid") String uid);
    //读取消息
    @Update(" UPDATE message SET is_read = 1 WHERE id = #{mid} ")
    void readMessage(@Param("mid") String mid);

    //获取用户审核中投稿
    @Select(" SELECT * FROM contribution " +
            " WHERE user_id = #{uid} and total_result = 0 " +
            " ORDER BY total_submit DESC ")
    List<Contribution> getPendingContributions(@Param("uid") String uid);
    //获取用户已通过投稿
    @Select(" SELECT * FROM contribution " +
            " WHERE user_id = #{uid} and total_result = 1 " +
            " ORDER BY total_submit DESC ")
    List<Contribution> getApprovedContributions(@Param("uid") String uid);
    //获取用户修改中投稿
    @Select(" SELECT * FROM contribution " +
            " WHERE user_id = #{uid} and total_result = 2 " +
            " ORDER BY total_submit DESC ")
    List<Contribution> getFixingContributions(@Param("uid") String uid);
    //获取用户已拒绝投稿
    @Select(" SELECT * FROM contribution " +
            " WHERE user_id = #{uid} and total_result = 3 " +
            " ORDER BY total_submit DESC ")
    List<Contribution> getRejectedContributions(@Param("uid") String uid);
    //获取用户审核中投稿数量
    @Select(" SELECT count(*) FROM contribution " +
            " WHERE user_id = #{uid} and total_result = 0 ")
    Integer getPendingContributionsCount(@Param("uid") String uid);
    //获取用户已通过投稿数量
    @Select(" SELECT count(*) FROM contribution " +
            " WHERE user_id = #{uid} and total_result = 1 ")
    Integer getApprovedContributionsCount(@Param("uid") String uid);
    //获取用户修改中投稿数量
    @Select(" SELECT count(*) FROM contribution " +
            " WHERE user_id = #{uid} and total_result = 2 ")
    Integer getFixingContributionsCount(@Param("uid") String uid);
    //获取用户已拒绝投稿数量
    @Select(" SELECT count(*) FROM contribution " +
            " WHERE user_id = #{uid} and total_result = 3 ")
    Integer getRejectedContributionsCount(@Param("uid") String uid);

    //注册的未开幕会议
    @Select(" SELECT conference.* FROM conference " +
            " JOIN conference_register on conference.id = conference_register.conference_id " +
            " WHERE user_id = #{uid} and start_date > now() " +
            " ORDER BY start_date ")
    List<Conference> getNotOpenConference(@Param("uid") String uid);
    //注册的正在进行的会议
    @Select(" SELECT conference.* FROM conference " +
            " JOIN conference_register on conference.id = conference_register.conference_id " +
            " WHERE user_id = #{uid} and start_date < now() and end_date > now() " +
            " ORDER BY start_date ")
    List<Conference> getOpeningConference(@Param("uid") String uid);
    //注册的已结束的会议
    @Select(" SELECT conference.* FROM conference " +
            " JOIN conference_register on conference.id = conference_register.conference_id " +
            " WHERE user_id = #{uid} and end_date < now() " +
            " ORDER BY start_date ")
    List<Conference> getEndedConference(@Param("uid") String uid);
    //注册的未开幕会议数量
    @Select(" SELECT count(*) FROM conference " +
            " JOIN conference_register on conference.id = conference_register.conference_id " +
            " WHERE user_id = #{uid} and start_date > now() " +
            " ORDER BY start_date ")
    Integer getNotOpenConferenceCount(@Param("uid") String uid);
    //注册的正在进行会议数量
    @Select(" SELECT count(*) FROM conference " +
            " JOIN conference_register on conference.id = conference_register.conference_id " +
            " WHERE user_id = #{uid} and start_date < now() and end_date > now() " +
            " ORDER BY start_date ")
    Integer getOpeningConferenceCount(@Param("uid") String uid);
    //注册的已结束会议数量
    @Select(" SELECT count(*) FROM conference " +
            " JOIN conference_register on conference.id = conference_register.conference_id " +
            " WHERE user_id = #{uid} and end_date < now() " +
            " ORDER BY start_date ")
    Integer getEndedConferenceCount(@Param("uid") String uid);

    //判断是否注册
    @Select(" SELECT count(*) FROM conference_register WHERE user_id = #{uid} and conference_id = #{cid} ")
    Integer isRegister(@Param("uid") String uid, @Param("cid") String cid);
    //用户注册会议
    @Insert(" INSERT INTO conference_register (conference_id, user_id, payment, type, paper_number) " +
            " VALUES(#{conference_id}, #{user_id}, #{payment}, #{type}, #{paper_number}) ")
    @Options(useGeneratedKeys = true)
    void registerConference(ConferenceRegister register);
    //增加参会者
    @Insert(" INSERT INTO participant (register_id, name, sex, job, contract, is_book, note) " +
            " VALUES(#{register_id}, #{name}, #{sex}, #{job}, #{contract}, #{is_book}, #{note}) ")
    void insertParticipant(Participant participant);

    //注册时获取论文信息
    @Select(" SELECT * FROM contribution " +
            " WHERE conference_id = #{conferID} and user_id = #{userID} and total_result = 1 ")
    List<Contribution> getPassedContributionsByConferID(@Param("conferID") String conferID, @Param("userID") String userID);


    //收藏的未开幕会议
    @Select(" SELECT * FROM conference " +
            " JOIN collect on collect.conference_id = conference.id " +
            " WHERE collect.user_id = #{userID} and start_date > now() " +
            " ORDER BY start_date ")
    List<Conference> getNotOpenConferenceCollected(@Param("userID") String userID);
    //收藏的已开始会议
    @Select(" SELECT * FROM conference " +
            " JOIN collect on collect.conference_id = conference.id " +
            " WHERE collect.user_id = #{userID} and start_date < now() and end_date > now() " +
            " ORDER BY start_date ")
    List<Conference> getOpeningConferenceCollected(@Param("userID") String userID);
    //收藏的已结束会议
    @Select(" SELECT * FROM conference " +
            " JOIN collect on collect.conference_id = conference.id " +
            " WHERE collect.user_id = #{userID} and end_date < now() " +
            " ORDER BY start_date ")
    List<Conference> getEndedConferenceCollected(@Param("userID") String userID);
    //收藏的未开幕会议数量
    @Select(" SELECT count(*) FROM conference " +
            " JOIN collect on collect.conference_id = conference.id " +
            " WHERE collect.user_id = #{userID} and start_date > now() " +
            " ORDER BY start_date ")
    Integer getNotOpenConferenceCollectedCount(@Param("userID") String userID);
    //收藏的已开始会议数量
    @Select(" SELECT count(*) FROM conference " +
            " JOIN collect on collect.conference_id = conference.id " +
            " WHERE collect.user_id = #{userID} and start_date < now() and end_date > now() " +
            " ORDER BY start_date ")
    Integer getOpeningConferenceCollectedCount(@Param("userID") String userID);
    //收藏的已结束会议数量
    @Select(" SELECT count(*) FROM conference " +
            " JOIN collect on collect.conference_id = conference.id " +
            " WHERE collect.user_id = #{userID} and end_date < now() " +
            " ORDER BY start_date ")
    Integer getEndedConferenceCollectedCount(@Param("userID") String userID);

}
