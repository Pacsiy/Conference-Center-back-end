package edu.buaa.acmp.dataAccessLayer.domain;

import edu.buaa.acmp.util.JSON;

import java.math.BigInteger;

public class User {
    public BigInteger id;
    public String email;
    public String password;
    public String name;
    public String avator;
    public String phone;
    public String agency;
    public String profile;
    public BigInteger state;

    public JSON toJSON(){
        JSON user = new JSON();
        user.put("email",email);
        user.put("name",name);
        user.put("avator",avator);
        user.put("phone",phone);
        user.put("agency",agency);
        user.put("profile",profile);
        return user;
    }
}
