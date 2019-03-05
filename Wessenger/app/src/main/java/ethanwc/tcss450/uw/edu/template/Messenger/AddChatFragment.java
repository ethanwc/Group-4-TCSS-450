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
import ethanwc.tcss450.uw.edu.template.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddChatFragment extends Fragment {

    private OnAddChatFragmentAction mListener;
    private EditText mChatName;

    public AddChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_chat, container, false);
    }


    /**
     * OnAttach used to check whether the correct listeners have been implemented.
     * @param context Context of the current ui situation.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddChatFragment.OnAddChatFragmentAction) {
            mListener = (AddChatFragment.OnAddChatFragmentAction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnHomeFragmentInteractionListener");
        }
    }

    public interface OnAddChatFragmentAction extends WaitFragment.OnFragmentInteractionListener{
        void addChat(String chatName);
    }

    @Override
    public void onStart() {
        super.onStart();

        mChatName = getActivity().findViewById(R.id.edittext_addchat_email);

        Button addButton = getActivity().findViewById(R.id.button_addchat_add);
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mChatName.getText().toString().isEmpty()) {
                    mChatName.setError("Please enter a chat name.");
                } else {
                    mListener.addChat(mChatName.getText().toString());

                }
            }
        });
    }


}
