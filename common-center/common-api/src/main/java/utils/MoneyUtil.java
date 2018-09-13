package utils;

import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;


/**
 * 金额转化类
 * Title: MVNO-CRM <br>
 * Description: <br>
 * Date: 2014-10-25 <br>
 * Copyright (c) 2014 AILK <br>
 *
 * @author liangyi
 * @update liwb3
 */
public class MoneyUtil {
    /**
     * 把精确到分的整型转换为货币字符串。如果结尾为“.00”，则省去该结尾，否则肯定保留两位小数，不足补0。
     *
     * @param cent 精确到分的整型
     */
    public static String formatCentToYuan(Long cent) {
        if (null == cent) return "";
        if (cent < 0) return "-" + formatCentToYuan(-cent);
        if (0 == cent) return "0";

        if (10 > cent) return "0.0" + cent;
        if (100 > cent) return "0." + cent;

        String centStr = cent.toString();
        int pointIndex = centStr.length() - 2;

        String integer = centStr.substring(0, pointIndex);
        String fraction = centStr.substring(pointIndex);

        if ("00".equals(fraction)) return integer;

        return integer + "." + fraction;
    }

    /**
     * @see #formatCentToYuan(Long)
     */
    public static String formatCentToYuan(Integer cent) {
        return formatCentToYuan(Long.valueOf(cent));
    }

    /**
     * 按照设置的精度把元转为分
     *
     * @param yuanStr
     * @param scale
     * @return
     * @author linwb3
     */
    public static long convertYuanToCent(String yuanStr, int scale) throws Exception {
        try {
            return (new BigDecimal(yuanStr).setScale(scale).multiply(new BigDecimal(100))).longValue();
        }
        catch (Exception e) {
            throw new Exception("把String转化为数字失败");
        }
    }

    /**
     * 把元转为分
     *
     * @param yuanStr
     * @return
     * @author liangyi
     */
    public static long convertYuanToCent(String yuanStr) throws Exception {
        try {
            return (new BigDecimal(yuanStr).multiply(new BigDecimal(100))).longValue();
        }
        catch (Exception e) {
            throw new Exception("把String转化为数字失败");
        }
    }

    /**
     * 把元转为分
     *
     * @param yuanStr
     * @return
     * @author liangyi
     */
    public static int convertYuanToCentRetInt(String yuanStr) throws Exception {
        try {
            return (new BigDecimal(yuanStr).multiply(new BigDecimal(100))).intValue();
        }
        catch (Exception e) {
            throw new Exception("把String转化为数字失败");
        }
    }

    /**
     * 把分转为元
     *
     * @param yuanStr
     * @return
     * @author liangyi
     */
    public static String convertCentToYuan(long cent) {
    	String result = (new BigDecimal(cent).divide(new BigDecimal(100))).toString();
        return result;
    }

    /**
     * 把分转为元
     *
     * @param yuanStr
     * @return
     * @author liangyi
     */
    public static String convertCentToYuan(int cent) throws Exception {
        try {
            String result = (new BigDecimal(cent).divide(new BigDecimal(100))).toString();
            return result;
        }
        catch (Exception e) {
            throw new Exception("转化失败");
        }
    }

    /**
     * 把分转为元
     *
     * @param yuanStr
     * @return
     * @author liangyi
     */
    public static String convertCentToYuanInt(int cent) throws Exception {
        try {
            String result = (new BigDecimal(cent).divide(new BigDecimal(100))).toString();
            return result;
        }
        catch (Exception e) {
            throw new Exception("转化失败");
        }
    }

    /**
     * 把分转为元
     *
     * @param yuanStr
     * @return
     * @author liangyi
     */
    public static String convertCentToYuan(String centStr) throws Exception {
        try {
            String result = (new BigDecimal(centStr).divide(new BigDecimal(100))).toString();
            return result;
        }
        catch (Exception e) {
            throw new Exception("把String转化为数字失败");
        }
    }


    /**
     * 生成BigDecimal，只支持int,long,String,double
     *
     * @param value
     * @return
     * @author liangyi
     */
    public static BigDecimal buildBigDecimal(Object value) throws Exception {
        BigDecimal result = null;
        try {
            if (value == null) {
                return new BigDecimal(0);
            }

            if (value instanceof String) {
                String valueStr = (String) value;
                if (StringUtils.isBlank(valueStr)) {
                    valueStr = "0";
                }
                result = new BigDecimal(valueStr);
            }
            else if (value instanceof Double) {
                result = new BigDecimal((double) value);
            }
            else if (value instanceof Integer) {
                result = new BigDecimal((int) value);
            }
            else if (value instanceof Long) {
                result = new BigDecimal((long) value);
            }
            else {
                throw new Exception("生成BigDecimal失败，只支持int,long,String,double");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return result;
    }

    /**
     * 按照传入的精度，四色五入获取参数value的double值
     *
     * @param value    只支持int,long,String,double类型
     * @param newScale 精度,保留newScale位小数
     * @return
     * @author liangyi
     */
    public static String convertDoubleToString(Object value, int newScale) throws Exception {
        BigDecimal bigDecimal = buildBigDecimal(value).setScale(newScale, BigDecimal.ROUND_HALF_UP);
        return bigDecimal.toString();
    }

    /**
     * 将分转化成元，并以字符串返回 。
     * 当cents值很大时，会丢失精度…… 如2222222222222222222L
     */
//    @Deprecated  
//    public static String getMoneyStr(long cents){
//       // 当cents值很大时，会丢失精度……
//        return new java.text.DecimalFormat("#.00").format(cents/100.00);
//    }
    public static void main(String[] args) {
        //   System.out.println(getMoneyStr(2222222222222222222L));

//        System.out.println(convertCentToYuan(2222222222222222222L));
//        System.out.println(convertYuanToCent("12345678912345678.22"));
        try {

            System.out.println(convertYuanToCent("45678"));
//            System.out.println(buildBigDecimal(37.94));
//            System.out.println(buildBigDecimal(37.94).doubleValue());
//            System.out.println(buildBigDecimal("37.945").doubleValue());
//            System.out.println(convertDoubleToString(.945, 2));


//            System.out.println(buildBigDecimal(2.80000000).doubleValue()==buildBigDecimal("2.80000000000000").doubleValue());


        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
