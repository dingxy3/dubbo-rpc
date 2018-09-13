package com.ai.rai.interests.common.utils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: ReflectionUtil
 * @Description: 反射工具类.
 * @author sunjl
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class ReflectionUtil {

    /**
     * @Fields CGLIB_CLASS_SEPARATOR : cglib类的分隔符
     */
    public static final String CGLIB_CLASS_SEPARATOR = "$$";

    /**
     * @Fields LOGGER : 日志操作类
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtil.class);

    /**
     * 调用目标对象指定属性的Getter方法.
     * @param target 目标对象Object
     * @param propertyName 属性字段名称
     * @param <T> 该属性的值的类型
     * @return Object 指定属性的值
     */
    public static <T> T invokeGetterMethod(final Object target, final String propertyName) {
        final String getterMethodName = "get" + StringUtils.capitalize(propertyName);
        return (T) invokeMethod(target, getterMethodName, new Class[] {}, new Object[] {});
    }

    /**
     * 调用Setter方法.使用value的Class来查找Setter方法.
     * @param target 目标对象Object
     * @param propertyName 属性名称
     * @param value 值
     */
    public static void invokeSetterMethod(final Object target, final String propertyName, final Object value) {
        invokeSetterMethod(target, propertyName, value, null);
    }

    /**
     * 调用Setter方法.
     * @param target 目标对象Object
     * @param propertyName 属性名称
     * @param value 值
     * @param propertyType 用于查找Setter方法,为空时使用value的Class替代.
     */
    public static void invokeSetterMethod(final Object target, final String propertyName, final Object value, final Class<?> propertyType) {
        final Class<?> type = propertyType != null ? propertyType : value.getClass();
        final String setterMethodName = "set" + StringUtils.capitalize(propertyName);
        invokeMethod(target, setterMethodName, new Class[] { type }, new Object[] { value });
    }

    /**
     * @Title: invokeMethod
     * @Description: 调用目标的无参方法
     * @param method 调用的方法
     * @param target 目标对象
     * @return 方法调用结果
     */
    public static Object invokeMethod(final Method method, final Object target) {
        return invokeMethod(target, method, null);
    }

    // public static Object invokeMethod(final Method method, final Object target, final Object[] args) {
    // final Class<?>[] argTypes = new Class[args.length];
    // for (int i = 0; i < args.length; i++) {
    // argTypes[i] = args[i].getClass();
    // }
    // return invokeMethod(target, method, args);
    // }

    /**
     * @Title: invokeMethod
     * @Description: 调用目标的带参数方法
     * @param method 要调用的方法
     * @param target 目标
     * @param args 按顺序的方法入参
     * @return 方法调用结果
     */
    public static Object invokeMethod(final Object target, final Method method, final Object[] args) {
        try {
            return method.invoke(target, args);
        } catch (final Exception e) {
            handleReflectionException(e);
            return null;
        }
    }

    /**
     * @Title: invokeMethod
     * @Description: 已知方法和参数类型的方法调用
     * @param target 对象
     * @param methodName 对象的方法
     * @param parameterTypes 方法的入参类型
     * @param args 方法入参
     * @return 调用的返回结果
     */
    public static Object invokeMethod(final Object target, final String methodName, final Class<?>[] parameterTypes, final Object[] args) {
        final Method method = getAccessibleMethod(target, methodName, parameterTypes);
        return invokeMethod(target, method, args);
    }

    /**
     * @Title: invokeStaticMethod
     * @Description: 已知方法和参数类型的静态方法调用
     * @param clazz 类
     * @param methodName 对象的静态方法
     * @param argTypes 方法的入参类型
     * @param args 方法入参
     * @return 调用的返回结果
     */
    public static Object invokeStaticMethod(final Class<?> clazz, final String methodName, final Class<?>[] argTypes, final Object[] args) {
        final Method method = getAccessibleMethod(clazz, methodName, argTypes);
        try {
            return method.invoke(clazz, args);
        } catch (final Exception e) {
            handleReflectionException(e);
            return null;
        }
    }

    /**
     * @Title: invokeStaticMethod
     * @Description: 已知方法和参数类型的静态方法调用
     * @param clazz 类
     * @param methodName 对象的静态方法
     * @param args 方法入参
     * @return 调用的返回结果
     */
    public static final Object invokeStaticMethod(final Class clazz, final String methodName, final Object[] args) {
        final Class<?>[] argTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            argTypes[i] = args[i].getClass();
        }
        return invokeStaticMethod(clazz, methodName, argTypes, args);
    }

    /**
     * 判断obj参数是否存在fiedlName字段
     * @param target 要判断的目标对象
     * @param fieldName 字段名称
     * @return boolean
     */
    public static boolean hasField(final Object target, final String fieldName) {
        return getAccessibleField(target, fieldName) != null;
    }

    /**
     * @Title: getStaticField
     * @Description: 利用java反射机制获取静态参数
     * @param clazz 类
     * @param fieldName 参数名
     * @param <T> 静态属性值的类型
     * @return Object 返回静态参数对象
     */
    public static final <T> T getStaticFieldValue(final Class clazz, final String fieldName) {
        final Field field = getAccessibleField(clazz, fieldName);
        if (field == null) {
            throw new IllegalArgumentException("Can't find the static fieldName:" + fieldName + " in the class:" + clazz);
        }
        Object result = null;
        try {
            result = field.get(null);
        } catch (final IllegalAccessException e) {
            handleReflectionException(e);
            return null;
        }
        return (T) result;
    }

    /**
     * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
     * @param target 目标对象Object
     * @param fieldName 字段名称
     * @param <T> 属性值的类型
     * @return Object
     */
    public static <T> T getFieldValue(final Object target, final String fieldName) {
        final Field field = getAccessibleField(target, fieldName);

        Object result = null;
        try {
            result = field.get(target);
        } catch (final IllegalAccessException e) {
            handleReflectionException(e);
            return null;
        }
        return (T) result;
    }

    /**
     * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
     * @param target 目标对象Object
     * @param fieldName 字段名称
     * @param value 值
     */
    public static void setFieldValue(final Object target, final String fieldName, final Object value) {
        final Field field = getAccessibleField(target, fieldName);
        try {
            field.set(target, value);
        } catch (final IllegalAccessException e) {
            handleReflectionException(e);
        }
    }

    /**
     * 循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问.如向上转型到Object仍无法找到, 返回null.
     * @param target 目标对象Object
     * @param fieldName 字段名称
     * @return {@link Field}
     */
    public static Field getAccessibleField(final Object target, final String fieldName) {
        final Field field = getAccessibleField(getTargetClass(target), fieldName);
        if (field == null) {
            throw new IllegalArgumentException("Cannot find the fieldName [" + fieldName + "] in the object [" + target + "].");
        }
        return field;
    }

    /**
     * 循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问. 如向上转型到Object仍无法找到, 返回null.
     * @param targetClass 目标对象Class
     * @param fieldName class中的字段名
     * @return {@link Field}
     */
    public static Field getAccessibleField(final Class targetClass, final String fieldName) {
        for (Class<?> superClass = targetClass; superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                final Field field = superClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (final NoSuchFieldException e) {
                LOGGER.error("Cannot throw exceptions: {}", e.getMessage());
            }
        }
        return null;
    }

    /**
     * 循环向上转型, 获取对象的所有DeclaredField, 并强制设置为可访问.
     * @param target 目标对象
     * @return List 可访问的Field列表
     */
    public static List<Field> getAccessibleFields(final Object target) {
        return getAccessibleFields(getTargetClass(target));
    }

    /**
     * 循环向上转型, 获取对象的所有DeclaredField, 并强制设置为可访问.
     * @param targetClass 目标对象Class
     * @return List
     */
    public static List<Field> getAccessibleFields(final Class targetClass) {
        return getAccessibleFields(targetClass, false);
    }

    /**
     * 获取对象的所有DeclaredField,并强制设置为可访问.
     * @param targetClass 目标对象Class
     * @param ignoreParent 是否循环向上转型,获取所有父类的Field
     * @return List
     */
    public static List<Field> getAccessibleFields(final Class targetClass, final boolean ignoreParent) {
        final List<Field> fields = new ArrayList<Field>();
        Class<?> sc = targetClass;
        do {
            final Field[] result = sc.getDeclaredFields();
            if (ArrayUtils.isEmpty(result)) {
                continue;
            }
            for (final Field field : result) {
                field.setAccessible(true);
            }
            CollectionUtils.addAll(fields, result);
            sc = sc.getSuperclass();
        } while (sc != Object.class && !ignoreParent);
        return fields;
    }

    /**
     * @Title: getAccessibleMethod
     * @Description: 获取指定方法
     * @param target 目标对象
     * @param methodName 方法名
     * @param args 入参数组
     * @return 指定方法
     */
    public static Method getAccessibleMethod(final Object target, final String methodName, final Object[] args) {
        final Class<?>[] argTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            argTypes[i] = args[i].getClass();
        }
        return getAccessibleMethod(getTargetClass(target), methodName, argTypes);
    }

    /**
     * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问. 如向上转型到Object仍无法找到, 返回null. 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
     * @param target Object对象
     * @param methodName 方法名称
     * @param parameterTypes 方法参数类型
     * @return {@link Method}
     */
    public static Method getAccessibleMethod(final Object target, final String methodName, final Class<?>... parameterTypes) {
        final Method method = getAccessibleMethod(getTargetClass(target), methodName, parameterTypes);
        if (method == null) {
            final StringBuffer errorStr = new StringBuffer();
            errorStr.append("Cannot find the method [").append(methodName).append("] with params[");
            for (final Class<?> parameterType : parameterTypes) {
                errorStr.append(parameterType.getName()).append(";");
            }
            errorStr.append("] in the object:").append(target);
            throw new IllegalArgumentException(errorStr.toString());
        }
        return method;
    }

    /**
     * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问. 如向上转型到Object仍无法找到, 返回null. 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
     * @param targetClass 目标对象Class
     * @param methodName 方法名称
     * @param parameterTypes 方法参数类型数组
     * @return {@link Method}
     */
    public static Method getAccessibleMethod(final Class targetClass, final String methodName, final Class<?>... parameterTypes) {
        for (Class<?> superClass = targetClass; superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                final Method method = superClass.getDeclaredMethod(methodName, parameterTypes);
                method.setAccessible(true);
                return method;
            } catch (final NoSuchMethodException e) {
                LOGGER.info("Cannot find method[{}] in class[{}]", methodName, superClass);
            }
        }
        return null;
    }

    /**
     * 循环向上转型, 获取对象的所有DeclaredMethod 并强制设置为可访问.
     * @param target 目标对象Object
     * @return List
     */
    public static List<Method> getAccessibleMethods(final Object target) {
        return getAccessibleMethods(getTargetClass(target));
    }

    /**
     * 循环向上转型, 获取对象的所有DeclaredMethod 并强制设置为可访问.
     * @param targetClass 目标对象Class
     * @return List
     */
    public static List<Method> getAccessibleMethods(final Class targetClass) {
        return getAccessibleMethods(targetClass, false);
    }

    /**
     * 获取对象的所有DeclaredMethod 并强制设置为可访问.
     * @param targetClass 目标对象Class
     * @param ignoreParent 是否循环向上转型,获取所有父类的Method
     * @return List
     */
    public static List<Method> getAccessibleMethods(final Class targetClass, final boolean ignoreParent) {
        final List<Method> methods = new ArrayList<Method>();
        Class<?> superClass = targetClass;
        do {
            final Method[] result = superClass.getDeclaredMethods();
            if (ArrayUtils.isEmpty(result)) {
                continue;
            }
            for (final Method method : result) {
                method.setAccessible(true);
            }
            CollectionUtils.addAll(methods, result);
            superClass = superClass.getSuperclass();
        } while (superClass != Object.class && !ignoreParent);

        return methods;
    }

    /**
     * 获取对象中的注解
     * @param target 目标对象Object
     * @param annotationClass 注解
     * @param <T> 注解类型
     * @return Object
     */
    public static <T> T getAnnotation(final Object target, final Class annotationClass) {
        return (T) getAnnotation(target.getClass(), annotationClass);
    }

    /**
     * 获取对象中的注解
     * @param targetClass 目标对象Class
     * @param annotationClass 注解类型Class
     * @param <T> 注解类型
     * @return Object
     */
    public static <T extends Annotation> T getAnnotation(final Class targetClass, final Class annotationClass) {
        if (targetClass.isAnnotationPresent(annotationClass)) {
            return (T) targetClass.getAnnotation(annotationClass);
        }
        return null;
    }

    /**
     * 获取Object对象中所有annotationClass类型的注解
     * @param target 目标对象Object
     * @param annotationClass Annotation类型
     * @param <T> 注解类型
     * @return {@link Annotation}
     */
    public static <T extends Annotation> List<T> getAnnotations(final Object target, final Class annotationClass) {
        return getAnnotations(getTargetClass(target), annotationClass);
    }

    /**
     * 获取对象中的所有annotationClass注解
     * @param targetClass 目标对象Class
     * @param annotationClass 注解类型Class
     * @param <T> 注解类型
     * @return List
     */
    public static <T extends Annotation> List<T> getAnnotations(final Class targetClass, final Class annotationClass) {
        final List<T> result = new ArrayList<T>();
        final Annotation annotation = targetClass.getAnnotation(annotationClass);
        if (annotation != null) {
            result.add((T) annotation);
        }
        final Constructor[] constructors = targetClass.getDeclaredConstructors();
        // 获取构造方法里的注解
        CollectionUtils.addAll(result, getAnnotations(constructors, annotationClass).iterator());
        final Field[] fields = targetClass.getDeclaredFields();
        // 获取字段中的注解
        CollectionUtils.addAll(result, getAnnotations(fields, annotationClass).iterator());
        final Method[] methods = targetClass.getDeclaredMethods();
        // 获取方法中的注解
        CollectionUtils.addAll(result, getAnnotations(methods, annotationClass).iterator());
        for (Class<?> superClass = targetClass.getSuperclass(); superClass == null || superClass == Object.class; superClass = superClass
            .getSuperclass()) {
            final List<T> temp = getAnnotations(superClass, annotationClass);
            if (CollectionUtils.isNotEmpty(temp)) {
                CollectionUtils.addAll(result, temp.iterator());
            }
        }
        return result;
    }

    /**
     * 获取field的annotationClass注解
     * @param field field对象
     * @param annotationClass annotationClass注解
     * @param <T> 注解类型
     * @return {@link Annotation}
     */
    public static <T extends Annotation> T getAnnotation(final Field field, final Class annotationClass) {
        field.setAccessible(true);
        if (field.isAnnotationPresent(annotationClass)) {
            return (T) field.getAnnotation(annotationClass);
        }
        return null;
    }

    /**
     * 获取field数组中匹配的annotationClass注解
     * @param fields field对象数组
     * @param annotationClass annotationClass注解
     * @param <T> 注解类型
     * @return List
     */
    public static <T extends Annotation> List<T> getAnnotations(final Field[] fields, final Class annotationClass) {
        if (ArrayUtils.isEmpty(fields)) {
            return null;
        }
        final List<T> result = new ArrayList<T>();
        for (final Field field : fields) {
            field.setAccessible(true);
            final Annotation annotation = getAnnotation(field, annotationClass);
            if (annotation != null) {
                result.add((T) annotation);
            }
        }
        return result;
    }

    /**
     * 获取method的annotationClass注解
     * @param method method对象
     * @param annotationClass annotationClass注解
     * @param <T> 注解类型
     * @return {@link Annotation}
     */
    public static <T extends Annotation> T getAnnotation(final Method method, final Class annotationClass) {
        method.setAccessible(true);
        if (method.isAnnotationPresent(annotationClass)) {
            return (T) method.getAnnotation(annotationClass);
        }
        return null;
    }

    /**
     * 获取method数组中匹配的annotationClass注解
     * @param methods method对象数组
     * @param annotationClass annotationClass注解
     * @param <T> 注解类型
     * @return List
     */
    public static <T extends Annotation> List<T> getAnnotations(final Method[] methods, final Class annotationClass) {
        if (ArrayUtils.isEmpty(methods)) {
            return null;
        }
        final List<T> result = new ArrayList<T>();
        for (final Method method : methods) {
            final Annotation annotation = getAnnotation(method, annotationClass);
            if (annotation != null) {
                result.add((T) annotation);
            }
        }
        return result;
    }

    /**
     * 获取constructor的annotationClass注解
     * @param constructor constructor对象
     * @param annotationClass annotationClass注解
     * @param <T> 注解类型
     * @return {@link Annotation}
     */
    public static <T extends Annotation> T getAnnotation(final Constructor constructor, final Class annotationClass) {
        Assert.notNull(constructor, "The constructor cannot be empty.");
        Assert.notNull(annotationClass, "The annotationClass cannot be empty.");
        constructor.setAccessible(true);
        if (constructor.isAnnotationPresent(annotationClass)) {
            return (T) constructor.getAnnotation(annotationClass);
        }
        return null;
    }

    /**
     * 获取constructors数组中匹配的annotationClass注解
     * @param constructors constructor对象数组
     * @param annotationClass annotationClass注解
     * @param <T> 注解类型
     * @return List
     */
    public static <T extends Annotation> List<T> getAnnotations(final Constructor[] constructors, final Class annotationClass) {
        if (ArrayUtils.isEmpty(constructors)) {
            return null;
        }
        final List<T> result = new ArrayList<T>();
        for (final Constructor constructor : constructors) {
            final Annotation annotation = getAnnotation(constructor, annotationClass);
            if (annotation != null) {
                result.add((T) annotation);
            }
        }
        return result;
    }

    /**
     * 更具类型获取o中的所有字段名称
     * @param targetClass 目标对象Class
     * @param type 要获取名称的类型
     * @return List
     */
    public static List<String> getAccessibleFieldNames(final Class targetClass, final Class type) {
        final List<String> list = new ArrayList<String>();
        for (final Field field : targetClass.getDeclaredFields()) {
            if (field.getType().equals(type)) {
                list.add(field.getName());
            }
        }
        return list;
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class，否则返回首个泛参数型类型
     * 
     * <pre>
     * 例如
     * public UserDao extends HibernateDao<User>
     * </pre>
     * @param targetClass 要反射的目标对象Class
     * @param <T> 父类类型
     * @return Object.clss或者T.class
     */
    public static <T> Class<T> getSuperClassGenricType(final Class targetClass) {
        return getSuperClassGenricType(targetClass, 0);
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.否则返回泛参数型类型
     * 
     * <pre>
     * 例如
     * public UserDao extends HibernateDao<User,Long>
     * </pre>
     * @param targetClass 要反射的目标对象Class
     * @param index 反省参数的位置
     * @return class
     */
    public static Class getSuperClassGenricType(final Class targetClass, final int index) {
        final Type genType = targetClass.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            LOGGER.warn(targetClass.getSimpleName() + "'s superclass not ParameterizedType");
            return Object.class;
        }
        final Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            LOGGER.warn("Index: " + index + ", Size of " + targetClass.getSimpleName() + "'s Parameterized Type: " + params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            LOGGER.warn(targetClass.getSimpleName() + " not set the actual Class targetClassn superclass generic parameter");
            return Object.class;
        }
        return (Class) params[index];
    }

    /**
     * 通过Class创建对象
     * @param targetClass 目标对象Class
     * @param <T> 目标对象类型
     * @return Object
     */
    public static <T> T newInstance(final Class targetClass) {
        try {
            return (T) targetClass.newInstance();
        } catch (final Exception e) {
            handleReflectionException(e);
            return null;
        }
    }

    /**
     * @Title: getClass
     * @Description: 根据类名全路径，获取类
     * @param className 类名全路径
     * @return 类
     */
    public static Class<?> getClass(final String className) {
        try {
            return Class.forName(className);
        } catch (final ClassNotFoundException e) {
            handleReflectionException(e);
            return null;
        }
    }

    /**
     * 获取对象Class如果被cglib AOP过的对象或对象为CGLIB的Class，将获取真正的Class类型
     * @param target 对象
     * @return Class
     */
    public static Class<?> getTargetClass(final Object target) {
        Assert.notNull(target, "The target cannot be empty.");
        return getTargetClass(target.getClass());
    }

    /**
     * @Title: getTargetClass
     * @Description: 获取Class如果被cglib AOP过的对象或对象为CGLIB的Class，将获取真正的Class类型
     * @param targetClass 可能被cglib AOP过的对象或对象为CGLIB的Class
     * @return 真正的class类型
     */
    public static Class<?> getTargetClass(final Class<?> targetClass) {
        Assert.notNull(targetClass, "The targetClass cannot be empty.");
        final Class clazz = targetClass;
        if (clazz != null && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
            final Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && !Object.class.equals(superClass)) {
                return superClass;
            }
        }
        return clazz;
    }

    /**
     * @Title: handleReflectionException
     * @Description: 使用Spring的反射工具类直接抛出运行时异常
     * @param e 异常
     */
    public static void handleReflectionException(final Exception e) {
        ReflectionUtils.handleReflectionException(e);
    }
}
