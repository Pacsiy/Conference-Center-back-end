package edu.buaa.acmp.dataAccessLayer.mapper;

import edu.buaa.acmp.dataAccessLayer.domain.Conference;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
@Mapper
public interface SearchMapper {
    @Select("select count(*) from conference where match(title,introduction) against(#{keyword})")
    Integer getItemsCount(@Param("keyword") String keyword);

    @Select("select * from conference where match(title,introduction) against(#{keyword})")
    List<Conference> getConferenceItem(@Param("keyword") String keyword);

    //时间和关键字一起搜索

    //paper
    @Select("select count(*) from conference where match(title,introduction) against(#{keyword}) and paper_ddl>=#{base}")
    Integer getItemsCountByDatePaperKeyword(@Param("keyword") String keyword, @Param("base") Timestamp base);

    @Select("select * from conference where match(title,introduction) against(#{keyword}) and paper_ddl>=#{base}")
    List<Conference> getConferenceItemByDatePaperKeyword(@Param("keyword") String keyword,@Param("base") Timestamp base);

    //register
    @Select("select count(*) from conference where match(title,introduction) against(#{keyword}) and register_ddl>=#{base}")
    Integer getItemsCountByDateRegisterKeyword(@Param("keyword") String keyword,@Param("base") Timestamp base);

    @Select("select * from conference where match(title,introduction) against(#{keyword}) and register_ddl>=#{base}")
    List<Conference> getConferenceItemByDateRegisterKeyword(@Param("keyword") String keyword,@Param("base") Timestamp base);

    //start_time
    @Select("select count(*) from conference where match(title,introduction) against(#{keyword}) and start_date>=#{base}")
    Integer getItemsCountByDateStartKeyword(@Param("keyword") String keyword,@Param("base") Timestamp base);

    @Select("select * from conference where match(title,introduction) against(#{keyword}) and start_date>=#{base}")
    List<Conference> getConferenceItemByDateStartKeyword(@Param("keyword") String keyword,@Param("base") Timestamp base);

    //end_date
    @Select("select count(*) from conference where match(title,introduction) against(#{keyword}) and end_date>=#{base}")
    Integer getItemsCountByDateEndKeyword(@Param("keyword") String keyword,@Param("base") Timestamp base);

    @Select("select * from conference where match(title,introduction) against(#{keyword}) and end_date>=#{base}")
    List<Conference> getConferenceItemByDateEndKeyword(@Param("keyword") String keyword,@Param("base") Timestamp base);

    //时间单独搜索
    //paper
    @Select("select count(*) from conference where paper_ddl>=#{base}")
    Integer getItemsCountByDatePaper(@Param("base") Timestamp base);

    @Select("select * from conference where paper_ddl>=#{base}")
    List<Conference> getConferenceItemByDatePaper(@Param("base") Timestamp base);

    //register
    @Select("select count(*) from conference where register_ddl>=#{base}")
    Integer getItemsCountByDateRegister(@Param("base") Timestamp base);

    @Select("select * from conference where register_ddl>=#{base}")
    List<Conference> getConferenceItemByDateRegister(@Param("base") Timestamp base);

    //start_time
    @Select("select count(*) from conference where start_date>=#{base}")
    Integer getItemsCountByDateStart(@Param("base") Timestamp base);

    @Select("select * from conference where start_date>=#{base}")
    List<Conference> getConferenceItemByDateStart(@Param("base") Timestamp base);

    //end_date
    @Select("select count(*) from conference where end_date>=#{base}")
    Integer getItemsCountByDateEnd(@Param("base") Timestamp base);

    @Select("select * from conference where end_date>=#{base}")
    List<Conference> getConferenceItemByDateEnd(@Param("base") Timestamp base);
}
