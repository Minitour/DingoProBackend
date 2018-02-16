package mobi.newsound.models;

import com.google.gson.annotations.Expose;
import mobi.newsound.database.Column;
import mobi.newsound.database.DBObject;

import java.util.Date;
import java.util.Optional;

public class VolunteerReport extends Report {

    @Expose
    private String evidenceLink;

    public VolunteerReport(String alphaNum, Date violationDate, String description, String status, String violationType, Defendant defendant, Vehicle vehicle, Appeal appeal, String evidenceLink) {
        super(alphaNum,violationDate,description,status,violationType,defendant,vehicle,appeal);
        setEvidenceLink(evidenceLink);
    }

    public void setEvidenceLink(String evidenceLink) {
        this.evidenceLink = evidenceLink;
    }

    public String getEvidenceLink() {
        return evidenceLink;
    }

    @Override
    public Column[] db_columns() {
        return new Column[]{
                new Column("evidenceLink",evidenceLink)
        };
    }
}
