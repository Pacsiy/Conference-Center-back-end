package edu.buaa.acmp.dataAccessLayer.domain;

import edu.buaa.acmp.util.JSON;

import java.math.BigInteger;
import java.sql.Timestamp;

public class Message {
    public BigInteger id;
    public BigInteger receiver_id;
    public String content;
    public Timestamp sent_time;
    public BigInteger state;
    public Integer is_read;

    public JSON toJSON(){
        JSON message = new JSON();
        message.put("id",id);
        message.put("content",content);
        message.put("sent_time",sent_time.toString());
        message.put("state",is_read);
        return message;
    }
}
