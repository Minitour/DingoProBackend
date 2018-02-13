package mobi.newsound.models;

import com.google.gson.annotations.Expose;
import mobi.newsound.database.Column;
import mobi.newsound.database.DBObject;

import java.util.Optional;

public class Vehicle extends DBObject {

    @Expose
    private String licensePlate;

    @Expose
    private String colorHEX;

    @Expose
    private VehicleModel model;

    public Vehicle(String licensePlate, String colorHEX, VehicleModel model) {
        setLicensePlate(licensePlate);
        setColorHEX(colorHEX);
        setModel(model);
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public void setColorHEX(String colorHEX) {
        this.colorHEX = colorHEX;
    }

    public void setModel(VehicleModel model) {
        this.model = model;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getColorHEX() {
        return colorHEX;
    }

    public VehicleModel getModel() {
        return model;
    }

    @Override
    public Column[] db_columns() {
        return new Column[]{
                new Column("licensePlate",licensePlate),
                new Column("colorHEX",colorHEX),
                new Column<>("modleNum", Optional.ofNullable(model), VehicleModel::getModelNum) //FK
        };
    }
}
