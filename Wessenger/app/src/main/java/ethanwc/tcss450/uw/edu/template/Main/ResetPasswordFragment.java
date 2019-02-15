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
 * Fragment used to represent the forgotten password reset password page.
 */
public class ResetPasswordFragment extends Fragment {
    //Local variables
    private OnFragmentInteractionListener mListener;
    private String mEmail;
    private Credentials mCredentials;
    private boolean mHasAlphabet;
    private boolean mHasNumber;
    private boolean mHasSymbol;
    private String mPassword;
    private String mPassword2;
    private String mTempPwd;

    /**
     * Required empty public constructor.
     */
    public ResetPasswordFragment() {
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
        View mView = inflater.inflate(R.layout.fragment_reset_password, container, false);
    //Add a listener to change password button.
    Button changePasswordBtn = mView.findViewById(R.id.btn_resetpwd_changepwd);
    changePasswordBtn.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            changePassword();
        }

         /**
         * handle the action on change password button clicked
         */
         void changePassword(){
             //Reset error flags for every new entry
             mHasAlphabet = false;
             mHasNumber = false;
             mHasSymbol = false;
             //Store user input.
             EditText tmpPwd = getActivity().findViewById(R.id.editText_resetPassword_temorarypassword);
             EditText pwd =  getActivity().findViewById(R.id.edittext_resetPassword_newPassword);
             EditText pwd2 = getActivity().findViewById(R.id.editText_resetPwd_passwordAgain);
             mPassword = pwd.getText().toString();
             mPassword2 = pwd2.getText().toString();
             mTempPwd = tmpPwd.getText().toString();
             //Begin extensive checks for correct user input.
             //Check passwords match.
             if(mPassword.equals(mPassword2)){
                 //Preform extensive checks for correct password input.
                 checkPasswordCorrect(mPassword);

                    //Send information to web service if correct information has been input.
                    if(mPassword.length() >5 && mHasNumber && mHasAlphabet && mHasSymbol){

                        Credentials credentials = new Credentials.Builder(mEmail,mPassword).addtempPwd(mTempPwd).build();
                        Uri uri = new Uri.Builder().scheme("https")
                                .appendPath(getString(R.string.ep_base_url))
                                .appendPath(getString(R.string.ep_resetDone)).build();
                        //build the JSONObject
                        JSONObject msg = credentials.asJSONObject();
                        mCredentials = credentials;
                        //instantiate and execute the AsyncTask.
                        new SendPostAsyncTask.Builder(uri.toString(), msg)
                                .onPreExecute(this::handlePwdResetPre)
                                .onPostExecute(this::handlePwdResetOnPost)
                                .onCancelled(this::handleErrorsInTask)
                                .build().execute();

                    }
                    //Inform user of why their input was incorrect.
                    else {
                        boolean passLength = mPassword.length() > 5;
                        // condition to check the password criteria
                        if (!passLength) {
                            pwd.setError("Password must be at least 6 characters.");
                        }
                        if (!mHasAlphabet) {
                            pwd.setError("Password must contain alphabetic(a,b,c,...) characters.");
                        }
                        if (!mHasNumber) {
                            pwd.setError("Password must contain numeric(1,2,3,...) characters.");
                        }
                        if (!mHasSymbol) {
                            pwd.setError("Password must contain special(!,@,#,...) characters");
                        }
                        if (!mHasAlphabet && !mHasNumber) {
                            pwd.setError("Password must include numeric(1,2,3,...) and alphabetic(a,b,c,...) characters.");
                        }
                        if (!mHasAlphabet && !mHasSymbol) {
                            pwd.setError("Password must include alphabetic(a,b,c,...) and special(!,@,#,...) characters.");
                        }
                        if (!passLength && !mHasAlphabet) {
                            pwd.setError("Password must be at least 6 characters and must include alphabetic(a,b,c,...) characters.");
                        }
                        if (!passLength && !mHasNumber) {
                            pwd.setError("Password must be at least 6 characters and must include numeric(1,2,3,...) characters.");
                        }
                        if (!passLength && !mHasSymbol) {
                            pwd.setError("Password must be at least 6 characters and must include special(!,@,#,...) characters.");
                        }
                        if (!mHasNumber && !mHasSymbol) {
                            pwd.setError("Password must include numeric(1,2,3,...) and special(!,@,#,...) characters.");
                        }
                        if (!passLength && !mHasAlphabet && !mHasNumber) {
                            pwd.setError("Password must be at least 6 characters and must include numeric(1,2,3,...) and alphabetic(a,b,c,...) characters.");
                        }
                        if (!passLength && !mHasAlphabet && !mHasSymbol) {
                            pwd.setError("Password must be at least 6 characters and must include alphabetic(a,b,c,...) and special(!,@,#,...) characters.");
                        }
                        if (!passLength && !mHasNumber && !mHasSymbol) {
                            pwd.setError("Password must be at least 6 characters and must include numeric(1,2,3,...) and special(!,@,#,...) characters.");
                        }
                        if (!mHasAlphabet && !mHasNumber && !mHasSymbol) {
                            pwd.setError("Password must include numeric(1,2,3,...), alphabetic(a,b,c,...), and special(!,@,#,...) characters.");
                        }
                        if (!passLength && !mHasAlphabet && !mHasNumber && !mHasSymbol) {
                            pwd.setError("Password must be at least 6 characters and must include numeric(1,2,3,...), alphabetic(a,b,c,...), and special(!,@,#,...) characters.");
                        }
                    }
                }else{
                    pwd2.setError("Password not match");
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
            private void handlePwdResetPre() {
                mListener.onWaitFragmentInteractionShow();
            }

            /**
             * Handle onPostExecute of the AsynceTask. The result from our webservice is
             * a JSON formatted String. Parse it for success or failure.
             * @param result the JSON formatted String response from the web service
             */
            private void handlePwdResetOnPost(String result) {
                try {
                    JSONObject resultsJSON = new JSONObject(result);
                    boolean success =
                            resultsJSON.getBoolean(
                                    getString(R.string.keys_json_login_success));
                    //condition on succsessfully reset password
                    if (success) {
                        //on successfully reset password
                        mListener.onResetPassword(mCredentials);


                    } else {
                        //set Error if user enters wrong temporary password code
                        ((TextView) getView().findViewById(R.id.editText_resetPassword_temorarypassword))
                                .setError("Please enter the right temporary password code");

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
         * Helper method used to check if the input password was correct format.
         * @param passwrd String used to represent the user's input password.
         */
        void checkPasswordCorrect(String passwrd){
                if(passwrd.length()>0) {
                    for (int i = 0; i < passwrd.length(); i++) {

                        mHasSymbol = isSpecialCharater(passwrd.charAt(i));
                        mHasAlphabet = ismHasAlphabet(passwrd.charAt(i));
                        mHasNumber = isHasNumber(passwrd.charAt(i));
                    }
                }
            }
        } );
        return mView;
    }

    /**
     * on start of fragment
     */
    public void onStart(){
        super.onStart();
        if(getArguments()!= null){
            mEmail = getArguments().getString(getString(R.string.email_registerToLogin));

        }
    }

    /**
     * OnAttach used to check whether the correct listeners have been implemented.
     * @param context Context of the current ui situation.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener  extends WaitFragment.OnFragmentInteractionListener  {
        void onResetPassword(Credentials credentials);
    }

    /**
     * Method to check if string has number
     * @param c, character
     * @return boolean
     */
    public boolean isHasNumber(Character c){
        System.out.println(c);
        if((c > 47 && c < 58)){
            mHasNumber = true;
        }
        return  mHasNumber;
    }
    /**
     * Method to check if string has Alphabet
     * @param c, character
     * @return boolean
     */
    public boolean ismHasAlphabet(Character c){
        if((c > 64 && c < 91) || (c > 96 && c < 123)){
            mHasAlphabet = true;
        }
        return mHasAlphabet;

    }
    /**
     * Method to check if string has special character
     * @param c, character
     * @return boolean
     */
    public boolean isSpecialCharater(Character c){
        if(c != 32 &&	 (c < 48 || c > 57) && 	(c < 65 || c > 90) && (c < 97 || c > 122)){
            Log.e("adam!", "" + c);

            mHasSymbol =  true;
        }
        return mHasSymbol;
    }

}
