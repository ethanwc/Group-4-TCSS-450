package ethanwc.tcss450.uw.edu.template.Main;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.SignInButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import ethanwc.tcss450.uw.edu.template.Connections.SendPostAsyncTask;
import ethanwc.tcss450.uw.edu.template.model.Credentials;
import ethanwc.tcss450.uw.edu.template.R;
import me.pushy.sdk.Pushy;


/**
 * Fragment used to represent the login page.
 * Has-A wait fragment
 * Has-A OnLoginFragmentInteractionListener
 */
public class LoginFragment extends WaitFragment {

    private Credentials mCredentials;
    private EditText mEditEmail, mEditPass;
    private String mJwt;
    private OnLoginFragmentInteractionListener mListener;
    private String mArrayEmail[];

    /**
     * Required empty public constructor.
     */
    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * OnStart used to store the email and password if sent by authentication fragment.
     */
    @Override
    public void onStart() {
        super.onStart();

        //Store email and password if sent from authentication fragment.
        if(getArguments() != null) {
            String uname = getArguments().getString(getString(R.string.email_registerToLogin));
            String pwd = getArguments().getString(getString(R.string.password_registerToLogin));
            updateContent(uname, pwd);
        }

        //Handle auto login
        SharedPreferences prefs =
                Objects.requireNonNull(getActivity()).getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        //retrieve the stored credentials from SharedPrefs
        if (prefs.contains(getString(R.string.keys_prefs_email)) &&
                prefs.contains(getString(R.string.keys_prefs_password))) {
            final String email = prefs.getString(getString(R.string.keys_prefs_email), "");
            final String password = prefs.getString(getString(R.string.keys_prefs_password), "");
            //Load the two login EditTexts with the credentials found in SharedPrefs
            EditText emailEdit = getActivity().findViewById(R.id.edittext_login_email);
            emailEdit.setText(email);
            EditText passwordEdit = getActivity().findViewById(R.id.edittext_login_password);
            passwordEdit.setText(password);

            //login
            doLogin(new Credentials.Builder(
                    emailEdit.getText().toString(),
                    passwordEdit.getText().toString())
                    .build());


        }
    }

    /**
     * Helper method used to update the email and password fields.
     * @param theEmail String used to represent the email.
     * @param thePass String used to represent the password.
     */
    public void updateContent(String theEmail, String thePass) {
        EditText editText_email = Objects.requireNonNull(getActivity()).findViewById(R.id.edittext_login_email);
        editText_email.setText(theEmail);
        EditText editText_pwd = getActivity().findViewById(R.id.edittext_login_password);
        editText_pwd.setText(thePass);
    }

    /**
     * OnCreateView used to instantiate relevant items to the fragment.
     *
     * @param inflater LayoutInflater used to inflate the layout for the fragment.
     * @param container ViewGroup used as a container to hold the items in the fragment.
     * @param savedInstanceState bundle.
     * @return inflated fragment
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment.
        View mView = inflater.inflate(R.layout.fragment_login, container, false);
        //Add listener to sign in button.
        Button btnLogin = mView.findViewById(R.id.button_login_login);
        btnLogin.setOnClickListener(this::signIn);
        //Add listener to register button.
        TextView newUser = mView.findViewById(R.id.testview_login_newuser);
        newUser.setOnClickListener(this::register);
        //Add listener to forgotten password button.
        TextView forgotPassword = mView.findViewById(R.id.txt_login_forgetPassword);
        forgotPassword.setOnClickListener(this :: forgotPassword);

        mEditEmail = mView.findViewById(R.id.edittext_login_email);
        mEditPass = mView.findViewById(R.id.edittext_login_password);


        return mView;

    }

    /**
     * OnAttach used to check whether the correct listeners have been implemented.
     * @param context Context of the current ui situation.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginFragmentInteractionListener) {
            mListener = (OnLoginFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnHomeFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void signIn(View view) {

        //no empty text fields, email must contain '@'

        String email = mEditEmail.getText().toString();
        String pass = mEditPass.getText().toString();
        Boolean at = email.contains("@");

        if (!email.isEmpty() && !pass.isEmpty() && at) attemptLogin();

        if (!at) mEditEmail.setError("Please enter a valid email");

        if (pass.length() < 1 || email.length() < 1) mEditPass.setError("Please fill out the fields");

    }

    public void register(View view) {

        if (mListener != null) mListener.onRegisterClicked();

    }
    private void forgotPassword(View view) {
        if(mListener != null) {
            if (view.getId() == R.id.txt_login_forgetPassword) {
                mListener.onForgotPasswordClicked();
            }
        }
    }

    /**
     * Middle man method for calling account hint selector in Main Activity.
     * @param view
     */
    public void showHint(View view) {
        mListener.loadHintClicked();
    }


    /**
     * Handle errors that may occur during the AsyncTask.
     * @param result the error message provide from the AsyncTask
     */
    private void handleErrorsInTask(String result) {
        Log.e("ASYNC_TASK_ERROR", result);
    }

    /**
     * Handle the setup of the UI before the HTTP call to the webservice.
     */
    private void handleLoginOnPre() {
        mListener.onWaitFragmentInteractionShow();
    }

