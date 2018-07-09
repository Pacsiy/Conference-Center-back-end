package edu.buaa.acmp.dataAccessLayer.domain;

import edu.buaa.acmp.util.JSON;
import edu.buaa.acmp.util.StringUtils;
import edu.buaa.acmp.util.TimeConversion;

import java.math.BigInteger;
import java.sql.Timestamp;

public class Conference {
    public BigInteger id;
    public BigInteger institution_id;
    public String title;
    public String introduction;
    public Timestamp start_date;
    public Timestamp end_date;
    public String convening_place;
    public String essay_information;
    public String essay_instructions;
    public Timestamp paper_ddl;
    public Timestamp employ_date;
    public Timestamp register_ddl;
    public String schedule;
    public String paper_template;
    public String register_information;
    public String ATinformation;
    public String contact;
    public BigInteger conference_template;
    public Timestamp release_time;
    public String backimg;
    public BigInteger state;

    public JSON toJson() {
        JSON confer = new JSON();
        confer.put("id", id);
        confer.put("institution_id", institution_id);
        confer.put("title",  StringUtils.lineHelp(title));
        confer.put("introduction", StringUtils.lineHelp(introduction));
        confer.put("start_date", TimeConversion.toString(start_date));
        confer.put("end_date", TimeConversion.toString(end_date));
        confer.put("convening_place",  StringUtils.lineHelp(convening_place));
        confer.put("essay_information", StringUtils.lineHelp(essay_information));
        confer.put("essay_instructions", StringUtils.lineHelp(essay_instructions));
        confer.put("paper_ddl", paper_ddl);
        confer.put("employ_date", TimeConversion.toString(employ_date));
        confer.put("register_ddl", register_ddl);
        confer.put("schedule", StringUtils.lineHelp(schedule));
        confer.put("paper_template", paper_template);
        confer.put("register_information",  StringUtils.lineHelp(register_information));
        confer.put("ATinformation",  StringUtils.lineHelp(ATinformation));
        confer.put("contact",  StringUtils.lineHelp(contact));
        confer.put("conference_template", conference_template);
        confer.put("release_time", release_time);
        confer.put("backimg", backimg);
        confer.put("state", state);

        return confer;
    }

    public JSON searchConferToJson(){
        JSON confer = new JSON();
        confer.put("id",id);
        confer.put("title", StringUtils.lineHelp(title));
        confer.put("introduction", StringUtils.lineHelp(introduction));
        confer.put("start_date",start_date);
        confer.put("convening_place", convening_place);
        confer.put("paper_ddl", paper_ddl);
        confer.put("contact",  StringUtils.lineHelp(contact));
        return confer;
    }

}
