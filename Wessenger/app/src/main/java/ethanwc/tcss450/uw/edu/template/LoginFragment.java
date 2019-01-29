package ethanwc.tcss450.uw.edu.template;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ethanwc.tcss450.uw.edu.template.model.Credentials;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    View mView;
    private OnLoginFragmentInteractionListener mListener;
    public LoginFragment() {
        // Required empty public constructor
    }

    public void onStart() {
        super.onStart();
        if(getArguments() != null) {
            String uname = getArguments().getString(getString(R.string.email_registerToLogin));
            String pwd = getArguments().getString(getString(R.string.password_registerToLogin));
            System.out.println(uname +"------"+pwd);
            updateContent(uname, pwd);
        }

    }
    public void updateContent(String uname, String pwd) {
        EditText editText_email = getActivity().findViewById(R.id.edittext_login_email);
        editText_email.setText(uname);
        EditText editText_pwd = getActivity().findViewById(R.id.edittext_login_password);
        editText_pwd.setText(pwd);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_login, container, false);
        Button btnLogin = (Button) mView.findViewById(R.id.button_login_login);
        btnLogin.setOnClickListener(this);
        TextView newUser = (TextView) mView.findViewById(R.id.testview_login_newuser);
        newUser.setOnClickListener(this);

        return mView;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginFragmentInteractionListener) {
            mListener = (OnLoginFragmentInteractionListener) context;
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
    @Override
    public void onClick(View view) {
//        System.out.println("--------clicked in login"+view.getId());
        if(mListener != null){
            switch ((view.getId())){
                case R.id.button_login_login:

                    EditText emailText = ((EditText) mView.findViewById(R.id.edittext_login_email));
                    String email = emailText.getText().toString();
                    EditText passwordText = ((EditText) mView.findViewById(R.id.edittext_login_password));
                    String password = passwordText.getText().toString();
                    if(!email.isEmpty() && (!password.isEmpty())){
                        if(checkspecialcharacter(email) && (password.length()>5)) {
                            Credentials credentials = new Credentials.Builder(email,password).build();
                            mListener.onLoginSuccess(credentials);

                        }else{
                            if(password.length()<6){passwordText.setError("Password should be of 6 character");}

                        }
                    }else{

                        if(!checkspecialcharacter(email)) {emailText.setError("Email should contain @");}
                        if(email.isEmpty()){emailText.setError("Email is empty");}
                        if(password.isEmpty()){passwordText.setError("Password is empty");}




                    }
                    break;
                case R.id.testview_login_newuser:
                    mListener.onRegisterClicked();
                    break;
                    default:
            }
        }

    }
    /**
     * method to check if the string contains @
     * @param email
     * @return true or false
     */
    public boolean checkspecialcharacter(String email){
        char a;
        for(int i=0; i<email.length(); i++){
            a = email.charAt(i);
            if(Character.toString(a).equals("@")){
                return true;
            }

        }
        return false;
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
    public interface OnLoginFragmentInteractionListener {
        // TODO: Update argument type and name

        void onLoginSuccess(Credentials credentials);
        void onRegisterClicked();

    }
}
