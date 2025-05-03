package com.example.auction_web.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpUtils {
    public static String getServerIpAddress() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            return "127.0.0.1";
        }
    }
}
