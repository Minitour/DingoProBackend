package mobi.newsound.model;

import com.google.gson.annotations.Expose;
import mobi.newsound.database.Column;
import mobi.newsound.database.DBObject;

import java.util.Date;
import java.util.Map;

public class Appeal extends DBObject {

    @Expose
    private int serialNum;

    @Expose
    private String reason;

    @Expose
    private Date appealDate;

    @Expose
    private Report report;

    @Expose
    private Defendant defendant;

    public Appeal(int serialNum, String reason, Date appealDate) {
        setSerialNum(serialNum);
        setReason(reason);
        setAppealDate(appealDate);
    }

    public Appeal(Map<String,Object> map) {
        super(map);
    }

    public void setSerialNum(int serialNum) {
        this.serialNum = serialNum;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setAppealDate(Date appealDate) {
        this.appealDate = appealDate;
    }

    public int getSerialNum() {
        return serialNum;
    }

    public String getReason() {
        return reason;
    }

    public Date getAppealDate() {
        return appealDate;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public Defendant getDefendant() {
        return defendant;
    }

    public void setDefendant(Defendant defendant) {
        this.defendant = defendant;
    }

    @Override
    public Column[] db_columns() {
        return new Column[]{
                new Column("serialNum",serialNum),
                new Column("reason",reason),
                new Column("appealDate",appealDate)
        };
    }

    @Override
    public String db_table() {
        return "TblAppeals";
    }
}
