package mobi.newsound.utils;

import com.google.gson.Gson;
import spark.ResponseTransformer;

/**
 * Created by Antonio Zaitoun on 09/02/2018.
 */
public class JSONTransformer implements ResponseTransformer {
    private Gson gson = new Gson();
    @Override
    public String render(Object model) throws Exception {
        return gson.toJson(model);
    }
}
