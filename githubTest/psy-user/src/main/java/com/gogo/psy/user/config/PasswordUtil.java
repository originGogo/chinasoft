package com.gogo.psy.user.config;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

public class PasswordUtil {

    private static final char[] DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    /**
     * 加密算法
     */
    private static final String algorithmName = "md5";

    /**
     * 混淆次数
     */
    private static final Integer hashIterations = 2;

    public static String generateSalt() {
        return RandomStringUtils.randomAlphabetic(32);
    }

    /**
     * 生成密码  md5（盐+密码）
     *
     * @param password
     * @param salt
     * @return
     */
    public static String generatePassword(String password, String salt) {
        return DigestUtils.md5Hex(salt + password);
//        try {
//            MessageDigest digest = MessageDigest.getInstance(algorithmName);
//            if (salt != null) {
//                digest.reset();
//                digest.update(salt.getBytes(StandardCharsets.UTF_8));
//            }
//
//            byte[] hashed = digest.digest(password.getBytes(StandardCharsets.UTF_8));
//            int iterations = hashIterations - 1;
//
//            for (int i = 0; i < iterations; ++i) {
//                digest.reset();
//                hashed = digest.digest(hashed);
//            }
//
//
//            int l = hashed.length;
//            char[] out = new char[l << 1];
//            int i = 0;
//
//            for (int j = 0; i < l; ++i) {
//                out[j++] = DIGITS[(240 & hashed[i]) >>> 4];
//                out[j++] = DIGITS[15 & hashed[i]];
//            }
//
//            return new String(out);
//        } catch (Exception e) {
//            throw new BizException(ResultCodeEnum.DATA_ERROR);
//        }
    }

    /**
     * 校验密码是否一致
     *
     * @param inputPassword
     * @param dbPassword
     * @param salt
     * @return
     */
    public static Boolean checkPassword(String inputPassword, String dbPassword, String salt) {
        String newPassword = PasswordUtil.generatePassword(inputPassword, salt);
        if (newPassword.equals(dbPassword)) {
            return true;
        } else {
            return false;
        }
    }

}
