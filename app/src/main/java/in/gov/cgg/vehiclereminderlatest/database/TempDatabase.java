package in.gov.cgg.vehiclereminderlatest.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

import in.gov.cgg.vehiclereminderlatest.bean.BatteryDetails;
import in.gov.cgg.vehiclereminderlatest.bean.InsuranceDetails;
import in.gov.cgg.vehiclereminderlatest.bean.LicenceDetails;
import in.gov.cgg.vehiclereminderlatest.bean.PollutionDetails;
import in.gov.cgg.vehiclereminderlatest.bean.RCDetails;
import in.gov.cgg.vehiclereminderlatest.bean.ServicingDetails;
import in.gov.cgg.vehiclereminderlatest.bean.VehicleDetails;
import in.gov.cgg.vehiclereminderlatest.utils.GlobalDeclarations;

/**
 * Created by niharika.p on 03-07-2019.
 */

public class TempDatabase extends SQLiteOpenHelper {

    private final SQLiteDatabase db;
    Context context;

    private static final String sno = "SNO";
    private static final String vehicle_no = "VEHICLE_NO";
    private static final String id = "ID";

    private String vehicle_tableName = "VEHICLE_DATA";

    // Vehicle Table Columns names
    private static final String make = "MAKE";
    private static final String model = "MODEL";
    private static final String year = "YEAR";
    private static final String fuel_type = "FUEL_TYPE";
    private static final String vehicle_img1_path = "VEHICLE_IMG1_PATH";
    private static final String vehicle_img2_path = "VEHICLE_IMG2_PATH";
    private static final String vehicle_img3_path = "VEHICLE_IMG3_PATH";
    private static final String rc_date = "RC_DATE";
    private static final String ins_date = "INSURANCE_DATE";
    private static final String poll_date = "POLLUTION_DATE";
    private static final String serv_date = "SERVICING_DATE";
    private static final String battery_date = "BATTERY_DATE";
    private static final String flag = "CHECK_FLAG";

    private String lic_tableName = "LICENCE_DATA";

    private static final String lic_no = "LICENCE_NO";
    private static final String lic_front_path = "LICENCE_FRONT_IMG_PATH";
    private static final String lic_back_path = "LICENCE_BACK_IMG_PATH";

    private String rc_tableName = "RC_DATA";

    private static final String rc_valid_upto = "RC_VALID_UPTO";
    private static final String rc_front_path = "RC_FRONT_IMG_PATH";
    private static final String rc_back_path = "RC_BACK_IMG_PATH";

    private String insurance_tableName = "INSURANCE_DATA";

    // Insurance Table Columns names
    private static final String img_path = "IMG_PATH";
    private static final String valid_from = "VALID_FROM";
    private static final String valid_upto = "VALID_UPTO";
    private static final String agency = "AGENCY";
    private static final String amount = "AMOUNT";
    private static final String current_date = "DATE";

    private String pollution_tableName = "POLLUTION_DATA";

    private String servicing_tableName = "SERVICING_DATA";

    private static final String ser_valid_from = "SERVICING_DATE";
    private static final String ser_valid_upto = "NEXT_SERVICING_DATE";
    private static final String ser_general_service = "GENERAL_SERVICE";
    private static final String gs_amount = "GENERAL_SERVICE_AMOUNT";
    private static final String os_amount = "OTHERS_AMOUNT";
    private static final String services = "SERVICES";
    private static final String km = "SERVICING_AT_KM";
    private static final String next_km = "NEXT_SERVICING_AT_KM";

    private String battery_tableName = "BATTERY_DATA";

    private static final String warranty_end_date = "WARRANTY_END_DATE";
    private static final String purchased_on = "PURCHASED_ON";
    private static final String bill_img_path = "BILL_IMG_PATH";
    private static final String battery_img_path = "BATTERY_IMG_PATH";

