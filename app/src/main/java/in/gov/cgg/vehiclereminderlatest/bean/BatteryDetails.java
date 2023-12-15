package in.gov.cgg.vehiclereminderlatest.bean;

/**
 * Created by niharika.p on 27-06-2019.
 */

public class BatteryDetails {

    String vehicleNo;
    String warrantyEndDate;
    String purchasedOn;
    String make;
    String billImgPath;
    long id;
    String batteryImgPath;
    String currentDate;

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getWarrantyEndDate() {
        return warrantyEndDate;
    }

    public void setWarrantyEndDate(String warrantyEndDate) {
        this.warrantyEndDate = warrantyEndDate;
    }

    public String getPurchasedOn() {
        return purchasedOn;
    }

    public void setPurchasedOn(String purchasedOn) {
        this.purchasedOn = purchasedOn;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getBillImgPath() {
        return billImgPath;
    }

    public void setBillImgPath(String billImgPath) {
        this.billImgPath = billImgPath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBatteryImgPath() {
        return batteryImgPath;
    }

    public void setBatteryImgPath(String batteryImgPath) {
        this.batteryImgPath = batteryImgPath;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }
}
