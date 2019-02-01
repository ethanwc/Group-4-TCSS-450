package ethanwc.tcss450.uw.edu.template.Main;


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

import ethanwc.tcss450.uw.edu.template.R;
import ethanwc.tcss450.uw.edu.template.model.Credentials;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewUserFragment extends Fragment implements View.OnClickListener{
    private OnNewUserFragmentButtonAction mListener;
    View mView;
    public NewUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_new_user, container, false);
        TextView txtLoginClick = mView.findViewById(R.id.textview_newuser_login);
        txtLoginClick.setOnClickListener(this);


        Button btnRegister = mView.findViewById(R.id.button_newuser_register);
        btnRegister.setOnClickListener(this);

        return mView;
    }

    /**
     * These buttons are to handle the button click of New USer.
     * It handles for Register and Login
     * @param view
     */
    @Override
    public void onClick(View view) {

        if (mListener != null) {
            switch ((view.getId())) {
                case R.id.textview_newuser_login:
                    mListener.loginButtonAction();
                    break;

                case R.id.button_newuser_register:
                    EditText nickName_text = (EditText) mView.findViewById(R.id.edittext_newuser_nickname);
                    EditText firstName_text = (EditText) mView.findViewById(R.id.edittext_newuser_first);
                    EditText lastName_text = (EditText) mView.findViewById(R.id.edittext_newuser_last);
                    EditText email_text = (EditText) mView.findViewById(R.id.edittext_newuser_email);
                    EditText pwd_text = (EditText) mView.findViewById(R.id.edittext_newuser_password);
                    EditText re_pwd_text = (EditText) mView.findViewById(R.id.edittext_newuser_password2);
                    String nickName = nickName_text.getText().toString();
                    String firstName = firstName_text.getText().toString();
                    String lastName = lastName_text.getText().toString();

                    String email = email_text.getText().toString();
                    String password = pwd_text.getText().toString();
                    String re_password = re_pwd_text.getText().toString();
                    if (!email.isEmpty() && (!password.isEmpty()) && (!re_password.isEmpty())
                            && (!nickName.isEmpty()) && (!firstName.isEmpty()) && (!lastName.isEmpty())) {

                        if (!checkspecialcharacter(email)) {
                            email_text.setError("email should contain @");
                        }
                        if (!password.equals(re_password)) {
                            re_pwd_text.setError("Password not match");
                        }
                        if (password.length() < 6) {
                            pwd_text.setError("password lenght smaller than 6");
                        }
                        if((checkspecialcharacter(email)) && (password.equals(re_password)) && (password.length() > 5)){
                            Credentials credentials = new Credentials.Builder(email,password).build();
                            mListener.registerSuccess(credentials);


                        }
                    }else{
                        if (email.isEmpty()) {
                            email_text.setError("Email is empty");
                        }
                        if(password.isEmpty()) {
                            pwd_text.setError("Password is empty");
                        }
                        if(re_password.isEmpty()){
                            re_pwd_text.setError("Re-Password is empty");
                        }
                        if(firstName.isEmpty()){
                            firstName_text.setError("First Name is empty");
                        }
                        if(lastName.isEmpty()){
                            lastName_text.setError("Last Name is empty");
                        }
                        if(nickName.isEmpty()){
                            nickName_text.setError("Nick Name is empty");
                        }
                    }
                    break;

            }


        }
    }
    /**
     * method to check if the string contains @
     * @param email
     * @return
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


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     *
     */
    public interface OnNewUserFragmentButtonAction {
        // TODO: Update argument type and name
        void registerSuccess(Credentials credentials);
        void loginButtonAction();
    }
}