    /**
     * Handle onPostExecute of the AsynceTask. The result from our webservice is
     * a JSON formatted String. Parse it for success or failure.
     * @param result the JSON formatted String response from the web service
     */
    private void handleLoginOnPost(String result) {
        try {
            JSONObject resultsJSON = new JSONObject(result);
            boolean success =
                    resultsJSON.getBoolean(
                            getString(R.string.keys_json_login_success));


            if (success) {
                //Login was successful. Switch to the loadSuccessFragment.

                mJwt = resultsJSON.getString(
                        getString(R.string.keys_json_login_jwt));
                JSONArray emailArray = resultsJSON.getJSONArray("emailArray");
                String emailList[] = new String[emailArray.length()];
                for(int i=0; i<emailArray.length();i++){
                    emailList[i]=emailArray.get(i).toString();
                }
                mArrayEmail = emailList;
//                System.out.println("---email length----"+ar.length());

                new RegisterForPushNotificationsAsync().execute();



                return;
            } else {
                if (resultsJSON.getString("error").equals("true")){
                    mEditEmail.setError("Email does not exist in the system.");
                } else {
                    mEditPass.setError(resultsJSON.getString("error"));
                }

            }

            mListener.onWaitFragmentInteractionHide();
        } catch (JSONException e) {
            //It appears that the web service did not return a JSON formatted
            //String or it did not have what we expected in it.
            Log.e("JSON_PARSE_ERROR", result
                    + System.lineSeparator()
                    + e.getMessage());

            mListener.onWaitFragmentInteractionHide();


            ((TextView) Objects.requireNonNull(getView()).findViewById(R.id.edittext_login_email))
                    .setError("Incorrect format returned.");
        }
    }

    private void attemptLogin() {
        boolean hasError = false;
        if (mEditEmail.getText().length() == 0) {
            hasError = true;
            mEditEmail.setError("Field must not be empty.");
        } else if (mEditEmail.getText().toString().chars().filter(ch -> ch == '@').count() != 1) {
            hasError = true;
            mEditEmail.setError("Field must contain a valid email address.");
        }
        if (mEditPass.getText().length() == 0) {
            hasError = true;
            mEditPass.setError("Field must not be empty.");
        }
        if (!hasError) {
            doLogin(new Credentials.Builder(
                    mEditEmail.getText().toString(),
                    mEditPass.getText().toString())
                    .build());
        }
    }

    private void doLogin(Credentials credentials) {
        //build the web service URL
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_login))
                .build();
        //build the JSONObject
        JSONObject msg = credentials.asJSONObject();
        mCredentials = credentials;
        Log.d("JSON Credentials", msg.toString());
        //instantiate and execute the AsyncTask.
        //Feel free to add a handler for onPreExecution so that a progress bar
        //is displayed or maybe disable buttons.
        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPreExecute(this::handleLoginOnPre)
                .onPostExecute(this::handleLoginOnPost)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();
    }


    private void saveCredentials(final Credentials credentials) {
        SharedPreferences prefs =
                Objects.requireNonNull(getActivity()).getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        //Store the credentials in SharedPrefs
        prefs.edit().putString(getString(R.string.keys_prefs_email), credentials.getEmail()).apply();
        prefs.edit().putString(getString(R.string.keys_prefs_password), credentials.getPassword()).apply();
    }

    private void handlePushyTokenOnPost(String result) {
        try {
            Log.d("JSON result",result);
            JSONObject resultsJSON = new JSONObject(result);
            boolean success = resultsJSON.getBoolean("success");
            if (success) {
                saveCredentials(mCredentials);
                mListener.onLoginSuccess(mCredentials, mJwt, mArrayEmail);
                return;
            } else {
                //Saving the token wrong. Don’t switch fragments and inform the user
                ((TextView) Objects.requireNonNull(getView()).findViewById(R.id.edittext_login_email))
                        .setError("Login Unsuccessful");
            }
            mListener.onWaitFragmentInteractionHide();
        } catch (JSONException e) {
            //It appears that the web service didn’t return a JSON formatted String
            //or it didn’t have what we expected in it.
            Log.e("JSON_PARSE_ERROR", result
                    + System.lineSeparator()
                    + e.getMessage());
            mListener.onWaitFragmentInteractionHide();
            ((TextView) Objects.requireNonNull(getView()).findViewById(R.id.edittext_login_email))
                    .setError("Login Unsuccessful");
        }
    }


    public interface OnLoginFragmentInteractionListener extends WaitFragment.OnFragmentInteractionListener {

        void onLoginSuccess(Credentials credentials, String jwt, String[] mArrayEmail);
        void onRegisterClicked();
        void onForgotPasswordClicked();
        void loadHintClicked();
        void saveCredentialsClicked(Credentials credentials);

    }
    private class RegisterForPushNotificationsAsync extends AsyncTask<Void, String, String>
    {
        protected String doInBackground(Void... params) {
            String deviceToken;
            try {
                // Assign a unique token to this device
                deviceToken = Pushy.register(Objects.requireNonNull(getActivity()).getApplicationContext());
                //subscribe to a topic (this is a Blocking call)
                Pushy.subscribe("all", getActivity().getApplicationContext());
            }
            catch (Exception exc) {
                cancel(true);
                // Return exc to onCancelled
                return exc.getMessage();
            }
            // Success
            return deviceToken;
        }
        @Override
        protected void onCancelled(String errorMsg) {
            super.onCancelled(errorMsg);
            Log.d("PhishApp", "Error getting Pushy Token: " + errorMsg);
        }
        @Override
        protected void onPostExecute(String deviceToken) {
            // Log it for debugging purposes
            Log.d("PhishApp", "Pushy device token: " + deviceToken);
            //build the web service URL
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_pushy))
                    .appendPath(getString(R.string.ep_token))
                    .build();
            //build the JSONObject
            JSONObject msg = mCredentials.asJSONObject();
            try {
                msg.put("token", deviceToken);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //instantiate and execute the AsyncTask.
            new SendPostAsyncTask.Builder(uri.toString(), msg)
                    .onPostExecute(LoginFragment.this::handlePushyTokenOnPost)
                    .onCancelled(LoginFragment.this::handleErrorsInTask)
                    .addHeaderField("authorization", mJwt)
                    .build().execute();
        }

    }

}
