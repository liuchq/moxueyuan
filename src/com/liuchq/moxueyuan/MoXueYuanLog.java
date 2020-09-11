package com.liuchq.moxueyuan;

import java.io.*;
import java.time.LocalDateTime;

/**
 * @author: liuchq
 * @date: 2020/9/9 17:34
 * @description: 记录日志
 */
public class MoXueYuanLog {

    /**
     * 记录日志
     * @author: liuchq
     * @date: 2020/9/9 17:35
     * @param text     日志文本
     * @return: void
     */
    public static void setLog(String text,Throwable ex){
        PrintWriter pw = null;
        try {
            FileOutputStream out = new FileOutputStream(MoXueYuanConstant.LOG_ADDRESS,true);
            OutputStreamWriter writer = new OutputStreamWriter(out);
            pw = new PrintWriter(writer);
            if (ex != null){
                ex.printStackTrace();
                pw.println(errorToString(ex));
            }
            String msg = LocalDateTime.now()+"---"+text;
            pw.println(msg);
            System.out.println(msg);
        }catch (FileNotFoundException e){
            e.printStackTrace();
            System.out.println("日志存储的地址不正确"+MoXueYuanConstant.LOG_ADDRESS);
        }
        pw.close();
    }


    /**
     * 异常堆栈转String
     * @author: liuchq
     * @date: 2020/9/9 18:01
     * @param e  异常
     * @return: java.lang.String  String
     */
    private static String errorToString(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

}
