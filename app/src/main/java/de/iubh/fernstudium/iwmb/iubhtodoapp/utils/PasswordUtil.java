package de.iubh.fernstudium.iwmb.iubhtodoapp.utils;

import org.mindrot.jbcrypt.BCrypt;

import de.iubh.fernstudium.iwmb.iubhtodoapp.domain.exceptions.InvalidPasswordException;

/**
 * Created by ivanj on 10.03.2018.
 */

public class PasswordUtil {

    private static String salt = BCrypt.gensalt(5);

    public static boolean checkPw(String password, String storedHash) throws InvalidPasswordException {
        if(null == storedHash || !storedHash.startsWith("$2a$")){
            throw new InvalidPasswordException("Invalid hash provided for comparison");
        }
        return BCrypt.checkpw(password, storedHash);
    }

    public static String hashPw(String pw){
        return BCrypt.hashpw(pw, salt);
    }
}
