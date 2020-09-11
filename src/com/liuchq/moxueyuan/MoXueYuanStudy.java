package com.liuchq.moxueyuan;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: liuchq
 * @date: 2020/9/9 16:52
 * @description: 学习进程
 */
public class MoXueYuanStudy {

    /**
     * 获取课程
     * @author: liuchq
     * @date: 2020/9/9 15:08
     * @param
     * @return: java.util.List<java.util.Map < java.lang.String, java.lang.String>>
     */
    public static List<Map<String,String>> getCourse() throws IOException {
        List<Map<String,String>> courseList = new ArrayList<>();

        FileReader fileReader = new FileReader(MoXueYuanConstant.COURSE_ID_ADDRESS);
        BufferedReader reader = new BufferedReader(fileReader);
        String len = reader.readLine();
        while (len != null && !MoXueYuanUtil.isBlank(len)){
            String finalLen = len.trim();
            len = reader.readLine();
            if (finalLen.contains(MoXueYuanEnum.STUDY_FINISHED.getValue())
                ||finalLen.contains(MoXueYuanEnum.STUDY_ERROR.getValue())){
                //做出判断，如果是已经学完的，或者学习出错的。就跳过
                MoXueYuanLog.setLog(finalLen,null);
                continue;
            }
            courseList.add(new HashMap<String,String>(2){
                {
                    String[] ids = finalLen.split(",");
                    put(MoXueYuanEnum.COURSE_ID_NAME.getValue(),ids[0]);
                    put(MoXueYuanEnum.CHAPTER_ID_NAME.getValue(),ids[1]);
                }
            });
        }
        return courseList;
    }


    /**
     * 自动学习
     * @author: liuchq
     * @date: 2020/9/9 15:07
     * @param
     * @return: void
     */
    public static void courseStudyMethod(String courseid,String chapterid){
        //参数集合
        Map<String,String> map = MoXueYuanUtil.setParam(
                MoXueYuanConstant.USER_ID,
                MoXueYuanConstant.TOKEN,
                MoXueYuanConstant.EID,
                MoXueYuanConstant.VERSION,
                courseid,chapterid);
        //url拼接
        String requestUrl = MoXueYuanUtil.urlMakeUp(MoXueYuanEnum.URL_START.getValue(),map);
        //发起请求
        String returnMsg = MoXueYuanUtil.requestMoHttp(requestUrl);
        //处理返回值
        MoXueYuanUtil.readMsg(returnMsg,chapterid);
    }

    /**
     * 当课程学习完毕后，在课程ID文本中写入学习完毕的标志
     * @author: liuchq
     * @date: 2020/9/9 19:17
     * @param courseID       课程ID
     * @param finishSign     学习完成标志 或者 学习错误标志+返回MSG
     * @param fileAddress    课程ID地址
     * @return: void
     */
    public static void setStudyFinishSign(String courseID, String finishSign, String fileAddress){
        try {
            //存储修改好的字符
            List<String> strList = new ArrayList<>();
            //定义换行符
            String newLine = System.getProperty("line.separator");

            //此处逻辑是先读取，修改，存好后，写入
            FileReader fileReader = new FileReader(fileAddress);
            BufferedReader reader = new BufferedReader(fileReader);
            String len = reader.readLine();
            while (len != null && !MoXueYuanUtil.isBlank(len)) {
                String finalLen = len;
                len = reader.readLine();
                if (finalLen.contains(courseID)){
                    strList.add(finalLen+finishSign);
                }else {
                    strList.add(finalLen);
                }
                strList.add(newLine);
            }

            //现在开始写入
            FileOutputStream out = new FileOutputStream(fileAddress);
            for (String s : strList) {
                out.write(s.getBytes());
            }
            out.close();
        }catch (IOException e){
            MoXueYuanLog.setLog("setStudyFinishSign方法出错",e);
        }
    }
}
