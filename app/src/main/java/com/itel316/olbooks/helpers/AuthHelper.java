package com.itel316.olbooks.helpers;

import org.mindrot.jbcrypt.BCrypt;

public class AuthHelper {

    public static String hashPassword(String toHash) {
        String hashed = BCrypt.hashpw(toHash, BCrypt.gensalt());
        return hashed;
    }

    public static boolean comparePassword(String word, String hash){
        if(BCrypt.checkpw(word, hash)) return true;
        return false;
    }
}