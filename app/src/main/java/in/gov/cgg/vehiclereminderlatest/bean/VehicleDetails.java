package in.gov.cgg.vehiclereminderlatest.bean;

/**
 * Created by niharika.p on 27-06-2019.
 */

public class VehicleDetails {

    String vehicleNo;
    String make;
    String model;
    String year;
    String fueltype;
    String vehicleImg1Path;
    long id;
    String vehicleImg2Path;
    String vehicleImg3Path;
    String date;
    int position;
    boolean flag;
    String rcDate;
    String insDate;
    String pollDate;
    String servDate;
    String batteryDate;
    String checked;


    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getBatteryDate() {
        return batteryDate;
    }

    public void setBatteryDate(String batteryDate) {
        this.batteryDate = batteryDate;
    }

    public String getRcDate() {
        return rcDate;
    }

    public void setRcDate(String rcDate) {
        this.rcDate = rcDate;
    }

    public String getInsDate() {
        return insDate;
    }

    public void setInsDate(String insDate) {
        this.insDate = insDate;
    }

    public String getPollDate() {
        return pollDate;
    }

    public void setPollDate(String pollDate) {
        this.pollDate = pollDate;
    }

    public String getServDate() {
        return servDate;
    }

    public void setServDate(String servDate) {
        this.servDate = servDate;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVehicleImg3Path() {
        return vehicleImg3Path;
    }

    public void setVehicleImg3Path(String vehicleImg3Path) {
        this.vehicleImg3Path = vehicleImg3Path;
    }

    public String getVehicleImg2Path() {
        return vehicleImg2Path;
    }

    public void setVehicleImg2Path(String vehicleImg2Path) {
        this.vehicleImg2Path = vehicleImg2Path;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getVehicleImg1Path() {
        return vehicleImg1Path;
    }

    public void setVehicleImg1Path(String vehicleImg1Path) {
        this.vehicleImg1Path = vehicleImg1Path;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getFueltype() {
        return fueltype;
    }

    public void setFueltype(String fueltype) {
        this.fueltype = fueltype;
    }
}
