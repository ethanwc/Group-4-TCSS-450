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

import ethanwc.tcss450.uw.edu.template.Connections.SendPostAsyncTask;
import ethanwc.tcss450.uw.edu.template.R;
import ethanwc.tcss450.uw.edu.template.model.Credentials;


/**
 * Fragment used to represent the registration page.
 * Has-A wait fragment.
 * Has-A OnNewUserFragmentButtonAction
 */
public class NewUserFragment extends WaitFragment {
    //Local variables to NewUserFragment.
    private OnNewUserFragmentButtonAction mListener;
    private EditText mEditEmail, mEditPass, mEditSecondPass, mEditFirstName, mEditLastName, mEditUsername;
    private Credentials mCredentials;
    private Boolean mHasSpecialCharacter = false;
    private Boolean mHasNumber= false;
    private Boolean mHasAlphabet = false;
    private String mEmail;
    private String mPass;
    private String mUsername;
    private String mFirstName;
    private String mLastName;

    /**
     * Required empty public constructor.
     */
    public NewUserFragment() {
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
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_new_user, container, false);

        //Add a listener to login button which directs to goBack method.
        TextView txtLoginClick = mView.findViewById(R.id.textview_newuser_login);
        txtLoginClick.setOnClickListener(this::goBack);

        //Add a listener to register button which directs to register method.
        Button btnRegister = mView.findViewById(R.id.button_newuser_register);
        btnRegister.setOnClickListener(this::register);

        //Store edit text objects as local varaibles
        mEditEmail = mView.findViewById(R.id.edittext_newuser_email);
        mEditPass = mView.findViewById(R.id.edittext_newuser_password);
        mEditSecondPass = mView.findViewById(R.id.edittext_newuser_password2);
        mEditFirstName = mView.findViewById(R.id.edittext_newuser_first);
        mEditLastName = mView.findViewById(R.id.edittext_newuser_last);
        mEditUsername = mView.findViewById(R.id.edittext_newuser_nickname);

