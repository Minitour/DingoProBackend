package mobi.newsound.model;

import com.google.gson.annotations.Expose;
import mobi.newsound.database.Column;
import mobi.newsound.database.DBObject;

import java.util.Date;
import java.util.Map;

public class Shift extends DBObject {

    @Expose
    private Integer shiftCode;

    @Expose
    private Date shiftDate;

    @Expose
    private String type;

    public Shift(Integer shiftCode, Date shiftDate, String type) {
        setShiftCode(shiftCode);
        setShiftDate(shiftDate);
        setType(type);
    }

    public Shift(Map<String, Object> map) {
        super(map);
    }

    public void setShiftCode(Integer shiftCode) {
        this.shiftCode = shiftCode;
    }

    public void setShiftDate(Date shiftDate) {
        this.shiftDate = shiftDate;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getShiftCode() {
        return shiftCode;
    }

    public Date getShiftDate() {
        return shiftDate;
    }

    public String getType() {
        return type;
    }

    @Override
    public Column[] db_columns() {
        return new Column[]{
                new Column("shiftCode",shiftCode),
                new Column("shiftDate",shiftDate),
                new Column("type",type)
        };
    }
}
