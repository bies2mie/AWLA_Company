package com.badrul.awlacompany;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddJob extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner sp;
    List<String> list;
    ArrayAdapter<String> adp;
    String locat = "";
    Button addJob;
    EditText getJobPos,getJobDetail,getOpenDate,getCloseDate;
    String openDate = "",closeDate = "";
    DatePickerDialog picker;
    String companyID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        companyID = sharedPreferences.getString(Config.C_COMPANY_ID, "Not Available");

        sp = findViewById(R.id.spinner);
        getJobPos = findViewById(R.id.jobPos);
        getJobDetail = findViewById(R.id.jobDetail);
        getOpenDate = findViewById(R.id.chooseOpenDate);
        getCloseDate = findViewById(R.id.chooseCloseDate);
        addJob = findViewById(R.id.addJobBtn);


        sp.setOnItemSelectedListener(this);
        list = new ArrayList<>();

        list.add("IT");
        list.add("Business");
        list.add("Manufacturing");

        adp = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, list);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adp);

        getOpenDate.setInputType(InputType.TYPE_NULL);
        getOpenDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AddJob.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                               openDate = (dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                               getOpenDate.setText(openDate);

                            }
                        }, year, month, day);
                picker.show();

            }
        });

        getCloseDate.setInputType(InputType.TYPE_NULL);
        getCloseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AddJob.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                closeDate = (dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                getCloseDate.setText(closeDate);

                            }
                        }, year, month, day);
                picker.show();

            }
        });

        addJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String jp = getJobPos.getText().toString().trim();
                final String jd = getJobDetail.getText().toString().trim();


                if (jp.length() < 5) {
                    Toast.makeText(getApplicationContext(), "Please enter minimum 5 characters for Job Position",
                            Toast.LENGTH_LONG).show();
                } else if (jd.length() < 20) {
                    Toast.makeText(getApplicationContext(), "Please enter minimum 20 characters for Job Details",
                            Toast.LENGTH_LONG).show();
                } else if ("".equalsIgnoreCase(openDate)) {
                    Toast.makeText(getApplicationContext(),
                            "Please select job open date", Toast.LENGTH_LONG).show();
                } else if ("".equalsIgnoreCase(closeDate)) {
                    Toast.makeText(getApplicationContext(),
                            "Please select job close date", Toast.LENGTH_LONG).show();
                } else if ("".equalsIgnoreCase(locat)) {
                    Toast.makeText(getApplicationContext(),
                            "Please select job category", Toast.LENGTH_LONG).show();

                } else {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddJob.this);
                    alertDialogBuilder.setTitle("Do you confirm?");
                    alertDialogBuilder.setMessage("Job posting cannot be change after submmission");

                    final Dialog dialog = new Dialog(AddJob.this);

                    alertDialogBuilder.setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    dialog.setCanceledOnTouchOutside(true);

                                    final ProgressDialog loading = ProgressDialog.show(AddJob.this,"Please Wait","Contacting Server",false,false);

                                    StringRequest stringRequest = new StringRequest(Request.Method.POST,
                                            Config.URL_API+"addjob.php", new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                            loading.dismiss();

                                            if(response.contains("Success")){

                                                Toast.makeText(AddJob.this, "Apply success. Thank you", Toast.LENGTH_LONG)
                                                        .show();

                                                Intent intent = new Intent(AddJob.this, MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                finish();



                                            }
                                            else {

                                                Toast.makeText(AddJob.this, "Error. Please try again", Toast.LENGTH_LONG)
                                                        .show();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            loading.dismiss();
                                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                                Toast.makeText(AddJob.this,"No internet . Please check your connection",
                                                        Toast.LENGTH_LONG).show();
                                            }
                                            else{

                                                Toast.makeText(AddJob.this, error.toString(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }) {
                                        @Override
                                        protected Map<String, String> getParams() {
                                            Map<String, String> params = new HashMap<String, String>();
                                            params.put("jobPosition", jp);
                                            params.put("jobDetails", jd);
                                            params.put("jobOpenDate", openDate);
                                            params.put("jobCloseDate", closeDate);
                                            params.put("jobCategory", locat);
                                            params.put("companyID", companyID);
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
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        locat = parent.getSelectedItem().toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + locat, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        Toast.makeText(arg0.getContext(), "Please Select Your Category", Toast.LENGTH_LONG).show();

    }
}
