package edu.buaa.acmp.dataAccessLayer.domain;

import edu.buaa.acmp.util.JSON;

import java.math.BigInteger;

public class Principal {
    public BigInteger id;
    public BigInteger belongIns;
    public String email;
    public String password;
    public String name;
    public String avator;
    public String location;
    public String phone;
    public String power;
    public BigInteger state;

    public JSON toJSON(){
        JSON principal = new JSON();
        principal.put("id",id);
        principal.put("email",email);
        principal.put("name",name);
        principal.put("avator",avator);
        principal.put("location",location);
        principal.put("phone",phone);
        if(power.equals("ALL")){
            principal.put("power","all");
        }else {
            principal.put("power","general");
        }
        return principal;
    }

}
