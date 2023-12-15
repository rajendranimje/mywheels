package in.gov.cgg.vehiclereminderlatest.bean;

/**
 * Created by niharika.p on 27-06-2019.
 */

public class LicenceDetails {

    String licNo;
    String validUpto;
    String FrontImgPath;
    String BackImgPath;
    long id;

    public String getLicNo() {
        return licNo;
    }

    public void setLicNo(String licNo) {
        this.licNo = licNo;
    }

    public String getValidUpto() {
        return validUpto;
    }

    public void setValidUpto(String validUpto) {
        this.validUpto = validUpto;
    }

    public String getFrontImgPath() {
        return FrontImgPath;
    }

    public void setFrontImgPath(String frontImgPath) {
        FrontImgPath = frontImgPath;
    }

    public String getBackImgPath() {
        return BackImgPath;
    }

    public void setBackImgPath(String backImgPath) {
        BackImgPath = backImgPath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
