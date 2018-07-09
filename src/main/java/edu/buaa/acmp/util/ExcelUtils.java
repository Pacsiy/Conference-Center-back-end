package edu.buaa.acmp.util;

import edu.buaa.acmp.dataAccessLayer.domain.*;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONArray;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelUtils {
    public final static String EXPORT_PATH =  "/usr/local/tomcat9/webapps/export/";

    /**
     * 导出某会议的投稿情况
     * @param data 投稿数据
     * @throws IOException 读写异常处理
     */
    public static String exportContributions(List<Contribution> data) throws Exception {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("投稿情况");
        HSSFRow titleRow = sheet.createRow(0);

        titleRow.createCell(0).setCellValue("题目");
        titleRow.createCell(1).setCellValue("摘要");
        titleRow.createCell(2).setCellValue("论文编号");
        titleRow.createCell(3).setCellValue("上传者姓名");
        titleRow.createCell(4).setCellValue("作者姓名");
        titleRow.createCell(5).setCellValue("机构");
        titleRow.createCell(6).setCellValue("邮箱");

        for(int i=0;i<data.size();i++){
            Contribution cur = data.get(i);
            HSSFRow row = sheet.createRow(i+1);
            row.createCell(0).setCellValue(cur.title);
            row.createCell(1).setCellValue(cur.abstract_);
            row.createCell(2).setCellValue(cur.paper_number);
            row.createCell(3).setCellValue(cur.uploader);
            JSONArray authors = new JSONArray(cur.author);
            StringBuilder authorsName = new StringBuilder();
            StringBuilder authorsIns = new StringBuilder();
            StringBuilder authorsEmail = new StringBuilder();
            for(int j = 0;j<authors.length();j++) {
                authorsName.append(authors.getJSONObject(j).getString("name"));
                authorsIns.append(authors.getJSONObject(j).getString("institution"));
                authorsEmail.append(authors.getJSONObject(j).getString("email"));
                if(j != authors.length()-1){
                    authorsName.append(";");
                    authorsIns.append(";");
                    authorsEmail.append(";");
                }
            }
            row.createCell(4).setCellValue(authorsName.toString());
            row.createCell(5).setCellValue(authorsIns.toString());
            row.createCell(6).setCellValue(authorsEmail.toString());
        }
        String path = UuidUtils.generateUuid()+".xls";
        FileOutputStream fos = new FileOutputStream(new File(EXPORT_PATH+path));
        wb.write(fos);
        wb.close();
        fos.close();
        System.out.println("导出完成");
        return "/export/"+path;
    }

    /**
     * 导出注册人员信息
     *
     */
    public static String exportRegisters(List<ConferenceRegister> registers, List<Participant> participants) throws Exception{
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("注册人员名单");
        HSSFRow titleRow = sheet.createRow(0);

        titleRow.createCell(0).setCellValue("论文编号");
        titleRow.createCell(1).setCellValue("姓名");
        titleRow.createCell(2).setCellValue("性别");
        titleRow.createCell(3).setCellValue("职业");
        titleRow.createCell(4).setCellValue("联系方式");
        titleRow.createCell(5).setCellValue("是否预定住宿");
        titleRow.createCell(6).setCellValue("备注");
        titleRow.createCell(7).setCellValue("类型");

        // TODO: 2018/7/3 暂有疑问，明天完成
        for(int i=0;i<participants.size();i++){
            Participant participant = participants.get(i);
            HSSFRow row = sheet.createRow(i+1);
            row.createCell(1).setCellValue(participant.name);
            row.createCell(2).setCellValue(participant.sex);
            row.createCell(3).setCellValue(participant.job);
            row.createCell(4).setCellValue(participant.contract);
            if(participant.is_book.toString().equals("1")){
                row.createCell(5).setCellValue("是");
            }else {
                row.createCell(5).setCellValue("否");
            }
            row.createCell(6).setCellValue(participant.note);
            for(ConferenceRegister register:registers){
                if(register.id.toString().equals(participant.register_id.toString())){
                    if(register.type.toString().equals("0")){
                        row.createCell(7).setCellValue("投稿人");
                    }else {
                        row.createCell(7).setCellValue("旁听者");
                    }
                    row.createCell(0).setCellValue(register.paper_number);
                }
            }

        }
        String path = UuidUtils.generateUuid()+".xls";
        FileOutputStream fos = new FileOutputStream(new File(EXPORT_PATH+path));
        wb.write(fos);
        wb.close();
        fos.close();
        System.out.println("导出完成");
        return "/export/"+path;
    }
}
