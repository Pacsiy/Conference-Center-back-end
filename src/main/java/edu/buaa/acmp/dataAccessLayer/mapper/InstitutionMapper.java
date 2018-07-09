package edu.buaa.acmp.dataAccessLayer.mapper;


import edu.buaa.acmp.dataAccessLayer.domain.Institution;
import edu.buaa.acmp.dataAccessLayer.domain.Principal;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface InstitutionMapper {
    /**
     * 设置机构审核状态
     * @param id
     * @param status
     */
    @Update("update institution set state=#{status} where id=#{id}")
    void setInstitutionStatus(@Param("id") String id, @Param("status") String status);

    @Update("update institution set name = #{uuid} where id = #{id}")
    void setInstitutionName(@Param("uuid") String uuid,@Param("id") String id);

    /**
     * 得到需要审核的机构信息
     * @return
     */
    @Select("select * from institution where state=0")
    List<Institution> getInstitutionToCheck();

    @Delete("delete from institution where id = #{id}")
    void deleteInstitution(@Param("id") String id);

    @Select("select * from principal where belongIns = #{id} and power = #{power}")
    Principal getPrincipal(@Param("id") String id,@Param("power") String power);
}
