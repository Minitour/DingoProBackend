package mobi.newsound.model;

import com.google.gson.annotations.Expose;
import mobi.newsound.database.Column;
import mobi.newsound.database.DBObject;

import java.util.Map;

public class Route extends DBObject {

    @Expose
    private int serialNum;

    public Route(int serialNum) {
        setSerialNum(serialNum);
    }

    public Route(Map<String,Object> map) {
        super(map);
    }

    public void setSerialNum(int serialNum) {
        this.serialNum = serialNum;
    }

    public int getSerialNum() {
        return serialNum;
    }

    @Override
    public Column[] db_columns() {
        return new Column[]{
                new Column("serialNum",serialNum)
        };
    }
}
