package com.ai.rai.interests.common.utils;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @author : sunxh3
 * @description :〈描述〉
 * @date : 16/11/10
 */
public class HostUtils
{
    /**
     * 判断当前操作是否Windows.
     *
     * @return true---是Windows操作系统
     */
    public static boolean isWindowsOS(){
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        if(osName.toLowerCase().indexOf("windows")>-1){
            isWindowsOS = true;
        }
        return isWindowsOS;
    }

    /**
     * 获取本机IP地址，并自动区分Windows还是Linux操作系统
     * @return String
     */
    public static String getLocalIP(){
        String sIP = "";
        InetAddress ip = null;
        try {
            //如果是Windows操作系统
            if(isWindowsOS()){
                ip = InetAddress.getLocalHost();
                sIP=ip.getHostAddress();
            }
            //如果是Linux操作系统
            else {
                Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
                boolean bFindIP=false;
                while (enumeration.hasMoreElements()) {
                    NetworkInterface networkInterface = enumeration.nextElement();
                    //Ignore Loop/virtual/Non-started network interface
                    if (networkInterface.isLoopback() || networkInterface.isVirtual() || !networkInterface.isUp()) {
                        continue;
                    }
                    if (bFindIP)
                    {
                        break;
                    }
                    Enumeration<InetAddress> addressEnumeration = networkInterface.getInetAddresses();
                    while (addressEnumeration.hasMoreElements()) {
                        ip = addressEnumeration.nextElement();
                        String address = ip.getHostAddress();
                        //if(address != null && address.indexOf(":") == -1 && !address.startsWith("10."))//ipv4
                        if(address != null && address.indexOf(":") == -1)//ipv4
                        {
                            sIP=address;
                            bFindIP=true;
                            break;
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return sIP;
    }
    public static String getPid()
    {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.split("@")[0];
        return pid;
    }

    /**
     * 获取ip、进程
     * @return
     */
    public static String getHostAndPId()
    {
        String id= HostUtils.getLocalIP().replaceAll("\\.","")+getPid();
        int length=id.length();
        for (int i=0;i<17-length;i++)
        {
            id="0"+id;
        }
        return id;
    }
}