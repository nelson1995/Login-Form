package com.theedge.nelsongkatale.login_form;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN=Pattern.compile("^"+"(?=.*[0-9])"+
            "(?=.*[a-z])"+
            "(?=.*[A-Z])"+
            "(?=.*[@#$%^&+=!])"+
            "(?=\\S+$)"+
            ".{8,}"+"$");
    Toolbar mToolbar;
    TextInputLayout firstname;
    TextInputLayout lastname;
    TextInputLayout password;
    TextInputLayout email_address;
    Button registrationbtn;
    SqliteDbHelper db;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mToolbar=(Toolbar)findViewById(R.id.back_navigation);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        firstname=(TextInputLayout)findViewById(R.id.fname);
        lastname=(TextInputLayout)findViewById(R.id.lname);
        email_address=(TextInputLayout)findViewById(R.id.email);
        password=(TextInputLayout)findViewById(R.id.passwd);
        registrationbtn=(Button)findViewById(R.id.regbtn);
        /**
         * Takes care of closing the registration activity on clickin the back button.
         *
         */
        mToolbar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        registrationbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                registerDetails(v);

            }
        });
    }

    public boolean registerDetails(View v){
        hideVirtualKeyboard();
        db=new SqliteDbHelper(this);
        String fname=firstname.getEditText().getText().toString();
        String lname=lastname.getEditText().getText().toString();
        String email=email_address.getEditText().getText().toString().trim();
        String passwrd=password.getEditText().getText().toString().trim();

        if(fname.isEmpty() || lname.isEmpty()){
            //firstname.setError("Fields cannot be empty");
            Toast.makeText(getApplicationContext(),"Fields cannot be empty",Toast.LENGTH_SHORT).show();
            return false;
        }else if(!(validateEmail(email))){

            Toast.makeText(getApplicationContext(),"Invalid email",Toast.LENGTH_SHORT).show();
            return false;
        }else if(!(validatePassword(passwrd))) {

            Toast.makeText(getApplicationContext(),"password too weak",Toast.LENGTH_SHORT).show();
            return false;
        }else{

            boolean insertDetails=db.insertData(fname,lname,email,passwrd);

            if(insertDetails==true){
                Toast.makeText(getApplicationContext()," registration successful ",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext()," registration failed ",Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    }

    /**
     * Hide's the virtual keyboard whenever user clicks login button.
     */
    public void hideVirtualKeyboard(){

        View view=getCurrentFocus();
        if(view!=null){
            ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     *
     * @return true if email address is valid
     */
    public boolean validateEmail(String email){

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    /**
     *
     * @return true if password is strong
     */
    public boolean validatePassword(String passwd){

        return PASSWORD_PATTERN.matcher(passwd).matches();
    }

}



