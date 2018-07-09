package edu.buaa.acmp.dataAccessLayer.domain;

import edu.buaa.acmp.util.JSON;
import org.json.JSONArray;

import java.math.BigInteger;
import java.sql.Timestamp;

public class Contribution {
    public BigInteger id;
    public BigInteger conference_id;
    public BigInteger user_id;
    public String title;
    public String uploader;
    public String author;
    public String abstract_;
    public Timestamp total_submit;
    public String total_result;
    public String paper_number;
    public BigInteger state;

    public JSON toJSON(){
        JSON con = new JSON();
        con.put("id",id);
        con.put("conference_id",conference_id);
        con.put("user_id",user_id);
        con.put("uploader",uploader);
        con.put("title",title);
        con.put("abstract_",abstract_);
        con.put("total_submit",total_submit);
        con.put("author",new JSONArray(author));
        con.put("total_result",total_result);
        con.put("paper_number",paper_number);
        con.put("state",state);
        return con;
    }
}
