package ethanwc.tcss450.uw.edu.template.Messenger;


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

import ethanwc.tcss450.uw.edu.template.Main.LoginFragment;
import ethanwc.tcss450.uw.edu.template.Main.WaitFragment;
import ethanwc.tcss450.uw.edu.template.R;
import ethanwc.tcss450.uw.edu.template.model.Credentials;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddContactFragment extends WaitFragment {
    private OnNewContactFragmentButtonAction mListener;
    private EditText mEmail;

    public AddContactFragment() {
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
        return inflater.inflate(R.layout.fragment_add_contact, container, false);
    }

    /**
     * OnAttach used to check whether the correct listeners have been implemented.
     * @param context Context of the current ui situation.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddContactFragment.OnNewContactFragmentButtonAction) {
            mListener = (AddContactFragment.OnNewContactFragmentButtonAction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * OnStart used to populate the textview fields with the information in the arguments.
     */
    @Override
    public void onStart() {
        super.onStart();
        mEmail = getActivity().findViewById(R.id.edittext_newcontact_email);

        Button addButton = getActivity().findViewById(R.id.button_newcontact_add);
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mEmail.getText().toString().isEmpty() || !mEmail.getText().toString().contains("@")) {
                    mEmail.setError("Please enter a valid email.");
                } else {
                    mListener.addContactButton(new Credentials.Builder(mEmail.getText().toString())
                            .addEmail2(mEmail.getText().toString())
                            .addVerify(1).build());

                }
            }
        });



    }


    public interface OnNewContactFragmentButtonAction extends WaitFragment.OnFragmentInteractionListener{

        void addContactButton(Credentials credentials);


    }
}
