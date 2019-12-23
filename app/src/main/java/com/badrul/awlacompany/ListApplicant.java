package com.badrul.awlacompany;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListApplicant extends AppCompatActivity implements ListApplicantAdapter.OnItemClicked{

    String companyID;
    String jobID,jobStatus;
    ImageView imgGone,imgJobOff;
    TextView txtGone,txtJobOff;
    List<JobApplicant> applicantList;
    ImageButton logout,activeOn,activeOff;
    Button closejob;

    //the recyclerview
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_applicant);
        recyclerView = findViewById(R.id.recylcerView);
        imgGone = findViewById(R.id.imageViewGone);
        txtGone = findViewById(R.id.textViewGone);
        closejob = findViewById(R.id.closejob);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        companyID = sharedPreferences.getString(Config.C_COMPANY_ID, "Not Available");
        jobID = sharedPreferences.getString(Config.J_JOB_ID, "Not Available");
        jobStatus = sharedPreferences.getString(Config.J_JOB_STATUS, "Not Available");
        
        
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        //initializing the joblist
        
        applicantList = new ArrayList<>();

        loadApplicant();


        if ("CLOSE".equalsIgnoreCase(jobStatus)){
            
         closejob.setVisibility(View.GONE);   
        }
        
        
        closejob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ListApplicant.this);
                alertDialogBuilder.setTitle("Do you want close the job application?");
                alertDialogBuilder.setMessage("This action cannot be retracted");

                final Dialog dialog = new Dialog(ListApplicant.this);

                alertDialogBuilder.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                dialog.setCanceledOnTouchOutside(true);

                                final ProgressDialog loading = ProgressDialog.show(ListApplicant.this,"Please Wait","Contacting Server",false,false);

                                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                                        Config.URL_API+"closejob.php", new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        loading.dismiss();

                                        if(response.contains("Success")){

                                            Toast.makeText(ListApplicant.this, "Close success. Thank you", Toast.LENGTH_LONG)
                                                    .show();

                                            Intent intent = new Intent(ListApplicant.this, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();



                                        }
                                        else  {

                                            Toast.makeText(ListApplicant.this, "Error. Please try again", Toast.LENGTH_LONG)
                                                    .show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        loading.dismiss();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Toast.makeText(ListApplicant.this,"No internet . Please check your connection",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                        else{

                                            Toast.makeText(ListApplicant.this, error.toString(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("jobID", jobID);

                                        return params;
                                    }

                                };

                                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                        30000,
                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                requestQueue.add(stringRequest);

                            }

                        });

                alertDialogBuilder.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                dialog.setCanceledOnTouchOutside(true);

                            }
                        });
                alertDialogBuilder.setOnCancelListener(
                        new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                            }
                        }
                );

                //Showing the alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                
            }
        });


    }

    public void loadApplicant() {
        final ProgressDialog loading = ProgressDialog.show(this, "Please Wait", "Contacting Server", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.URL_API + "loadapplicant.php?companyID="+companyID+"&jobID="+jobID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting job object from json array
                                JSONObject applicant = array.getJSONObject(i);

                                //adding the job to job list
                                applicantList.add(new JobApplicant(
                                        applicant.getString("userID"),
                                        applicant.getString("userName"),
                                        applicant.getString("userEmail"),
                                        applicant.getString("userPhone"),
                                        applicant.getString("userAge"),
                                        applicant.getString("userWorkExp"),
                                        applicant.getString("userToken"),
                                        applicant.getString("iv_url"),
                                        applicant.getString("applyStatus")
                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            ListApplicantAdapter adapter = new ListApplicantAdapter(getApplicationContext(), applicantList);
                            recyclerView.setAdapter(adapter);
                            adapter.setOnClick(ListApplicant.this);

                            if (adapter.getItemCount() == 0) {
                                imgGone.setVisibility(View.VISIBLE);
                                txtGone.setVisibility(View.VISIBLE);
                            } else {

                                imgGone.setVisibility(View.GONE);
                                txtGone.setVisibility(View.GONE);
                            }

                            //add shared preference ID,nama,credit here
                            loading.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(ListApplicant.this, "No internet . Please check your connection",
                                    Toast.LENGTH_LONG).show();
                        } else {

                            Toast.makeText(ListApplicant.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //adding our stringrequest to queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    @Override
    public void onItemClick(int position) {
        // The onClick implementation of the RecyclerView item click
        //ur intent code here
        JobApplicant jobapplicant = applicantList.get(position);
        //Toast.makeText(FoodMenu.this, job.getLongdesc(),
        //      Toast.LENGTH_LONG).show();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME,
                Context.MODE_PRIVATE);

        // Creating editor to store values to shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Adding values to editor

        editor.putString(Config.U_USER_ID, jobapplicant.getUserID());
        editor.putString(Config.U_USER_NAME, jobapplicant.getUserName());
        editor.putString(Config.U_USER_EMAIL, jobapplicant.getUserEmail());
        editor.putString(Config.U_USER_PHONE, jobapplicant.getUserPhone());
        editor.putString(Config.U_USER_AGE, jobapplicant.getUserAge());
        editor.putString(Config.U_USER_WORKEXP, jobapplicant.getUserWorkExp());
        editor.putString(Config.U_USER_TOKEN, jobapplicant.getUserToken());
        editor.putString(Config.U_IV_URL, jobapplicant.getIv_url());
        editor.putString(Config.U_APPLY_STATUS, jobapplicant.getApplyStatus());


        // Saving values to editor
        editor.commit();

        Intent i = new Intent(ListApplicant.this, ApplicantDetails.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        //finish();
    }
}
