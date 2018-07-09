package edu.buaa.acmp.dataAccessLayer.domain;

import edu.buaa.acmp.util.JSON;

import java.math.BigInteger;

public class Participant {
    public BigInteger id;
    public BigInteger register_id;
    public String name;
    public String sex;
    public String job;
    public String contract;
    public BigInteger is_book;
    public String note;//备注
    public BigInteger state;

    public JSON toJSON(){
        JSON part = new JSON();
        part.put("id",id);
        part.put("name",name);
        part.put("sex",sex);
        part.put("job",job);
        part.put("contract",contract);
        part.put("is_book",is_book);
        part.put("note",note);
        part.put("state",state);
        return part;
    }

}
