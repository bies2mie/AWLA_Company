package com.badrul.awlacompany;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ApplicantDetails extends AppCompatActivity {

    String userID;
    String userName;
    String userEmail;
    String userPhone;
    String userAge;
    String userWorkExp;
    String userToken;
    String iv_url;
    String applyStatus;
    TextView appName,appEmail,appPhone,appAge,appWorkExp,appApplyStatus;
    //private String path="https://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4";
    private VideoView show_iv;
    Button calltoIV;
    String jobID;

    // Current playback position (in milliseconds).
    private int mCurrentPosition = 0;

    // Tag for the instance state bundle.
    private static final String PLAYBACK_TIME = "play_time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_details);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        jobID = sharedPreferences.getString(Config.J_JOB_ID, "Not Available");
        userID = sharedPreferences.getString(Config.U_USER_ID, "Not Available");
        userName= sharedPreferences.getString(Config.U_USER_NAME, "Not Available");
        userEmail= sharedPreferences.getString(Config.U_USER_EMAIL, "Not Available");
        userPhone= sharedPreferences.getString(Config.U_USER_PHONE, "Not Available");
        userAge= sharedPreferences.getString(Config.U_USER_AGE, "Not Available");
        userWorkExp= sharedPreferences.getString(Config.U_USER_WORKEXP, "Not Available");
        userToken= sharedPreferences.getString(Config.U_USER_TOKEN, "Not Available");
        iv_url= sharedPreferences.getString(Config.U_IV_URL, "Not Available");
        applyStatus= sharedPreferences.getString(Config.U_APPLY_STATUS, "Not Available");


        appName = findViewById(R.id.applNametxt);
        appEmail = findViewById(R.id.appEmailtxt);
        appPhone = findViewById(R.id.phoneNumtxt);
        appAge = findViewById(R.id.agetxt);
        appWorkExp = findViewById(R.id.applworkexptxt);
        appApplyStatus = findViewById(R.id.applystatus_a);
        show_iv = findViewById(R.id.applivideoView);
        calltoIV = findViewById(R.id.approveiv);

        appName.setText(userName);
        appEmail.setText(userEmail);
        appPhone.setText(userPhone);
        appAge.setText(userAge+" years old");
        appWorkExp.setText(userWorkExp+ "years");
        appApplyStatus.setText(applyStatus);

        if (applyStatus.equalsIgnoreCase("PROCESSING")){

            show_iv.setVisibility(View.GONE);
            calltoIV.setVisibility(View.VISIBLE);

        }else if(applyStatus.equalsIgnoreCase("INTERVIEW")){

            show_iv.setVisibility(View.GONE);
            calltoIV.setVisibility(View.GONE);

        }else if(applyStatus.equalsIgnoreCase("FINISH")){

            show_iv.setVisibility(View.VISIBLE);
            calltoIV.setVisibility(View.GONE);
            initializePlayer();


        }
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(PLAYBACK_TIME);
        }

        // Set up the media controller widget and attach it to the video view.
        MediaController controller = new MediaController(this);
        controller.setMediaPlayer(show_iv);
        show_iv.setMediaController(controller);

        calltoIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ApplicantDetails.this);
                alertDialogBuilder.setTitle("Do you want to interview this person?");
                alertDialogBuilder.setMessage("This action cannot be retracted");

                final Dialog dialog = new Dialog(ApplicantDetails.this);

                alertDialogBuilder.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                dialog.setCanceledOnTouchOutside(true);

                                final ProgressDialog loading = ProgressDialog.show(ApplicantDetails.this,"Please Wait","Contacting Server",false,false);

                                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                                        Config.URL_API+"callinterview.php", new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        loading.dismiss();

                                        if(response.contains("Success")){

                                            Toast.makeText(ApplicantDetails.this, "Call success. Thank you", Toast.LENGTH_LONG)
                                                    .show();

                                            Intent intent = new Intent(ApplicantDetails.this, ListApplicant.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();



                                        }
                                        else  {

                                            Toast.makeText(ApplicantDetails.this, "Error. Please try again", Toast.LENGTH_LONG)
                                                    .show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        loading.dismiss();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                            Toast.makeText(ApplicantDetails.this,"No internet . Please check your connection",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                        else{

                                            Toast.makeText(ApplicantDetails.this, error.toString(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("jobID", jobID);
                                        params.put("userID", userID);

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
    }@Override
    protected void onStart() {
        super.onStart();

        // Load the media each time onStart() is called.  
        //initializePlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // In Android versions less than N (7.0, API 24), onPause() is the  
        // end of the visual lifecycle of the app.  Pausing the video here  
        // prevents the sound from continuing to play even after the app  
        // disappears.  
        //  
        // This is not a problem for more recent versions of Android because  
        // onStop() is now the end of the visual lifecycle, and that is where  
        // most of the app teardown should take place.  
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            show_iv.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Media playback takes a lot of resources, so everything should be  
        // stopped and released at this time.  
        releasePlayer();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current playback position (in milliseconds) to the  
        // instance state bundle.  
        outState.putInt(PLAYBACK_TIME, show_iv.getCurrentPosition());
    }

    private void initializePlayer() {
        // Show the "Buffering..." message while the video loads.
        final ProgressDialog loading = ProgressDialog.show(ApplicantDetails.this,"Please Wait","Downloading Interview Video",false,false);

        // Buffer and decode the video sample.  
        Uri uri = Uri.parse(iv_url);
        show_iv.setVideoURI(uri);

        // Listener for onPrepared() event (runs after the media is prepared).  
        show_iv.setOnPreparedListener(
                new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {

                        // Hide buffering message.

                        loading.dismiss();

                        // Restore saved position, if available.  
                        if (mCurrentPosition > 0) {
                            show_iv.seekTo(mCurrentPosition);
                        } else {
                            // Skipping to 1 shows the first frame of the video.  
                            show_iv.seekTo(1);
                        }

                        // Start playing!  
                        show_iv.start();
                    }
                });

        // Listener for onCompletion() event (runs after media has finished  
        // playing).  
        show_iv.setOnCompletionListener(
                new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        Toast.makeText(ApplicantDetails.this,
                                "Ended",
                                Toast.LENGTH_SHORT).show();

                        // Return the video position to the start.  
                        show_iv.seekTo(0);
                    }
                });
    }


    // Release all media-related resources. In a more complicated app this  
    // might involve unregistering listeners or releasing audio focus.  
    private void releasePlayer() {
        show_iv.stopPlayback();
    }
}
