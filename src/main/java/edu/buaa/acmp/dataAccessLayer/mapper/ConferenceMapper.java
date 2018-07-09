package edu.buaa.acmp.dataAccessLayer.mapper;



import edu.buaa.acmp.dataAccessLayer.domain.Conference;
import edu.buaa.acmp.dataAccessLayer.domain.ConferenceRegister;
import edu.buaa.acmp.dataAccessLayer.domain.Contribution;
import edu.buaa.acmp.dataAccessLayer.domain.Participant;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
@Mapper
public interface ConferenceMapper {
    @Select("select * from conference where id=#{id}")
    Conference getConferenceDetail(@Param("id")String id );

    @Insert(" INSERT INTO conference(institution_id,title,introduction,start_date,end_date, " +
            " convening_place,essay_information,essay_instructions,paper_ddl,employ_date, " +
            " register_ddl,schedule,paper_template,register_information,ATinformation, " +
            " contact,conference_template,backimg,state) " +
            " VALUES (#{institution_id},#{title},#{introduction},#{start_date},#{end_date}," +
            " #{convening_place},#{essay_information},#{essay_instructions},#{paper_ddl},#{employ_date}, " +
            " #{register_ddl},#{schedule},#{paper_template},#{register_information},#{ATinformation}, " +
            " #{contact},#{conference_template},#{backimg},#{state}) ")
    @Options(useGeneratedKeys = true)
    void postConference(Conference conference);

    //查看所有会议，按发布时间逆序
    @Select(" SELECT * FROM conference WHERE institution_id = #{insID} ORDER BY release_time DESC ")
    List<Conference> getAllConference(@Param("insID") String insID);

    //查看会议注册总人数（参会人总数）
    @Select(" SELECT count(*) FROM participant " +
            "   JOIN conference_register ON participant.register_id = conference_register.id " +
            " WHERE conference_register.conference_id = #{conferID} ")
    Integer getRegisterNumber(@Param("conferID") String conferID);
    //查看会议投稿数量
    @Select(" SELECT count(*) FROM contribution WHERE conference_id = #{conferID} ")
    Integer getPaperNumber(@Param("conferID") String conferID);
    //通过负责人ID获得机构ID
    @Select("select belongIns from principal where id= #{pid}")
    Integer getInstitutionIDByPID(@Param("pid") String pid);

    //获得会议的全部投稿
    @Select("select * from contribution where conference_id = #{conferID}")
    List<Contribution> getAllContributions(@Param("conferID") String id);

    //获取某会议的注册信息导出
    @Select(" SELECT * FROM conference_register WHERE conference_id = #{conferID} ")
    List<ConferenceRegister> getRegisters(@Param("conferID") String conferID);
    //获得某注册对应的参会者
    @Select(" SELECT * FROM participant WHERE register_id = #{regID} ")
    List<Participant> getParticipants(@Param("regID") String regID);

    //更新会议
    @Update(" UPDATE conference " +
            " SET title=#{title}, introduction=#{introduction}, start_date=#{start_date}, end_date=#{end_date}, " +
            " convening_place=#{convening_place}, essay_information=#{essay_information}, essay_instructions=#{essay_instructions}, " +
            " paper_ddl=#{paper_ddl}, employ_date=#{employ_date}, register_ddl=#{register_ddl}, schedule=#{schedule}, " +
            " paper_template=#{paper_template}, register_information=#{register_information}, ATinformation=#{ATinformation}, " +
            " contact=#{contact}, conference_template=#{conference_template}, release_time=now(), backimg=#{backimg}, state=#{state} " +
            " WHERE conference.id = #{id} ")
    void updateConferInfo(Conference conference);
}
