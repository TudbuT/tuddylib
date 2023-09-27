package de.tudbut.tools;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher {
    public static final String TYPE_SHA256HEX = "sha256hex";
    public static final String TYPE_SHA512HEX = "sha512hex";
    public static final String TYPE_INT = "int";
    @SuppressWarnings("CanBeFinal")
    public static String LETTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!\"§$%&/()=?#_-.:,;µ<>|^°{[]}\\ ";

    public static String sha256hex(String toHash) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Impossible condition reached");
        }
        return hash(toHash, digest);
    }

    public static String sha512hex(String toHash) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-512");
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Impossible condition reached");
        }
        return hash(toHash, digest);
    }

    private static String hash(String toHash, MessageDigest digest) {
        byte[] hash = digest.digest(
                toHash.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String bruteforce(String type, String hash, int beginAt) {
        switch (type) {
            case TYPE_SHA256HEX:
                return bf_sha256hex(hash, beginAt);
            case TYPE_SHA512HEX:
                return bf_sha512hex(hash, beginAt);
            case TYPE_INT:
                return bf_int(Integer.parseInt(hash), beginAt);
        }
        return null;
    }

    private static String bf_int(int hash, int beginAt) {
        for (int length = beginAt; true; length++) {
            String s;
            if ((s = bf_int_tryGenChar("", 0, length, hash)) != null) {
                if (s.hashCode() == hash)
                    return s;
            }
        }
    }

    private static String bf_int_tryGenChar(String str, int pos, int length, int hash) {
        if (length == 0) {
            if (str.hashCode() == hash)
                return str;
        }
        else {
            if (pos != 0) {
                pos = 0;
            }
            for (int i = pos; i < LETTERS.toCharArray().length; i++) {
                String s;
                if ((s = bf_int_tryGenChar(str + LETTERS.charAt(i), i, length - 1, hash)) != null) {
                    return s;
                }
            }
        }
        return null;
    }

    private static String bf_sha256hex(String hash, int beginAt) {
        for (int length = beginAt; true; length++) {
            String s;
            if ((s = bf_sha256hex_tryGenChar("", 0, length, hash)) != null) {
                return s;
            }
        }
    }

    private static String bf_sha256hex_tryGenChar(String str, int pos, int length, String hash) {
        if (length == 0) {
            if (sha256hex(str).equals(hash))
                return str;
        }
        else {
            if (pos != 0) {
                pos = 0;
            }
            for (int i = pos; i < LETTERS.toCharArray().length; i++) {
                String s;
                if ((s = bf_sha256hex_tryGenChar(str + LETTERS.charAt(i), i, length - 1, hash)) != null) {
                    return s;
                }
            }
        }
        return null;
    }

    private static String bf_sha512hex(String hash, int beginAt) {
        for (int length = beginAt; true; length++) {
            String s;
            if ((s = bf_sha512hex_tryGenChar("", 0, length, hash)) != null) {
                return s;
            }
        }
    }

    private static String bf_sha512hex_tryGenChar(String str, int pos, int length, String hash) {
        if (length == 0) {
            if (sha512hex(str).equals(hash))
                return str;
        }
        else {
            if (pos != 0) {
                pos = 0;
            }
            for (int i = pos; i < LETTERS.toCharArray().length; i++) {
                String s;
                if ((s = bf_sha512hex_tryGenChar(str + LETTERS.charAt(i), i, length - 1, hash)) != null) {
                    return s;
                }
            }
        }
        return null;
    }
}
