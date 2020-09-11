package com.liuchq.moxueyuan;

/**
 * @author: liuchq
 * @date: 2020/9/9 16:22
 * @description: 常量列表
 *
 * 其实此处直接使用静态常量表示是最简单得，用这个Enum反而使得代码复杂化，
 * ENUM的使用，应该是元素值之间有着某种规律，ENUM可以方便扩展
 * 类似这种无规律的，以后应该避免使用ENUM
 */
public enum MoXueYuanEnum {

    /**
     * 公共得URL
     */
    URL_START("https://api.moxueyuan.com/appapi.php/index?r=apiCourse/setCourseCompleted&test=pc"),


    /**
     * 课程ID  key（键）
     */
    COURSE_ID_NAME("courseid"),
    CHAPTER_ID_NAME("chapterid"),

    /**
     * 学习完毕标识
     */
    STUDY_FINISHED("学习完毕"),

    /**
     * 学习出错标识
     */
    STUDY_ERROR("学习出错"),


    /**
     * 判断返回参数合法的随便选的一个字段
     */
    COMPLETED("completed"),

    /**
     * 请求状态
     */
    REQUEST_STATUS("status"),

    /**
     * 返回值
     */
    RETURN_MSG("msg"),

    /**
     * 课程是否学习完成
     */
    COURSE_COMPLETED("courseCompleted"),

    /**
     * 学习课程需要得时间  单位 秒   例如 20
     */
    NEET_TIME("needTime"),

    /**
     * 课程已经学过得时间  单位 秒   例如  20
     */
    CUM_TIME("cumtime"),

    /**
     * 已经学过得时间   单位  分秒   例如  1分21秒
     */
    VALITD_TIME("validtime");


    private String value;

    private MoXueYuanEnum(String value) {
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
