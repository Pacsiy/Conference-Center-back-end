package edu.buaa.acmp.controllerLayer;

import com.sendgrid.*;
import edu.buaa.acmp.controllerLayer.utils.R;
import edu.buaa.acmp.dataAccessLayer.domain.User;
import edu.buaa.acmp.serviceLayer.UserService;
import edu.buaa.acmp.util.JSON;
import edu.buaa.acmp.util.RetData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MailController {
    private final SendGrid sendGrid;
    private final UserService userService;

    @Autowired
    public MailController(SendGrid sendGrid, UserService userService) {
        this.sendGrid = sendGrid;
        this.userService = userService;
    }
    @RequestMapping("/api/email/send")
    public String sendEmail(@RequestBody String request){
        RetData retData;
        try{
            JSON re = new JSON(request);
            String text = re.getString("text");
            String subject = re.getString("subject");
            String to = re.getString("to");
            User user = userService.getUserInfoByID(to);

            Email from = new Email("buaa_acmp@163.com");
            Email toUser = new Email(user.email);
            Content content = new Content("text/plain", text);
            Mail mail = new Mail(from, subject, toUser, content);

            Request requests = new Request();
            requests.setMethod(Method.POST);
            requests.setEndpoint("mail/send");
            requests.setBody(mail.build());
            sendGrid.api(requests);
            retData = new RetData(R.STATE_SUCC,"",null);
        }catch (Exception e){
            e.printStackTrace();
            retData = new RetData(R.STATE_FAIL,e.getMessage(),null);
        }
        return retData.toString();
    }
}
