package mobi.newsound.database;

import java.util.Random;

/**
 * Created By Tony on 10/02/2018
 */
public final class TokenGenerator {

    private TokenGenerator(){}

    public static String generateToken(int length,String from) {
        StringBuilder sb =  new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++)
            sb.append(from.charAt(random.nextInt(from.length())));

        return sb.toString();
    }

    public static String generateToken(int length) {
        return generateToken(length,
                "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890");
    }

    public static String generateToken(){
        return generateToken(128);
    }
}
