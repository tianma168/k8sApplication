package com.anran.k8s.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.net.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;

/**
 * 类LocalInfoUtils.java的实现描述：获取本机ip,hostname的工具类
 * 
 * @author charles.chengc 2015年6月29日 下午3:26:33
 */
public class LocalInfoUtils {

    // private static final String HOST_NAME = "";
    private static final String HOST_IP = getFirstNoLoopbackAddress();

    private static Logger logger = LoggerFactory.getLogger(LocalInfoUtils.class);

    private static Collection<InetAddress> getAllHostAddress() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            Collection<InetAddress> addresses = new ArrayList<InetAddress>();

            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                try {
                    // 可能是拨号连接, 或者TUN, TAP设备等
                    if (networkInterface.isPointToPoint()) {
                        continue;
                    }
                } catch (Exception e) {
                    // logger.error(e.getMessage());
                }
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    addresses.add(inetAddress);
                }
            }

            return addresses;
        } catch (SocketException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static Collection<String> getAllIpv4NoLoopbackAddresses() {
        Collection<String> noLoopbackAddresses = new ArrayList<String>();
        Collection<InetAddress> allInetAddresses = getAllHostAddress();
        for (InetAddress address : allInetAddresses) {
            if (!address.isLoopbackAddress() && address instanceof Inet4Address) {
                noLoopbackAddresses.add(address.getHostAddress());
            }
        }
        return noLoopbackAddresses;
    }

    public static String getFirstNoLoopbackAddress() {
        Collection<String> allNoLoopbackAddresses = getAllIpv4NoLoopbackAddresses();
        Assert.isTrue(!allNoLoopbackAddresses.isEmpty(), " Sorry, seems you don't have a network card!");
        return allNoLoopbackAddresses.iterator().next();
    }

    public static String getLocalIp() {
        return HOST_IP;
    }

    public static String getIpByDomain(String domain) {
        Assert.hasText(domain, "domain can not be blank!");
        try {
            InetAddress myServer = InetAddress.getByName(domain);
            return myServer.getHostAddress();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * copy from xflush
     * 
     * @param hostname
     * @return
     */
    public static String getIp(String hostname) {
        hostname = StringUtils.trim(hostname);

        if (StringUtils.isBlank(hostname)) {
            return hostname;
        }
        InetAddress addr;
        try {
            addr = InetAddress.getByName(hostname);
            String ret = addr.getHostAddress();
            return ret;
        } catch (UnknownHostException e) {
            return hostname;
        }
    }

    /**
     * copy from xflush
     * 
     * @param ip
     * @return
     */
    public static String getDNSHostname(String ip) {
        if (StringUtils.isBlank(ip)) {
            return null;
        }
        InetAddress addr;
        try {
            addr = InetAddress.getByName(ip);
            String ret = addr.getCanonicalHostName();
            return ret;
        } catch (UnknownHostException e) {
            return null;
        }
    }

    public static String test() {
        String lineBreak = "\n";
        StringBuilder sb = new StringBuilder();
        sb.append("getAllIpv4NoLoopbackAddresses:" + getAllIpv4NoLoopbackAddresses());
        sb.append(lineBreak);
        sb.append("getFirstNoLoopbackAddress:" + getFirstNoLoopbackAddress());
        sb.append(lineBreak);
        sb.append("getLocalIp:" + getLocalIp());
        return sb.toString();
    }

    public static String getHostNameForLiunx() {
        try {
            return (InetAddress.getLocalHost()).getHostName();
        } catch (UnknownHostException uhe) {
            String host = uhe.getMessage(); // host = "hostname: hostname"
            if (host != null) {
                int colon = host.indexOf(':');
                if (colon > 0) {
                    return host.substring(0, colon);
                }
            }
            return "UnknownHost";
        }
    }

    public static void main(String[] args) throws Exception {
        // InetAddress myServer = InetAddress.getByName("www.baidu.com");
        // System.out.println(myServer.getHostAddress());
        System.out.println(getAllHostAddress());
    }

}
