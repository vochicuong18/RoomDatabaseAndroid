package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.database.UserDatabase;

public class UpdateActivity extends AppCompatActivity {

    private EditText edtUsername,edtAddress;
    private Button btnUpdate;

    private User muser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        edtUsername = findViewById(R.id.edt_username);
        edtAddress = findViewById(R.id.edt_address);
        btnUpdate = findViewById(R.id.btn_update);

        muser = (User) getIntent().getExtras().get("object_user");
        if(muser != null){
            edtUsername.setText(muser.getName());
            edtAddress.setText(muser.getAddRess());
        }
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser();
            }
        });
    }

    private void updateUser() {
        String strUserName = edtUsername.getText().toString().trim();
        String strAddress = edtAddress.getText().toString().trim();

        if(TextUtils.isEmpty(strUserName) || TextUtils.isEmpty(strAddress)){
            return;
        }
        //Update user
        muser.setName(strUserName);
        muser.setAddRess(strAddress);

        UserDatabase.getInstance(this).userDAO().udpateUser(muser);
        Toast.makeText(this,"Update thành công",Toast.LENGTH_SHORT).show();

        Intent intentResult = new Intent();
        setResult(Activity.RESULT_OK,intentResult);
        finish();
    }
}