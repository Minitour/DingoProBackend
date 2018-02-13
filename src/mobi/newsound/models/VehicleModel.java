package mobi.newsound.models;

import com.google.gson.annotations.Expose;
import mobi.newsound.database.Column;
import mobi.newsound.database.DBObject;

public class VehicleModel extends DBObject {

    @Expose
    private String modelNum;

    @Expose
    private String name;

    public VehicleModel(String modelNum, String name) {
        setModelNum(modelNum);
        setName(name);
    }

    public void setModelNum(String modelNum) {
        this.modelNum = modelNum;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModelNum() {
        return modelNum;
    }

    public String getName() {
        return name;
    }

    @Override
    public Column[] db_columns() {
        return new Column[]{
                new Column("modelNum",modelNum),
                new Column("name",name)
        };
    }
}
