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
import android.view.LayoutInflater;
import android.view.Menu;
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
 * Created by bevuk on 12/21/2015.
 */
public class ForgotPasswordFragment extends Fragment {
    /*
       Tag used for logging
    */
    public static final String TAG = "forgot_password";

    /*
        Url used to connect and submit forgot password requests
     */
    private static final String URL = "http://www.barakahchicago.org/bcwebservice/forgotPassword.php";

    /*
        Tag used for logging
     */
    private static final String LOG_TAG = "ForgotPasswordFragment";
    private Button send;
    private TextView email;
    private TextView message;
    private TextInputLayout inputLayoutEmail;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setting option menu on so we can clear it on onPrepareOption menu.
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.forgot_password_fragment, container, false);
        email = (TextView) view.findViewById(R.id.forgot_password_email);
        message = (TextView) view.findViewById(R.id.forgot_password_message);
        send = (Button) view.findViewById(R.id.forgot_password_send);
        inputLayoutEmail = (TextInputLayout) view.findViewById(R.id.forgot_password_input_layout_email);
        email.addTextChangedListener(new BarakahEditBoxListener(email));
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle(getResources().getString(R.string.message_password_reset_checking_email));
        progressDialog.setMessage(getResources().getString(R.string.message_please_wait));
        progressDialog.hide();
        send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isValidEmail(email.getText())) {
                    JsonObject data = new JsonObject();
                    data.addProperty("email", email.getText().toString());
                    progressDialog.show();
                    Ion.with(getContext()).load(URL).setJsonObjectBody(data)
                            .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject jsonObject) {
                            if (jsonObject != null) {

                                Log.d(LOG_TAG,
                                        "Response:  "
                                                + jsonObject.toString());

                                if (jsonObject.get("status").getAsString()
                                        .equals("success")) {
                                    Toast.makeText(getContext(),
                                            jsonObject.get("message").getAsString(),
                                            Toast.LENGTH_SHORT).show();
                                    message.setText(jsonObject.get("message").getAsString());

                                    email.setText("");
                                    email.setVisibility(view.GONE);
                                    send.setVisibility(view.GONE);
                                    inputLayoutEmail.setErrorEnabled(false);
                                    // getActivity().getSupportFragmentManager().popBackStack();

                                } else {
                                    Toast.makeText(getContext(),
                                            jsonObject.get("message").getAsString(),
                                            Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(getContext(),
                                        "Error connecting",
                                        Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.hide();
                        }
                    });
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
                case R.id.forgot_password_email: {
                    isValidEmail(email.getText());
                    break;
                }

            }


        }
    }
}