        return mView;
    }

    /**
     * OnAttach used to check whether the correct listeners have been implemented.
     * @param context Context of the current ui situation.
     */
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

    /**
     * OnDetach used to clear the ButtonAction.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Helper used to route to login screen on button press.
     * @param view Required view for method call.
     */
    public void goBack(View view) {
        mListener.loginButtonAction();
    }

    /**
     * Helper used to route to authentication page on button press.
     * Handles warning user client-side of improper field inputs.
     * @param view Required view for method call.
     */
    public void register(View view) {
        //Flags used for password input checks
        mHasSpecialCharacter = false;
        mHasAlphabet = false;
        mHasNumber = false;
        Boolean mHasAt;
        Boolean mPassMatch;
        Boolean mPassLength;
        //Store user input values to variables
        mEmail = mEditEmail.getText().toString();
        mPass = mEditPass.getText().toString();
        String mSecondPass = mEditSecondPass.getText().toString();
        mFirstName = mEditFirstName.getText().toString();
        mLastName = mEditLastName.getText().toString();
        mUsername = mEditUsername.getText().toString();

        //Run checks on user input
        mHasAt = mEmail.contains("@");
        mPassMatch = mPass.equals(mSecondPass);

        int length = mPass.length();
        if(mPass.length()>0) {
            for (int i = 0; i < mPass.length(); i++) {

                mHasSpecialCharacter = isSpecialCharater(mPass.charAt(i));
                mHasAlphabet = ismHasAlphabet(mPass.charAt(i));
                mHasNumber = isHasNumber(mPass.charAt(i));
            }
        }
        Boolean mPasswordContain = (mPass.length() > 5 && mHasSpecialCharacter && mHasAlphabet && mHasNumber && !mUsername.isEmpty());
        mPassLength = mPass.length() > 5;

        //Send user warnings to textfields with specific information.
        if (mFirstName.isEmpty()) mEditFirstName.setError("First name must be entered.");

        if (mLastName.isEmpty()) mEditLastName.setError("Last name must be entered.");

        if (mSecondPass.isEmpty()) mEditSecondPass.setError("Password must be entered.");

        if (mUsername.isEmpty()) mEditUsername.setError("Nickname must be entered.");

        if (!mEmail.contains("@")) mEditEmail.setError("Please enter valid e-mail('@' required).");

        if (mEmail.isEmpty()) mEditEmail.setError("Please enter an mEmail");

        if (!mPassLength) {
            mEditPass.setError("Password must be at least 6 characters.");
        }
        if (!mHasAlphabet) {
            mEditPass.setError("Password must contain alphabetic(a,b,c,...) characters.");
        }
        if (!mHasNumber) {
            mEditPass.setError("Password must contain numeric(1,2,3,...) characters.");
        }
        if (!mHasSpecialCharacter) {
            mEditPass.setError("Password must contain special(!,@,#,...) characters");
        }
        if (!mHasAlphabet && !mHasNumber) {
             mEditPass.setError("Password must include numeric(1,2,3,...) and alphabetic(a,b,c,...) characters.");
         }
         if (!mHasAlphabet && !mHasSpecialCharacter) {
             mEditPass.setError("Password must include alphabetic(a,b,c,...) and special(!,@,#,...) characters.");
         }
         if (!mPassLength && !mHasAlphabet) {
             mEditPass.setError("Password must be at least 6 characters and must include alphabetic(a,b,c,...) characters.");
         }
         if (!mPassLength && !mHasNumber) {
             mEditPass.setError("Password must be at least 6 characters and must include numeric(1,2,3,...) characters.");
         }
         if (!mPassLength && !mHasSpecialCharacter) {
             mEditPass.setError("Password must be at least 6 characters and must include special(!,@,#,...) characters.");
         }
         if (!mHasNumber && !mHasSpecialCharacter) {
             mEditPass.setError("Password must include numeric(1,2,3,...) and special(!,@,#,...) characters.");
         }
         if (!mPassLength && !mHasAlphabet && !mHasNumber) {
            mEditPass.setError("Password must be at least 6 characters and must include numeric(1,2,3,...) and alphabetic(a,b,c,...) characters.");
        }
        if (!mPassLength && !mHasAlphabet && !mHasSpecialCharacter) {
            mEditPass.setError("Password must be at least 6 characters and must include alphabetic(a,b,c,...) and special(!,@,#,...) characters.");
        }
        if (!mPassLength && !mHasNumber && !mHasSpecialCharacter) {
            mEditPass.setError("Password must be at least 6 characters and must include numeric(1,2,3,...) and special(!,@,#,...) characters.");
        }
        if (!mHasAlphabet && !mHasNumber && !mHasSpecialCharacter) {
            mEditPass.setError("Password must include numeric(1,2,3,...), alphabetic(a,b,c,...), and special(!,@,#,...) characters.");
        }
        if (!mPassLength && !mHasAlphabet && !mHasNumber && !mHasSpecialCharacter) {
            mEditPass.setError("Password must be at least 6 characters and must include numeric(1,2,3,...), alphabetic(a,b,c,...), and special(!,@,#,...) characters.");
        }

        if (!mPass.equals(mSecondPass)) mEditSecondPass.setError("Passwords do not match");

        //If clientside checks pass, build credentials and attempt registration serverside
        if (mPasswordContain && mHasAt && mPassMatch && length > 5 && (!mFirstName.isEmpty()) && (!mLastName.isEmpty()) && mPass.length() > 5) {
            if (mListener != null) {
                mCredentials = new Credentials.Builder(mEmail, mPass)
                        .addFirstName(mFirstName)
                        .addLastName(mLastName)
                        .addUsername(mUsername)
                        .build();
                attemptRegister();
            }
        }

    }
    /**
     * Helper method used to check if a character is a number.
     * @param c Character that is being checked.
     * @return Boolean which represent whether the character is a number.
     */
    public boolean isHasNumber(Character c){
        System.out.println(c);
        if((c > 47 && c < 58)){
            mHasNumber = true;
        }
        return  mHasNumber;
    }

    /**
     * Helper method used to check if a character is alphabetic.
     * @param c Character that is being checked.
     * @return Boolean which represents whether the character is alphabetic.
     */
    public boolean ismHasAlphabet(Character c){
        if((c > 64 && c < 91) || (c > 96 && c < 123)){
            mHasAlphabet = true;
        }
        return mHasAlphabet;

    }

    /**
     * Helper method used to check if a character is a special character.
     * @param c Character that is being checked.
     * @return Boolean which represents whether the character is a special character.
     */
    public boolean isSpecialCharater(Character c){
        if(c != 32 &&	 (c < 48 || c > 57) && 	(c < 65 || c > 90) && (c < 97 || c > 122)){
            Log.e("adam!", "" + c);

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
            //Set up the authentication fragment.
            if (success) {
                int mAuthenticationCode = resultsJSON.getInt(
                        getString(R.string.keys_json_authentication_code));
                mCredentials = new Credentials.Builder(mEmail, mPass)
                        .addFirstName(mFirstName)
                        .addLastName(mLastName)
                        .addUsername(mUsername)
                        .addCode(mAuthenticationCode)
                        .build();
                //Register was successful. Switch to the loadSuccessFragment.
                mListener.registerSuccess(mCredentials);
                return;
            //Notify user that web service returned unsuccessful.
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
     * Helper method used to attempt registration through an async task.
     */
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


    /**
     * Internal interface used to handle successful registration and login button.
     */
    public interface OnNewUserFragmentButtonAction extends WaitFragment.OnFragmentInteractionListener{
        /** Method used to handle successful registration. */
        void registerSuccess(Credentials credentials);
        /** Method used to handle login button click. */
        void loginButtonAction();

    }
}
