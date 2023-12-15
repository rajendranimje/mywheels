package in.gov.cgg.vehiclereminderlatest.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import in.gov.cgg.vehiclereminderlatest.R;
import in.gov.cgg.vehiclereminderlatest.activity.DetailsActivity;
import in.gov.cgg.vehiclereminderlatest.bean.DetailsList;
import in.gov.cgg.vehiclereminderlatest.database.Database;
import in.gov.cgg.vehiclereminderlatest.utils.Utils;


/**
 * Created by niharika.p on 29-06-2019.
 */

public class DetailsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<DetailsList> detailslist;
    Database database;
    int insuranceSize, pollutionSize, servicingSize;
    Utils utils;

    public DetailsListAdapter(Context context, ArrayList<DetailsList> detailslist, int insuranceSize, int pollutionSize, int servicingSize) {
        this.context = context;
        this.detailslist = detailslist;
        this.insuranceSize = insuranceSize;
        this.pollutionSize = pollutionSize;
        this.servicingSize = servicingSize;
        utils = new Utils(context);
        database = new Database(context);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view;
        switch (viewType) {
            case DetailsList.INSURANCE_TYPE:
                view = LayoutInflater.from(context).inflate(R.layout.adapter_insurance_details, viewGroup, false);
                return new InsuranceViewHolder(view);

            case DetailsList.POLLUTION_TYPE:
                view = LayoutInflater.from(context).inflate(R.layout.adapter_pollution_details, viewGroup, false);
                return new PollutionViewHolder(view);

            case DetailsList.SERVICING_TYPE:
                view = LayoutInflater.from(context).inflate(R.layout.adapter_servicing_details, viewGroup, false);
                return new ServicingViewHolder(view);


        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder myViewHolder, final int position) {

        final DetailsList list = detailslist.get(position);
        if (list != null) {
            switch (list.type) {

                case DetailsList.INSURANCE_TYPE:

                    if (insuranceSize > 0 && position == 0)
                        ((InsuranceViewHolder) myViewHolder).insurance_title.setVisibility(View.VISIBLE);
                    else
                        ((InsuranceViewHolder) myViewHolder).insurance_title.setVisibility(View.GONE);

                    ((InsuranceViewHolder) myViewHolder).tv_date.setText(list.insuranceDetails.getCurrentDate());
                    ((InsuranceViewHolder) myViewHolder).tv_valid_from.setText(list.insuranceDetails.getValidFrom());
                    ((InsuranceViewHolder) myViewHolder).tv_valid_upto.setText(list.insuranceDetails.getValidUpto());
                    ((InsuranceViewHolder) myViewHolder).tv_agency.setText(list.insuranceDetails.getAgency());

                    utils.loadImage(list.insuranceDetails.getImage(), ((InsuranceViewHolder) myViewHolder).insurance_image);

                    ((InsuranceViewHolder) myViewHolder).insurance_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((DetailsActivity) context).openImage(list.insuranceDetails.getImage());
                        }
                    });
                    break;
                case DetailsList.POLLUTION_TYPE:

                    if (pollutionSize > 0 && insuranceSize == position) {
                        ((PollutionViewHolder) myViewHolder).pollution_title.setVisibility(View.VISIBLE);
                    } else {
                        ((PollutionViewHolder) myViewHolder).pollution_title.setVisibility(View.GONE);
                    }

                    ((PollutionViewHolder) myViewHolder).tv_valid_from.setText(list.pollutionDetails.getValidFrom());
                    ((PollutionViewHolder) myViewHolder).tv_valid_upto.setText(list.pollutionDetails.getValidUpto());

                    utils.loadImage(list.pollutionDetails.getImage(),((PollutionViewHolder) myViewHolder).insurance_image);

                    ((PollutionViewHolder) myViewHolder).insurance_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((DetailsActivity) context).openImage(list.pollutionDetails.getImage());
                        }
                    });

                    break;
                case DetailsList.SERVICING_TYPE:

                    if (servicingSize > 0 && (insuranceSize + pollutionSize) == position) {
                        ((ServicingViewHolder) myViewHolder).servicing_title.setVisibility(View.VISIBLE);
                    } else {
                        ((ServicingViewHolder) myViewHolder).servicing_title.setVisibility(View.GONE);
                    }


                    ((ServicingViewHolder) myViewHolder).tv_date.setText(list.servicingDetails.getDate());
                    ((ServicingViewHolder) myViewHolder).tv_next_date.setText(list.servicingDetails.getNext_date());
                    ((ServicingViewHolder) myViewHolder).tv_services.setText(list.servicingDetails.getServices());
                    ((ServicingViewHolder) myViewHolder).tv_km.setText(list.servicingDetails.getKm());
                    ((ServicingViewHolder) myViewHolder).tv_next_km.setText(list.servicingDetails.getNext_km());
                    break;

            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        switch (detailslist.get(position).type) {
            case 1:
                return DetailsList.INSURANCE_TYPE;
            case 2:
                return DetailsList.POLLUTION_TYPE;
            case 3:
                return DetailsList.SERVICING_TYPE;
            default:
                return -1;

        }

    }

    @Override
    public int getItemCount() {
        return detailslist.size();
    }

    class InsuranceViewHolder extends RecyclerView.ViewHolder {

        TextView tv_valid_from, tv_valid_upto, tv_agency, tv_amount_paid, insurance_title, tv_date;
        ImageView insurance_image, btn_delete;
        ProgressBar pb_insurance;


        public InsuranceViewHolder(View view) {
            super(view);

            tv_valid_from = view.findViewById(R.id.tv_valid_from);
            tv_valid_upto = view.findViewById(R.id.tv_valid_upto);
            tv_agency = view.findViewById(R.id.tv_agency);
            tv_amount_paid = view.findViewById(R.id.tv_amount_paid);
            insurance_image = view.findViewById(R.id.insurance_image);
            btn_delete = view.findViewById(R.id.btn_delete);
            insurance_title = view.findViewById(R.id.insurance_title);
            tv_date = view.findViewById(R.id.currentDate);
            pb_insurance = view.findViewById(R.id.pb_insurance);
        }
    }

    class PollutionViewHolder extends RecyclerView.ViewHolder {

        TextView tv_valid_from, tv_valid_upto, tv_amount_paid, pollution_title;
        ImageView insurance_image;
        ProgressBar pb_pollution;


        public PollutionViewHolder(View view) {
            super(view);

            tv_valid_from = view.findViewById(R.id.tv_valid_from);
            tv_valid_upto = view.findViewById(R.id.tv_valid_upto);
            tv_amount_paid = view.findViewById(R.id.tv_amount_paid);
            insurance_image = view.findViewById(R.id.insurance_image);
            pollution_title = view.findViewById(R.id.pollution_title);
            pb_pollution = view.findViewById(R.id.pb_pollution);
        }
    }

    class ServicingViewHolder extends RecyclerView.ViewHolder {

        TextView tv_date, tv_next_date, tv_services, tv_km, tv_next_km, tv_amount_paid, servicing_title;


        public ServicingViewHolder(View view) {
            super(view);

            tv_date = view.findViewById(R.id.tv_valid_from);
            tv_next_date = view.findViewById(R.id.tv_valid_upto);
            tv_amount_paid = view.findViewById(R.id.tv_amount_paid);
            tv_services = view.findViewById(R.id.tv_quantity);
            tv_km = view.findViewById(R.id.tv_amount_claimed);
            tv_next_km = view.findViewById(R.id.tv_amount_settled);
            servicing_title = view.findViewById(R.id.servicing_title);
        }
    }

}
