package in.gov.cgg.vehiclereminderlatest.bean;

import java.util.ArrayList;

/**
 * Created by niharika.p on 10-07-2019.
 */

public class DetailsList {


    public static final int INSURANCE_TYPE = 1;
    public static final int POLLUTION_TYPE = 2;
    public static final int SERVICING_TYPE = 3;
    public static final int CHALLANPAID_TYPE = 4;
    public static final int INSURANCECLAIMS_TYPE = 5;
    public static final int FUELREFILLS_TYPE = 6;
    public int type;

   /* public ArrayList<InsuranceDetails> insuranceDetails;
    public ArrayList<PollutionDetails> pollutionDetails;
    public ArrayList<ChallansPaidDetails> challansPaidDetails;
    public ArrayList<InsuranceClaimsDetails> insuranceClaimsDetails;
    public ArrayList<FuelRefillsDetails> fuelRefillsDetails;
    public ArrayList<ServicingDetails> servicingDetails;*/


    public InsuranceDetails insuranceDetails;
    public PollutionDetails pollutionDetails;
    public ChallansPaidDetails challansPaidDetails;
    public InsuranceClaimsDetails insuranceClaimsDetails;
    public FuelRefillsDetails fuelRefillsDetails;
    public ServicingDetails servicingDetails;

    public DetailsList(int type, InsuranceDetails insuranceDetails, PollutionDetails pollutionDetails, ChallansPaidDetails challansPaidDetails, InsuranceClaimsDetails insuranceClaimsDetails, FuelRefillsDetails fuelRefillsDetails, ServicingDetails servicingDetails) {
        this.type = type;
        this.insuranceDetails = insuranceDetails;
        this.pollutionDetails = pollutionDetails;
        this.challansPaidDetails = challansPaidDetails;
        this.insuranceClaimsDetails = insuranceClaimsDetails;
        this.fuelRefillsDetails = fuelRefillsDetails;
        this.servicingDetails = servicingDetails;


    } /* public DetailsList(int type, ArrayList<InsuranceDetails> insuranceDetails, ArrayList<PollutionDetails> pollutionDetails, ArrayList<ChallansPaidDetails> challansPaidDetails, ArrayList<InsuranceClaimsDetails> insuranceClaimsDetails, ArrayList<FuelRefillsDetails> fuelRefillsDetails, ArrayList<ServicingDetails> servicingDetails) {
        this.type = type;
        this.insuranceDetails = insuranceDetails;
        this.pollutionDetails = pollutionDetails;
        this.challansPaidDetails = challansPaidDetails;
        this.insuranceClaimsDetails = insuranceClaimsDetails;
        this.fuelRefillsDetails = fuelRefillsDetails;
        this.servicingDetails = servicingDetails;


    }
*/
  /*  public ArrayList<InsuranceDetails> getInsuranceDetails() {
        return insuranceDetails;
    }

    public void setInsuranceDetails(int type, ArrayList<InsuranceDetails> insuranceDetails) {
        this.type = type;
        this.insuranceDetails = insuranceDetails;

    }

    public ArrayList<PollutionDetails> getPollutionDetails() {
        return pollutionDetails;
    }

    public void setPollutionDetails(int type, ArrayList<PollutionDetails> pollutionDetails) {
        this.type = type;
        this.pollutionDetails = pollutionDetails;
    }

    public ArrayList<ChallansPaidDetails> getChallansPaidDetails() {
        return challansPaidDetails;
    }

    public void setChallansPaidDetails(int type, ArrayList<ChallansPaidDetails> challansPaidDetails) {
        this.type = type;
        this.challansPaidDetails = challansPaidDetails;
    }

    public ArrayList<InsuranceClaimsDetails> getInsuranceClaimsDetails() {
        return insuranceClaimsDetails;
    }

    public void setInsuranceClaimsDetails(int type, ArrayList<InsuranceClaimsDetails> insuranceClaimsDetails) {
        this.type = type;
        this.insuranceClaimsDetails = insuranceClaimsDetails;
    }

    public ArrayList<FuelRefillsDetails> getFuelRefillsDetails() {
        return fuelRefillsDetails;
    }

    public void setFuelRefillsDetails(int type, ArrayList<FuelRefillsDetails> fuelRefillsDetails) {
        this.type = type;
        this.fuelRefillsDetails = fuelRefillsDetails;
    }

    public ArrayList<ServicingDetails> getServicingDetails() {
        return servicingDetails;
    }

    public void setServicingDetails(int type, ArrayList<ServicingDetails> servicingDetails) {
        this.type = type;
        this.servicingDetails = servicingDetails;
    }*/

}
