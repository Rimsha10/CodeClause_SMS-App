package com.example.smsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainPageActivity extends AppCompatActivity {
    EditText phone,msg;
    ImageButton contact;
    Button sms;
    int count;
    private static final int RESULT_PICK_CONTACT =1;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        phone=findViewById(R.id.phone);
        msg=findViewById(R.id.editTextTextPersonName3);
        sms=findViewById(R.id.contactbtn);
        count=0;

            sms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        SmsManager smsManager = SmsManager.getDefault();
                        try {
                            smsManager.sendTextMessage(phone.getText().toString(), null, msg.getText().toString(), null, null);
                        Toast.makeText(MainPageActivity.this, "message sent successfully", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            System.out.println("message sending failed");
                        }
                        //to enable permission in app settings
                        /*Toast.makeText(MainPageActivity.this, "message cannot be sent without this permission being enabled", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + MainPageActivity.this.getPackageName()));
                        MainPageActivity.this.startActivity(intent);*/
                    }

            });


        contact=findViewById(R.id.imageButton2);
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainPageActivity.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    Intent in = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                    startActivityForResult(in, RESULT_PICK_CONTACT);
                } else {
                    ActivityCompat.requestPermissions(MainPageActivity.this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            0);
                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    System.out.println(data);
                    contactPicked(data);
                    break;
            }
        } else {
            Toast.makeText(this, "Failed To pick contact", Toast.LENGTH_SHORT).show();
        }
    }

    private void contactPicked(Intent data) {
        Cursor cursor = null;

        try {
            String phoneNo = null;
            Uri uri = data.getData();
            cursor = getContentResolver().query(uri, null, null,null,null);
            if (cursor != null  && cursor.getCount()>0) {
            cursor.moveToFirst();
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

            phoneNo = cursor.getString(phoneIndex);
            System.out.println(phoneNo);
            phone.setText(phoneNo);}


        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}