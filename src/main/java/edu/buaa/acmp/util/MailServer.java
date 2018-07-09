package edu.buaa.acmp.util;

import com.sendgrid.*;

public class MailServer {
    public static Boolean sendEmail(String text, String subject, String to, SendGrid sendGrid){
        try{
            Email from = new Email("buaa_acmp@163.com");
            Email toUser = new Email(to);
            Content content = new Content("text/plain", text);
            Mail mail = new Mail(from, subject, toUser, content);

            Request requests = new Request();
            requests.setMethod(Method.POST);
            requests.setEndpoint("mail/send");
            requests.setBody(mail.build());
            sendGrid.api(requests);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
