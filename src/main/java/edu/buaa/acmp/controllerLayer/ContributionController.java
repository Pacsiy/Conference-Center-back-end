package edu.buaa.acmp.controllerLayer;

import edu.buaa.acmp.controllerLayer.utils.AESToken;
import edu.buaa.acmp.dataAccessLayer.domain.Contribution;
import edu.buaa.acmp.dataAccessLayer.domain.Review;
import edu.buaa.acmp.serviceLayer.ContributionService;
import edu.buaa.acmp.controllerLayer.utils.R;
import edu.buaa.acmp.util.JSON;
import edu.buaa.acmp.util.PageDataWrapper;
import edu.buaa.acmp.util.RetData;
import edu.buaa.acmp.util.UuidUtils;
import org.json.JSONArray;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.util.List;

/**
 * edit by yushijie
 */

@RequestMapping("/api")
@RestController
public class ContributionController {
    private final ContributionService contributionService;

    public ContributionController(ContributionService contributionService) {
        this.contributionService = contributionService;
    }

    /**
     * 用户投稿
     */
    @RequestMapping(value = "contribute",method = RequestMethod.POST)
    public String contribute(@RequestBody String request, HttpSession session){
        JSON req = new JSON(request);
        Contribution contribution = new Contribution();
        contribution.conference_id = req.getBigInteger("conference_id");
        contribution.author = req.getString("authors");
        contribution.abstract_ = req.getString("abstract");
        contribution.title = req.getString("title");
        Review review = new Review();
        review.attachment = req.getString("file_url");
        String token = req.getString("token");
        RetData retData;
        try{
//            JSON user = new JSON(session.getAttribute(R.USER_SESSION).toString());
            JSON user = new JSON(new AESToken().decrypt(token));
            if(!user.getString("type").equals("user")) throw new RuntimeException("普通用户才可以投稿");
            contribution.user_id = user.getBigInteger("id");
            contribution.uploader = user.getString("name");

            contribution.paper_number = UuidUtils.generateUuid();

            contributionService.userContributePapers(contribution,review);
            JSON ret = new JSON();
            ret.put("contribution_id",contribution.id);
            retData = new RetData(R.STATE_SUCC,"投稿成功",ret);
        }catch (Exception e){
            e.printStackTrace();
            retData = new RetData(R.STATE_FAIL,"信息不完整，投稿失败",null);
        }
        return retData.toString();
    }

    /**
     * 主办方及工作人员查看投稿列表
     */
    @RequestMapping(value = "/manage/conference/{id}/contributions")
    public String getContribution(@RequestBody String request,
              @PathVariable("id") BigInteger conferID, HttpSession session) {
        RetData retData;
        JSON re = new JSON(request);
        String index = re.getBigInteger("index").toString();
        String size = re.getBigInteger("size").toString();
        String type = re.getString("type");
        String token = re.getString("token");
        try {
            PageDataWrapper pageData = new PageDataWrapper();
            JSON user = new JSON(new AESToken().decrypt(token));
            if(!user.getString("type").equals("principal")) throw new RuntimeException("单位工作人员才可以审稿");
            List<Contribution> contributions = contributionService.getAllContributionsByConferID(conferID,index,size,pageData,type);
            JSONArray conArr = new JSONArray();
            for(Contribution contribution:contributions){
                JSON item = contribution.toJSON();
//                List<Review> reviews = contributionService.getReviewsByContributionID(contribution.id);
//                JSONArray temp = new JSONArray();
//                for(Review review:reviews){
//                    JSON reviewItem = review.toJSON();
//                    temp.put(reviewItem);
//                }
//                item.put("review",temp);
                conArr.put(item);
            }
            JSON data = new JSON();
            data.put("contributions",conArr);
            data.put("total_num",new JSONArray(contributionService.getTotal(conferID)));
            data.put("page_num",pageData.pageNum);
            retData = new RetData(R.STATE_SUCC,"查看投稿成功",data);
        } catch (Exception e) {
            e.printStackTrace();
            retData = new RetData(R.STATE_FAIL,"查看投稿失败",null);
        }
        return retData.toString();

    }

    /**
     * 查看单个稿件
     */
    @RequestMapping("manage/contribution")
    public String getContributionByID(@RequestBody String request){
        JSON req = new JSON(request);
        String token = req.getString("token");
        String id = req.getBigInteger("id").toString();
        RetData retData;
        try{
            JSON user = AESToken.decode(token);
            Contribution contribution = contributionService.getContributionByID(id);
            JSON data = contribution.toJSON();
            List<Review> reviews = contributionService.getReviewsByContributionID(contribution.id);
            JSONArray temp = new JSONArray();
            for(Review review:reviews){
                JSON reviewItem = review.toJSON();
                temp.put(reviewItem);
            }
            data.put("review",temp);
            retData = new RetData(R.STATE_SUCC,"获取投稿信息成功",data);
        }catch (Exception e){
            e.printStackTrace();
            retData = new RetData(R.STATE_FAIL,"获取投稿信息失败",null);
        }
        return retData.toString();
    }

    /**
     * 修改投稿然后提交
     */
    @RequestMapping("user/updateContribution")
    public String updateContribution(@RequestBody String request){
        JSON req = new JSON(request);
        RetData retData;
        String token = req.getString("token");
        try{
            JSON user = AESToken.decode(token);
            if(!user.getString("type").equals("user")) throw new RuntimeException("类型不是普通用户");
            Contribution contribution = new Contribution();
            contribution.id = req.getBigInteger("contribution_id");
            contribution.title = req.getString("title");
            contribution.abstract_ = req.getString("abstract_");
            contribution.author = req.getString("author");
            Review review = new Review();
            review.contribution_id = contribution.id;
            review.attachment = req.getString("attachment");
            review.description = req.getString("description");
            contributionService.updateContributions(contribution,review);
            retData = new RetData(R.STATE_SUCC,"更新投稿成功",null);
        }catch (Exception e){
            e.printStackTrace();
            retData = new RetData(R.STATE_FAIL,"信息不完整，更新失败",null);
        }
        return retData.toString();
    }
}
