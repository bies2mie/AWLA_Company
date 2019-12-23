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

public class CurrentJobAdapter extends RecyclerView.Adapter<CurrentJobAdapter.JobViewHolder> {


    private Context mCtx;
    private List<Job> jobList;
    private OnItemClicked onClick;


    public interface OnItemClicked {
        void onItemClick(int position);
    }

    public CurrentJobAdapter(Context mCtx, List<Job> jobList) {
        this.mCtx = mCtx;
        this.jobList = jobList;
    }

    @Override
    public JobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.job_list, null);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(JobViewHolder holder,final int position) {
        Job job = jobList.get(position);


        holder.jobTitle.setText(job.getJobPosition()); //getName
        holder.jobCategory.setText("Category: "+job.getJobCategory()); //GetICnum

        holder.test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    class JobViewHolder extends RecyclerView.ViewHolder {

        TextView jobTitle, jobCategory;
        // ImageView imageView;
        RelativeLayout test;

        public JobViewHolder(View itemView) {
            super(itemView);

            test=itemView.findViewById(R.id.testing);
            jobTitle = itemView.findViewById(R.id.jobTitle);
            jobCategory = itemView.findViewById(R.id.jobCat);
        }
    }
    public void setOnClick(OnItemClicked onClick)
    {
        this.onClick=onClick;
    }
}

