package com.blucorsys.app.labourcontractorapp.Labour;

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
import com.blucorsys.app.ServerCall.Preferences;
import com.blucorsys.app.labourcontractorapp.Contractor.ContractorConsole;
import com.blucorsys.app.labourcontractorapp.MainActivity;
import com.blucorsys.app.labourcontractorapp.R;


public class LabourConsole extends AppCompatActivity {
CardView cardprofile,cardapply,card_acceptjob,card_requestpay,card_summary,card_modify;
TextView tv_name;
Preferences pref;
    ImageView iv_logout;
    public static int backPressed = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labour_console);
        cardprofile=findViewById(R.id.cardprofile);
        cardapply=findViewById(R.id.cardapply);
        iv_logout=findViewById(R.id.btnlogout);
        card_modify=findViewById(R.id.card_modify);
        card_summary=findViewById(R.id.card_summary);
        tv_name=findViewById(R.id.tv_name);
        card_acceptjob=findViewById(R.id.card_acceptjob);
        card_requestpay=findViewById(R.id.card_requestpay);
        pref=new Preferences(this);
        tv_name.setText(pref.get(Constants.FIRSTNAME));
        cardprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LabourConsole.this, LabourProfile.class));
            }
        });



        if(pref.get(Constants.STATUS).equals("0")) {
            cardapply.setOnClickListener(new View.OnClickListener() {
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
            card_requestpay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopUp();
                }
            });
            card_summary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopUp();
                }
            });
            card_modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopUp();
                }
            });
        }

        else {
            cardapply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(LabourConsole.this, JobApplication.class));
                }
            });

            card_acceptjob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(LabourConsole.this, JobStatus.class));
                }
            });
            card_requestpay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(LabourConsole.this, PaymentRequest.class));
                }
            });
            card_summary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(LabourConsole.this, SummaryLabour.class));
                }
            });
            card_modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(LabourConsole.this, ModifyLabourSide.class));
                }
            });
        }

        iv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(LabourConsole.this, android.R.style.Theme_Translucent_NoTitleBar);
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

                //set value
                //tvAlertMsg.setText("CONFIRMATION ALERT..!!!");
                // tvReason.setText("ARE YOU SURE WANT TO LOGOUT?");
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
                        startActivity(new Intent(LabourConsole.this, MainActivity.class));
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
        AlertDialog alertDialog = new AlertDialog.Builder(LabourConsole.this).create();
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
            Toast.makeText(LabourConsole.this, "Press back again to exit", Toast.LENGTH_SHORT).show();
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