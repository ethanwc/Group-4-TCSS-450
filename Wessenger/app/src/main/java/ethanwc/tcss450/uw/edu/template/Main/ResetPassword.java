package ethanwc.tcss450.uw.edu.template.Main
        ;


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
import ethanwc.tcss450.uw.edu.template.R;
import ethanwc.tcss450.uw.edu.template.model.Credentials;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResetPassword extends Fragment {
private Credentials mCredential;
private OnFragmentInteractionListener mListener;
private String mEmail;
private View mView;
private Credentials mCredentials;
    private boolean mHasAlphabet=false;
    private boolean mHasNumber=false;
    private boolean mHasSymbol=false;
    private String mPassword;
    private String mPassword2;
    private String mTempPwd;
    public ResetPassword() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView= inflater.inflate(R.layout.fragment_reset_password, container, false);

        Button changePasswordBtn = (Button) mView.findViewById(R.id.btn_resetpwd_changepwd) ;
        changePasswordBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                changePassword();
            }
            public  void changePassword(){
                EditText tmpPwd = (EditText) getActivity().findViewById(R.id.editText_resetPassword_temorarypassword);
                EditText pwd = (EditText) getActivity().findViewById(R.id.edittext_resetPassword_newPassword);
                EditText pwd2 = (EditText) getActivity().findViewById(R.id.editText_resetPwd_passwordAgain);
                mPassword = pwd.getText().toString();
                mPassword2 = pwd2.getText().toString();
                mTempPwd = tmpPwd.getText().toString();
                if(mPassword.equals(mPassword2)){
                    checkPasswordCorrect(mPassword);
                    if(mPassword.length() >5 && mHasNumber==true && mHasAlphabet==true && mHasSymbol==true){
                        System.out.println("has Number "+mHasNumber);
                        System.out.println("has Symbol "+mHasSymbol);
                        System.out.println("has alphabet "+mHasAlphabet);
                        //

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
                    else {
                    pwd.setError("Password should be more than 5 character long, and must have symbol, number, alphabet");
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
                    if (success) {
                        //Login was successful. Switch to the loadSuccessFragment.
                        mListener.onResetPassword(mCredentials);
//                        return;
                        System.out.println("password reset");

                    } else {
                        //Login was unsuccessful. Don’t switch fragments and
                        // inform the user
                        ((TextView) getView().findViewById(R.id.editText_resetPassword_temorarypassword))
                                .setError("Please enter right code");
                        System.out.println("--------not success---------");
                    }
                    mListener.onWaitFragmentInteractionHide();
                } catch (JSONException e) {
                    //It appears that the web service did not return a JSON formatted
                    //String or it did not have what we expected in it.
                    Log.e("JSON_PARSE_ERROR", result
                            + System.lineSeparator()
                            + e.getMessage());
                    mListener.onWaitFragmentInteractionHide();
//                    ((TextView) getView().findViewById(R.id.edittext_login_email))
//                            .setError("Login Unsuccessful");
                }
            }
            public void checkPasswordCorrect(String passwrd){
                if(passwrd.length()>0) {
                    for (int i = 0; i < passwrd.length(); i++) {

                        mHasAlphabet = isSpecialCharater(passwrd.charAt(i));
                        mHasSymbol = ismHasAlphabet(passwrd.charAt(i));
                        mHasNumber = isHasNumber(passwrd.charAt(i));
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
                    mHasSymbol =  true;
                }
                return mHasSymbol;
            }

        } );

        return mView;
    }

    public void onStart(){
        super.onStart();
        if(getArguments()!= null){
            mEmail = getArguments().getString(getString(R.string.email_registerToLogin));

        }
    }
//    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onResetPassword(mCredential);
        }
    }

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
        // TODO: Update argument type and name
        void onResetPassword(Credentials credentials);
    }
}
