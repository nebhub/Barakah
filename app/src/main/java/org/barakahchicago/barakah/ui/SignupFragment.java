package org.barakahchicago.barakah.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.barakahchicago.barakah.R;

/**
 * Created by bevuk on 12/18/2015.
 */
public class SignupFragment extends Fragment {
    /*
      Tag used for fragment transaction
    */
    public static final String TAG = "signup_frag";

    /*
      Url used to connect to webservice to sign up a user
    */
    private static final String URL = "http://www.barakahchicago.org/bcwebservice/signup.php";
    private TextView email;
    private TextView firsnName;
    private TextView lastName;
    private TextView password;
    private TextView phoneNumber;
    private Button signup;
    private boolean status = false;
    private LoginFragment.OnLoginListener listener;
    private TextInputLayout inputLayoutEmail;
    private TextInputLayout inputLayoutPassword;
    private TextInputLayout inputLayoutFirstName;
    private TextInputLayout inputLayoutLastName;
    private ProgressDialog progressDialog;
    private TextInputLayout inputLayoutPhoneNumber;


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setting option menu on so we can clear it on onPrepareOption menu.
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup_fragment, container, false);

        listener = (LoginFragment.OnLoginListener) getActivity();

        email = (TextView) view.findViewById(R.id.signup_email);
        firsnName = (TextView) view.findViewById(R.id.signup_first_name);
        lastName = (TextView) view.findViewById(R.id.signup_last_name);
        phoneNumber = (TextView) view.findViewById(R.id.signup_phone_number);
        password = (TextView) view.findViewById(R.id.signup_password);
        signup = (Button) view.findViewById(R.id.button2);

        email.addTextChangedListener(new BarakahEditBoxListener(email));
        firsnName.addTextChangedListener(new BarakahEditBoxListener(firsnName));
        lastName.addTextChangedListener(new BarakahEditBoxListener(lastName));
        phoneNumber.addTextChangedListener(new BarakahEditBoxListener(phoneNumber));
        password.addTextChangedListener(new BarakahEditBoxListener(password));

        inputLayoutEmail = (TextInputLayout) view.findViewById(R.id.signup_input_layout_email);
        inputLayoutPassword = (TextInputLayout) view.findViewById(R.id.signup_input_layout_password);
        inputLayoutFirstName = (TextInputLayout) view.findViewById(R.id.signup_input_layout_first_name);
        inputLayoutLastName = (TextInputLayout) view.findViewById(R.id.signup_input_layout_last_name);
        inputLayoutPhoneNumber = (TextInputLayout) view.findViewById(R.id.signup_input_layout_phone_number);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle(getResources().getString(R.string.message_singing_up));
        progressDialog.setMessage(getResources().getString(R.string.message_please_wait));
        progressDialog.hide();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateForm()) {

                    signUp();
                } else {

                }
            }


        });

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(final Menu menu) {

        super.onPrepareOptionsMenu(menu);

        menu.clear();
    }


    /*
        connects to webservice and registers new user
        @return tru for success
    */
    public boolean signUp() {


        status = false;
        JsonObject user = new JsonObject();

        user.addProperty("email", email.getText().toString());
        user.addProperty("password", password.getText().toString());
        user.addProperty("first_name", firsnName.getText().toString());
        user.addProperty("last_name", lastName.getText().toString());
        user.addProperty("phone_number", phoneNumber.getText().toString());

        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
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

                        Toast.makeText(getContext(), jsonObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();

                        status = true;

                        listener.onLogin(email.getText().toString());

                    } else {
                        Toast.makeText(getContext(), jsonObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.error_cannot_connect), Toast.LENGTH_SHORT).show();
                }

                progressDialog.hide();
            }

        });

        return status;
    }

    /*
     Validates sign up form
     */
    public boolean validateForm() {


        if (!isValidEmail(email.getText())) {
            return false;
        }
        if (!isValidFirstName(firsnName.getText())) {
            return false;
        }
        if (!isValidLastName(lastName.getText())) {
            return false;
        }
        if (!isValidPhoneNumber(phoneNumber.getText())) {
            return false;
        }
        if (!isValidPassword(password.getText())) {
            return false;
        }
        return true;

    }

    /*
             Checks for valid email
             @return true for valid
             */
    public final boolean isValidEmail(CharSequence target) {
        if (!TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()) {
            inputLayoutEmail.setError(null);
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
            inputLayoutPassword.setError(null);
            inputLayoutPassword.setErrorEnabled(false);
//
            return true;
        } else {

            inputLayoutPassword.setError(getResources().getString(R.string.error_password));
            return false;
        }
    }

    /*
             Checks for valid Name
             @return true for valid
    */
    public final boolean isValidFirstName(CharSequence target) {
        if (!TextUtils.isEmpty(target)) {
            //regex here

            inputLayoutFirstName.setErrorEnabled(false);


            return true;
        } else {
            inputLayoutFirstName.setErrorEnabled(true);
            inputLayoutFirstName.setError(getResources().getString(R.string.error_first_name));
            return false;
        }
    }


    /*
            Checks for valid Name
            @return true for valid
   */
    public final boolean isValidLastName(CharSequence target) {
        if (!TextUtils.isEmpty(target)) {
            //regex here

            inputLayoutLastName.setErrorEnabled(false);


            return true;
        } else {
            inputLayoutLastName.setErrorEnabled(true);
            inputLayoutLastName.setError(getResources().getString(R.string.error_last_name));
            return false;
        }
    }


    /*
            Checks for valid Phone number
            @return true for valid
   */
    public final boolean isValidPhoneNumber(CharSequence target) {
        if (!TextUtils.isEmpty(target) && target.toString().trim().length() > 9 && Patterns.PHONE.matcher(target).matches()) {
            //regex here
            inputLayoutPhoneNumber.setError(null);
            inputLayoutPhoneNumber.setErrorEnabled(false);
            return true;

        } else {
            inputLayoutPhoneNumber.setErrorEnabled(true);
            inputLayoutPhoneNumber.setError(getResources().getString(R.string.error_phone_number));
            return false;
        }
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
                case R.id.signup_email: {
                    isValidEmail(email.getText());
                    break;
                }
                case R.id.signup_first_name: {
                    isValidFirstName(firsnName.getText());
                    break;
                }
                case R.id.signup_last_name: {
                    isValidLastName(lastName.getText());
                    break;
                }
                case R.id.signup_phone_number: {
                    isValidPhoneNumber(phoneNumber.getText());
                    break;
                }
                case R.id.signup_password: {
                    isValidPassword(password.getText());
                    break;
                }
            }


        }
    }
}
