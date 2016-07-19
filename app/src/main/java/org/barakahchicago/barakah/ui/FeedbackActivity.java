package org.barakahchicago.barakah.ui;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.barakahchicago.barakah.R;

public class FeedbackActivity extends AppCompatActivity {

    /*
        Url used to connect to webservice
     */
    private static final String URL = "http://www.barakahchicago.org/bcwebservice/feedback.php";

    /*
        Tag used for logging
     */
    private final static String LOG_TAG = "FEEDBACK ACTIVITY";

    /*
        tag used for fragment transaction
     */
    public static String TAG = "feedback";

    private String username;
    private TextView feedbackEmail;
    private TextView feedbackMessage;
    private TextInputLayout emailTextInputLayout;
    private TextInputLayout messageTextInputLayout;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        getSupportActionBar().setElevation(0);
        feedbackEmail = (TextView) findViewById(R.id.feedback_email);
        feedbackMessage = (TextView) findViewById(R.id.feedback_message);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);


        emailTextInputLayout = (TextInputLayout) findViewById(R.id.feedback_input_layout_email);
        messageTextInputLayout = (TextInputLayout) findViewById(R.id.feedback_input_layout_feedback);


        username = getString(R.string.pref_key_username);
        feedbackEmail.setText(sharedPref.getString(username, ""));
        feedbackEmail.addTextChangedListener(new BarakahEditBoxListener(feedbackEmail));
        feedbackMessage.addTextChangedListener(new BarakahEditBoxListener(feedbackMessage));

    }

    /*
        Connects to webservice and submits user feedback

        @returns true for sucess
     */
    public boolean submit() {

        JsonObject json = new JsonObject();
        json.addProperty("email", feedbackEmail.getText().toString());
        json.addProperty("feedback", feedbackMessage.getText().toString());

        Ion.with(FeedbackActivity.this).load(URL).setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if (result != null) {

                            Log.d(LOG_TAG,
                                    "Feedback response:  "
                                            + result.toString());

                            if (result.get("status").getAsString()
                                    .equals("success")) {
                                Toast.makeText(FeedbackActivity.this,
                                        getResources().getString(R.string.message_feedback_sent), Toast.LENGTH_SHORT)
                                        .show();
                                feedbackMessage.setText("");
                                messageTextInputLayout.setErrorEnabled(false);
                            } else {
                                Toast.makeText(FeedbackActivity.this,
                                        getResources().getString(R.string.error_cannot_send),
                                        Toast.LENGTH_SHORT).show();
                            }

                        }

                    }
                });


        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feedback, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_submit) {
            if (isValidEmail(feedbackEmail.getText()) && isValidMessage(feedbackMessage.getText())) {
                submit();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /*
     Checks for valid email
     @return true for valid
     */
    public final boolean isValidEmail(CharSequence target) {
        if (!TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()) {
            emailTextInputLayout.setError(null);
            emailTextInputLayout.setErrorEnabled(false);
            return true;
        } else {

            emailTextInputLayout.setError(getResources().getString(R.string.error_email));
            return false;

        }
    }

    /*
     Checks for valid message
     @return true for valid
     */
    public final boolean isValidMessage(CharSequence target) {
        if (!TextUtils.isEmpty(target)) {
            //regex here

            messageTextInputLayout.setErrorEnabled(false);


            return true;
        } else {
            messageTextInputLayout.setErrorEnabled(true);
            messageTextInputLayout.setError(getResources().getString(R.string.error_feedback_message));
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
                case R.id.feedback_email: {
                    isValidEmail(feedbackEmail.getText());
                    break;
                }
                case R.id.feedback_message: {
                    isValidMessage(feedbackMessage.getText());
                    break;
                }

            }


        }
    }
}
