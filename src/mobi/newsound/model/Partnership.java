package mobi.newsound.model;

import com.google.gson.annotations.Expose;
import mobi.newsound.database.Column;
import mobi.newsound.database.DBObject;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class Partnership extends DBObject {

    @Expose
    private Integer ptshipNum;

    @Expose
    private Collection<OperationalOfficer> officers;


    public Partnership(Integer ptshipNum, OperationalOfficer officerABadge, OperationalOfficer officerBBadge) {
        setPtshipNum(ptshipNum);
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
            return officers.add(officer);
        }
        return false;
    }

    public Collection<OperationalOfficer> getOfficers() {
        return Collections.unmodifiableCollection(officers);
    }


    @Override
    public Column[] db_columns() {
        return new Column[]{
                new Column("ptshipNum",ptshipNum)
        };
    }
}

