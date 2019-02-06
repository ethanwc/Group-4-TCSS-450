package ethanwc.tcss450.uw.edu.template.Main;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import ethanwc.tcss450.uw.edu.template.Messenger.MessagingHomeActivity;
import ethanwc.tcss450.uw.edu.template.R;
import ethanwc.tcss450.uw.edu.template.model.Credentials;

public class MainActivity extends AppCompatActivity implements NewUserFragment.OnNewUserFragmentButtonAction,
        LoginFragment.OnLoginFragmentInteractionListener, AuthenticationFragment.OnAuthenticationFragmentButtonAction {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        if (findViewById(R.id.activity_main_container) != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_main_container, new LoginFragment())
                    .commit();
        }


    }

//    /**
//     * These buttons are to handle the button click of Main Login Fragment.
//     * It handles for Sign in and New User
//     * @param v View used to represent the view passed in.
//     */
//    @Override
//    public void onClick(View v) {
//        if (R.id.button_login_login == v.getId()) {
//
//            Intent intent = new Intent(MainActivity.this, MessagingHomeActivity.class);
//            startActivity(intent);
//
//
//        } else if (v.getId() == R.id.textview_newuser_login) {
//            NewUserFragment newUser = new NewUserFragment();
//            FragmentTransaction transaction = getSupportFragmentManager()
//                    .beginTransaction().replace(R.id.activity_main_container, newUser).addToBackStack(null);
//            transaction.commit();
//
//        }
//    }


    @Override
    public void registerSuccess(Credentials credentials) {
        AuthenticationFragment authenticationFragment;
        authenticationFragment = new AuthenticationFragment();
        Bundle args = new Bundle();
        args.putSerializable(getString(R.string.email_registerToLogin), credentials.getEmail());
        args.putSerializable(getString(R.string.password_registerToLogin), credentials.getPassword());
        args.putSerializable(getString(R.string.keys_json_authentication_code), credentials.getCode());
        authenticationFragment.setArguments(args);
        loadFragment(authenticationFragment);


//        LoginFragment loginFragment;
//        loginFragment = new LoginFragment();
//        Bundle args = new Bundle();
//        args.putSerializable(getString(R.string.email_registerToLogin), credentials.getEmail());
//        args.putSerializable(getString(R.string.password_registerToLogin), credentials.getPassword());
//        loginFragment.setArguments(args);
//
//        loadFragment(loginFragment);

    }

    @Override
    public void loginButtonAction() {
        loadFragment(new LoginFragment());
    }

    /**
     * This method is to load fragment
     * @param frag, fragment
     */
    public void loadFragment(Fragment frag){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_container,  frag)
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void onLoginSuccess(Credentials credentials) {
        Intent intent = new Intent(MainActivity.this, MessagingHomeActivity.class);
            startActivity(intent);
    }

    @Override
    public void onRegisterClicked() {
        loadFragment(new NewUserFragment());

    }

    @Override
    public void onWaitFragmentInteractionShow() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_main_container, new WaitFragment(), "WAIT")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onWaitFragmentInteractionHide() {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(getSupportFragmentManager().findFragmentByTag("WAIT"))
                .commit();
    }

    @Override
    public void authenticationSuccess(Credentials credentials) {
        LoginFragment loginFragment;
        loginFragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putSerializable(getString(R.string.email_registerToLogin), credentials.getEmail());
        args.putSerializable(getString(R.string.password_registerToLogin), credentials.getPassword());
        loginFragment.setArguments(args);

        loadFragment(loginFragment);
    }

}
