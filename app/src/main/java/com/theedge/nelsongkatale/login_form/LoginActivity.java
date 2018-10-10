package com.theedge.nelsongkatale.login_form;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaCodec;
import android.os.PatternMatcher;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN=Pattern.compile("^"+"(?=.*[0-9])"+
            "(?=.*[a-z])"+
            "(?=.*[A-Z])"+
            "(?=.*[@#$%^&+=!])"+
            "(?=\\S+$)"+
            ".{8,}"+"$");
    private TextInputLayout email_address;
    private TextInputLayout password;
    TextView signup;
    Button login;
    RegistrationActivity registrationActivity;
    SqliteDbHelper sqliteDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sqliteDbHelper=new SqliteDbHelper(this);
        email_address=(TextInputLayout)findViewById(R.id.email);
        password=(TextInputLayout)findViewById(R.id.passwd);
        login=(Button)findViewById(R.id.loginbtn);
        signup=(TextView)findViewById(R.id.signUp);
        registrationActivity=new RegistrationActivity();
        email_address.setHint("email address");
        password.setHint("password");
        loadRegisterActivity();


    }

    public void loadRegisterActivity(){

        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(intent);
            }
        });
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

    public boolean loginBtn(View v){
        hideVirtualKeyboard();

        String emailAddress=email_address.getEditText().getText().toString().trim();
        String passwd=password.getEditText().getText().toString().trim();

        if(emailAddress.isEmpty()){
            //email_address.setError("Field cannot be empty");
            Toast.makeText(getApplicationContext(),R.string.error_message,Toast.LENGTH_SHORT).show();


        }else if(passwd.isEmpty()){

            //password.setError("Field cannot be empty");
            Toast.makeText(getApplicationContext(),R.string.error_message,Toast.LENGTH_SHORT).show();


        }else if(!(validateEmail(emailAddress))){

            //email_address.setError("Invalid email address");
            Toast.makeText(getApplicationContext(),R.string.error_message2,Toast.LENGTH_SHORT).show();

        }else if(!(validatePassword(passwd))){

            //password.setError("password too weak");
            Toast.makeText(getApplicationContext(),R.string.error_message3,Toast.LENGTH_SHORT).show();

        }else{
            email_address.setErrorEnabled(false);
            password.setErrorEnabled(false);
            Cursor cursor=sqliteDbHelper.checkAccount(emailAddress,passwd);

            if(cursor.getCount()>0){
                Toast.makeText(getApplicationContext(),R.string.login_message,Toast.LENGTH_SHORT).show();
                cursor.close();
                return true;
            }else{
                Toast.makeText(getApplicationContext(),R.string.error_message4,Toast.LENGTH_SHORT).show();
                cursor.close();
                return false;

            }
        }
        return true;
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

}