    VehicleDetails vehicleDetails = new VehicleDetails();
    LicenceDetails licenceDetails = new LicenceDetails();
    RCDetails rcDetails = new RCDetails();
    BatteryDetails batteryDetails = new BatteryDetails();
    InsuranceDetails insuranceDetails = new InsuranceDetails();
    PollutionDetails pollutionDetails = new PollutionDetails();

    ServicingDetails servicingDetails = new ServicingDetails();
    Calendar calendar;


    public TempDatabase(Context context) {
        super(context, GlobalDeclarations.TEMP_DATABASE_NAME, null, 1);
        db = this.getWritableDatabase();
        this.context = context;
        calendar = Calendar.getInstance();
        GlobalDeclarations.TEMP_DATABASE_PATH = context.getDatabasePath(GlobalDeclarations.TEMP_DATABASE_NAME).getAbsolutePath();
        Log.i("databasePath", GlobalDeclarations.TEMP_DATABASE_PATH);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String create_vehicle_table = "CREATE TABLE " + vehicle_tableName + "("
                + sno + " INTEGER ,"
                + id + " BIGINT,"
                + vehicle_no + " TEXT,"
                + make + " TEXT,"
                + model + " TEXT,"
                + year + " TEXT,"
                + fuel_type + " TEXT,"
                + vehicle_img1_path + " TEXT,"
                + vehicle_img2_path + " TEXT,"
                + vehicle_img3_path + " TEXT,"
                + rc_date + " TEXT,"
                + ins_date + " TEXT,"
                + poll_date + " TEXT,"
                + serv_date + " TEXT,"
                + battery_date + " TEXT,"
                + flag + " TEXT"
                + ")";
        db.execSQL(create_vehicle_table);

        String create_lic_table = "CREATE TABLE " + lic_tableName + "("
                + sno + " INTEGER ,"
                + lic_no + " TEXT,"
                + valid_upto + " TEXT,"
                + lic_front_path + " TEXT,"
                + lic_back_path + " TEXT"
                + ")";
        db.execSQL(create_lic_table);

        String create_rc_table = "CREATE TABLE " + rc_tableName + "("
                + sno + " INTEGER ,"
                + id + " BIGINT,"
                + vehicle_no + " TEXT,"
                + rc_valid_upto + " TEXT,"
                + rc_front_path + " TEXT,"
                + rc_back_path + " TEXT,"
                + current_date + " TEXT"
                + ")";
        db.execSQL(create_rc_table);

        String create_battery_table = "CREATE TABLE " + battery_tableName + "("
                + sno + " INTEGER ,"
                + id + " BIGINT,"
                + vehicle_no + " TEXT,"
                + make + " TEXT,"
                + warranty_end_date + " TEXT,"
                + purchased_on + " TEXT,"
                + bill_img_path + " TEXT,"
                + battery_img_path + " TEXT,"
                + current_date + " TEXT"
                + ")";
        db.execSQL(create_battery_table);

        String create_serv_table = "CREATE TABLE " + servicing_tableName + "("
                + sno + " INTEGER ,"
                + id + " BIGINT,"
                + vehicle_no + " TEXT,"
                + ser_valid_from + " TEXT,"
                + ser_valid_upto + " TEXT,"
                + ser_general_service + " TEXT,"
                + gs_amount + " TEXT,"
                + os_amount + " TEXT,"
                + services + " TEXT,"
                + km + " TEXT,"
                + next_km + " TEXT,"
                + current_date + " TEXT"
                + ")";
        db.execSQL(create_serv_table);

        String create_poll_table = "CREATE TABLE " + pollution_tableName + "("
                + sno + " INTEGER ,"
                + id + " BIGINT,"
                + vehicle_no + " TEXT,"
                + img_path + " TEXT,"
                + valid_from + " TEXT,"
                + valid_upto + " TEXT,"
                + current_date + " TEXT"
                + ")";
        db.execSQL(create_poll_table);

        String create_ins_table = "CREATE TABLE " + insurance_tableName + "("
                + sno + " INTEGER ,"
                + id + " BIGINT,"
                + vehicle_no + " TEXT,"
                + img_path + " TEXT,"
                + valid_from + " TEXT,"
                + valid_upto + " TEXT,"
                + agency + " TEXT,"
                + amount + " TEXT,"
                + current_date + " TEXT"
                + ")";
        db.execSQL(create_ins_table);

    }

