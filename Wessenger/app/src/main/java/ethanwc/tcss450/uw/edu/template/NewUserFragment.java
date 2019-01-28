package ethanwc.tcss450.uw.edu.template;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewUserFragment extends Fragment {
    private OnNewUserFragmentButtonAction mListener;
    public NewUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new_user, container, false);
        TextView txtLoginClick = v.findViewById(R.id.textview_newuser_login);
        txtLoginClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onNewUserButtonAction(v);
            }
        });

        Button btnRegister = v.findViewById(R.id.button_newuser_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onNewUserButtonAction(v);
            }
        });
        return v;
    }

    /**
//     * These buttons are to handle the button click of New USer.
//     * It handles for Register and Login
//     * @param btnclicked
//     */
//    @Override
//    public void onClick(View btnclicked) {
////        if(v.getId() == R.id.btn_newUser_LogIn){
////            Intent intent = new Intent(NewUserFragment.this, MainActivity.class);
////        }else {
//        System.out.println("---btn clicked-0000000");
//            mListener.onNewUserButtonAction(btnclicked);
////        }
//    }
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
        void onNewUserButtonAction(View btn);
    }
}
