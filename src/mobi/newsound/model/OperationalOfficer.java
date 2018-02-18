package mobi.newsound.model;

import com.google.gson.annotations.Expose;
import mobi.newsound.database.Column;
import mobi.newsound.database.DBObject;

import java.util.Map;
import java.util.Optional;

public class OperationalOfficer extends Account {

    @Expose
    private String pin;

    @Expose
    private String name;

    @Expose
    private String phoneExtension;

    @Expose
    private int position;

    @Expose
    private Partnership ptship;

    public OperationalOfficer(String ID, String EMAIL, Integer ROLE_ID, String pin, String name, String phoneExtension, int position, Partnership ptship) {
        super(ID, EMAIL, ROLE_ID);
        this.pin = pin;
        this.name = name;
        this.phoneExtension = phoneExtension;
        this.position = position;
        this.ptship = ptship;
    }

    public OperationalOfficer(Map<String,Object> map) {
        super(map);
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneExtension(String phoneExtension) {
        this.phoneExtension = phoneExtension;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setPtship(Partnership ptship) {
        this.ptship = ptship;
    }

    public String getPin() {
        return pin;
    }

    public String getName() {
        return name;
    }

    public String getPhoneExtension() {
        return phoneExtension;
    }

    public int getPosition() {
        return position;
    }

    public Partnership getPtship() {
        return ptship;
    }

    @Override
    public Column[] db_columns() {
        return new Column[]{
                new Column("pin",pin),
                new Column("name",name),
                new Column("phoneExtension",phoneExtension),
                new Column("position",position),
                new Column("account_id",getID()),
                new Column<>("ptship", Optional.ofNullable(ptship), Partnership::getPtshipNum) //FK
        };
    }

    @Override
    public String db_table() {
        return "TblOperationalOfficers";
    }
}
