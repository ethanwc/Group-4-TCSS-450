package ethanwc.tcss450.uw.edu.template.Main;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.CredentialPickerConfig;
import com.google.android.gms.auth.api.credentials.CredentialRequest;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.android.gms.auth.api.credentials.CredentialsClient;
import com.google.android.gms.auth.api.credentials.CredentialsOptions;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.credentials.IdentityProviders;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
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

    private CredentialsClient mCredentialsClient;
    private Credential mCurrentCredential;
    private boolean mIsResolving = false;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String KEY_IS_RESOLVING = "is_resolving";
    private static final int RC_SAVE = 1;
    private static final int RC_HINT = 2;
    private static final int RC_READ = 3;

    /**
     * OnCreate used to setup the fragment.
     * @param savedInstanceState bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CredentialsOptions options = new CredentialsOptions.Builder()
                .forceEnableSaveDialog()
                .build();
        mCredentialsClient = com.google.android.gms.auth.api.credentials.Credentials.getClient(this, options);

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
     * Called when the Load Hints button is clicked. Requests a Credential "hint" which will
     * be the basic profile information and an ID token for an account on the device. This is useful
     * to auto-fill sign-up forms with an email address, picture, and name or to do password-free
     * authentication with a server by providing an ID Token.
     */
    public void loadHintClicked() {
        HintRequest hintRequest = new HintRequest.Builder()
                .setHintPickerConfig(new CredentialPickerConfig.Builder()
                        .setShowCancelButton(true)
                        .build())
                .setEmailAddressIdentifierSupported(true)
                .setAccountTypes(IdentityProviders.GOOGLE)
                .build();


        PendingIntent intent = mCredentialsClient.getHintPickerIntent(hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(), RC_HINT, null, 0, 0, 0);
            mIsResolving = true;
        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "Could not start hint picker Intent", e);
            mIsResolving = false;
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
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_container, authenticationFragment)

                .commit();

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
    public void onLoginSuccess(Credentials credentials, String jwt, String[] arrayEmail) {
        ArrayList<String> emailList = new ArrayList<String>();
        for(int i=0; i<arrayEmail.length; i++){
            emailList.add(arrayEmail[i]);
//            System.out.println("-----"+arrayEmail[i]);
        }



        //Bundle user information from input into intent and send to messaging home activity.
        Intent intent = new Intent(MainActivity.this, MessagingHomeActivity.class);
        intent.putExtra(getString(R.string.email_registerToLogin), credentials);

        intent.putExtra(getString(R.string.keys_intent_jwt), jwt);
        intent.putExtra(getString(R.string.keys_intent_notification_msg), mLoadFromChatNotification);
        intent.putExtra(EXTRA_MESSAGE, credentials.getEmail());
        intent.putStringArrayListExtra("a", emailList);
        System.out.println("GOD MODE!!!" + credentials.getUsername());
        startActivity(intent);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);
        onWaitFragmentInteractionHide();

        switch (requestCode) {
            case RC_HINT:
                // Drop into handling for RC_READ
            case RC_READ:
                if (resultCode == RESULT_OK) {
                    boolean isHint = (requestCode == RC_HINT);
                    Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                    android.app.Fragment frag = getFragmentManager().findFragmentById(R.id.fragment_main_login);
//                  processRetrievedCredential(credential, isHint);
                } else {
                    Log.e(TAG, "Credential Read: NOT OK");
//                    showToast("Credential Read Failed");
                }

                mIsResolving = false;
                break;
            case RC_SAVE:
                if (resultCode == RESULT_OK) {
                    Log.d(TAG, "Credential Save: OK");
//                    showToast("Credential Save Success");
                } else {
                    Log.e(TAG, "Credential Save: NOT OK");
//                    showToast("Credential Save Failed");
                }

                mIsResolving = false;
                break;
        }
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
     * Called when the save button is clicked.  Reads the entries in the email and password
     * fields and attempts to save a new Credential to the Credentials API.
     */
    @Override
    public void saveCredentialsClicked(Credentials c) {
        String email = c.getEmail();
        String password = c.getPassword();

        // Create a Credential with the user's email as the ID and storing the password.  We
        // could also add 'Name' and 'ProfilePictureURL' but that is outside the scope of this
        // minimal sample.
        Log.d(TAG, "Saving Credential:" + email + ":" + password);
        final Credential credential = new Credential.Builder(email)
                .setPassword(password)
                .build();


        // NOTE: this method unconditionally saves the Credential built, even if all the fields
        // are blank or it is invalid in some other way.  In a real application you should contact
        // your app's back end and determine that the credential is valid before saving it to the
        // Credentials backend.
        onWaitFragmentInteractionShow();

        mCredentialsClient.save(credential).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            onWaitFragmentInteractionHide();
                            return;
                        }

                        Exception e = task.getException();
                        if (e instanceof ResolvableApiException) {
                            // The first time a credential is saved, the user is shown UI
                            // to confirm the action. This requires resolution.
                            ResolvableApiException rae = (ResolvableApiException) e;
                        } else {
                            // Save failure cannot be resolved.
                            Log.w(TAG, "Save failed.", e);
                            onWaitFragmentInteractionHide();
                        }
                    }
                });
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

        //Remove user's stored information upon successful authentication
        SharedPreferences prefs =
                getSharedPreferences(
                        getString(R.string.keys_shared_prefs_auth),
                        Context.MODE_PRIVATE);
        //remove the saved credentials from StoredPrefs
        prefs.edit().remove(getString(R.string.keys_prefs_password_auth)).apply();
        prefs.edit().remove(getString(R.string.keys_prefs_email_auth)).apply();
        prefs.edit().remove(getString(R.string.keys_prefs_password2_auth)).apply();
        prefs.edit().remove(getString(R.string.keys_prefs_username_auth)).apply();
        prefs.edit().remove(getString(R.string.keys_prefs_first_auth)).apply();
        prefs.edit().remove(getString(R.string.keys_prefs_last_auth)).apply();

        //Bundle the user information from input and send to login fragment.
        LoginFragment loginFragment;
        loginFragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putSerializable(getString(R.string.email_registerToLogin), credentials.getEmail());
        args.putSerializable(getString(R.string.password_registerToLogin), credentials.getPassword());
        loginFragment.setArguments(args);
        loadFragment(loginFragment);
    }

    @Override
    public void backToRegister(Credentials theCreds) {
        NewUserFragment newUser = new NewUserFragment();
        loadFragment(newUser);
    }

    @Override
    public void loginButtonActionAuth(Credentials credentials) {
        loadFragment(new LoginFragment());
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