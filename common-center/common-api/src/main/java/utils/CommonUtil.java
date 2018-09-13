package utils;


import utils.exception.BusinessException;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;


/**
 * @author dingxy
 * @ClassName: CommonUtil
 * @Description: 微支付 公共类
 */
public class CommonUtil
{
    /**
     * 商户生成的随机字符串 字符串类型，32个字节以下
     *
     * @param length
     * @return
     */
    public static String CreateNoncestr(int length)
    {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String res = "";
        for (int i = 0; i < length; i++)
        {
            Random rd = new Random();
            res += chars.indexOf(rd.nextInt(chars.length() - 1));
        }
        return res;
    }

    /**
     * 商户生成的随机字符串 字符串类型，32个字节以下
     *
     * @return
     */
    public static String CreateNoncestr()
    {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String res = "";
        for (int i = 0; i < 16; i++)
        {
            Random rd = new Random();
            res += chars.charAt(rd.nextInt(chars.length() - 1));
        }
        return res;
    }

    /**
     * 格式化参数
     *
     * @param parameters
     * @return
     * @throws
     */
    public static String FormatQueryParaMap(HashMap<String, String> parameters)
        throws BusinessException
    {
        String buff = "";
        try
        {
            List<Entry<String, String>> infoIds = new ArrayList<Entry<String, String>>(parameters.entrySet());

            // 根据文档对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）后，使用 URL 键值
            // 对的格式（即 key1=value1&key2=value2…）
            Collections.sort(infoIds, new Comparator<Entry<String, String>>()
            {
                public int compare(Entry<String, String> o1, Entry<String, String> o2)
                {
                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });
            // 对所有键值对中的 value 迚行 urlencode 转码
            for (int i = 0; i < infoIds.size(); i++)
            {
                Entry<String, String> item = infoIds.get(i);
                if (item.getKey() != "")
                {
                    buff += item.getKey() + "=" + URLEncoder.encode(item.getValue(), "utf-8") + "&";
                }
            }
            if (buff.isEmpty() == false)
            {
                buff = buff.substring(0, buff.length() - 1);
            }
        }
        catch (Exception e)
        {
            throw GloubFunc.getException(e);
        }

        return buff;
    }

    /**
     * 与上述方法相同，根据不同的编码方式编码
     *
     * @param paraMap
     * @param urlencode
     * @return
     * @throws
     */
    public static String FormatBizQueryParaMap(Map<String, String> paraMap, boolean urlencode)
        throws BusinessException
    {
        String buff = "";
        try
        {
            List<Entry<String, String>> infoIds = new ArrayList<Entry<String, String>>(paraMap.entrySet());

            Collections.sort(infoIds, new Comparator<Entry<String, String>>()
            {
                public int compare(Entry<String, String> o1, Entry<String, String> o2)
                {
                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });
            for (int i = 0; i < infoIds.size(); i++)
            {
                Entry<String, String> item = infoIds.get(i);
                if (item.getKey() != "")
                {
                    String key = item.getKey();
                    String val = item.getValue();
                    if (urlencode)
                    {
                        val = URLEncoder.encode(val, "utf-8");
                    }
                    buff += key +  val ;
                }
            }
//            if (buff.isEmpty() == false)
//            {
//                buff = buff.substring(0, buff.length() - 1);
//            }
        }
        catch (Exception e)
        {
            throw GloubFunc.getException(e);
        }
        return buff;
    }

    /**
     * 判断是否是数字
     *
     * @param str
     * @return
     */
    public static boolean IsNumeric(String str)
    {
        if (str.matches("\\d *"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * map转成xml
     *
     * @param arr
     * @return
     */
    public static String ArrayToXml(Map<String, String> arr)
    {
        String xml = "<xml>";
        Iterator<Entry<String, String>> iter = arr.entrySet().iterator();
        while (iter.hasNext())
        {
            Entry<String, String> entry = iter.next();
            String key = entry.getKey();
            String val = entry.getValue();
            if (IsNumeric(val))
            {
                xml += "<" + key + ">" + val + "</" + key + ">";

            }
            else
                xml += "<" + key + "><![CDATA[" + val + "]]></" + key + ">";
        }
        xml += "</xml>";
        return xml;
    }


    /**
     * @return
     * @Title: getRandomNumber
     * @Description: 获取随机10位数
     */
    public static String getRandomNumber()
    {
        String a = System.currentTimeMillis() + "";
        return a.substring(3);
    }

    /**
     * @return
     * @Title: getTimeStr
     * @Description: 获取年月日字符串
     */
    public static String getTimeStr()
    {
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        return sf.format(new Date());
    }

    
    /**
	 * 从map中获取int。若value为空或不是数字，返回0
	 * @param map
	 * @param key
	 * @return
	 * @author liangyi
	 */
	public static int getIntValueFromMap(Map<String,Object> map,String key){
		
		if (map==null) {
			return 0;
		}
		
		Object valueObj=map.get(key);
		if (valueObj==null) {
			return 0;
		}
		
		int result=0;
		try {
			result=Integer.valueOf(valueObj.toString());
		} catch (Exception e) {
			result=0;
		}
		
		return result;
	}
	
	/**
	 * 从map中获取int。若value为空或不是数字，返回0
	 * @param map
	 * @param key
	 * @return
	 * @author liangyi
	 */
	public static int getIntValueFromIntMap(Map map,String key){
		
		if (map==null) {
			return 0;
		}
		
		Object valueObj=map.get(key);
		if (valueObj==null) {
			return 0;
		}
		if (valueObj instanceof Integer) {
			int result=(int)valueObj;
			return result;
		}
		
		return 0;
	}
	
	/**
	 * 从map中获取long。若value为空或不是数字，返回0
	 * @param map
	 * @param key
	 * @return
	 * @author liangyi
	 */
	public static long getLongValueFromIntMap(Map map,String key){
		
		if (map==null) {
			return 0;
		}
		
		Object valueObj=map.get(key);
		if (valueObj==null) {
			return 0;
		}
		
		if (valueObj instanceof Long) {
			long result=(long)valueObj;
			return result;
		}
		
		if (valueObj instanceof String) {
			String resultStr=(String)valueObj;
			try {
				return Long.valueOf(resultStr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return 0;
	}
	
	/**
	 * 从map中获取int。若value为空或不是数字，返回0
	 * @param map
	 * @param key
	 * @return
	 * @author liangyi
	 */
	public static double getDoubleValueFromMap(Map<String,Object> map,String key){
		
		if (map==null) {
			return 0;
		}
		
		Object valueObj=map.get(key);
		if (valueObj==null) {
			return 0;
		}
		
		double result=0;
		try {
			result=Double.valueOf(valueObj.toString());
		} catch (Exception e) {
			result=0;
		}
		
		return result;
	}
	
	/**
	 * 从map中获取String。若value为空，返回nullValue的值
	 * @param map
	 * @param key
	 * @param nullValue 当map中value为null时，返回的默认值
	 * @return
	 * @author liangyi
	 */
	public static String getStringValueFromMap(Map<String,Object> map,String key,String nullValue){
		if (map==null) {
			return nullValue;
		}
		Object valueObj=map.get(key);
		if (valueObj==null) {
			return nullValue;
		}
		return valueObj.toString();
	}
	
	/**
	 * 从map中获取Date。若value为空，返回nullValue的值
	 * @param map
	 * @param key
	 * @param nullValue 当map中value为null时，返回的默认值
	 * @return
	 * @author liangyi
	 */
	public static Date getDateValueFromMap(Map<String,Object> map,String key){
		if (map==null) {
			return null;
		}
		Object valueObj=map.get(key);
		if (valueObj==null) {
			return null;
		}
		return (Date)valueObj;
	}

	/**
	 * 从map中获取boolean。若value为空，返回nullValue的值
	 * @param map
	 * @param key
	 * @param nullValue 当map中value为null时，返回的默认值
	 * @return
	 * @author liangyi
	 */
	public static boolean getBooleanValueFromMap(Map<String,Object> map,String key,boolean nullValue){
		if (map==null) {
			return nullValue;
		}
		Object valueObj=map.get(key);
		if (valueObj==null) {
			return nullValue;
		}
		return (boolean)valueObj;
	}


}