    public void close() {
        this.close();
    }

    public long insertVehicleData(VehicleDetails details) {

        this.vehicleDetails = details;

        ContentValues values = new ContentValues();
        values.put(sno, id);
        values.put(vehicle_no, details.getVehicleNo());
        values.put(make, details.getMake());
        values.put(model, details.getModel());
        values.put(year, details.getYear());
        values.put(fuel_type, details.getFueltype());
        values.put(vehicle_img1_path, details.getVehicleImg1Path());
        values.put(vehicle_img2_path, details.getVehicleImg2Path());
        values.put(vehicle_img3_path, details.getVehicleImg3Path());
        values.put(rc_date, details.getRcDate());
        values.put(ins_date, details.getInsDate());
        values.put(poll_date, details.getPollDate());
        values.put(serv_date, details.getServDate());
        values.put(battery_date, details.getBatteryDate());
        values.put(flag, details.getChecked());
        values.put(id, details.getId());

        return db.insert(vehicle_tableName, null, values);

    }

    public long deleteVehicleData(){
        return db.delete(vehicle_tableName, null, null);
    }

    public long insertLicenceData(LicenceDetails details) {

        this.licenceDetails = details;

        ContentValues values = new ContentValues();
        values.put(sno, id);
        values.put(lic_no, details.getLicNo());
        values.put(valid_upto, details.getValidUpto());
        values.put(lic_front_path, details.getFrontImgPath());
        values.put(lic_back_path, details.getBackImgPath());

        return db.insert(lic_tableName, null, values);

    }

    public long insertRCData(RCDetails details) {

        this.rcDetails = details;

        ContentValues values = new ContentValues();
        values.put(sno, id);
        values.put(vehicle_no, details.getVehicleNo());
        values.put(rc_valid_upto, details.getValidUpto());
        values.put(rc_front_path, details.getRcFrontImgPath());
        values.put(rc_back_path, details.getRcBackImgPath());
        values.put(current_date, details.getRcBackImgPath());
        values.put(id, details.getId());

        return db.insert(rc_tableName, null, values);

    }

