package com.ai.rai.interests.common.utils;

/**
 * Created by fangyunfeng on 2017/4/17.
 */
public class SiteLocaleUtil {
    private static ThreadLocal<Long> siteLocale = new ThreadLocal();

    public SiteLocaleUtil() {
    }

    public static Long getSite() {
        Long site = (Long)siteLocale.get();
        return site == null?Long.valueOf(0L):site;
    }

    public static void setSite(Long site) {
        siteLocale.set(site);
    }
}
