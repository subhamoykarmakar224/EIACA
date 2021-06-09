package com.example.collectdata;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.collectdata.sharedpref.SharedPreferenceControl;

public class LoginActivity extends AppCompatActivity {

    EditText editTextUsername;
    SharedPreferenceControl spControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        editTextUsername = findViewById(R.id.editTextUsername);

        spControl = new SharedPreferenceControl(this);

        if(spControl.getData(Constants.SP_KEY_MY_UNAME).length() > 0) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    public void checkUserName(View view) {
        if(editTextUsername.getText().toString().length() > 0) {
            spControl.setData(Constants.SP_KEY_MY_UNAME, editTextUsername.getText().toString());
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Alert")
                    .setMessage("Hi There! Please tell me your name to continue.")
                    .setPositiveButton(android.R.string.ok, null)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();
        }
    }
}