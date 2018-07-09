package edu.buaa.acmp.serviceLayer;

import com.github.pagehelper.PageHelper;
import edu.buaa.acmp.dataAccessLayer.domain.Contribution;
import edu.buaa.acmp.dataAccessLayer.domain.Review;
import edu.buaa.acmp.dataAccessLayer.mapper.ContributionMapper;
import edu.buaa.acmp.util.PageDataWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ContributionService {
    private final ContributionMapper contributionMapper;

    public ContributionService(ContributionMapper contributionMapper) {
        this.contributionMapper = contributionMapper;
    }

    /**
     * 用户第一次投稿
     * @param contribution 投稿信息
     * @param review 评审信息
     * @throws RuntimeException 数据库出现错误
     */
    public void userContributePapers(Contribution contribution,Review review) throws RuntimeException{
        contributionMapper.insertContribution(contribution);
        review.contribution_id = contribution.id;
        contributionMapper.insertReview(review);
    }

    /**
     * 获得主办方某次会议所受到的所有投稿
     * @param conferID 会议ID
     * @param index 第几页
     * @param size 页面大小
     * @param pageData 统计数据
     * @param type "all", // "pending", "passed", "fixing", "rejected"
     * @return 投稿情况
     */
    public List<Contribution> getAllContributionsByConferID(BigInteger conferID, String index, String size,
                                                            PageDataWrapper pageData, String type){
        try{
            Integer pageNum_num = Integer.valueOf(index);
            Integer pageSize_num = Integer.valueOf(size);
            switch (type) {
                case "all":
                    pageData.pageNum = (int) Math.ceil(1.0 * contributionMapper.getContributionCount(conferID.toString()) / pageSize_num);
                    PageHelper.startPage(pageNum_num, pageSize_num);
                    return contributionMapper.getContribution(conferID.toString());

                case "pending":
                    pageData.pageNum = (int) Math.ceil(1.0 * contributionMapper.getContributionCountCheck(conferID.toString()) / pageSize_num);
                    PageHelper.startPage(pageNum_num, pageSize_num);
                    return contributionMapper.getContributionCheck(conferID.toString());

                case "passed":
                    pageData.pageNum = (int) Math.ceil(1.0 * contributionMapper.getContributionCountPass(conferID.toString()) / pageSize_num);
                    PageHelper.startPage(pageNum_num, pageSize_num);
                    return contributionMapper.getContributionPass(conferID.toString());

                case "fixing":
                    pageData.pageNum = (int) Math.ceil(1.0 * contributionMapper.getContributionCountFix(conferID.toString()) / pageSize_num);
                    PageHelper.startPage(pageNum_num, pageSize_num);
                    return contributionMapper.getContributionFix(conferID.toString());

                case "rejected":
                    pageData.pageNum = (int) Math.ceil(1.0 * contributionMapper.getContributionCountReject(conferID.toString()) / pageSize_num);
                    PageHelper.startPage(pageNum_num, pageSize_num);
                    return contributionMapper.getContributionReject(conferID.toString());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 根据稿件ID获得详细信息
     */
    public Contribution getContributionByID(String contriID){
        return contributionMapper.getContributionDetial(contriID);
    }

    /**
     * 获得某个投稿的修改意见
     * @param contriID 稿件
     * @return 修改意见等
     */
    public List<Review> getReviewsByContributionID(BigInteger contriID){
        try{
            return contributionMapper.getReviewByContriID(contriID.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 获得统计结果
     */
    public ArrayList<Integer> getTotal(BigInteger conferID){
        Integer all = contributionMapper.getContributionCount(conferID.toString());
        Integer passed = contributionMapper.getContributionCountPass(conferID.toString());
        Integer pended = contributionMapper.getContributionCountCheck(conferID.toString());
        Integer fixing = contributionMapper.getContributionCountFix(conferID.toString());
        Integer rejected = contributionMapper.getContributionCountReject(conferID.toString());
        ArrayList<Integer> total = new ArrayList<>();
        total.add(all);
        total.add(passed);
        total.add(pended);
        total.add(fixing);
        total.add(rejected);
        return total;
    }

    /**
     * 修改论文后的提交
     */
    @Transactional
    public void updateContributions(Contribution contribution, Review review){
        contributionMapper.updateContribution(contribution);
        contributionMapper.insertReviewAgain(review);
    }
}
