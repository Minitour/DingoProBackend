package mobi.newsound.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created By Tony on 12/02/2018
 */
public class PropertiesLoader {
    private final Properties properties = new Properties();

    public PropertiesLoader(String path){
        InputStream is = null;
        try {
            properties.load(is = new FileInputStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public String get(String key){
        return properties.getProperty(key);
    }

}
