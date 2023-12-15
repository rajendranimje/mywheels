package in.gov.cgg.vehiclereminderlatest.Interface;

import android.content.Context;
import android.widget.ImageView;

public interface VehicleInterface {

     void showRCPopup(String vehicleNo);
     void insurancePopup(String vehicleNo);
     void pollutionPopup(String vehicleNo);
     void servicingPopup(String vehicleNo);
     void batteryInfoPopup(String vehicleNo);
     void chooseImage(String flag, ImageView imgview);
     void selectAllVehicles();
//     void deSelectAllVehicles();
}
