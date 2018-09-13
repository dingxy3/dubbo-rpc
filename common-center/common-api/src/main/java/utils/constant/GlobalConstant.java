/*
 * 文 件 名:  GlobalConstant
 * 版    权:  AsiaInfo Technologies (Nanjing), Inc. Copyright 1993-2016,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  gejun
 * 修改时间:  2017年07月28日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.ai.rai.interests.common.Constant;

/**
 * GlobalConstant
 * 系统级常量定义类
 *
 * @author gejun
 * @version [版本号, 2017年07月28日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class GlobalConstant
{

    public static final String RESPONSE_SECCESS = "1";

    public static final String RESPONSE_FAIL = "0";
    
    /**上海省份编码：31*/
    public static final String PROVINCE_CODE_SHANGHAI ="31";
    
    public static final String CHARTSET_UTF_8="UTF-8";
    
    public static final String TEXT_DOC_HTML=".html";
    public static final String TEXT_HTML="html";

    /**
     * 分页常量类
     */
    public static class PageInfo
    {

        //分页每页条数
        public static final int PAGE_SIZE = 10;

    }

    /**
     * 系统异常
     */
    public static final String SYS_ERR_CODE = "999999";

    /**
     * 有效
     */
    public static final String VALID = "1";

    /**
     * 无效
     */
    public static final String IN_VALID = "0";

    /**
     * 平台
     */
    public static String STAFF_TYPE_PT = "1";

    /**
     * 合作伙伴
     */
    public static String STAFF_TYPE_HZ = "2";

    /**
     * 平台 中文
     */
    public static  String STAFF_TYPE_PT_CN="平台";

    /**
     * 合作伙伴 中文
     */
    public static String STAFF_TYPE_HZ_CN ="合作伙伴";




    /**
     * 菜单类型-菜单
     */
    public static final String MEAU_TYPE_MEAU="0";


    /**
     * 菜单类型-操作
     */
    public static final String MEAU_TYPE_OPER="1";

    /**
     * 菜单类型-菜单中文名称
     */
    public static final String MEAU_TYPE_MEAU_CN="菜单";

    /**
     * 菜单类型-按钮中文名称
     */
    public static final String MEAU_TYPE_OPER_CN="按钮";

    /**
     * 顶层菜单编码
     */
    public static final String TOP_MENU_CODE="0";

    /**
     * 全国编码
     */
    public static final String COUNTRY_CODE = "ZZZZ";

    /**
     * 短信有效时间
     */
    public static final int  NMINUTE = 5;

    /**
     * 验证码位数
     */
    public static final int VERIFY_CODE = 6 ;

    /**
     * 首页
     */
    public static final String PAGE_INDEX = "http://qy.chinaunicom.cn/mobile-h5/main/home.html";

    /**
     * 我的卡券
     */
    public static final String PAGE_CARDLIST = "http://qy.chinaunicom.cn/cc";
}