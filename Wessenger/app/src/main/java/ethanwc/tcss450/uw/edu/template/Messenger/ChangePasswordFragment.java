package ethanwc.tcss450.uw.edu.template.Messenger;


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
import ethanwc.tcss450.uw.edu.template.Main.LoginFragment;
import ethanwc.tcss450.uw.edu.template.Main.WaitFragment;
import ethanwc.tcss450.uw.edu.template.R;
import ethanwc.tcss450.uw.edu.template.model.Credentials;

/**
 * Fragment used to represent the change password page.
 */
public class ChangePasswordFragment extends WaitFragment {

    private OnChangePasswordFragmentInteractionListener mListener;
    private Boolean mHasSpecialCharacter = false;
    private Boolean mHasNumber= false;
    private Boolean mHasAlphabet = false;
    private Boolean mPasswordContain = false;
    private String mEmail;

    /**
     * Required empty public constructor.
     */
    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    /**
     * OnStart used to store the email for the user.
     */
    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            mEmail = (String) getArguments().getSerializable("email");

        }
    }

    /**
     * OnAttach used to check whether the correct listeners have been implemented.
     * @param context Context of the current ui situation.
     */
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_change_password, container, false);
        //Add listener to change password button.
        Button btnChangePassword = v.findViewById(R.id.button_change_password);
        btnChangePassword.setOnClickListener(v1 ->changePasswordCheck(v));

        return v;
    }

    /**
     * Helper method used to check if the passwords match and the given password is correct.
     * @param view Required view to call the method.
     */
    private void changePasswordCheck(View view) {
        //Store user input into variables
        EditText currentPw = view.findViewById(R.id.editText_changepassword_currentpw);
        EditText newPw = view.findViewById(R.id.editText_changepassword_newpw);
        EditText newPw2 = view.findViewById(R.id.editText_changepassword_newpw2);

        //Begin checks
        String currentPwString = currentPw.getText().toString();
        String newPwString = newPw.getText().toString();
        String newPw2String = newPw2.getText().toString();
        Log.e("password: ", " " + currentPwString);
        if (currentPwString.length() == 0 ||
                newPwString.length() == 0 || newPw2String.length() == 0) {
            if (currentPwString.length() == 0) {
                ((TextView) getView().findViewById(R.id.editText_changepassword_currentpw))
                        .setError("Please Enter Your Current Password.");
            } else if (newPwString.length() == 0) {
                ((TextView) getView().findViewById(R.id.editText_changepassword_newpw))
                        .setError("Please Enter Your New Password.");
            } else {
                ((TextView) getView().findViewById(R.id.editText_changepassword_newpw2))
                        .setError("Please Re Enter Your Password.");
            }

        } else if (!newPwString.equals(newPw2String)) {
            ((TextView) getView().findViewById(R.id.editText_changepassword_newpw2))
                    .setError("Please Match With Your New Password.");
        } else {
            for (int i = 0; i < newPwString.length(); i++) {
                mHasSpecialCharacter = isSpecialCharater(newPwString.charAt(i));
                mHasAlphabet = ismHasAlphabet(newPwString.charAt(i));
                mHasNumber = isHasNumber(newPwString.charAt(i));
            }
            mPasswordContain = (newPwString.length() > 5 && mHasSpecialCharacter && mHasAlphabet && mHasNumber);
        }
        //Check change password with the web service.
        if (mPasswordContain){
            Credentials credentials = new Credentials.Builder(
                    mEmail, currentPwString)
                    .addChangePassword(newPwString)
                    .build();
            //build the web service URL
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_change_password))
                    .build();
            //build the JSONObject
            JSONObject msg = credentials.asJSONObject();

            //instantiate and execute the AsyncTask.
            new SendPostAsyncTask.Builder(uri.toString(), msg)
                    .onPreExecute(this::handleLoginOnPre)
                    .onPostExecute(this::handleLoginOnPost)
                    .onCancelled(this::handleErrorsInTask)
                    .build().execute();
        //Change password unsuccessful.
        } else {
            warning(newPwString);
        }

    }

    /**
     * OnDetach used to remove the listener.
     */
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

            if (success) {
                mListener.onWaitFragmentInteractionHide();
                //Change password was successful. Switch to the loadSuccessFragment.
                mListener.onChangePasswordClicked();
                return;
            } else {
                    ((TextView) getView().findViewById(R.id.editText_changepassword_currentpw))
                            .setError("Password is incorrect.");
            }
            mListener.onWaitFragmentInteractionHide();
        } catch (JSONException e) {
            //It appears that the web service did not return a JSON formatted
            //String or it did not have what we expected in it.
            Log.e("JSON_PARSE_ERROR", result
                    + System.lineSeparator()
                    + e.getMessage());

            mListener.onWaitFragmentInteractionHide();
        }
    }

    /**
     * Helper method used to check if given character is a number.
     * @param c Given character.
     * @return Boolean which represents whether the character is a number.
     */
    public boolean isHasNumber(Character c){
        if((c > 47 && c < 58)){
            mHasNumber = true;
        }
        return  mHasNumber;
    }

    /**
     * Helper method used to check if given character is alphabetic.
     * @param c Given character.
     * @return Boolean which represents whether the character is alphabetic.
     */
    public boolean ismHasAlphabet(Character c){
        if((c > 64 && c < 91) || (c > 96 && c < 123)){
            mHasAlphabet = true;
        }
        return mHasAlphabet;

    }

    /**
     * Helper method used to check if given character is a special character.
     * @param c Given character.
     * @return Boolean which represents whether the character is a special character.
     */
    public boolean isSpecialCharater(Character c){
        if(c != 32 &&	 (c < 48 || c > 57) && 	(c < 65 || c > 90) && (c < 97 || c > 122)){
            mHasSpecialCharacter =  true;
        }
        return mHasSpecialCharacter;
    }

    /**
     * Helper method used to check the numerous incorrect values for input password.
     * @param string Represents the password.
     */
    private void warning(String string) {
        Boolean passLength = string.length() > 5;
        if (!passLength) {
            ((TextView) getView().findViewById(R.id.editText_changepassword_newpw)).setError("Password must be at least 6 characters.");
        }
        if (!mHasAlphabet) {
            ((TextView) getView().findViewById(R.id.editText_changepassword_newpw)).setError("Password must contain alphabetic(a,b,c,...) characters.");
        }
        if (!mHasNumber) {
            ((TextView) getView().findViewById(R.id.editText_changepassword_newpw)).setError("Password must contain numeric(1,2,3,...) characters.");
        }
        if (!mHasSpecialCharacter) {
            ((TextView) getView().findViewById(R.id.editText_changepassword_newpw)).setError("Password must contain special(!,@,#,...) characters");
        }
        if (!mHasAlphabet && !mHasNumber) {
            ((TextView) getView().findViewById(R.id.editText_changepassword_newpw))
                    .setError("Password must include numeric(1,2,3,...) and alphabetic(a,b,c,...) characters.");
        }
        if (!mHasAlphabet && !mHasSpecialCharacter) {
            ((TextView) getView().findViewById(R.id.editText_changepassword_newpw))
                    .setError("Password must include alphabetic(a,b,c,...) and special(!,@,#,...) characters.");
        }
        if (!passLength && !mHasAlphabet) {
            ((TextView) getView().findViewById(R.id.editText_changepassword_newpw))
                    .setError("Password must be at least 6 characters and must include alphabetic(a,b,c,...) characters.");
        }
        if (!passLength && !mHasNumber) {
            ((TextView) getView().findViewById(R.id.editText_changepassword_newpw))
                    .setError("Password must be at least 6 characters and must include numeric(1,2,3,...) characters.");
        }
        if (!passLength && !mHasSpecialCharacter) {
            ((TextView) getView().findViewById(R.id.editText_changepassword_newpw))
                    .setError("Password must be at least 6 characters and must include special(!,@,#,...) characters.");
        }
        if (!mHasNumber && !mHasSpecialCharacter) {
            ((TextView) getView().findViewById(R.id.editText_changepassword_newpw))
                    .setError("Password must include numeric(1,2,3,...) and special(!,@,#,...) characters.");
        }
        if (!passLength && !mHasAlphabet && !mHasNumber) {
            ((TextView) getView().findViewById(R.id.editText_changepassword_newpw))
                    .setError("Password must be at least 6 characters and must include numeric(1,2,3,...) and alphabetic(a,b,c,...) characters.");
        }
        if (!passLength && !mHasAlphabet && !mHasSpecialCharacter) {
            ((TextView) getView().findViewById(R.id.editText_changepassword_newpw))
                    .setError("Password must be at least 6 characters and must include alphabetic(a,b,c,...) and special(!,@,#,...) characters.");
        }
        if (!passLength && !mHasNumber && !mHasSpecialCharacter) {
            ((TextView) getView().findViewById(R.id.editText_changepassword_newpw))
                    .setError("Password must be at least 6 characters and must include numeric(1,2,3,...) and special(!,@,#,...) characters.");
        }
        if (!mHasAlphabet && !mHasNumber && !mHasSpecialCharacter) {
            ((TextView) getView().findViewById(R.id.editText_changepassword_newpw))
                    .setError("Password must include numeric(1,2,3,...), alphabetic(a,b,c,...), and special(!,@,#,...) characters.");
        }
        if (!passLength && !mHasAlphabet && !mHasNumber && !mHasSpecialCharacter) {
            ((TextView) getView().findViewById(R.id.editText_changepassword_newpw))
                    .setError("Password must be at least 6 characters and must include numeric(1,2,3,...), alphabetic(a,b,c,...), and special(!,@,#,...) characters.");
        }
    }

    /**
     * Internal interface used to handle changing password.
     */
    public interface OnChangePasswordFragmentInteractionListener extends WaitFragment.OnFragmentInteractionListener {
        /** Method used to handle change password button being clicked. */
        void onChangePasswordClicked();
    }

}
