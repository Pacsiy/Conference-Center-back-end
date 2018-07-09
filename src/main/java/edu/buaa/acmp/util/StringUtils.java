package edu.buaa.acmp.util;

public class StringUtils {

    public static String lineHelp(String input){
        StringBuilder ans = new StringBuilder();
        for(int i = 0;i<input.length()-1;i++){
            if(input.charAt(i) == '\\' && input.charAt(i+1) == 'n'){
                ans.append("<br>");
                i = i+1;
                continue;
            }
            ans.append(input.charAt(i));
        }
        if(input.length()>=2&&input.charAt(input.length()-1) != '\\'&&input.charAt(input.length()-1) != 'n'){
            ans.append(input.charAt(input.length()-1));
        }
        return ans.toString();
    }
}
