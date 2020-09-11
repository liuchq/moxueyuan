package com.liuchq.moxueyuan;

import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author: liuchq
 * @date: 2020/9/8 17:51
 * @description: 请求工具类
 */
public class MoXueYuanUtil {


    /**
     * 请求
     * @author: liuchq
     * @date: 2020/9/8 19:03
     * @param imageUrl     地址
     * @return: java.lang.String 返回结果
     */
    public static String requestMoHttp(String imageUrl){
        String str = "";
        try {
            MoXueYuanLog.setLog("开始请求moxueyuan---"+imageUrl,null);
            URL url = new URL(imageUrl);
            //打开和url之间的连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            OutputStreamWriter out = null;
            //请求方式
            conn.setRequestMethod("POST");
            //设置通用的请求属性
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Connection", "Keep-Alive");
            //设置是否向httpUrlConnection输出，设置是否从httpUrlConnection读入，此外发送post请求必须设置这两个
            //最常用的Http请求无非是get和post，get请求可以获取静态页面，也可以把参数放在URL字串后面，传递给servlet，
            //post与get的 不同之处在于post的参数不是放在URL字串里面，而是放在http请求的正文内。
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            //获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
            //发送请求参数即数据
            out.write("");
            //缓冲数据
            out.flush();
            //获取URLConnection对象对应的输入流
            InputStream is = conn.getInputStream();
            //构造一个字符流缓存
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
            str = br.readLine();
            if (str != null) {
                MoXueYuanLog.setLog("返回值---"+str,null);
            }
            //关闭流
            is.close();
            //断开连接，最好写上，disconnect是在底层tcp socket链接空闲时才切断。如果正在被其他线程使用就不切断。
            //固定多线程的话，如果不disconnect，链接会增多，直到收发不出信息。写上disconnect后正常一些。
            conn.disconnect();
        }catch (Exception e){
            MoXueYuanLog.setLog("请求moxueyuan出错---"+e.getMessage(),e);
        }
        return str;
    }

    /**
     * 组装参数
     * @author: liuchq
     * @date: 2020/9/8 19:03
     * @param userID 用户ID
     * @param token
     * @param eid
     * @param version  魔学院版本
     * @param courseID   课程ID
     * @param chapterID     课程ID
     * @return: java.util.Map<java.lang.String, java.lang.String>
     */
    public static Map<String, String> setParam(String userID,String token,String eid,
                                               String version,String courseID,String chapterID){
        Map<String,String> map = new HashMap<>();
        map.put("platform","pcweb");
        map.put("completed","1");
        map.put("percent","0.5");
        map.put("durgress","60");
        map.put("cumtime","60");
        map.put("usePlaybackRates","1");

        map.put("userid",userID);
        map.put("token",token);
        map.put("eid",eid);
        map.put("version",version);
        map.put("courseid",courseID);
        map.put("chapterid",chapterID);

        return map;
    }

    /**
     * 拼接URL
     * @author: liuchq
     * @date: 2020/9/8 19:04
     * @param url    初始地址
     * @param map     请求参数
     * @return: java.lang.String 拼接好的URL
     */
    public static String urlMakeUp(String url,Map<String,String> map){
        StringBuilder urlSB = new StringBuilder(url);

        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()){
            String key = iterator.next();
            urlSB.append("&").append(key).append("=").append(map.get(key));
        }

        return  urlSB.toString();
    }

    /**
     * 解析返回值
     * @author: liuchq
     * @date: 2020/9/8 19:04
     * @param returnMsg     返回值
     * @return: void
     */
    public static void readMsg(String returnMsg,String chapterid){
        try{
            JSONObject jsonObject = (JSONObject)JSONObject.parse(returnMsg);
            if (!jsonObject.containsKey(MoXueYuanEnum.COMPLETED.getValue())){
                String msg = jsonObject.get(MoXueYuanEnum.RETURN_MSG.getValue()).toString();
                if (!isBlank(msg)){
                    MoXueYuanLog.setLog(msg,null);
                    MoXueYuanStudy.setStudyFinishSign(chapterid,
                            MoXueYuanEnum.STUDY_ERROR.getValue()+msg,
                            MoXueYuanConstant.COURSE_ID_ADDRESS);
                }else {
                    MoXueYuanLog.setLog("解析返回值出错啦：未能识别返回MSG,请看输出得返回值是否正确",null);
                }
            }
            //课程学习是否完成
            String courseCompleted = jsonObject.get(MoXueYuanEnum.COURSE_COMPLETED.getValue()).toString();
            //学习这个课程需要得时间  单位秒
            String needTime = jsonObject.get(MoXueYuanEnum.NEET_TIME.getValue()).toString();
            //已经学习得时间  单位秒
            String cumtime = jsonObject.get(MoXueYuanEnum.CUM_TIME.getValue()).toString();

            if ("Y".equals(courseCompleted) || needTime.equals(cumtime)){
                MoXueYuanLog.setLog("####"+MoXueYuanEnum.CHAPTER_ID_NAME.getValue()+"="+jsonObject.get(MoXueYuanEnum.CHAPTER_ID_NAME.getValue()).toString()+"的课程已经学习完毕",null);
                //写入学习完成标志到 课程ID 文件中
                MoXueYuanStudy.setStudyFinishSign(jsonObject.get(MoXueYuanEnum.CHAPTER_ID_NAME.getValue()).toString(),
                        MoXueYuanEnum.STUDY_FINISHED.getValue(),
                        MoXueYuanConstant.COURSE_ID_ADDRESS);
            }else {
                int i = Integer.valueOf(needTime) - Integer.valueOf(cumtime);
                MoXueYuanLog.setLog("####"+MoXueYuanEnum.CHAPTER_ID_NAME.getValue()+"="+jsonObject.get(MoXueYuanEnum.CHAPTER_ID_NAME.getValue()).toString()+"的课程已学习"+jsonObject.get(MoXueYuanEnum.VALITD_TIME.getValue())+"；还需要学习"+i+"秒",null);
            }
        }catch (Exception e){
            MoXueYuanLog.setLog("解析返回值出错啦："+e.getMessage(),e);
        }
    }

    /**
     * 判断字符串是否为null 或者 ""
     * @author: liuchq
     * @date: 2020/9/10 8:29
     * @param cs     字符串
     * @return: java.lang.Boolean
     */
    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
