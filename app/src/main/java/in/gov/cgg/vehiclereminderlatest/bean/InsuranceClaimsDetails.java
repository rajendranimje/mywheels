package in.gov.cgg.vehiclereminderlatest.bean;

/**
 * Created by niharika.p on 03-07-2019.
 */

public class InsuranceClaimsDetails {

    String currentDate;
    String purpose;
    String date;
    String amount_claimed;
    String amount_settled;
    String amount_paid;

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getAmount_claimed() {
        return amount_claimed;
    }

    public void setAmount_claimed(String amount_claimed) {
        this.amount_claimed = amount_claimed;
    }

    public String getAmount_settled() {
        return amount_settled;
    }

    public void setAmount_settled(String amount_settled) {
        this.amount_settled = amount_settled;
    }

    public String getAmount_paid() {
        return amount_paid;
    }

    public void setAmount_paid(String amount_paid) {
        this.amount_paid = amount_paid;
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
