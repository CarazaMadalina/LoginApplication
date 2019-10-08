package com.example.loginapplication.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.loginapplication.R;
import com.example.loginapplication.database.DatabaseHelper;
import com.example.loginapplication.model.User;
import com.example.newloghinapplication.helpers.InputValidation;

public class LoginActivity extends AppCompatActivity {
    private final AppCompatActivity activity = LoginActivity.this;
    private ScrollView scrollView;

    //Declaration EditTexts
    private EditText editTextEmail, editTextPassword;

    //Declaration TextInputLayout
    private TextInputLayout textInputLayoutEmail, textInputLayoutPassword;

    //Declaration button
    private Button loginButton, registerButton;

    //Declaration Database Helper
    private DatabaseHelper databaseHelper;

    //Declaration Input Validation
    private InputValidation inputValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        initCreateAccountTextView();
        initViews();

        // set click event of login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {

                    //Get the values from EditText fields
                    String email = editTextEmail.getText().toString();
                    String password = editTextPassword.getText().toString();

                    //Authenticate user
                    User currentUser = databaseHelper.Authenticate(new User(null, null, email, password));

                    //Check Authentication is successful or not
                    if (currentUser != null) {
                        Snackbar.make(loginButton, "Successfully Logged in!", Snackbar.LENGTH_LONG).show();
                    } else {
                        Snackbar.make(loginButton, "Failed to Logged in! Please try again!", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void initCreateAccountTextView() {
        TextView textViewCreateAccount = findViewById(R.id.textViewCreateAccount);
        textViewCreateAccount.setText(fromHtml("<font color='#ffffff'>I don't have account yet. </font><font color='#0c0099'>create one</font>"));
        textViewCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    private void initViews() {
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        loginButton = (Button) findViewById(R.id.buttonLogin);
    }

    public boolean validate() {
        boolean valid = false;

        //Get values from EditText fields
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();


        //Handling validation for Email fields
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            valid = false;
            textInputLayoutEmail.setError("Please enter valid email!");
        } else {
            valid = true;
            editTextEmail.setError(null);
        }

        //Handling validation for Password field

        if (password.isEmpty()) {
            valid = false;
            textInputLayoutPassword.setError("Please enter a valid password!");
        } else {
            if (password.length() > 5) {
                valid = true;
                textInputLayoutPassword.setError(null);
            } else {
                valid = false;
                textInputLayoutPassword.setError("Password is to short!");
            }
        }
        return valid;
    }
}
