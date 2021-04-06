package com.blucorsys.app.labourcontractorapp.Contractor;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blucorsys.app.CustomComponent.Constants;
import com.blucorsys.app.CustomComponent.CustomLoader;
import com.blucorsys.app.ServerCall.Preferences;
import com.blucorsys.app.labourcontractorapp.Labour.LabourConsole;
import com.blucorsys.app.labourcontractorapp.MainActivity;
import com.blucorsys.app.labourcontractorapp.R;


public class ContractorConsole extends AppCompatActivity {
CardView cardpostjob,card_acceptjob,cardprofile,card_track,card_pay,card_summary,card_modify;
Preferences pref;
TextView tv_name;
CustomLoader loader;
ImageView iv_logout;
    public static int backPressed = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contractor_console);
        cardpostjob=(CardView)findViewById(R.id.cardpostjob);
        card_acceptjob=(CardView)findViewById(R.id.card_acceptjob);
        cardprofile=(CardView)findViewById(R.id.cardprofile);
        card_modify=(CardView)findViewById(R.id.card_modify);
        card_pay=(CardView)findViewById(R.id.card_pay);
        tv_name=(TextView)findViewById(R.id.tv_name);
        iv_logout=findViewById(R.id.btnlogout);
        card_track=findViewById(R.id.card_track);
        card_summary=findViewById(R.id.card_summary);


        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        pref=new Preferences(this);

        tv_name.setText(pref.get(Constants.FIRSTNAME )+ " " +pref.get(Constants.LASTNAME));
        cardprofile
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ContractorConsole.this, ContractorProfile.class));
            }
        });

        if(pref.get(Constants.STATUS).equals("0")){
            cardpostjob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopUp();
                }
            });
            card_acceptjob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopUp();
                }
            });
            card_track.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopUp();
                }
            });
            card_pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopUp();
                }
            });
            card_summary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopUp();
                }
            });
            card_modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopUp();
                }
            });
        }
        else if(pref.get(Constants.STATUS).equals("1")){

           if(pref.get(Constants.newjobstatus).equals(1)) {
                cardpostjob.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(), "You Are Not eligible for new job posting", Toast.LENGTH_LONG).show();
                        //startActivity(new Intent(ContractorConsole.this, PostJob.class));
                    }
                });
            }
           else {

               cardpostjob.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       //Toast.makeText(getApplicationContext(), "You Are Not eligible for new job posting", Toast.LENGTH_LONG).show();
                       startActivity(new Intent(ContractorConsole.this, PostJob.class));
                   }
               });
            }
            card_acceptjob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ContractorConsole.this, ContractorAcceptReject.class));
                }
            });
            card_track.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ContractorConsole.this, TrackingActivity.class));
                }
            });
            card_pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ContractorConsole.this, Pay.class));
                }
            });
            card_summary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ContractorConsole.this, SummaryContractor.class));
                }
            });
            card_modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ContractorConsole.this, ModifyContractorSide.class));
                }
            });
        }
        iv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(ContractorConsole.this, android.R.style.Theme_Translucent_NoTitleBar);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.alertyesno);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.CENTER;
                wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
                window.setAttributes(wlp);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                dialog.show();
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);


                //findId
                TextView tvYes = (TextView) dialog.findViewById(R.id.tvOk);
                TextView tvCancel = (TextView) dialog.findViewById(R.id.tvcancel);
                TextView tvReason = (TextView) dialog.findViewById(R.id.textView22);
                TextView tvAlertMsg = (TextView) dialog.findViewById(R.id.tvAlertMsg);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();

                //set listener
                tvYes.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(View v) {
                      pref.set(Constants.USERID,"");
                      pref.commit();
                        Log.e("",""+ Constants.USERID);
                        startActivity(new Intent(ContractorConsole.this, MainActivity.class));
                        finishAffinity();
                        dialog.dismiss();
                    }
                });
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    public void showPopUp(){
        AlertDialog alertDialog = new AlertDialog.Builder(ContractorConsole.this).create();
        alertDialog.setTitle("Please Update Profile");
        //alertDialog.setMessage(" Your Last Location Is "+mCurrentLocation);

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        backPressed = backPressed + 1;
        if (backPressed == 1) {
            Toast.makeText(ContractorConsole.this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            new CountDownTimer(5000, 1000) { // adjust the milli seconds here
                public void onTick(long millisUntilFinished) {
                }
                public void onFinish() { backPressed = 0;
                }
            }.start();
        }
        if (backPressed == 2) {
            backPressed = 0;
            finishAffinity();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}