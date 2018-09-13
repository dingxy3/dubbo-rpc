package utils;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: Obj2MapListUtil
 * @Description: 转换工具类，支持反射的方式将对象转换为Map，将数组和Collection转换为List供XStrem使用
 * @author Jim Wu modified by linyl linyuliang.85@gmail.com
 */
public class Obj2MapListUtil {

    /**
     * @Fields instance :当前类实例
     */
    private static volatile Obj2MapListUtil instance;

    // Private constructor suppresses
    // default public constructor
    /**
     * <p>
     * Title: 私有化，使只能通过单例模式使用
     * </p>
     * <p>
     * Description:
     * </p>
     */
    private Obj2MapListUtil() {
    }

    // thread safe and performance promote
    /**
     * @Title: getInstance
     * @Description: 获取当前类的单例
     * @return 当前类的实例
     */
    public static Obj2MapListUtil getInstance() {
        if (instance == null) {
            synchronized (Obj2MapListUtil.class) {
                // when more than two threads run into the first null check same time, to avoid instanced more than one time, it needs to be checked
                // again.
                if (instance == null) {
                    instance = new Obj2MapListUtil();
                }
            }
        }
        return instance;
    }

    /**
     * @Title: toHashMap
     * @Description: 将对象转换成Map或者List
     * @param obj 要转换的对象
     * @return Map 或者 List
     */
    public static Object toHashMap(final Object obj) {
        return toHashMap(obj, false);
    }

    /**
     * @Title: toHashMap
     * @Description: 将对象转换成map
     * @param obj 对象
     * @param useClassConvert 是否使用类转换
     * @return 返回map对象
     */
    public static Object toHashMap(final Object obj, final boolean useClassConvert) {
        return getInstance().getMapListObject(obj, useClassConvert, true, true);
    }

    /**
     * @Title: toHashMap
     * @Description: 将对象转换成map
     * @param obj 要转换的对象
     * @param useClassConvert 是否使用类转换
     * @param isCamelize 是否将对象属性名称驼峰化
     * @param needIsMethod 是否需要is开头的属性的get
     * @return 转换后的map
     */
    public static Object toHashMap(final Object obj, final boolean useClassConvert, final boolean isCamelize, final boolean needIsMethod) {
        return getInstance().getMapListObject(obj, useClassConvert, isCamelize, needIsMethod);
    }

    /**
     * @Title: getMapListObject
     * @Description: 将对象转换成MAP 或者List
     * @param bean 转换bean
     * @param useClassConvert 是否将类直接toString转换成字符串
     * @param isCamelize 是否将对象属性名称驼峰化
     * @param needIsMethod 是否需要is开头的属性的get
     * @return 转换后的map或者list
     */
    public Object getMapListObject(final Object bean, final boolean useClassConvert, final boolean isCamelize, final boolean needIsMethod) {
        Object resultObj;
        // 1.是否为null
        if (bean == null) {
            resultObj = "";
            // 2.是否为以下数据类型
        } else if (bean instanceof Number || bean instanceof Boolean || bean instanceof String) {
            resultObj = bean.toString();
            // 3.是否为日期
        } else if (bean instanceof Date) {
            resultObj = DateUtils.format((Date) bean);
            // 4.其他
        } else if (bean instanceof Calendar) {
            resultObj = DateUtils.format(((Calendar) bean).getTime());
        } else {
            final Class<?> clazz = bean.getClass();

            // 4.1是数组
            if (clazz.isArray()) {
                resultObj = convertArray2List(bean, useClassConvert, isCamelize, needIsMethod);
                // 4.2是集合
            } else if (bean instanceof Collection) {
                resultObj = convertCollection2List(bean, useClassConvert, isCamelize, needIsMethod);
                // 4.3 是map
            } else if (bean instanceof Map) {
                resultObj = convertMap2Map(bean, useClassConvert, isCamelize, needIsMethod);
                // 4.3 是其他类
            } else {
                resultObj = convertOtherObj2Map(bean, useClassConvert, isCamelize, needIsMethod);
            }

        }
        return resultObj;
    }

