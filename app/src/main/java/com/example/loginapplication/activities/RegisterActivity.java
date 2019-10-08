package com.example.loginapplication.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.loginapplication.R;
import com.example.loginapplication.database.DatabaseHelper;
import com.example.loginapplication.model.User;

public class RegisterActivity extends Activity {
    //Declaration EditTexts
    EditText editTextUserName, editTextEmail, editTextPassword;

    //Declaration TextInputLayout
    TextInputLayout textInputLayoutUserName, textInputLayoutEmail, textInputLayoutPassword;

    //Declaration Button
    Button buttonRegister;

    //Declaration DatabaseHelper
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        databaseHelper = new DatabaseHelper(this);
        initTextViewLogin();
        initViews();
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    String userName = editTextUserName.getText().toString();
                    String email = editTextEmail.getText().toString();
                    String password = editTextPassword.getText().toString();

                    if (!databaseHelper.isEmailExists(email)) {
                        databaseHelper.addUser(new User(null, userName, email, password));
                        Snackbar.make(buttonRegister, "User created successfully! Please Login ", Snackbar.LENGTH_LONG).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, Snackbar.LENGTH_LONG);
                    } else {
                        Snackbar.make(buttonRegister, "User already exists with same email ", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void initTextViewLogin() {
        TextView textViewLogin = findViewById(R.id.buttonLogin);
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextUserName = findViewById(R.id.editTextUserName);
        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);
        textInputLayoutUserName = findViewById(R.id.textInputLayoutUserName);
        buttonRegister = findViewById(R.id.buttonRegister);
    }

    public boolean validate() {
        boolean valid = false;

        //Get values from EditText fields
        String userName = editTextUserName.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        //Handling validation for userName field
        if (userName.isEmpty()) {
            valid = false;
            textInputLayoutUserName.setError("Please enter valid username!");
        } else {
            if (userName.length() > 5) {
                valid = true;
                textInputLayoutUserName.setError(null);
            } else {
                valid = false;
                textInputLayoutUserName.setError("Username is to short!");
            }
        }

        //Handling validation for Email field
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            valid = false;
            textInputLayoutEmail.setError("Please enter valid password!");
        } else {
            valid = true;
            textInputLayoutEmail.setError(null);
        }

        //Handling validation for password field
        if (password.isEmpty()) {
            valid = false;
            textInputLayoutPassword.setError("Please enter a valid password!");
        } else {
            if (password.length() > 5) {
                valid = true;
                textInputLayoutPassword.setError(null);
            } else {
                valid = false;
                textInputLayoutPassword.setError("The password is too short!");
            }
        }
        return valid;
    }
}