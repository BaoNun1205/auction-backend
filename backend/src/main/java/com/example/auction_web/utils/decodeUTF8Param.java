package com.example.auction_web.utils;

import java.io.UnsupportedEncodingException;

public class  decodeUTF8Param {
    public static String decodeUTF8(String input) {
        try {
            return new String(input.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return input;
        }
    }

}
