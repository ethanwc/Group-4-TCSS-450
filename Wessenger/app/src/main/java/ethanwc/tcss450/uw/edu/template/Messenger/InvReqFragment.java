package ethanwc.tcss450.uw.edu.template.Messenger;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ethanwc.tcss450.uw.edu.template.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InvReqFragment extends Fragment {


    public InvReqFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inv_req, container, false);
    }

}