    public long deleteRCData(){
        return db.delete(rc_tableName, null, null);
    }
    public long insertBatteryData(BatteryDetails details) {

        this.batteryDetails = details;

        ContentValues values = new ContentValues();
        values.put(sno, id);
        values.put(vehicle_no, details.getVehicleNo());
        values.put(make, details.getMake());
        values.put(warranty_end_date, details.getWarrantyEndDate());
        values.put(purchased_on, details.getPurchasedOn());
        values.put(bill_img_path, details.getBillImgPath());
        values.put(battery_img_path, details.getBatteryImgPath());
        values.put(current_date, details.getCurrentDate());
        values.put(id, details.getId());

        return db.insert(battery_tableName, null, values);

    }
    public long deleteBatteryData(){
        return db.delete(battery_tableName, null, null);
    }
    public long insertInsuranceData(InsuranceDetails details) {

        this.insuranceDetails = details;

        ContentValues values = new ContentValues();
        values.put(sno, id);
        values.put(vehicle_no, details.getVehicleNo());
        values.put(img_path, details.getImage());
        values.put(valid_from, details.getValidFrom());
        values.put(valid_upto, details.getValidUpto());
        values.put(agency, details.getAgency());
        values.put(amount, details.getAmount());
        values.put(current_date, details.getCurrentDate());
        values.put(id, details.getId());

        return db.insert(insurance_tableName, null, values);

//        db.execSQL("INSERT INTO " + insurance_tableName + "(ID,VehicleNo,Insurance_photo,Insurance_validFrom,Insurance_validUpto,Insurance_agency,Insurance_Date,Insurance_ID)VALUES('" + id + "','" + GlobalDeclarations.VEHICLENO + "','" + insuranceDetails.getImage() + "','" + insuranceDetails.getValidFrom() + "','" + insuranceDetails.getValidUpto() + "','" + insuranceDetails.getAgency() + "','" + insuranceDetails.getCurrentDate() + "','" + insuranceDetails.getId() + "')");
    }
    public long deleteInsuranceData(){
        return db.delete(insurance_tableName, null, null);
    }
    public long insertPollutionData(PollutionDetails details) {

        this.pollutionDetails = details;

        ContentValues values = new ContentValues();
        values.put(sno, id);
        values.put(vehicle_no, details.getVehicleNo());
        values.put(img_path, details.getImage());
        values.put(valid_from, details.getValidFrom());
        values.put(valid_upto, details.getValidUpto());
        values.put(current_date, details.getCurrentDate());
        values.put(id, details.getId());

        return db.insert(pollution_tableName, null, values);

//        db.execSQL("INSERT INTO " + pollution_tableName + "(ID,VehicleNo,Pollution_photo ,Pollution_validFrom , Pollution_validUpto ,Date,Pollution_ID )VALUES('" + id + "','" + GlobalDeclarations.VEHICLENO + "','" + pollutionDetails.getImage() + "','" + pollutionDetails.getValidFrom() + "','" + pollutionDetails.getValidUpto() + "','" + pollutionDetails.getCurrentDate() + "','" + pollutionDetails.getId() + "')");
    }
    public long deletePollutionData(){
        return db.delete(pollution_tableName, null, null);
    }
    public long insertServicingData(ServicingDetails details) {

        this.servicingDetails = details;

        ContentValues values = new ContentValues();
        values.put(sno, id);
        values.put(vehicle_no, details.getVehicleNo());
        values.put(ser_valid_from, details.getDate());
        values.put(ser_valid_upto, details.getNext_date());
        values.put(ser_general_service, details.getGeneralService());
        values.put(gs_amount, details.getGsAmount());
        values.put(services, details.getServices());
        values.put(os_amount, details.getOsAmount());
        values.put(km, details.getKm());
        values.put(next_km, details.getNext_km());
        values.put(current_date, details.getCurrentDate());
        values.put(id, details.getId());

        return db.insert(servicing_tableName, null, values);

//        db.execSQL("INSERT INTO " + servicing_tableName + "(ID,VehicleNo,Servicing_date,Next_Servicing_date ,Services ,Servicing_Km ,Next_Servicing_Km ,Date,Servicing_ID )VALUES('" + id + "','" + GlobalDeclarations.VEHICLENO + "','" + servicingDetails.getCurrentDate() + "','" + servicingDetails.getNext_date() + "','" + servicingDetails.getServices() + "','" + servicingDetails.getKm() + "','" + servicingDetails.getNext_km() + "','" + servicingDetails.getCurrentDate() + "','" + servicingDetails.getId() + "')");
    }
    public long deleteServicingData(){
        return db.delete(servicing_tableName, null, null);
    }
    public long updateVehicleData(VehicleDetails details) {
        this.vehicleDetails = details;

    /*    db.execSQL("UPDATE " + vehicle_tableName
                + " SET " + vehicle_no + "='" + details.getVehicleNo()
                + "'," + make + "='" + details.getMake()
                + "'," + model + "='" + details.getModel()
                + "'," + year + "='" + details.getYear()
                + "'," + fuel_type + "='" + details.getFueltype()
                + "'," + vehicle_img1_path + "='" + details.getVehicleImg1Path()
                + "'," + vehicle_img2_path + "='" + details.getVehicleImg2Path()
                + "'," + vehicle_img3_path + "='" + details.getVehicleImg3Path()
                + "' WHERE " + id + "= " + details.getId());*/

        ContentValues values = new ContentValues();
        values.put(sno, id);
        values.put(vehicle_no, details.getVehicleNo());
        values.put(make, details.getMake());
        values.put(model, details.getModel());
        values.put(year, details.getYear());
        values.put(fuel_type, details.getFueltype());
        values.put(vehicle_img1_path, details.getVehicleImg1Path());
        values.put(vehicle_img2_path, details.getVehicleImg2Path());
        values.put(vehicle_img3_path, details.getVehicleImg3Path());

        return db.update(vehicle_tableName, values, "" + id + "='" + details.getId() + "'", null);
    }

