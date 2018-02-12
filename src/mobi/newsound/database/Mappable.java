package mobi.newsound.database;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public interface Mappable {

    default void map(Map map){
        System.err.println("Not Implemented");
    }

    default Map getMap(){
        return new ObjectMapper().convertValue(this,Map.class);
    }

    default Column[] db_columns(){
        return new Column[]{};
    }

}
