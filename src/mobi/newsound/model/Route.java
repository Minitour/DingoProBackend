package mobi.newsound.model;

import com.google.gson.annotations.Expose;
import mobi.newsound.database.Column;
import mobi.newsound.database.DBObject;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

public class Route extends DBObject {

    @Expose
    private Integer serialNum;

    @Expose
    private Date creationDate;

    @Expose
    private Collection<Landmark> landmarks;

    public Route(Integer serialNum,Date creationDate) {
        setSerialNum(serialNum);
        setCreationDate(creationDate);
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        if(creationDate == null)
            creationDate = new Date();
        this.creationDate = creationDate;
    }

    @Override
    public Column[] db_columns() {
        return new Column[]{
                new Column("serialNum",serialNum),
                new Column("creationDate",creationDate)
        };
    }
}
