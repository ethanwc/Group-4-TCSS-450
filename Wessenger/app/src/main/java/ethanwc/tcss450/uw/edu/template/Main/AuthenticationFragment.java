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
public class AuthenticationFragment extends WaitFragment {


    private int code = 0;
    private int inputCode = 1;
    View mView;
    private Credentials mCredentials;
    private OnAuthenticationFragmentButtonAction mListener;
    private EditText edit_email, edit_pw;
    private String email;
    private String pass;
    private String fn;
    private String ln;
    private String username;
    public AuthenticationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_authentication, container, false);

        email = getArguments().getString("email");
        pass = getArguments().getString("password");
        fn = getArguments().getString("first");
        ln = getArguments().getString("last");
        username = getArguments().getString("username");

        Button btnLogin = (Button) mView.findViewById(R.id.button_authentication_submit);
        mCredentials = new Credentials.Builder(email, pass)
                .addFirstName(fn)
                .addLastName(ln)
                .addUsername(username)
                .build();

        btnLogin.setOnClickListener(this::authenticate);

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            code = getArguments().getInt("code");


        }
    }

    public void authenticate(View view) {



        Credentials credentials = mCredentials;
        //build the web service URL
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_authenticate))
                .build();
        //build the JSONObject
        JSONObject msg = credentials.asJSONObject();
        mCredentials = credentials;
        //instantiate and execute the AsyncTask.
        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPreExecute(this::handleLoginOnPre)
                .onPostExecute(this::handleAuthenticateOnPost)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();


    }
    public interface OnAuthenticationFragmentButtonAction extends WaitFragment.OnFragmentInteractionListener{
        void authenticationSuccess(Credentials credentials);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAuthenticationFragmentButtonAction) {
            mListener = (OnAuthenticationFragmentButtonAction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    /**
     * Handle onPostExecute of the AsynceTask. The result from our webservice is
     * a JSON formatted String. Parse it for success or failure.
     * @param result the JSON formatted String response from the web service
     */
    private void handleAuthenticateOnPost(String result) {
        try {
            JSONObject resultsJSON = new JSONObject(result);
            boolean success =
                    resultsJSON.getBoolean(
                            getString(R.string.keys_json_login_success));
            if (success) {

                mCredentials = new Credentials.Builder(email, pass)
                        .addFirstName(fn)
                        .addLastName(ln)
                        .addUsername(username)
                        .build();
                //Register was successful. Switch to the loadSuccessFragment.
                mListener.authenticationSuccess(mCredentials);
                return;
            } else {
                //Register was unsuccessful. Don’t switch fragments and
                // inform the user
                ((TextView) getView().findViewById(R.id.edittext_newuser_email))
                        .setError("Email has already been registered.");
            }
            mListener.onWaitFragmentInteractionHide();
        } catch (JSONException e) {
            //It appears that the web service did not return a JSON formatted
            //String or it did not have what we expected in it.
            Log.e("JSON_PARSE_ERROR", result
                    + System.lineSeparator()
                    + e.getMessage());
            mListener.onWaitFragmentInteractionHide();
            ((TextView) getView().findViewById(R.id.edittext_newuser_email))
                    .setError("Login Unsuccessful2");
        }
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





}
