package ethanwc.tcss450.uw.edu.template.Main;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import java.util.Objects;

import ethanwc.tcss450.uw.edu.template.Messenger.MessagingHomeActivity;
import ethanwc.tcss450.uw.edu.template.R;
import ethanwc.tcss450.uw.edu.template.model.Credentials;
import me.pushy.sdk.Pushy;

/**
 * Activity which holds all of the login and registration fragments.
 * Has-A AppCompatActivity
 */
public class MainActivity extends AppCompatActivity implements NewUserFragment.OnNewUserFragmentButtonAction,
        LoginFragment.OnLoginFragmentInteractionListener, AuthenticationFragment.OnAuthenticationFragmentButtonAction,
        TemoraryPassSendFragment.OnFragmentInteractionListener, ResetPasswordFragment.OnFragmentInteractionListener {

    public static final String EXTRA_MESSAGE = "email";
    private boolean mLoadFromChatNotification = false;

    /**
     * OnCreate used to setup the fragment.
     * @param savedInstanceState bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Pushy.listen(this);

        setContentView(R.layout.activity_main);

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey("type")) {
                mLoadFromChatNotification = Objects.requireNonNull(getIntent().getExtras().getString("type")).equals("msg");
            }
        }

        //Set fragment in container
        if (findViewById(R.id.activity_main_container) != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_main_container, new LoginFragment())
                    .commit();
        }

    }

    /**
     * Method used to handle a successful registration.
     * @param credentials Bundle of user information.
     */
    @Override
    public void registerSuccess(Credentials credentials) {

        //Bundle information from credentials and send to fragment
        AuthenticationFragment authenticationFragment;
        authenticationFragment = new AuthenticationFragment();
        Bundle args = new Bundle();
        args.putSerializable(getString(R.string.email_registerToLogin), credentials.getEmail());
        args.putSerializable(getString(R.string.password_registerToLogin), credentials.getPassword());
        args.putSerializable("first", credentials.getFirstName());
        args.putSerializable("last", credentials.getLastName());
        args.putSerializable("username", credentials.getUsername());
        args.putSerializable(getString(R.string.keys_json_authentication_code), credentials.getCode());
        authenticationFragment.setArguments(args);
        loadFragment(authenticationFragment);

    }

    /**
     * Method used to load login fragment after login button is pressed.
     */
    @Override
    public void loginButtonAction() {
        loadFragment(new LoginFragment());
    }

    /**
     * This method is to load fragment
     *
     * @param frag, Fragment that is to be loaded in the container.
     */
    public void loadFragment(Fragment frag) {
        //Replace container with input fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_container, frag)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Method used to hadle successful login.
     * @param credentials Bundle of user information.
     * @param jwt Token used for auto login verification.
     */
    @Override
    public void onLoginSuccess(Credentials credentials, String jwt) {

        //Bundle user information from input into intent and send to messaging home activity.
        Intent intent = new Intent(MainActivity.this, MessagingHomeActivity.class);
        intent.putExtra(getString(R.string.email_registerToLogin), credentials);
        intent.putExtra(getString(R.string.keys_intent_jwt), jwt);
        intent.putExtra(getString(R.string.keys_intent_notification_msg), mLoadFromChatNotification);
        intent.putExtra(EXTRA_MESSAGE, credentials.getEmail());
        Log.e("mainactivity: ", " " + credentials.getEmail());
        startActivity(intent);
        finish();
    }

    /**
     * Method used to load registration fragment when new user button is clicked.
     */
    @Override
    public void onRegisterClicked() {
        loadFragment(new NewUserFragment());

    }

    /**
     * Method used to display the wait fragment.
     */
    @Override
    public void onWaitFragmentInteractionShow() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_main_container, new WaitFragment(), "WAIT")
                .addToBackStack(null)
                .commit();
    }

    /**
     * Method used to remove the wait fragment.
     */
    @Override
    public void onWaitFragmentInteractionHide() {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(Objects.requireNonNull(getSupportFragmentManager().findFragmentByTag("WAIT")))
                .commit();
    }

    /**
     * Method used to load temporary password send fragment when forgot password is clicked.
     */
    @Override
    public void onForgotPasswordClicked() {
        loadFragment(new TemoraryPassSendFragment());
    }

    /**
     * Method used to handle successful authentication of access to supplied email.
     * @param credentials Bundle of user information.
     */
    @Override
    public void authenticationSuccess(Credentials credentials) {

        //Bundle the user information from input and send to login fragment.
        LoginFragment loginFragment;
        loginFragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putSerializable(getString(R.string.email_registerToLogin), credentials.getEmail());
        args.putSerializable(getString(R.string.password_registerToLogin), credentials.getPassword());
        loginFragment.setArguments(args);
        loadFragment(loginFragment);
    }

    /**
     * Method used to send a temporary password when send button is clicked.
     * @param credentials Bundle of user information.
     */
    @Override
    public void onSendTemporaryPassword(Credentials credentials) {
        //Bundle email and send to reset password fragment.
        ResetPasswordFragment resetPassword = new ResetPasswordFragment();
        Bundle args = new Bundle();
        args.putSerializable((getString(R.string.email_registerToLogin)), credentials.getEmail());
        resetPassword.setArguments(args);
        loadFragment(resetPassword);

    }

    /**
     * Method used to handle successful password reset.
     * @param credentials Bundle of user information.
     */
    @Override
    public void onResetPassword(Credentials credentials) {
        //Bundle user information and send to login fragment.
        LoginFragment loginFragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putSerializable((getString(R.string.email_registerToLogin)), credentials.getEmail());
        args.putSerializable((getString(R.string.password_registerToLogin)), credentials.getPassword());
        loginFragment.setArguments(args);
        loadFragment(loginFragment);
    }
}