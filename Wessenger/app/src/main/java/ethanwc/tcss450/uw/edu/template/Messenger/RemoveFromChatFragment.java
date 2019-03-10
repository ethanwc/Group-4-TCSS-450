package ethanwc.tcss450.uw.edu.template.Messenger;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ethanwc.tcss450.uw.edu.template.Main.WaitFragment;
import ethanwc.tcss450.uw.edu.template.model.Credentials;
import ethanwc.tcss450.uw.edu.template.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RemoveFromChatFragment extends Fragment {

    private OnRemoveFromChatFragmentAction mListener;
    private EditText mEmail;


    public RemoveFromChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_to_chat, container, false);
    }

    /**
     * OnAttach used to check whether the correct listeners have been implemented.
     * @param context Context of the current ui situation.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RemoveFromChatFragment.OnRemoveFromChatFragmentAction) {
            mListener = (RemoveFromChatFragment.OnRemoveFromChatFragmentAction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnHomeFragmentInteractionListener");
        }
    }

    public interface OnRemoveFromChatFragmentAction extends WaitFragment.OnFragmentInteractionListener {
        void removeFromChat(Credentials credentials);
    }

    @Override
    public void onStart() {
        super.onStart();

        mEmail = getActivity().findViewById(R.id.edittext_addtochat_email);

        Button addButton = getActivity().findViewById(R.id.button_addtochat_add);
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mEmail.getText().toString().isEmpty() || !mEmail.getText().toString().contains("@")) {
                    mEmail.setError("Please enter a valid email.");
                } else {
                    String chatId = getArguments().getString("chatid");
                    mListener.removeFromChat(new Credentials.Builder(mEmail.getText().toString()).addChatId(chatId).build());

                }
            }
        });
    }

}
