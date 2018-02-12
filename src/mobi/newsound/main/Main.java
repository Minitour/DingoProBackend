package mobi.newsound.main;

import mobi.newsound.util.JSONTransformer;
import mobi.newsound.util.RESTRoute;
import org.apache.log4j.BasicConfigurator;

import static spark.Spark.get;
import static spark.Spark.port;
import static mobi.newsound.util.Config.config;
import static spark.Spark.post;

public class Main {

    public static void main(String[] args) {

        BasicConfigurator.configure();

        port(config.get("port").getAsInt());

        //TODO: remove this later
        get("/test","application/json",(request, response) -> {
            response.header("Content-Type","application/json");
            return "Hello World";
        },new JSONTransformer());
    }

    static void make(String route, RESTRoute controller){
        post(route, "application/json", controller,new JSONTransformer());
    }
}
