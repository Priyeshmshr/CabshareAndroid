package com.example.cabshare.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cabshare.LoginActivity;
import com.example.cabshare.R;

/**
 * Created by Priyesh Mishra on 01-01-2016.
 */
public class LoginActivityTest extends ActivityInstrumentationTestCase2{

    private LoginActivity loginActivity;
    private Button LogIn;
    private TextView SignUp;
    private EditText username, password;

    public LoginActivityTest() {
        super(LoginActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        loginActivity = (LoginActivity) getActivity();
        LogIn = (Button) loginActivity.findViewById(R.id.bLogIn);
        SignUp = (TextView) loginActivity.findViewById(R.id.tvSignUp);
        username = (EditText) loginActivity.findViewById(R.id.etLoginUsername);
        password = (EditText) loginActivity.findViewById(R.id.etLoginPassword);
        username.setText("priyesh");
        password.setText("priyesh");
    }
    public void testPreconditions() {
        assertNotNull("loginActivity is null", loginActivity);
        assertNotNull("LogIn button is null", LogIn);
        assertNotNull("username is null", username);
        assertNotNull("password is null", password);
    }
    public void testLogIn_button(){
        loginActivity.onClick(LogIn);
    }
}