    public long updateVehicleImgPath(String path, String vehicleNo) {

        ContentValues values = new ContentValues();
        values.put(vehicle_img1_path, path);

        return db.update(vehicle_tableName, values, "" + vehicle_no + "='" + vehicleNo + "'", null);
    }

    public long updateInsDate(String date, String vehicleNo) {

        ContentValues values = new ContentValues();
        values.put(ins_date, date);

        return db.update(vehicle_tableName, values, "" + vehicle_no + "='" + vehicleNo + "'", null);
    }

    public long updateServDate(String date, String vehicleNo) {

        ContentValues values = new ContentValues();
        values.put(serv_date, date);

        return db.update(vehicle_tableName, values, "" + vehicle_no + "='" + vehicleNo + "'", null);
    }

    public long updateBatteryDate(String date, String vehicleNo) {

        ContentValues values = new ContentValues();
        values.put(battery_date, date);

        return db.update(vehicle_tableName, values, "" + vehicle_no + "='" + vehicleNo + "'", null);
    }

    public long updateFlag(String value, String vehicleNo) {

        ContentValues values = new ContentValues();
        values.put(flag, value);

        return db.update(vehicle_tableName, values, "" + vehicle_no + "='" + vehicleNo + "'", null);
    }

    public long updateRcFrontImgPath(String path, String vehicleNo) {

        ContentValues values = new ContentValues();
        values.put(rc_front_path, path);

        return db.update(rc_tableName, values, "" + vehicle_no + "='" + vehicleNo + "'", null);
    }

    public long updateRcBackImgPath(String path, String vehicleNo) {

        ContentValues values = new ContentValues();
        values.put(rc_back_path, path);

        return db.update(rc_tableName, values, "" + vehicle_no + "='" + vehicleNo + "'", null);
    }

    public long updateRcDate(String date, String vehicleNo) {

        ContentValues values = new ContentValues();
        values.put(rc_date, date);

        return db.update(vehicle_tableName, values, "" + vehicle_no + "='" + vehicleNo + "'", null);
    }

    public long updatePollDate(String date, String vehicleNo) {

        ContentValues values = new ContentValues();
        values.put(poll_date, date);

        return db.update(vehicle_tableName, values, "" + vehicle_no + "='" + vehicleNo + "'", null);
    }

    public long updateLicFrontImgPath(String path, String licNo) {

        ContentValues values = new ContentValues();
        values.put(lic_front_path, path);

        return db.update(lic_tableName, values, "" + lic_no + "='" + licNo + "'", null);
    }

    public long updateLicBackImgPath(String path, String licNo) {

        ContentValues values = new ContentValues();
        values.put(lic_back_path, path);

        return db.update(lic_tableName, values, "" + lic_no + "='" + licNo + "'", null);
    }

    public long updateInsImgPath(String path, String vehicleNo) {

        ContentValues values = new ContentValues();
        values.put(img_path, path);

        return db.update(insurance_tableName, values, "" + vehicle_no + "='" + vehicleNo + "'", null);
    }

    public long updatePollImgPath(String path, String vehicleNo) {

        ContentValues values = new ContentValues();
        values.put(img_path, path);

        return db.update(pollution_tableName, values, "" + vehicle_no + "='" + vehicleNo + "'", null);
    }