    /**
     * @Title: convertOtherObj2Map
     * @Description: 将Obj转成HashMap
     * @param bean 其他类型对象
     * @param useClassConvert 是否使用类转换
     * @param isCamelize 是否将对象属性名称驼峰化
     * @param needIsMethod 是否需要is开头的属性的get
     * @return Map
     */
    private Object convertOtherObj2Map(final Object bean, final boolean useClassConvert, final boolean isCamelize, final boolean needIsMethod) {
        final Map<String, Object> beanMap = new HashMap<String, Object>();
        final Class<?> klass = bean.getClass();
        final Method[] methods = klass.getMethods();
        final String[] getterMethodPrefix = { "get", "is", };
        for (final Method method : methods) {
            final String name = method.getName();
            String key = "";
            for (final String element : getterMethodPrefix) {
                if (name.startsWith(element)) {
                    key = name.substring(element.length());
                    break;
                }
            }
            if (key.length() > 0 && Character.isUpperCase(key.charAt(0)) && method.getParameterTypes().length == 0) {
                if (key.length() == 1) {
                    key = key.toLowerCase();
                } else if (!Character.isUpperCase(key.charAt(1))) {
                    key = key.substring(0, 1).toLowerCase() + key.substring(1);
                }
                final Object elementObj = ReflectionUtil.invokeMethod(method, bean);
                if (elementObj instanceof Class) {
                    beanMap.put(key, elementObj.toString());
                } else {
                    beanMap.put(key, getMapListObject(elementObj, useClassConvert, isCamelize, needIsMethod));
                }

            }
        }
        return beanMap;
    }

    /**
     * @Title: convertMap2Map
     * @Description: 将Map转成HashMap
     * @param bean Map类型对象
     * @param useClassConvert 是否使用类转换
     * @param isCamelize 是否将对象属性名称驼峰化
     * @param needIsMethod 是否需要is开头的属性的get
     * @return Map
     */
    private Object convertMap2Map(final Object bean, final boolean useClassConvert, final boolean isCamelize, final boolean needIsMethod) {
        final Map<String, Object> beanMap = new HashMap<String, Object>();

        @SuppressWarnings("unchecked")
        final Map<Object, Object> map = (Map<Object, Object>) bean;
        final Iterator<Object> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            final Object key = iterator.next();
            final Object rowObj = map.get(key);
            beanMap.put(key.toString(), getMapListObject(rowObj, useClassConvert, isCamelize, needIsMethod));
        }
        return beanMap;
    }

    /**
     * @Title: convertCollection2List
     * @Description: 将Collection转成List
     * @param bean Collection类型对象
     * @param useClassConvert 是否使用类转换
     * @param isCamelize 是否将对象属性名称驼峰化
     * @param needIsMethod 是否需要is开头的属性的get
     * @return List
     */
    private Object convertCollection2List(final Object bean, final boolean useClassConvert, final boolean isCamelize, final boolean needIsMethod) {
        final List<Object> arrayList = new ArrayList<Object>();

        @SuppressWarnings("unchecked")
        final Iterator<Object> iterator = ((Collection<Object>) bean).iterator();
        while (iterator.hasNext()) {
            final Object rowObj = iterator.next();
            arrayList.add(getMapListObject(rowObj, useClassConvert, isCamelize, needIsMethod));
        }
        return arrayList;
    }

    /**
     * @Title: convertArray2List
     * @Description: 将Array转成List
     * @param bean Array类型对象
     * @param useClassConvert 是否使用类转换
     * @param isCamelize 是否将对象属性名称驼峰化
     * @param needIsMethod 是否需要is开头的属性的get
     * @return List
     */
    private Object convertArray2List(final Object bean, final boolean useClassConvert, final boolean isCamelize, final boolean needIsMethod) {
        final List<Object> arrayList = new ArrayList<Object>();

        final int arrayLength = Array.getLength(bean);
        for (int i = 0; i < arrayLength; i++) {
            final Object rowObj = Array.get(bean, i);
            arrayList.add(getMapListObject(rowObj, useClassConvert, isCamelize, needIsMethod));
        }
        return arrayList;
    }
}
