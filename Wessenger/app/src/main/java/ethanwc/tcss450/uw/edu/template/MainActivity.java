package ethanwc.tcss450.uw.edu.template;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NewUserFragment.OnNewUserFragmentButtonAction {

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

    /**
     * These buttons are to handle the button click of Main Login Fragment.
     * It handles for Sign in and New User
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (R.id.login_button_login == v.getId()) {
        MessagingHomeActivity messagingHome;
        messagingHome= new MessagingHomeActivity();

            Intent intent = new Intent(MainActivity.this, MessagingHomeActivity.class);
            startActivity(intent);


        } else if (v.getId() == R.id.login_textView_newUser) {
            NewUserFragment newUser = new NewUserFragment();
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction().replace(R.id.activity_main_container, newUser).addToBackStack(null);
            transaction.commit();

        }
    }


    @Override
    public void onNewUserButtonAction(View btn) {

        if(btn.getId() ==R.id.btn_newUser_LogIn ){
            System.out.println("btn Login");
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
//            MainActivity mainA = new MainActivity();
//            FragmentTransaction transaction;
//            transaction = getSupportFragmentManager()
//                    .beginTransaction().replace(R.id.activity_main_container, mainA).addToBackStack(null);
//            transaction.commit();
        } else if (btn.getId() == R.id.btn_newUser_Register){
            AuthenticationFragment authentication = new AuthenticationFragment();
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction().replace(R.id.activity_main_container, authentication).addToBackStack(null);
            transaction.commit();

        }
    }
}
