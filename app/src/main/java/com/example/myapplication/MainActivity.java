package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myapplication.database.UserDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 10 ;
    private EditText edtUsername,edtAddress;
    private Button btnAdd;
    private RecyclerView rcvUser;

    private UserAdapter userAdapter;
    private List<User> mlistUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUi();
        userAdapter = new UserAdapter(new UserAdapter.IClickItem() {
            @Override
            public void updateUser(User user) {
                clickUpdateUser(user);
            }

            @Override
            public void deleteUser(User user) {
                clickDeleteUser(user);
            }
        });
        mlistUsers = new ArrayList<>();
        userAdapter.setData(mlistUsers);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvUser.setLayoutManager(linearLayoutManager);
        rcvUser.setAdapter(userAdapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               addUser();
            }
        });
        loadData();
    }

    private void initUi(){

        rcvUser = findViewById(R.id.rcv_user);
        edtUsername = findViewById(R.id.edt_username);
        edtAddress = findViewById(R.id.edt_address);
        btnAdd = findViewById(R.id.btn_adduser);
    }

    public void addUser(){
        String strUserName = edtUsername.getText().toString().trim();
        String strAddress = edtAddress.getText().toString().trim();

        if(TextUtils.isEmpty(strUserName) || TextUtils.isEmpty(strAddress)){
            return;
        }
        User user = new User(strUserName,strAddress);

        if(isUserExist(user)){
            Toast.makeText(this,"Người dùng đã tồn tại",Toast.LENGTH_SHORT).show();
            return;
        }

        UserDatabase.getInstance(this).userDAO().insertUser(user);
        Toast.makeText(this,"Thêm thành công",Toast.LENGTH_SHORT).show();

        edtAddress.setText("");
        edtUsername.setText("");

        hideKeyboard();
        loadData();

    }

    public void hideKeyboard(){
        try{
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }

    private void loadData(){
        mlistUsers = UserDatabase.getInstance(this).userDAO().getListUser();
        userAdapter.setData(mlistUsers);
    }

    public boolean isUserExist (User user) {
        List<User> list = UserDatabase.getInstance(this).userDAO().checkUser(user.getName());
        return list != null && !list.isEmpty();
    }

    private void clickUpdateUser(User user){
        Intent intent = new Intent(MainActivity.this,UpdateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_user",user);
        intent.putExtras(bundle);
        startActivityForResult(intent,MY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MY_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            loadData();
        }
    }

    private void clickDeleteUser(final User user){
        new AlertDialog.Builder(this).setTitle("Xác nhận xóa người dùng").setMessage("Bạn có chắc muốn xóa?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Delete user
                UserDatabase.getInstance(MainActivity.this).userDAO().deleteUser(user);
                Toast.makeText(MainActivity.this,"Xóa thành công",Toast.LENGTH_SHORT).show();
                loadData();
            }
        }).setNegativeButton("No",null).show();

    }
}