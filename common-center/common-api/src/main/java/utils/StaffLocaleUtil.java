package com.ai.rai.interests.common.utils;

/**
 * Created by fangyunfeng on 2017/4/17.
 */
public class StaffLocaleUtil {
    private static ThreadLocal<String> threadLocal = new ThreadLocal();

    public StaffLocaleUtil() {
    }

    public static String getCurrentUserId() {
        String currentStaffId = (String)threadLocal.get();
        return currentStaffId;
    }

    public static void setCurrentStaffId(String currentStaffId) {
        threadLocal.set(currentStaffId);
    }
}
