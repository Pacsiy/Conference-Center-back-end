package edu.buaa.acmp.dataAccessLayer.mapper;

import edu.buaa.acmp.dataAccessLayer.domain.Contribution;
import edu.buaa.acmp.dataAccessLayer.domain.Review;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ContributionMapper {
    @Insert("insert into contribution(conference_id,user_id,title,author,abstract_,uploader,paper_number) " +
            "values(#{conference_id},#{user_id},#{title},#{author},#{abstract_},#{uploader},#{paper_number})")
    @Options(useGeneratedKeys = true)
    void insertContribution(Contribution contribution);

    @Insert("insert into review(contribution_id,attachment) values(#{contribution_id},#{attachment})")
    void insertReview(Review review);

    @Select(" SELECT institution_id FROM conference WHERE id = #{conference_id} ")
    Integer getConferenceInstitutionID(@Param("conference_id")String conference_id);

    //获取某会议投稿（全部）
    @Select(" SELECT * from contribution " +
            " WHERE conference_id = #{conferID} " +
            " ORDER BY total_submit DESC ")
    List<Contribution> getContribution(@Param("conferID") String conferID);
    //获取某会议投稿（未审核）
    @Select(" SELECT * from contribution " +
            " WHERE conference_id = #{conferID} and total_result = 0 " +
            " ORDER BY total_submit DESC ")
    List<Contribution> getContributionCheck(@Param("conferID") String conferID);
    //获取某会议投稿（通过）
    @Select(" SELECT * from contribution " +
            " WHERE conference_id = #{conferID} and total_result = 1 " +
            " ORDER BY total_submit DESC ")
    List<Contribution> getContributionPass(@Param("conferID") String conferID);
    //获取某会议投稿（修改）
    @Select(" SELECT * from contribution " +
            " WHERE conference_id = #{conferID} and total_result = 2 " +
            " ORDER BY total_submit DESC ")
    List<Contribution> getContributionFix(@Param("conferID") String conferID);
    //获取某会议投稿（拒绝）
    @Select(" SELECT * from contribution " +
            " WHERE conference_id = #{conferID} and total_result = 3 " +
            " ORDER BY total_submit DESC ")
    List<Contribution> getContributionReject(@Param("conferID") String conferID);

    //查看某会议投稿总数
    @Select(" SELECT count(*) FROM contribution WHERE conference_id = #{conference_id} ")
    Integer getContributionCount(@Param("conference_id")String conference_id);
    //查看某会议投稿未审核数
    @Select(" SELECT count(*) FROM contribution WHERE conference_id = #{conference_id} and total_result = 0 ")
    Integer getContributionCountCheck(@Param("conference_id")String conference_id);
    //查看某会议投稿通过数
    @Select(" SELECT count(*) FROM contribution WHERE conference_id = #{conference_id} and total_result = 1 ")
    Integer getContributionCountPass(@Param("conference_id")String conference_id);
    //查看某会议投稿修改数
    @Select(" SELECT count(*) FROM contribution WHERE conference_id = #{conference_id} and total_result = 2 ")
    Integer getContributionCountFix(@Param("conference_id")String conference_id);
    //查看某会议投稿拒绝数
    @Select(" SELECT count(*) FROM contribution WHERE conference_id = #{conference_id} and total_result = 3 ")
    Integer getContributionCountReject(@Param("conference_id")String conference_id);

    //查看投稿信息
    @Select(" SELECT * FROM contribution WHERE id = #{contriID} ")
    Contribution getContributionDetial(@Param("contriID") String contriID);

    //用户更新投稿
    @Update(" UPDATE contribution SET title=#{title},author=#{author},abstract_=#{abstract_},total_result=0 " +
            " WHERE id = #{id} ")
    void updateContribution(Contribution contribution);
    @Insert(" insert into review(contribution_id,attachment,description) " +
            " values(#{contribution_id},#{attachment},#{description})")
    void insertReviewAgain(Review review);

    //Review表相关
    @Select(" SELECT * FROM review WHERE contribution_id = #{contriID} ")
    List<Review> getReviewByContriID(@Param("contriID") String contriID);
}
