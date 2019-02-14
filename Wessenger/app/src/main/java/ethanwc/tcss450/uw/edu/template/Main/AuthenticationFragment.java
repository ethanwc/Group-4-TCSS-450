package ethanwc.tcss450.uw.edu.template.Main;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import ethanwc.tcss450.uw.edu.template.Connections.SendPostAsyncTask;
import ethanwc.tcss450.uw.edu.template.R;
import ethanwc.tcss450.uw.edu.template.model.Credentials;


/**
 * Fragment used to represent the authentication code input page.
 * Has-A wait fragment.
 * Has-A OnAuthenticationFragmentButtonAction
 */
public class AuthenticationFragment extends WaitFragment {

    //Local variables
    private int mActualCode = 0;
    private Credentials mCredentials;
    private OnAuthenticationFragmentButtonAction mListener;
    private String mEmail;
    private String mPass;
    private String mFirstName;
    private String mLastName;
    private String mUsername;

    /**
     * Required empty public constructor
     */
    public AuthenticationFragment() {
        // Required empty public constructor
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
        View mView = inflater.inflate(R.layout.fragment_authentication, container, false);

        //Store user's information sent from NewUser fragment into local variables.
        mEmail = Objects.requireNonNull(getArguments()).getString("mEmail");
        mPass = getArguments().getString("password");
        mFirstName = getArguments().getString("first");
        mLastName = getArguments().getString("last");
        mUsername = getArguments().getString("mUsername");

        //Build credentials to send to login fragment.
        Button btnLogin = mView.findViewById(R.id.button_authentication_submit);
        mCredentials = new Credentials.Builder(mEmail, mPass)
                .addFirstName(mFirstName)
                .addLastName(mLastName)
                .addUsername(mUsername)
                .build();

        //Add listener to authentication button.
        btnLogin.setOnClickListener(this::authenticate);

        return mView;
    }

    /**
     * OnStart used to store authentication code send from new user fragment.
     */
    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) mActualCode = getArguments().getInt("mActualCode");
    }

    /**
     * Helper method used to check authentication code and load login fragment upon success.
     * @param view Required view to call the method.
     */
    public void authenticate(View view) {
        //Store input code and check against existing code.
        EditText edit = getActivity().findViewById(R.id.edittext_authentication_codeinput);
        int mInputCode = Integer.parseInt(edit.getText().toString());

        if (mActualCode == mInputCode) {

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
        //Let user know the input incorrect code and handle incorrect password attempts to 5 guesses.
        } else {
            edit.setError("Incorrect authentication code input.");
        }
    }

    /**
     * Internal interface used to handle successful authentication.
     */
    public interface OnAuthenticationFragmentButtonAction extends WaitFragment.OnFragmentInteractionListener{
        /** Method used to handle successful authentication. */
        void authenticationSuccess(Credentials credentials);
    }

    /**
     * OnAttach used to check whether the correct listeners have been implemented.
     * @param context Context of the current ui situation.
     */
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
            //Build login fragment.
            if (success) {

                mCredentials = new Credentials.Builder(mEmail, mPass)
                        .addFirstName(mFirstName)
                        .addLastName(mLastName)
                        .addUsername(mUsername)
                        .build();
                //Register was successful. Switch to the loadSuccessFragment.
                mListener.authenticationSuccess(mCredentials);
                return;
            //Inform user that web service returned unsuccessful.
            } else {
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
                    .setError("Login Unsuccessful");
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
