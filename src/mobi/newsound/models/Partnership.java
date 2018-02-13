package mobi.newsound.models;

import com.google.gson.annotations.Expose;
import mobi.newsound.database.Column;
import mobi.newsound.database.DBObject;

public class Partnership extends DBObject {

    @Expose
    private int ptshipNum;

    public Partnership(int ptshipNum) {
        setPtshipNum(ptshipNum);
    }

    public void setPtshipNum(int ptshipNum) {
        this.ptshipNum = ptshipNum;
    }

    public int getPtshipNum() {
        return ptshipNum;
    }

    @Override
    public Column[] db_columns() {
        return new Column[]{
                new Column("ptshipNum",ptshipNum)
        };
    }
}
