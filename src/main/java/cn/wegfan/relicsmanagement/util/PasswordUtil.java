package cn.wegfan.relicsmanagement.util;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;

import java.util.Date;

/**
 * 密码工具类
 */
public class PasswordUtil {

    /**
     * 加密算法
     */
    public final static String HASH_ALGORITHM = "SHA-256";

    /**
     * 加密迭代次数
     */
    public final static int HASH_ITERATIONS = 5;

    /**
     * 使用盐对密码加密
     *
     * @param password 密码
     * @param salt     盐
     *
     * @return 加密后的密码
     */
    public static String encryptPassword(String password, String salt) {
        SimpleHash simpleHash = new SimpleHash(HASH_ALGORITHM, password, salt, HASH_ITERATIONS);
        String result = simpleHash.toString();
        return result;
    }

    /**
     * 根据种子生成盐
     *
     * @param seed 种子
     *
     * @return 盐
     */
    public static String generateSalt(String seed) {
        SecureRandomNumberGenerator generator = new SecureRandomNumberGenerator();
        Date now = new Date();
        // base91.encode(hashlib.sha256('{},{},{}'.format( ... ).encode('utf-8')).hexdigest()[::-1].encode())[::-2]
        byte[] realSeed = (now.getTime() + "HvD84ykW{PD~sMFd1G]A@=uE7!FA.7tL@O%;e2}," + seed).getBytes();
        generator.setSeed(realSeed);
        String result = generator.nextBytes(32).toHex();
        return result;
    }

}
