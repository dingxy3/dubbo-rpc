package utils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

/**
 * @author yafei
 * @since 2016/11/22
 */
public class ObjectCopyUtil {
    public static final Logger log = LoggerFactory.getLogger(ObjectCopyUtil.class);

    public ObjectCopyUtil() {
    }

    public static void copyObjValue(Object vo, Object target, String not_copy, boolean isCopyNull) {
        if(vo == null) {
            log.info("对象复制，入参为空");
        } else {
            Class cls = vo.getClass();
            if(StringUtils.isEmpty(not_copy)) {
                not_copy = "serialVersionUID";
            } else {
                not_copy = not_copy + ",serialVersionUID";
            }

            while(!cls.getName().equals("java.lang.Object")) {
                copyObjectValue(vo, target, cls, not_copy, isCopyNull);
                cls = cls.getSuperclass();
            }

        }
    }

    private static void copyObjectValue(Object vo, Object target, Class cls, String not_copy, boolean isCopyNull) {
        boolean flag = false;
        if(!StringUtils.isEmpty(not_copy)) {
            not_copy = "," + not_copy + ",";
        }

        try {
            String e = "";
            Field[] fields = cls.getDeclaredFields();

            for(int i = 0; i < fields.length; ++i) {
                e = fields[i].getName();
                if(StringUtils.isEmpty(not_copy) || not_copy.indexOf("," + e + ",") == -1) {
                    PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(vo, e);
                    if(descriptor != null && descriptor.getReadMethod() != null) {
                        if((fields[i].getType().toString().startsWith("class") || fields[i].getType().toString().startsWith("interface")) && !fields[i].getType().getName().equals("java.lang.String")) {
                            try {
                                String ne = BeanUtils.getProperty(vo, e);
                                if((isCopyNull || ne != null) && null != ne) {
                                    BeanUtils.setProperty(target, e, MethodUtils.invokeMethod(vo, "get" + e.substring(0, 1).toUpperCase() + e.substring(1), (Object[])null));
                                }
                            } catch (Exception var11) {
                                log.error(var11.getMessage(), var11);
                                flag = true;
                            }
                        } else {
                            try {
                                if(isCopyNull || BeanUtils.getProperty(vo, e) != null) {
                                    BeanUtils.setProperty(target, e, BeanUtils.getProperty(vo, e));
                                }
                            } catch (Exception var12) {
                                log.error(var12.getMessage(), var12);
                                flag = true;
                            }
                        }
                    }
                }
            }
        } catch (Exception var13) {
            flag = true;
            log.error(var13.getMessage(), var13);
        }

        if(flag) {
            flag = false;
        }

    }
}
