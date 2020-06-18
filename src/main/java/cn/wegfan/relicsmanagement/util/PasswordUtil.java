package cn.wegfan.relicsmanagement.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;

import java.util.Date;

@Slf4j
public class PasswordUtil {

    public final static String HASH_ALGORITHM = "SHA-256";

    public final static int HASH_ITERATIONS = 5;

    public static String encryptPassword(String password, String salt) {
        SimpleHash simpleHash = new SimpleHash(HASH_ALGORITHM, password, salt, HASH_ITERATIONS);
        String result = simpleHash.toString();
        return result;
    }

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
