package com.liuchq.moxueyuan;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * @author: liuchq
 * @date: 2020/9/8 17:53
 * @description: 开始方法
 */
public class StartStudy {

    public static void main(String[] args){
        MoXueYuanLog.setLog("开始学习---本次学习设定的运行时间为"+MoXueYuanConstant.RUN_TIME+"秒",null);
        try {
            //获取全部课程ID
            List<Map<String,String>> mapList = MoXueYuanStudy.getCourse();
            //todo  是否此处可以用多线程解决，然后请求速度加快
            for (Map<String, String> map : mapList) {
                //开始课程学习
                MoXueYuanLog.setLog("---本次学习课程ID："+map.get(MoXueYuanEnum.COURSE_ID_NAME.getValue())+","+map.get(MoXueYuanEnum.CHAPTER_ID_NAME.getValue()),null);
                MoXueYuanStudy.courseStudyMethod(map.get(MoXueYuanEnum.COURSE_ID_NAME.getValue()),
                        map.get(MoXueYuanEnum.CHAPTER_ID_NAME.getValue()));
            }
        }catch (FileNotFoundException e){
            MoXueYuanLog.setLog("桌面文件没找到---"+MoXueYuanConstant.COURSE_ID_ADDRESS+e.getMessage(),e);
        }catch (IOException e){
            MoXueYuanLog.setLog("解析文件出错---"+e.getMessage(),e);
        }catch (Exception e){
            MoXueYuanLog.setLog("自动学习课程出错，请分析日志或者前往调试---",e);
        }
        MoXueYuanLog.setLog("学习结束",null);
    }
}
