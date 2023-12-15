package in.gov.cgg.vehiclereminderlatest.bean;

/**
 * Created by niharika.p on 27-06-2019.
 */

public class RCDetails {

    String vehicleNo;
    String validUpto;
    String rcFrontImgPath;
    long id;
    String rcBackImgPath;
    String currentDate;

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getValidUpto() {
        return validUpto;
    }

    public void setValidUpto(String validUpto) {
        this.validUpto = validUpto;
    }

    public String getRcFrontImgPath() {
        return rcFrontImgPath;
    }

    public void setRcFrontImgPath(String rcFrontImgPath) {
        this.rcFrontImgPath = rcFrontImgPath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRcBackImgPath() {
        return rcBackImgPath;
    }

    public void setRcBackImgPath(String rcBackImgPath) {
        this.rcBackImgPath = rcBackImgPath;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }
}
