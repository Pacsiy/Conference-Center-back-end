package edu.buaa.acmp.controllerLayer;


import com.sendgrid.SendGrid;
import edu.buaa.acmp.controllerLayer.utils.AESToken;
import edu.buaa.acmp.controllerLayer.utils.R;
import edu.buaa.acmp.dataAccessLayer.domain.Institution;
import edu.buaa.acmp.dataAccessLayer.domain.Principal;
import edu.buaa.acmp.serviceLayer.InstitutionService;
import edu.buaa.acmp.util.JSON;
import edu.buaa.acmp.util.MailServer;
import edu.buaa.acmp.util.RetData;
import org.json.JSONArray;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api")
public class InstitutionController {
    private final InstitutionService institutionService;
    private final SendGrid sendGrid;

    public InstitutionController(InstitutionService institutionService, SendGrid sendGrid){
        this.institutionService=institutionService;
        this.sendGrid = sendGrid;
    }

    /**
     * 得到需要审核的机构信息
     */
    @RequestMapping(value = "getInstitutionToCheck")
    public String getInstitutionToCheck(HttpSession session, @RequestBody String request){
        RetData retData;

        try {
//            JSON user = (JSON)session.getAttribute(R.USER_SESSION);
//            if(user == null) throw new RuntimeException("未登录");
            String token = new JSON(request).getString("token");
            JSON user = new JSON(new AESToken().decrypt(token));
            if(!user.getString("type").equals("manager")) throw new RuntimeException("没有管理员权限");
            List<Institution> institutions=institutionService.getInstitutionToCheck();
            if(institutions.size()==0) throw new Exception("no institution");
            JSONArray institutionArray=new JSONArray();
            for(Institution institution:institutions){
                institutionArray.put(institution.toJson());
            }
            retData=new RetData(R.STATE_SUCC,"",institutionArray);
        }catch (Exception e){
            retData=new RetData(R.STATE_FAIL,e.getMessage(),null);
        }
        return retData.toString();
    }

    /**
     * 设置机构的审核状态
     */
    @RequestMapping(value = "setInstitutionStatus/{id}/{status}")
    public String setInstitutionStatus(HttpSession session, @PathVariable String id,@PathVariable String status,
                                       @RequestBody String request){
        RetData retData;
        try{
//            JSON user = (JSON)session.getAttribute(R.USER_SESSION);
//            if(user == null) throw new RuntimeException("未登录");
            String token = new JSON(request).getString("token");
            JSON user = new JSON(new AESToken().decrypt(token));
            if(!user.getString("type").equals("manager")) throw new RuntimeException("没有管理员权限");
            Principal principal = institutionService.getPrincipal(id);
            institutionService.setInstitutionStatus(id, status);
            try {
                //邮件通知
                if (status.equals("1")) {
                    MailServer.sendEmail("恭喜你,贵单位的申请已被通过", "【机构申请审核结果】会议管理平台", principal.email, sendGrid);
                } else {
                    MailServer.sendEmail("很遗憾,由于贵单位材料不齐全等原因，申请无法通过", "【机构申请审核结果】会议管理平台", principal.email, sendGrid);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            retData=new RetData(R.STATE_SUCC,"",null);
        }catch (Exception e){
            retData=new RetData(R.STATE_FAIL,"",null);
        }
        return retData.toString();
    }
}