    public ArrayList<VehicleDetails> getVehicleDetails() {

        ArrayList<VehicleDetails> detailslist = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + vehicle_tableName, null);
        if (c.moveToFirst()) {
            do {
                VehicleDetails vehicleDetails = new VehicleDetails();
                vehicleDetails.setVehicleNo(c.getString(c.getColumnIndex(vehicle_no)));
                vehicleDetails.setMake(c.getString(c.getColumnIndex(make)));
                vehicleDetails.setModel(c.getString(c.getColumnIndex(model)));
                vehicleDetails.setYear(c.getString(c.getColumnIndex(year)));
                vehicleDetails.setFueltype(c.getString(c.getColumnIndex(fuel_type)));
                vehicleDetails.setVehicleImg1Path(c.getString(c.getColumnIndex(vehicle_img1_path)));
                vehicleDetails.setId(c.getLong(c.getColumnIndex(id)));
                vehicleDetails.setRcDate(c.getString(c.getColumnIndex(rc_date)));
                vehicleDetails.setInsDate(c.getString(c.getColumnIndex(ins_date)));
                vehicleDetails.setPollDate(c.getString(c.getColumnIndex(poll_date)));
                vehicleDetails.setServDate(c.getString(c.getColumnIndex(serv_date)));
                vehicleDetails.setBatteryDate(c.getString(c.getColumnIndex(battery_date)));
                vehicleDetails.setChecked(c.getString(c.getColumnIndex(flag)));
                detailslist.add(vehicleDetails);

            } while (c.moveToNext());
        }
        return detailslist;
    }

    public ArrayList<LicenceDetails> getLicenceDetails() {

        ArrayList<LicenceDetails> detailslist = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + lic_tableName, null);
        if (c.moveToFirst()) {
            do {
                LicenceDetails licenceDetails = new LicenceDetails();
                licenceDetails.setLicNo(c.getString(c.getColumnIndex(lic_no)));
                licenceDetails.setValidUpto(c.getString(c.getColumnIndex(valid_upto)));
                licenceDetails.setFrontImgPath(c.getString(c.getColumnIndex(lic_front_path)));
                licenceDetails.setBackImgPath(c.getString(c.getColumnIndex(lic_back_path)));

                detailslist.add(licenceDetails);

            } while (c.moveToNext());
        }
        return detailslist;
    }

    public ArrayList<RCDetails> getRCDetails(String vehicleNo) {

        ArrayList<RCDetails> detailslist = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + rc_tableName + " WHERE " + vehicle_no + "= '" + vehicleNo + "'", null);
        if (c.moveToFirst()) {
            do {
                RCDetails rcDetails = new RCDetails();
                rcDetails.setVehicleNo(c.getString(c.getColumnIndex(vehicle_no)));
                rcDetails.setValidUpto(c.getString(c.getColumnIndex(rc_valid_upto)));
                rcDetails.setRcFrontImgPath(c.getString(c.getColumnIndex(rc_front_path)));
                rcDetails.setRcBackImgPath(c.getString(c.getColumnIndex(rc_back_path)));
                rcDetails.setCurrentDate(c.getString(c.getColumnIndex(current_date)));
                rcDetails.setId(c.getLong(c.getColumnIndex(id)));
                detailslist.add(rcDetails);

            } while (c.moveToNext());
        }
        return detailslist;
    }

    public ArrayList<BatteryDetails> getBatteryDetails(String vehicleNo) {

        ArrayList<BatteryDetails> detailslist = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + battery_tableName + " WHERE " + vehicle_no + "= '" + vehicleNo + "'", null);
        if (c.moveToFirst()) {
            do {
                BatteryDetails rcDetails = new BatteryDetails();
                rcDetails.setVehicleNo(c.getString(c.getColumnIndex(vehicle_no)));
                rcDetails.setWarrantyEndDate(c.getString(c.getColumnIndex(warranty_end_date)));
                rcDetails.setPurchasedOn(c.getString(c.getColumnIndex(purchased_on)));
                rcDetails.setMake(c.getString(c.getColumnIndex(make)));
                rcDetails.setBillImgPath(c.getString(c.getColumnIndex(bill_img_path)));
                rcDetails.setBatteryImgPath(c.getString(c.getColumnIndex(battery_img_path)));
                rcDetails.setCurrentDate(c.getString(c.getColumnIndex(current_date)));
                rcDetails.setId(c.getLong(c.getColumnIndex(id)));
                detailslist.add(rcDetails);

            } while (c.moveToNext());
        }
        return detailslist;
    }

    public ArrayList<InsuranceDetails> getInsuranceDetailsList(String vehicleNo) {

        ArrayList<InsuranceDetails> insuranceDetailsList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + insurance_tableName + " WHERE " + vehicle_no + "='" + vehicleNo + "'", null);
        if (c.moveToFirst()) {
            do {
                InsuranceDetails insuranceDetails = new InsuranceDetails();

                insuranceDetails.setImage(c.getString(c.getColumnIndex(img_path)));
                insuranceDetails.setValidFrom(c.getString(c.getColumnIndex(valid_from)));
                insuranceDetails.setValidUpto(c.getString(c.getColumnIndex(valid_upto)));
                insuranceDetails.setAgency(c.getString(c.getColumnIndex(agency)));
                insuranceDetails.setAmount(c.getString(c.getColumnIndex(amount)));
                insuranceDetails.setCurrentDate(c.getString(c.getColumnIndex(current_date)));
                insuranceDetails.setId(c.getLong(c.getColumnIndex(id)));

                insuranceDetailsList.add(insuranceDetails);
            } while (c.moveToNext());
        }
        return insuranceDetailsList;
    }

    public ArrayList<PollutionDetails> getPollutionDetailsList(String vehicleNo) {

        ArrayList<PollutionDetails> pollutionDetailslist = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + pollution_tableName + " WHERE " + vehicle_no + "='" + vehicleNo + "'", null);
        if (c.moveToFirst()) {
            do {
                PollutionDetails pollutionDetails = new PollutionDetails();

                pollutionDetails.setImage(c.getString(c.getColumnIndex(img_path)));
                pollutionDetails.setValidFrom(c.getString(c.getColumnIndex(valid_from)));
                pollutionDetails.setValidUpto(c.getString(c.getColumnIndex(valid_upto)));
                pollutionDetails.setCurrentDate(c.getString(c.getColumnIndex(current_date)));
                pollutionDetails.setId(c.getLong(c.getColumnIndex(id)));

                pollutionDetailslist.add(pollutionDetails);
            } while (c.moveToNext());
        }
        return pollutionDetailslist;
    }

    public ArrayList<ServicingDetails> getServicingDetailsList(String vehicleNo) {

        ArrayList<ServicingDetails> servicingDetailsList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + servicing_tableName + " WHERE " + vehicle_no + "='" + vehicleNo + "'", null);
        if (c.moveToFirst()) {
            do {
                ServicingDetails servicingDetails = new ServicingDetails();

                servicingDetails.setDate(c.getString(c.getColumnIndex(ser_valid_from)));
                servicingDetails.setNext_date(c.getString(c.getColumnIndex(ser_valid_upto)));
                servicingDetails.setServices(c.getString(c.getColumnIndex(services)));
                servicingDetails.setGsAmount(c.getString(c.getColumnIndex(gs_amount)));
                servicingDetails.setOsAmount(c.getString(c.getColumnIndex(os_amount)));
                servicingDetails.setGeneralService(c.getString(c.getColumnIndex(ser_general_service)));
                servicingDetails.setKm(c.getString(c.getColumnIndex(km)));
                servicingDetails.setNext_km(c.getString(c.getColumnIndex(next_km)));
                servicingDetails.setCurrentDate(c.getString(c.getColumnIndex(current_date)));
                servicingDetails.setId(c.getLong(c.getColumnIndex(id)));

                servicingDetailsList.add(servicingDetails);
            } while (c.moveToNext());
        }
        return servicingDetailsList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS ' " + vehicle_tableName + "'");
        db.execSQL("DROP TABLE IF EXISTS' " + insurance_tableName + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + pollution_tableName + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + servicing_tableName + "'");


    }
}
