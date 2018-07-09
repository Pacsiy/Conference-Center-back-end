package edu.buaa.acmp.serviceLayer;

import edu.buaa.acmp.dataAccessLayer.mapper.MessageMapper;
import edu.buaa.acmp.serviceLayer.uitils.R;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    private final MessageMapper messageMapper;

    public MessageService(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    public void sendMessage(String userID, String type,String suggestion) {
        switch (type) {
            case "1":
                messageMapper.sendMessage(R.Message_Passed+suggestion, userID);
                break;
            case "2":
                messageMapper.sendMessage(R.Message_Modify+suggestion,userID);
                break;
            default:
                messageMapper.sendMessage(R.Message_Rejected+suggestion,userID);
                break;
        }
    }
}
