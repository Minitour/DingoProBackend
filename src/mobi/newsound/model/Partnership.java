package mobi.newsound.model;

import com.google.gson.annotations.Expose;
import mobi.newsound.database.Column;
import mobi.newsound.database.DBObject;

import java.util.*;

public class Partnership extends DBObject {

    @Expose
    private Integer ptshipNum;

    @Expose
    private Date creationDate;

    @Expose
    private Collection<OperationalOfficer> officers;

    public Partnership(Integer ptshipNum, OperationalOfficer officerABadge, OperationalOfficer officerBBadge) {
        setPtshipNum(ptshipNum);
    }

    public Partnership(Integer ptshipNum, Date creationDate) {
        this.ptshipNum = ptshipNum;
        this.creationDate = creationDate;
    }

    public Partnership(Map<String,Object> map) {
        super(map);
    }

    public void setPtshipNum(Integer ptshipNum) {
        this.ptshipNum = ptshipNum;
    }

    public Integer getPtshipNum() {
        return ptshipNum;
    }

    public boolean addOfficerToPartnership(OperationalOfficer officer) {
        if (officer != null) {
            if(officers == null)
                officers = new ArrayList<>();
            return officers.add(officer);
        }
        return false;
    }

    public Collection<OperationalOfficer> getOfficers() {
        return officers;
    }

    public void setOfficers(Collection<OperationalOfficer> officers) {
        this.officers = officers;
    }

    @Override
    public Column[] db_columns() {
        return new Column[]{
                new Column("ptshipNum",ptshipNum),
                new Column("creationDate",creationDate)
        };
    }
}

