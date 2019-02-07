package ethanwc.tcss450.uw.edu.template.Main;


import android.content.Context;
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

import ethanwc.tcss450.uw.edu.template.R;
import ethanwc.tcss450.uw.edu.template.model.Credentials;


/**
 * A simple {@link Fragment} subclass.
 */
public class AuthenticationFragment extends Fragment {


    private int code = 0;
    private int inputCode = 1;
    View mView;
    private Credentials mCredentials;
    private OnAuthenticationFragmentButtonAction mListener;
    private EditText edit_email, edit_pw;
    private String email;
    private String pass;
    public AuthenticationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_authentication, container, false);
        Button btnLogin = (Button) mView.findViewById(R.id.button_authentication_submit);
        btnLogin.setOnClickListener(this::authenticate);

        email = getArguments().getString("email");
        pass = getArguments().getString("password");
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

        EditText edit = getActivity().findViewById(R.id.edittext_authentication_codeinput);
        inputCode = Integer.parseInt(edit.getText().toString());

        if (inputCode == code) {

            mCredentials = new Credentials.Builder(email, pass).build();
            //Register was successful. Switch to the loadSuccessFragment.
            mListener.authenticationSuccess(mCredentials);
            return;
        } else {
            ((EditText) getView().findViewById(R.id.edittext_authentication_codeinput))
                    .setError("Incorrect code input, please check provided email.");
        }

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


}
