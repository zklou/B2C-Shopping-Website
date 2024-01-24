package com.onlineshop;

import java.math.BigInteger;
import java.security.MessageDigest;

public class PasswordUtil {

    static String dict = "\uFEFFadmin\n" +
            "admin12\n" +
            "admin888\n" +
            "admin8\n" +
            "admin123\n" +
            "sysadmin\n" +
            "adminxxx\n" +
            "adminx\n" +
            "6kadmin\n" +
            "base\n" +
            "feitium\n" +
            "admins\n" +
            "root\n" +
            "roots\n" +
            "test\n" +
            "test1\n" +
            "test123\n" +
            "test2\n" +
            "password\n" +
            "aaaAAA111\n" +
            "888888\n" +
            "88888888\n" +
            "000000\n" +
            "00000000\n" +
            "111111\n" +
            "11111111\n" +
            "aaaaaa\n" +
            "aaaaaaaa\n" +
            "135246\n" +
            "135246789\n" +
            "123456\n" +
            "654321";

    static String doSalt(String password) {
        var salt = "#OnlineShop";
        try {
            byte[] bytes = (password + salt).getBytes("UTF-8");
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");

            messageDigest.update(bytes);

            return new BigInteger(1, messageDigest.digest()).toString(16);
        }
        catch (Throwable t) {
            System.err.println("Error when do salt:");
            t.printStackTrace(System.err);
        }

        return password;
    }

    public static void main(String[] args) {
        var parts = dict.split("\n");
        for(var part: parts){
            part = part.strip();
            if(part.length() == 0) {
                continue;
            }

            System.out.println(doSalt(part) + "::" + part);

        }

    }

}
