package edu.buaa.acmp.dataAccessLayer.mapper;

import edu.buaa.acmp.dataAccessLayer.domain.Conference;
import edu.buaa.acmp.dataAccessLayer.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface IndexMapper {
    @Select(" SELECT * FROM conference ORDER BY release_time DESC LIMIT 3 ")
    List<Conference> getNewConference();

    @Select(" SELECT user.* FROM user " +
            " JOIN contribution ON user.id = contribution.user_id " +
            " WHERE contribution.total_result = 1 " +
            " GROUP BY contribution.user_id " +
            " ORDER BY count(*) ")
    List<User> getactiveScholars();

    //开始征稿
    @Select(" SELECT * FROM conference " +
            " WHERE conference.release_time > now() " +
            " ORDER BY conference.release_time LIMIT 2 ")
    List<Conference> getNewIssuesPaperStart();
    //停止征稿
    @Select(" SELECT * FROM conference " +
            " WHERE conference.paper_ddl > now() " +
            " ORDER BY conference.paper_ddl LIMIT 2 ")
    List<Conference> getNewIssuesPaperddl();
    //停止注册
    @Select(" SELECT * FROM conference " +
            " WHERE conference.register_ddl > now() " +
            " ORDER BY conference.register_ddl LIMIT 2 ")
    List<Conference> getNewIssuesRegisterddl();
    //会议开始
    @Select(" SELECT * FROM conference " +
            " WHERE conference.start_date > now() " +
            " ORDER BY conference.start_date LIMIT 2 ")
    List<Conference> getNewIssuesStart();
}
