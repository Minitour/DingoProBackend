package mobi.newsound.main;

import mobi.newsound.database.AuthContext;
import mobi.newsound.database.DataStore;
import mobi.newsound.models.Defendant;
import mobi.newsound.util.JSONResponse;
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

        initTests();
    }

    static void make(String route, RESTRoute controller){
        post(route, "application/json", controller,new JSONTransformer());




    }

    static void initTests() {

        get("/test5","application/json",(request, response) -> {
            response.header("Content-Type","application/json");
            //int id, String drivingLicense, String name, String address
            Defendant defendant = new Defendant(123, "2345678", "test", "addressTest");
            try(DataStore db = DataStore.getInstance()){
                //AuthContext context = db.signIn("goldfedertomer@gmail.com","5f1MXgqBzm");
                return db.createDefendant(null,defendant);
            }catch (DataStore.DSException e){
                return JSONResponse.FAILURE().message(e.getMessage());
            }
        },new JSONTransformer());
    }
}
