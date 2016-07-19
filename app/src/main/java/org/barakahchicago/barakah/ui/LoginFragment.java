package org.barakahchicago.barakah.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.barakahchicago.barakah.R;
import org.json.JSONException;
import org.json.JSONObject;


public final class LoginFragment extends Fragment implements
        View.OnClickListener {

    /*
        Tag used for fragment transaction
     */
    public static final String TAG = "login";

    /*
        Url used to connect to webservice
     */
    private static final String URL = "http://www.barakahchicago.org/bcwebservice/signin.php";

    /*
        Tag used for logging
     */
    private static final String LOG_TAG = "LOGIN FRAGMENT";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private Button signup;
    private EditText email;
    private EditText password;
    private Button signIn;
    private TextView forgotPassword;
    private TextInputLayout inputLayoutEmail;
    private TextInputLayout inputLayoutPassword;
    private OnLoginListener listener;
    private boolean status = false;
    private SharedPreferences sharedPref;
    private ProgressDialog progressDialog;

    public LoginFragment() {


    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        //setting option menu on so we can clear it on onPrepareOption menu.
        setHasOptionsMenu(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);


        email = (EditText) view.findViewById(R.id.login_email_txt);
        password = (EditText) view.findViewById(R.id.login_password_txt);
        signIn = (Button) view.findViewById(R.id.login_sign_in_btn);
        signup = (Button) view.findViewById(R.id.login_signup_btn);
        forgotPassword = (TextView) view.findViewById(R.id.login_forgot_password_txt);

        email.addTextChangedListener(new BarakahEditBoxListener(email));
        password.addTextChangedListener(new BarakahEditBoxListener(password));

        inputLayoutEmail = (TextInputLayout) view.findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) view.findViewById(R.id.input_layout_password);

        //  email.setText("new@yahoo.com");
        //   password.setText("hhdhhdhhi");

        signIn.setOnClickListener(this);
        signup.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);


        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle(getResources().getString(R.string.message_singing_in));
        progressDialog.setMessage(getResources().getString(R.string.message_please_wait));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.hide();

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(final Menu menu) {

        super.onPrepareOptionsMenu(menu);

        menu.clear();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (OnLoginListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    /*
         Click listener
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_sign_in_btn) {
            if (isValidEmail(email.getText()) && isValidPassword(password.getText())) {

                boolean result = signIn(email.getText().toString().trim(), password.getText().toString().trim());

                if (result) {
                    //load main activity
                    Log.i("", "Login Success");


                } else {
                    //stay here
                    Log.i("", "Login Failed");
                }
            } else {
                //Toast.makeText(getContext(),"Invalid email or password",Toast.LENGTH_SHORT).show();

            }
        } else if (v.getId() == R.id.login_signup_btn) {
            listener.onSignUp();
        } else if (v.getId() == R.id.login_forgot_password_txt) {
            listener.onForgotPassword();
        } else {

        }

    }

    /*
       connects to webserver and perfrom user sign in
       @retunrs tru for success
     */
    public boolean signIn(String email, String password) {

        status = false;
        final JsonObject user = new JsonObject();
        user.addProperty(EMAIL, email);
        user.addProperty(PASSWORD, password);


        progressDialog.show();
        Ion.with(getContext())
                .load(URL)
                .setJsonObjectBody(user)
                .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject jsonObject) {
                if (e != null) {
                    Log.i("Error", e.toString());
                }
                if (jsonObject != null) {

                    if (jsonObject.get("status").getAsString().equals("success")) {


                        String username = jsonObject.getAsJsonObject("user").get("email").getAsString();
                        String firstName = jsonObject.getAsJsonObject("user").get("first_name").getAsString();
                        String lastName = jsonObject.getAsJsonObject("user").get("last_name").getAsString();
                        Log.i("Username ", username);
                        Log.i("First Name ", firstName);
                        Log.i("Last Name ", lastName);

                        listener.onLogin(username);

                        status = true;

                    } else {
                        Toast.makeText(getContext(),
                                jsonObject.get("message").getAsString(),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(),
                            getResources().getString(R.string.error_cannot_connect),
                            Toast.LENGTH_SHORT).show();
                }
                progressDialog.hide();

            }
            //}
        });

        return status;
    }

    /*
         Checks for valid email
         @return true for valid
         */
    public final boolean isValidEmail(CharSequence target) {
        if (!TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()) {
            inputLayoutEmail.setErrorEnabled(false);
            return true;
        } else {

            inputLayoutEmail.setError(getResources().getString(R.string.error_email));
            return false;

        }
    }

    /*
         Checks for valid password
         @return true for valid
         */
    public final boolean isValidPassword(CharSequence target) {
        if (!TextUtils.isEmpty(target) && target.toString().trim().length() > 5) {
            //regex here
            inputLayoutPassword.setErrorEnabled(false);

            return true;
        } else {

            inputLayoutPassword.setError(getResources().getString(R.string.error_password));
            return false;
        }
    }

    /*
        An interface a class will implement so this fragment can call back
     */
    interface OnLoginListener {
        public void onLogin(String user);

        public void onSignUp();

        public void onForgotPassword();

    }

    /*
        Listener class used to listen to key presses
    */
    private class BarakahEditBoxListener implements TextWatcher {

        private final View view;

        BarakahEditBoxListener(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()) {
                case R.id.login_email_txt: {
                    isValidEmail(email.getText());
                    break;
                }

                case R.id.login_password_txt: {
                    isValidPassword(password.getText());
                    break;
                }
            }


        }
    }
}
