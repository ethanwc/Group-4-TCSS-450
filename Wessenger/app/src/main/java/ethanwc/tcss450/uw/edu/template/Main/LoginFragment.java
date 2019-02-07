package ethanwc.tcss450.uw.edu.template.Main;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import ethanwc.tcss450.uw.edu.template.Connections.SendPostAsyncTask;
import ethanwc.tcss450.uw.edu.template.R;
import ethanwc.tcss450.uw.edu.template.model.Credentials;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends WaitFragment {

    private Credentials mCredentials;

    private EditText edit_email, edit_pass;

    View mView;

    private OnLoginFragmentInteractionListener mListener;
    public LoginFragment() {
        // Required empty public constructor
    }

    public void onStart() {
        super.onStart();
        if(getArguments() != null) {
            String uname = getArguments().getString(getString(R.string.email_registerToLogin));
            String pwd = getArguments().getString(getString(R.string.password_registerToLogin));
            System.out.println(uname +"------"+pwd);
            updateContent(uname, pwd);
        }

    }
    public void updateContent(String uname, String pwd) {
        EditText editText_email = getActivity().findViewById(R.id.edittext_login_email);
        editText_email.setText(uname);
        EditText editText_pwd = getActivity().findViewById(R.id.edittext_login_password);
        editText_pwd.setText(pwd);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_login, container, false);
        Button btnLogin = (Button) mView.findViewById(R.id.button_login_login);
        btnLogin.setOnClickListener(this::signIn);

        TextView newUser = (TextView) mView.findViewById(R.id.testview_login_newuser);
        newUser.setOnClickListener(this::register);



        edit_email = (EditText) mView.findViewById(R.id.edittext_login_email);
        edit_pass =  (EditText) mView.findViewById(R.id.edittext_login_password);


        return mView;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginFragmentInteractionListener) {
            mListener = (OnLoginFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void signIn(View view) {

        //no empty text fields, email must contain '@'

        String email = edit_email.getText().toString();
        String pass = edit_pass.getText().toString();
        Boolean at = email.contains("@");

        if (!email.isEmpty() && !pass.isEmpty() && at) attemptLogin();

        if (!at) edit_email.setError("Please enter a valid email");

        if (pass.length() < 1 || email.length() < 1) edit_pass.setError("Please fill out the fields");

    }

    public void register(View view) {

        if (mListener != null) mListener.onRegisterClicked();

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
            boolean error = resultsJSON.getBoolean("error");


            if (success) {
                //Login was successful. Switch to the loadSuccessFragment.
                mListener.onLoginSuccess(mCredentials);
                return;
            } else {
                if (error) {
                    //Login was unsuccessful. Don’t switch fragments and
                    // inform the user
                    ((TextView) getView().findViewById(R.id.edittext_login_email))
                            .setError("Email does not exist.");
                } else {
                    ((TextView) getView().findViewById(R.id.edittext_login_password))
                            .setError("Password is incorrect.");
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


            String error = e.getMessage();
            ((TextView) getView().findViewById(R.id.edittext_login_email))
                    .setError("Email does not exist in the system.");
        }
    }
    private void attemptLogin() {
        boolean hasError = false;
        if (edit_email.getText().length() == 0) {
            hasError = true;
            edit_email.setError("Field must not be empty.");
        } else if (edit_email.getText().toString().chars().filter(ch -> ch == '@').count() != 1) {
            hasError = true;
            edit_email.setError("Field must contain a valid email address.");
        }
        if (edit_pass.getText().length() == 0) {
            hasError = true;
            edit_pass.setError("Field must not be empty.");
        }
        if (!hasError) {
            Credentials credentials = new Credentials.Builder(
                    edit_email.getText().toString(),
                    edit_pass.getText().toString())
                    .build();
            //build the web service URL
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_login))
                    .build();
            //build the JSONObject
            JSONObject msg = credentials.asJSONObject();
            mCredentials = credentials;
            //instantiate and execute the AsyncTask.
            new SendPostAsyncTask.Builder(uri.toString(), msg)
                    .onPreExecute(this::handleLoginOnPre)
                    .onPostExecute(this::handleLoginOnPost)
                    .onCancelled(this::handleErrorsInTask)
                    .build().execute();
        }
    }


    public interface OnLoginFragmentInteractionListener extends WaitFragment.OnFragmentInteractionListener {

        void onLoginSuccess(Credentials credentials);
        void onRegisterClicked();

    }
}
