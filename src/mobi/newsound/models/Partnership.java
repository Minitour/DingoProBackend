package mobi.newsound.models;

import com.google.gson.annotations.Expose;
import mobi.newsound.database.Column;
import mobi.newsound.database.DBObject;

import java.util.Collection;
import java.util.Collections;

public class Partnership extends DBObject {

    @Expose
    private int ptshipNum;

    @Expose
    private Collection<OperationalOfficer> officers;


    public Partnership(int ptshipNum, OperationalOfficer officerABadge, OperationalOfficer officerBBadge) {
        setPtshipNum(ptshipNum);
    }

    public void setPtshipNum(int ptshipNum) {
        this.ptshipNum = ptshipNum;
    }

    public int getPtshipNum() {
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

