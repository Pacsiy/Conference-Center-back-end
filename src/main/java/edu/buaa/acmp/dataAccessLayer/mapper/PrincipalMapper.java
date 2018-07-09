package edu.buaa.acmp.dataAccessLayer.mapper;

import edu.buaa.acmp.dataAccessLayer.domain.ConferenceRegister;
import edu.buaa.acmp.dataAccessLayer.domain.Institution;
import edu.buaa.acmp.dataAccessLayer.domain.Participant;
import edu.buaa.acmp.dataAccessLayer.domain.Principal;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PrincipalMapper {
    //获取Principal
    @Select(" SELECT * FROM principal WHERE id = #{pid} ")
    Principal getPrincipalInfo(@Param("pid") String pid);
    //获取Institution
    @Select(" SELECT * FROM institution WHERE id = #{insid}")
    Institution getInstitutionInfo(@Param("insid") String insid);
    //更新Principal
    @Update(" UPDATE principal " +
            " SET avator = #{avator}, phone = #{phone}" +
            " WHERE id = #{id} ")
    void updatePrincipal(Principal principal);
    //更新Institution
    @Update(" UPDATE institution " +
            " SET phone = #{phone}, backimg = #{backimg}, introduction = #{introduction} " +
            " WHERE id = #{id} ")
    void updateInstitution(Institution institution);

    @Insert(" INSERT INTO principal (belongIns, email, password, name, location, phone, power) " +
            " VALUES(#{belongIns}, #{email}, #{password}, #{name}, #{location}, #{phone}, #{power}) ")
    void addPrincipal(Principal principal);

    @Select(" SELECT * FROM principal WHERE belongIns = #{insID} ORDER BY id ")
    List<Principal> getAllPrincipal(@Param("insID") String insID);

    //获得会议所有注册
    @Select(" SELECT * FROM conference_register WHERE conference_id = #{conferID} ORDER BY id DESC ")
    List<ConferenceRegister> getAllRegisters(@Param("conferID") String conferID);
    //获得会议所有注册人数
    @Select(" SELECT count(*) FROM conference_register WHERE conference_id = #{conferID} ")
    Integer getAllRegistersCount(@Param("conferID") String conferID);

    //获得某注册对应的参会者
    @Select(" SELECT * FROM participant WHERE register_id = #{regID} ")
    List<Participant> getAllParticipants(@Param("regID") String regID);

    @Update(" UPDATE review " +
            " SET result = #{result}, suggestion = #{suggestion}, review_time = now() " +
            " WHERE id = #{reID} ")
    void updateReview(@Param("result") String result, @Param("suggestion") String suggestion,
                      @Param("reID") String reID);

    @Update(" update contribution set contribution.total_result = #{result} where id = #{contriID}")
    void updateContributionState(@Param("result") String result,@Param("contriID") String contriID);

    @Select("select contribution_id from review where id = #{reID}")
    Integer getContributionIDByReviewID(@Param("reID") String reID);

    @Update(" UPDATE principal SET password = #{password} WHERE id = #{prinID} ")
    void updatePassword(@Param("prinID") String prinID, @Param("password") String password);
}
