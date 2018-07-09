package edu.buaa.acmp.util;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeConversion {
    /**
     * 已知：截稿时间 < 录用时间 < 截止注册时间 < 开始时间 < 结束时间
     * 判断与现在时间大小决定状态
     * @param start 开始时间
     * @param end 结束时间
     * @param paper 截稿日期
     * @param employ 录用日期
     * @param register 注册日期
     * @return 状态数
     */
    public static BigInteger getConferenceState(Timestamp start, Timestamp end,
                                                Timestamp paper, Timestamp employ,
                                                Timestamp register) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if(now.after(end)) {
            return new BigInteger("8");//会议已结束
        } else if (now.after(start)) {
            return new BigInteger("4");//会议进行中
        } else if (now.after(register)) {
            return new BigInteger("0");//已截稿，停止注册，会议即将开始
        } else if (now.after(paper)) {
            return new BigInteger("2");//已截稿，会议注册中
        } else {
            return new BigInteger("3");//征稿中
        }
    }

    /**
     * 时间
     * @param original:Jan 18 2018 00:00:00
     */
    public static Timestamp toDateTime(String original) {
        String[] info = original.split(" ");
        String year = info[2];
        String month = getMonthNumber(info[0]);
        String day = info[1];
        if(day.length() < 2) day = "0" + day;
        String time = info[3];
        String date = year + month + day + " " + time;

        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
            Date newDate = simpleDateFormat.parse(date);

            return new Timestamp(newDate.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toString(Timestamp sqlTime) {
        String date = sqlTime.toString();
        String year = date.substring(0, 4);
        String month = getMonthABC(date.substring(5, 7));
        String day = date.substring(8, 10);

        return month+" "+day+","+year;
    }

    private static String getMonthNumber(String m) {
        switch (m) {
            case "Jan":
                return "01";
            case "Feb":
                return "02";
            case "Mar":
                return "03";
            case "Apr":
                return "04";
            case "May":
                return "05";
            case "Jun":
                return "06";
            case "Jul":
                return "07";
            case "Aug":
                return "08";
            case "Sep":
                return "09";
            case "Oct":
                return "10";
            case "Nov":
                return "11";
            case "Dec":
                return "12";
            default:
                return "01";
        }
    }

    private static String getMonthABC(String m) {
        switch (m) {
            case "01":
                return "Jan";
            case "02":
                return "Feb";
            case "03":
                return "Mar";
            case "Apr":
                return "04";
            case "05":
                return "May";
            case "06":
                return "Jun";
            case "07":
                return "Jul";
            case "08":
                return "Aug";
            case "09":
                return "Sep";
            case "10":
                return "Oct";
            case "11":
                return "Nov";
            case "12":
                return "Dec";
            default:
                return "Jan";
        }
    }
}
