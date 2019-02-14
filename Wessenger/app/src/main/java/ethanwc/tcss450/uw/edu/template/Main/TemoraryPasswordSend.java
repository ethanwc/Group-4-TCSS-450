package ethanwc.tcss450.uw.edu.template.Main;

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
 * Activities that contain this fragment must implement the
 * {@link TemoraryPasswordSend.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TemoraryPasswordSend#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TemoraryPasswordSend extends Fragment{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Credentials mCredentials;
    private View mView;


    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TemoraryPasswordSend() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TemoraryPasswordSend.
     */
    public static TemoraryPasswordSend newInstance(String param1, String param2) {
        TemoraryPasswordSend fragment = new TemoraryPasswordSend();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mView= inflater.inflate(R.layout.fragment_temorary_password_send, container, false);

        Button mbtnSend = (Button) mView.findViewById(R.id.btn_temporarypassword_reset);
        mbtnSend.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                resetPassword(mView);
            }

            public void resetPassword(final View theButton){
                EditText editTextEmail = getActivity().findViewById((R.id.editText_temporarypassword_email));
                String email = editTextEmail.getText().toString();
                if(email.length()<2){
                    editTextEmail.setError("Please enter your email.");
                }else{
                    Credentials credentials = new Credentials.Builder(email,email).build();

                    Uri uri = new Uri.Builder().scheme("https")
                            .appendPath(getString(R.string.ep_base_url))
                            .appendPath(getString(R.string.ep_resetpwd)).build();
                    //build the JSONObject
                    JSONObject msg = credentials.asJSONObject();
                    mCredentials = credentials;
                    //instantiate and execute the AsyncTask.
                    new SendPostAsyncTask.Builder(uri.toString(), msg)
                            .onPreExecute(this::handlePasswordSendOnPre)
                            .onPostExecute(this::handlePasswordSendOnPost)
                            .onCancelled(this::handleErrorsInTask)
                            .build().execute();

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
            private void handlePasswordSendOnPre() {
                mListener.onWaitFragmentInteractionShow();
            }

            /**
             * Handle onPostExecute of the AsynceTask. The result from our webservice is
             * a JSON formatted String. Parse it for success or failure.
             * @param result the JSON formatted String response from the web service
             */
            private void handlePasswordSendOnPost(String result) {
                try {
                    JSONObject resultsJSON = new JSONObject(result);
                    boolean success =
                            resultsJSON.getBoolean(
                                    getString(R.string.keys_json_login_success));
                    if (success) {
                        //Login was successful. Switch to the loadSuccessFragment.
                        mListener.onSendTemporaryPassword(mCredentials);
//                        return;
                        System.out.println("--------success---------");

                    } else {
                        //Login was unsuccessful. Don’t switch fragments and
                        // inform the user
                        ((TextView) getView().findViewById(R.id.editText_temporarypassword_email))
                                .setError("Email not registered.");
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
        });


        return mView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSendTemporaryPassword(mCredentials);
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
        void onSendTemporaryPassword(Credentials credentials);
    }
}
