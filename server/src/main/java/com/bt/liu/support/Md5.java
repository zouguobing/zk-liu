package com.bt.liu.support;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by binglove on 16/3/16.
 */
public class MD5 {

    private static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    private static final String ENCODE = "UTF-8";

    private MessageDigest digest;

    private ReentrantLock onlyLock = new ReentrantLock();

    private MD5() {
        try {
            digest = MessageDigest.getInstance("md5");
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static MD5 getInstance() {
        return InnerMD5.md5;
    }


    private byte[] hash(String content) {
        onlyLock.lock();
        byte[] bt = null;
        try {
            bt = digest.digest(content.getBytes(ENCODE));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("unsupported utf-8 encoding", e);
        } finally {
            onlyLock.unlock();
        }
        return bt;
    }


    public  String md5(String content) {
        byte[] bt = hash(content);
        if (bt == null || bt.length != 16) {
            throw new IllegalArgumentException("byte[] length not eq 16");
        }
        int l = bt.length;
        char[] out = new char[l << 1];
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = digits[(0xF0 & bt[i]) >>> 4];
            out[j++] = digits[0x0F & bt[i]];
        }
        return new String(out);
    }



    static class InnerMD5 {
        private static MD5 md5 = new MD5();
    }
}
