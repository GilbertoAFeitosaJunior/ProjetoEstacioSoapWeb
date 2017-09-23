package mobi.stos.projetoestacio.util;

import com.sun.jersey.core.util.Base64;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Random;

public class Security64 {

    private static final Random rand = new Random((new Date()).getTime());

    public static String encrypt(String str) throws UnsupportedEncodingException {
        byte[] salt = new byte[12];
        rand.nextBytes(salt);
        return new String(Base64.encode(salt)) + new String(Base64.encode(str.getBytes("UTF-8")));
    }

    public static String decrypt(String encstr) {
        if (encstr.length() > 16) {
            String cipher = encstr.substring(16);
            return new String(Base64.decode(cipher));
        }
        return null;
    }

}
