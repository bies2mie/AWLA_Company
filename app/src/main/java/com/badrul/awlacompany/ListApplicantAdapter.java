package com.badrul.awlacompany;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class ListApplicantAdapter extends RecyclerView.Adapter<ListApplicantAdapter.JobViewHolder> {


    private Context mCtx;
    private List<JobApplicant> applicantsList;
    private OnItemClicked onClick;


    public interface OnItemClicked {
        void onItemClick(int position);
    }

    public ListApplicantAdapter(Context mCtx, List<JobApplicant> applicantsList) {
        this.mCtx = mCtx;
        this.applicantsList = applicantsList;
    }

    @Override
    public JobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.applicant_list, null);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(JobViewHolder holder,final int position) {
        JobApplicant jobapplicant = applicantsList.get(position);


        holder.applicantName.setText(jobapplicant.getUserName()); //getName
        holder.applyStatus.setText("Application Status: "+jobapplicant.getApplyStatus()); //GetICnum

        holder.test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return applicantsList.size();
    }

    class JobViewHolder extends RecyclerView.ViewHolder {

        TextView applicantName, applyStatus;
        // ImageView imageView;
        RelativeLayout test;

        public JobViewHolder(View itemView) {
            super(itemView);

            test=itemView.findViewById(R.id.testing);
            applicantName = itemView.findViewById(R.id.applicantName);
            applyStatus = itemView.findViewById(R.id.applyStatus_v);
        }
    }
    public void setOnClick(OnItemClicked onClick)
    {
        this.onClick=onClick;
    }
}

