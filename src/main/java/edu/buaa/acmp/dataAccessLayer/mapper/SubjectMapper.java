package edu.buaa.acmp.dataAccessLayer.mapper;


import edu.buaa.acmp.dataAccessLayer.domain.Conference;
import edu.buaa.acmp.dataAccessLayer.domain.Subject;
import edu.buaa.acmp.util.SubjectBean;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SubjectMapper {
    @Select(" SELECT * FROM subject ")
    List<Subject> getAll();

    @Insert(" insert into conference_subject(conference_id,subject_id) values(#{cid},#{sid}) ")
    void insertConferenceSubject(@Param("cid")String cid, @Param("sid")String sid);

    @Select(" SELECT subject.* FROM subject " +
            " JOIN conference_subject on subject.id = conference_subject.subject_id " +
            " WHERE conference_subject.conference_id = #{conferID} ")
    Subject getConferenceSubject(@Param("conferID") String conferID);

    @Update(" UPDATE conference_subject SET subject_id = #{sid} WHERE conference_id = #{cid} ")
    void updateConferenceSubject(@Param("cid") String cid, @Param("sid") String sid);

    @Select("select conference.* from conference " +
            " join conference_subject on conference.id = conference_subject.conference_id " +
            "where conference_subject.subject_id = #{id}")
    List<Conference> getConferenceBySubID(@Param("id") String id);

    @Select("select count(*) from conference " +
            " join conference_subject on conference.id = conference_subject.conference_id " +
            "where conference_subject.subject_id = #{id}")
    Integer getConferenceCountBySubID(@Param("id") String id);


    @Select("select conference_subject.subject_id as id ,count(conference_id) as num from conference_subject group by subject_id")
    List<SubjectBean> getTotal();

}
