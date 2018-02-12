package mobi.newsound.database;

import java.util.UUID;

/**
 * Created By Tony on 10/02/2018
 */
public final class ObjectId {

    private ObjectId(){}

    public static String generate(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
