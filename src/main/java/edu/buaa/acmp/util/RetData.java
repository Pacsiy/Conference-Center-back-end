package edu.buaa.acmp.util;

/**
 * 数据返回类
 */
public class RetData {
    private String status;
    private String info;
    private Object data;

    public RetData(String status, String info, Object data) {
        this.status = status;
        this.info = info;
        this.data = data;
    }

    public RetData(String status, Throwable e, Object data) {
        if (e instanceof NullPointerException) {
            this.info = "信息缺失";
        } else {
            while (e.getCause() != null) {
                e = e.getCause();
            }
            this.info = e.getMessage();
        }
        this.status = status;
        this.data = data;
    }

    @Override
    public String toString() {
        JSON jsonObject = new JSON();
        jsonObject.put("status", status);
        jsonObject.put("info", info);
        jsonObject.put("data", data);
        return jsonObject.toString();
    }
}