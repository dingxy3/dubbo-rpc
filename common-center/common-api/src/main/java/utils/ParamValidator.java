package utils;

import com.ai.rai.interests.common.base.FieldName;
import com.ai.rai.interests.common.exception.BusinessException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author gejun
 * @ClassName: ParamValidator
 * @Description: 参数校验
 */
public class ParamValidator
{

    /**
     * 缺少字段
     */
    public static final int EMPTY_FIELD = 9999;

    /**
     * 校验map中必需的key
     *
     * @param map
     * @param params key
     * @Title: exists
     * @Description: 校验map中必需的key
     */
    public static void exists(Map<String, ?> map, String... params)
    {
        for (String param : params)
        {
            if (!map.containsKey(param) || null == map.get(param))
                throwNonExists(param);
        }
    }

    /**
     * 校验bean中必需的成员
     *
     * @param bean
     * @param fields
     * @Title: exists
     * @Description: 校验bean中必需的成员
     */
    public static void exists(Object bean, String... fields)
    {
        Class<?> clazz = bean.getClass();
        Class<?> superClazz = clazz.getSuperclass();
        for (String param : fields)
        {
            String paramName = param;
            try
            {
                Field field = clazz.getDeclaredField(param);
                FieldName fieldName = field.getAnnotation(FieldName.class);
                if (null != fieldName)
                    paramName = fieldName.value();
            }
            catch (NoSuchFieldException e)
            {
                if (superClazz == null)
                {
                    throwNonExists(param);
                }
                else
                {
                    try
                    {
                        Field field = superClazz.getDeclaredField(param);
                        FieldName fieldName = field.getAnnotation(FieldName.class);
                        if (null != fieldName)
                            paramName = fieldName.value();
                    }
                    catch (NoSuchFieldException se)
                    {
                        throwNonExists(param);
                    }
                }
            }
            Method method = null;
            try
            {
                method = clazz.getDeclaredMethod("get" + param.substring(0, 1).toUpperCase() + param.substring(1));
            }
            catch (NoSuchMethodException e)
            {
                if (superClazz == null)
                {
                    throwNonExists(param);
                }
                else
                {
                    try
                    {
                        method = superClazz.getDeclaredMethod(
                            "get" + param.substring(0, 1).toUpperCase() + param.substring(1));
                    }
                    catch (NoSuchMethodException se)
                    {
                        throwNonExists(param);
                    }
                }
            }
            if (null != method)
            {
                Class<?> returnType = method.getReturnType();
                Object result = null;
                try
                {
                    result = method.invoke(bean);
                }
                catch (Exception e)
                {
                    throwNonExists(param);
                }
                if (null == result)
                    throwNonExists(paramName);
                if (returnType.equals(String.class))
                {
                    if (result.toString() == null || "".equals(result.toString()))
                        throwNonExists(paramName);
                }
                else if (returnType.equals(List.class))
                {
                    if (((List<?>)result).size() == 0)
                        throwNonExists(paramName);
                }
            }
        }
    }

    /**
     * 校验list中的每个对象必需的成员
     *
     * @param list
     * @param fields
     * @Title: exists
     * @Description: 校验list中的每个对象必需的成员
     */
    public static void exists(List<?> list, String... fields)
    {
        if (null != list)
        {
            for (Iterator<?> iterator = list.iterator(); iterator.hasNext(); )
            {
                ParamValidator.exists(iterator.next(), fields);
            }
        }
    }

    /**
     * 抛出字段不存在的异常
     *
     * @param param
     * @Title: throwNonExists
     * @Description:
     */
    public static void throwNonExists(String param)
    {

        String[] parr = new String[1];
        parr[0] = param;

        throw new BusinessException(EMPTY_FIELD + "", parr);
    }
}
