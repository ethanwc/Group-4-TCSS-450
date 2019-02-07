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
public class NewUserFragment extends WaitFragment {
    private OnNewUserFragmentButtonAction mListener;
    View mView;
    private EditText edit_email, edit_pass1, edit_pass2, edit_fn, edit_ln, edit_username;
    private Credentials mCredentials;
    private Boolean mHasSpecialCharacter = false;
    private Boolean mHasNumber= false;
    private Boolean mHasAlphabet = false;
    private Boolean mPasswordContain = false;
    private int authenticationCode = 0;
    private String email;
    private String pass1;
    private String pass2;
    private String username;
    private String fn;
    private String ln;
    public NewUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_new_user, container, false);
        TextView txtLoginClick = mView.findViewById(R.id.textview_newuser_login);
        txtLoginClick.setOnClickListener(this::goBack);



        Button btnRegister = mView.findViewById(R.id.button_newuser_register);
        btnRegister.setOnClickListener(this::register);

        edit_email = ((EditText) mView.findViewById(R.id.edittext_newuser_email));
        edit_pass1 = ((EditText) mView.findViewById(R.id.edittext_newuser_password));
        edit_pass2 = ((EditText) mView.findViewById(R.id.edittext_newuser_password2));
        edit_fn = ((EditText) mView.findViewById(R.id.edittext_newuser_first));
        edit_ln = ((EditText) mView.findViewById(R.id.edittext_newuser_last));
        edit_username = ((EditText) mView.findViewById(R.id.edittext_newuser_nickname));

        return mView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNewUserFragmentButtonAction) {
            mListener = (OnNewUserFragmentButtonAction) context;
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


    public void goBack(View view) {
        mListener.loginButtonAction();
    }



    public void register(View view) {

        //no empty fields, passwords must match, password >= 6 chars
        email = edit_email.getText().toString();
        pass1 = edit_pass1.getText().toString();
        pass2 = edit_pass2.getText().toString();
        fn = edit_fn.getText().toString();
        ln = edit_ln.getText().toString();
        username = edit_username.getText().toString();


        Boolean at = email.contains("@");
        Boolean match = pass1.equals(pass2);

        int length = pass1.length();


        if (fn.isEmpty()) edit_fn.setError("First name must be entered.");

        if (ln.isEmpty()) edit_ln.setError("Last name must be entered.");

        if (pass2.isEmpty()) edit_pass2.setError("Password must be entered.");

        if (username.isEmpty()) edit_username.setError("Nickname must be entered.");

        if (!email.contains("@")) edit_email.setError("Invalid e-mail");
        if (pass1.length() < 6) edit_pass1.setError("Password must be at least 6 characters including numeric, alphabetic, and special characters.");


        if (!pass1.equals(pass2)) edit_pass2.setError("Passwords do not match");

//for checking special character, number & alphabet of password

        if(pass1.length()>0) {
            for (int i = 0; i < pass1.length(); i++) {

                mHasSpecialCharacter = isSpecialCharater(pass1.charAt(i));
                mHasAlphabet = ismHasAlphabet(pass1.charAt(i));
                mHasNumber = isHasNumber(pass1.charAt(i));
            }
        }

        if (!mHasSpecialCharacter) edit_pass1.setError("Password must contain a special character(!,@,#,...)");
        if (!mHasAlphabet) edit_pass1.setError("Password must contain an alphabetic character(a,b,c,...)");
        if (!mHasNumber) edit_pass1.setError("Password must contain a numeric character(1,2,3,...)");
        mPasswordContain = (pass1.length() > 5 && mHasSpecialCharacter==true && mHasAlphabet==true && mHasNumber==true);

System.out.println("++++++#####"+mPasswordContain);
        if (mPasswordContain && at && match && length > 5 && (!fn.isEmpty()) && (!ln.isEmpty())) {
            if (mListener != null) {
                mCredentials = new Credentials.Builder(email, pass1)
                        .addFirstName(fn)
                        .addLastName(ln)
                        .addUsername(username)
                        .build();
                attemptRegister();
            }
        }

    }
    public boolean isHasNumber(Character c){
        if((c > 47 && c < 58)){
            mHasNumber = true;
        }
        return  mHasNumber;
    }
    public boolean ismHasAlphabet(Character c){
        if((c > 64 && c < 91) || (c > 96 && c < 123)){
            mHasAlphabet = true;
        }
        return mHasAlphabet;

    }
    public boolean isSpecialCharater(Character c){
        if(c != 32 &&	 (c < 48 || c > 57) && 	(c < 65 || c > 90) && (c < 97 || c > 122)){
            mHasSpecialCharacter =  true;
        }
        return mHasSpecialCharacter;
    }



    /**
     * Handle onPostExecute of the AsynceTask. The result from our webservice is
     * a JSON formatted String. Parse it for success or failure.
     * @param result the JSON formatted String response from the web service
     */
    private void handleRegisterOnPost(String result) {
        try {
            JSONObject resultsJSON = new JSONObject(result);
            boolean success =
                    resultsJSON.getBoolean(
                            getString(R.string.keys_json_login_success));
            if (success) {
                authenticationCode =
                        resultsJSON.getInt(
                                getString(R.string.keys_json_authentication_code));
                mCredentials = new Credentials.Builder(email, pass1)
                        .addFirstName(fn)
                        .addLastName(ln)
                        .addUsername(username)
                        .addCode(authenticationCode)
                        .build();
                //Register was successful. Switch to the loadSuccessFragment.
                mListener.registerSuccess(mCredentials);
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

    private void attemptRegister() {

        Credentials credentials = mCredentials;
        //build the web service URL
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_register))
                .build();
        //build the JSONObject
        JSONObject msg = credentials.asJSONObject();
        mCredentials = credentials;
        //instantiate and execute the AsyncTask.
        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPreExecute(this::handleLoginOnPre)
                .onPostExecute(this::handleRegisterOnPost)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();
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




    public interface OnNewUserFragmentButtonAction extends WaitFragment.OnFragmentInteractionListener{
        void registerSuccess(Credentials credentials);
        void loginButtonAction();

    }
}
