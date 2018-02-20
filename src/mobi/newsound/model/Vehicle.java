package mobi.newsound.model;

import com.google.gson.annotations.Expose;
import mobi.newsound.database.Column;
import mobi.newsound.database.DBObject;

import java.util.Map;
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

    public Vehicle(Map<String,Object> map) {
        super(map);
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
                new Column<>("model", Optional.ofNullable(model), VehicleModel::getModelNum) //FK
        };
    }
}
