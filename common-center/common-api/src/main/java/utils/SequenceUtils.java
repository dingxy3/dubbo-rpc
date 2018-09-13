package utils;

import java.util.concurrent.atomic.AtomicInteger;

public class SequenceUtils
{
    public static String hostPid = HostUtils.getHostAndPId();
//    public static volatile int seqNumber = 100000;
    public static AtomicInteger seqNumber = new AtomicInteger(0);

    public static synchronized int getSeqNumber()
    {
        /*if (seqNumber == 999999)
        {
            seqNumber = 100000;
        }
        return seqNumber;*/

        return seqNumber.getAndIncrement();
    }

    /**
     * 获取64位序列(日期17位+9位毫微秒+17为ip进程号+6为递增数+15位随机数)
     *
     * @return 序列
     */
    public static String getSeqId()
    {
        StringBuilder b = new StringBuilder("");
        String t = String.valueOf(System.nanoTime());
        if (t.length() > 9)
        {
            t = t.substring(t.length() - 9, t.length());
        }
        String r = getFixLengthString(15);
        b.append(DateUtils.getSysdate("yyyyMMddkkmmssSSS")).append(t).append(hostPid).append(r);
        return b.toString();
    }

    /**
     * 返回长度为【strLength】的随机数，在前面补0
     */
    public static String getFixLengthString(int strLength)
    {
//    // 返回固定的长度的随机数
        double d = Double.parseDouble(String.valueOf(Math.pow(10, strLength)));
        long r = (long) Math.floor(Math.random() * d);
        String random = String.valueOf(r);
        for (int i = 0; i < strLength - String.valueOf(r).length(); i++)
        {
            random = "0" + random;
        }
        return random.equals("0") ? "" : random;
    }

    /**
     * 随机生成6位随机验证码
     * author by dingxy3
     * @return
     */
    public static String createRandomVcode(int VerifyCode){
        //验证码
        String vcode = "";
        for (int i = 0; i < VerifyCode; i++) {
            vcode = vcode + (int)(Math.random() * 9);
        }
        return vcode;
    }
}
