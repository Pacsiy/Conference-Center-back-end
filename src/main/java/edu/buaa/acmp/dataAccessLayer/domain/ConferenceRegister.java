package edu.buaa.acmp.dataAccessLayer.domain;

import edu.buaa.acmp.util.JSON;

import java.math.BigInteger;

public class ConferenceRegister {
    public BigInteger id;
    public BigInteger conference_id;
    public BigInteger user_id;
    public String payment;
    public BigInteger type;
    public String paper_number;
    public BigInteger state;

    public JSON toJSON(){
        JSON register = new JSON();
        register.put("user_id",user_id);
        register.put("payment",payment);
        register.put("type",type);
        register.put("paper_number",paper_number);
        register.put("state",state);
        return register;
    }
}
