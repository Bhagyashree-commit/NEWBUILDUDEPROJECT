package com.blucorsys.app.labourcontractorapp.Contractor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blucorsys.app.labourcontractorapp.R;

public class ModifyContractorSide extends AppCompatActivity {
TextView tv_modifyjob,tv_modifiedjob;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_contractor_side);

        tv_modifyjob=findViewById(R.id.tv_modifyjob);
        tv_modifiedjob=findViewById(R.id.tv_modifiedjob);

        replaceFragmentWithAnimation(new ModifyFragment());

        tv_modifyjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragmentWithAnimation(new ModifyFragment());
            }
        });
        tv_modifiedjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragmentWithAnimation(new ModifiedFragment());
            }
        });

    }

    public void replaceFragmentWithAnimation(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.frame_modify, fragment);
        FragmentManager mFragmentManager=getSupportFragmentManager();
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        // transaction.addToBackStack(null);
        transaction.commit();
    }
}