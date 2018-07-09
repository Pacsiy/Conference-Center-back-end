package edu.buaa.acmp.dataAccessLayer.domain;

import edu.buaa.acmp.util.JSON;

import java.math.BigInteger;

public class Institution {
    public BigInteger id;
    public String name;
    public String location;
    public String phone;
    public String backimg;
    public String introduction;
    public String evidence;
    public BigInteger state;

    public JSON toJson(){
        JSON institution=new JSON();
        institution.put("id",id);
        institution.put("name",name);
        institution.put("location",location);
        institution.put("phone",phone);
        institution.put("backimg",backimg);
        institution.put("introduction",introduction);
        institution.put("evidence",evidence);
        institution.put("state",state);
        return institution;
    }
}
