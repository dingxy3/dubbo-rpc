package utils;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

/**
 * @author yafei
 * @since 2016/11/16
 */
public class ResourceMsgUtil {

    private static ReloadableResourceBundleMessageSource messageSource;

    static {
        messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:message/commonMessage");
    }

    public ResourceMsgUtil() {
    }

    public static String getMessage(String code, Object[] keys, Locale locale) {
        if(locale == null) {
            locale = Locale.getDefault();
        }
        return messageSource.getMessage(code, keys, locale);
    }

    public static String getMessage(String code, Object[] keys) {
        return getMessage(code, keys, Locale.getDefault());
    }

    public void setMessageSource(ReloadableResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public ReloadableResourceBundleMessageSource getMessageSource() {
        return messageSource;
    }
}
