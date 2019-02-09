package ethanwc.tcss450.uw.edu.template.Messenger;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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
import ethanwc.tcss450.uw.edu.template.Main.LoginFragment;
import ethanwc.tcss450.uw.edu.template.Main.WaitFragment;
import ethanwc.tcss450.uw.edu.template.R;
import ethanwc.tcss450.uw.edu.template.model.Credentials;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragment extends WaitFragment {


    private OnChangePasswordFragmentInteractionListener mListener;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnChangePasswordFragmentInteractionListener) {
            mListener = (OnChangePasswordFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_change_password, container, false);

        Button btnChnagePassword = v.findViewById(R.id.button_change_password);

        btnChnagePassword.setOnClickListener(v1 ->changePasswordCheck(v));

        return v;
    }

    private void changePasswordCheck(View view) {

        EditText email = view.findViewById(R.id.editText_changepassword_email);
        EditText currentPw = view.findViewById(R.id.editText_changepassword_currentpw);
        EditText newPw = view.findViewById(R.id.editText_changepassword_newpw);
        EditText newPw2 = view.findViewById(R.id.editText_changepassword_newpw2);

        String emailString = email.toString();
        String currentPwString = currentPw.toString();
        String newPwString = newPw.toString();
        String newPw2String = newPw2.toString();

        if (emailString.length() == 0 ||currentPwString.length() == 0 ||
                newPwString.length() == 0 || newPw2String.length() == 0) {
            if (email.length() == 0) {
                ((TextView) getView().findViewById(R.id.editText_changepassword_email))
                        .setError("Please Enter Your Email.");
            } else if (currentPwString.length() == 0) {
                ((TextView) getView().findViewById(R.id.editText_changepassword_currentpw))
                        .setError("Please Enter Your Current Password.");
            } else if (newPwString.length() == 0) {
                ((TextView) getView().findViewById(R.id.editText_changepassword_newpw))
                        .setError("Please Enter Your New Password.");
            } else {
                ((TextView) getView().findViewById(R.id.editText_changepassword_newpw2))
                        .setError("Please Reentry Your Password.");
            }

        } else if (!newPwString.equals(newPw2String)) {
            ((TextView) getView().findViewById(R.id.editText_changepassword_newpw2))
                    .setError("Please Match With Your New Password.");
        } else {
            Credentials credentials = new Credentials.Builder(
                    emailString,
                    newPw2String)
                    .addChangePassword(newPwString)
                    .build();
            //build the web service URL
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_login))
                    .build();
            //build the JSONObject

            JSONObject msg = credentials.asJSONObject();

            //instantiate and execute the AsyncTask.
            new SendPostAsyncTask.Builder(uri.toString(), msg)
                    .onPreExecute(this::handleLoginOnPre)
                    .onPostExecute(this::handleLoginOnPost)
                    .onCancelled(this::handleErrorsInTask)
                    .build().execute();
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
                //Change password was successful. Switch to the loadSuccessFragment.
                mListener.onChangePasswordClicked();
                return;
            } else {
                if (error) {
                    //Change password was unsuccessful. Don’t switch fragments and
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

            ((TextView) getView().findViewById(R.id.edittext_login_email))
                    .setError("Email does not exist in the system.");
        }
    }




    public interface OnChangePasswordFragmentInteractionListener extends WaitFragment.OnFragmentInteractionListener {

        void onChangePasswordClicked();
    }

}
