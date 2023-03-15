package com.example.intentsapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvFullUserName;
    private SharedPreferences sPref;
    public static String MY_PREF="GETLOGINPASSAPP_PREFERENCE_FILE";
    public final static String FULLUSERNAME="fullusername";
    private int code=0;
    private Toast toast;

    private Button buttonSendInfoToContact;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvFullUserName=findViewById(R.id.tvViewFullName);
        buttonSendInfoToContact=findViewById(R.id.buttonSendInfoToContact);

        String fullUserName;
        if((fullUserName=loadUserFullNameFromMyPref())==null){
            getUserFullNameFromLoginActivity();
        }else {
            tvFullUserName.setText("Hi "+fullUserName);

            buttonSendInfoToContact.setOnClickListener(this);

        }
    }


    @Override
    public void onClick(View view) {
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"I have get up at 5:40!");
        intent.putExtra(Intent.EXTRA_SUBJECT,"Early report");

        Intent intentChooser=Intent.createChooser(intent,"SendReport");
        startActivity(intentChooser);

    }

    private String loadUserFullNameFromMyPref(){
        sPref=getApplicationContext().getSharedPreferences(MY_PREF, MODE_PRIVATE);
        String fullUserName=sPref.getString(FULLUSERNAME, "");
        if (fullUserName.isEmpty()){
            return null;
        } else {
            return fullUserName;
        }
    }

    private void getUserFullNameFromLoginActivity(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);

        startActivityForResult(intent, code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==code){
            if (resultCode==RESULT_OK){
                String fullUserName=data.getStringExtra(FULLUSERNAME);
                saveFullUserNameImMuPref(fullUserName);
                tvFullUserName.setText("Hi "+ fullUserName);
            } else {
                int duration= Toast.LENGTH_SHORT;
                if(toast!=null){
                    toast.cancel();
                }
                toast=Toast.makeText(this,"This User not found", duration);
                toast.show();
                getUserFullNameFromLoginActivity();
            }
        }
    }

    private void saveFullUserNameImMuPref(String data){
        sPref=getApplicationContext().getSharedPreferences(MY_PREF,MODE_PRIVATE);
        SharedPreferences.Editor ed=sPref.edit();
        ed.putString(FULLUSERNAME,data);
        ed.commit();
    }


}

