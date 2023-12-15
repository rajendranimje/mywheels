package in.gov.cgg.vehiclereminderlatest.bean;

/**
 * Created by niharika.p on 03-07-2019.
 */

public class ServicingDetails {

    String vehicleNo;
    String currentDate;
    String next_date;
    String date;
    String generalService;
    String gsAmount;
    String osAmount;
    String services;
    String km;
    String next_km;
    long id;

    public String getGsAmount() {
        return gsAmount;
    }

    public void setGsAmount(String gsAmount) {
        this.gsAmount = gsAmount;
    }

    public String getOsAmount() {
        return osAmount;
    }

    public void setOsAmount(String osAmount) {
        this.osAmount = osAmount;
    }

    public String getGeneralService() {
        return generalService;
    }

    public void setGeneralService(String generalService) {
        this.generalService = generalService;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNext_date() {
        return next_date;
    }

    public void setNext_date(String next_date) {
        this.next_date = next_date;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getNext_km() {
        return next_km;
    }

    public void setNext_km(String next_km) {
        this.next_km = next_km;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }


}
