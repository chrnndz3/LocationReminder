package com.example.twiganator.merged_androidapp;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.telephony.SmsManager;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.Button;
//import android.util.Log;
//
///**
// * Created by vaish on 4/25/2017.
// */
//
//public class SendMessageActivity extends Activity {
//
//    EditText message;
//    EditText number;
//    Button sendMessage;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sendmessage);
//
//        message = (EditText) findViewById(R.id.text);
//        number = (EditText) findViewById(R.id.number);
//        sendMessage = (Button) findViewById(R.id.send);
//
//        sendMessage.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View arg0){
//                String contactNumber = number.getText().toString();
//                String body = message.getText().toString();
//
//                sending(contactNumber, body);
//            }
//        });
//    }
//
//    public void sending(String contactNumber, String body){
//        try{
//            SmsManager smsManager = SmsManager.getDefault();
//            smsManager.sendTextMessage(contactNumber, null ,body, null,null);
//            Log.d("SENT","SENT");
//        }
//        catch(Exception e){
//            Log.d("FAILED", "FAILED");
//        }
//    }
//}
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Activity;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;

import android.util.Log;
import android.view.Menu;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendMessageActivity extends Activity {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    Button sendBtn;
    EditText txtphoneNo;
    EditText txtMessage;
    String phoneNo;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendmessage);

        sendBtn = (Button) findViewById(R.id.btnSendSMS);
        txtphoneNo = (EditText) findViewById(R.id.editText);
        txtMessage = (EditText) findViewById(R.id.editText2);

        sendBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                phoneNo = txtphoneNo.getText().toString();
                message = txtMessage.getText().toString();
                sendSMSMessage();
            }
        });
    }

    protected void sendSMSMessage() {
        Log.d("INSIDE SENDMESSAGEFUNC","OK HERE");
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }
}
