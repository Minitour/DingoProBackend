package mobi.newsound.model;

import com.google.gson.annotations.Expose;
import mobi.newsound.database.Column;
import mobi.newsound.database.DBObject;

import java.util.Collection;
import java.util.Map;

public class Route extends DBObject {

    @Expose
    private Integer serialNum;

    @Expose
    private Collection<Landmark> landmarks;

    public Route(Integer serialNum) {
        setSerialNum(serialNum);
    }

    public Route(Map<String,Object> map) {
        super(map);
    }

    public void setSerialNum(Integer serialNum) {
        this.serialNum = serialNum;
    }

    public Integer getSerialNum() {
        return serialNum;
    }

    public Collection<Landmark> getLandmarks() {
        return landmarks;
    }

    public void setLandmarks(Collection<Landmark> landmarks) {
        this.landmarks = landmarks;
    }

    @Override
    public Column[] db_columns() {
        return new Column[]{
                new Column("serialNum",serialNum)
        };
    }
}
