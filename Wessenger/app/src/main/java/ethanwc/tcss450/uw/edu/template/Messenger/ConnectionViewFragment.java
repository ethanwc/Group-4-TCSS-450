package ethanwc.tcss450.uw.edu.template.Messenger;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ethanwc.tcss450.uw.edu.template.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectionViewFragment extends Fragment {


    public ConnectionViewFragment() {
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
        return inflater.inflate(R.layout.fragment_connection_view, container, false);
    }

    /**
     * OnStart used to populate the textview fields with the information in the arguments.
     */
    @Override
    public void onStart() {
        super.onStart();
        if(getArguments() != null) {
            String email = getArguments().getString("email");
            String username = getArguments().getString("username");
            String first = getArguments().getString("first");
            String last = getArguments().getString("last");

            TextView tView = getActivity().findViewById(R.id.textview_connectionview_email);
            tView.setText(email);

            tView = getActivity().findViewById(R.id.textview_connectionview_username);
            tView.setText(username);

            tView = getActivity().findViewById(R.id.textview_connectionview_firstname);
            tView.setText(first);

            tView = getActivity().findViewById(R.id.textview_connectionview_lastname);
            tView.setText(last);

        }
    }
}
