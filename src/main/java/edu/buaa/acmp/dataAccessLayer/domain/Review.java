package edu.buaa.acmp.dataAccessLayer.domain;

import edu.buaa.acmp.util.JSON;

import java.math.BigInteger;
import java.sql.Timestamp;

public class Review {
    public BigInteger id;
    public BigInteger contribution_id;
    public String description;
    public String attachment;
    public Timestamp submit_time;
    public String result;
    public String suggestion;
    public Timestamp review_time;
    public BigInteger state;

    public JSON toJSON(){
        JSON review = new JSON();
        review.put("id",id);
        review.put("description",description);
        review.put("attachment",attachment);
        review.put("submit_time",submit_time);
        review.put("result",result);
        review.put("suggestion",suggestion);
        review.put("review_time",review_time);
        review.put("state",state);
        return review;
    }
}
