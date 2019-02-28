package ethanwc.tcss450.uw.edu.template.temp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ethanwc.tcss450.uw.edu.template.Connections.SendPostAsyncTask;
import ethanwc.tcss450.uw.edu.template.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatFragment2.OnChatFragmentInteraction} interface
 * to handle interaction events.
 * Use the {@link ChatFragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment2 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText mMessageInputEditText;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mEmail;
    private String mJwToken;
    private String mSendUrl;
    private String mChatID;
    ArrayList<ChatModel> list;
    private OnChatFragmentInteraction mListener;

    public ChatFragment2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment2 newInstance(String param1, String param2) {
        ChatFragment2 fragment = new ChatFragment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        list.add(new ChatModel(ChatModel.IMAGE_TYPE,"HI.I.AM.AN.IMAGE.WITH.TEXT",
//                R.drawable.ic_mail_outline_black_24dp));


    }

    public void finalizeChat() {

        mMessageInputEditText = getView().findViewById(R.id.edit_chat_message_input);

        MultiViewTypeAdapter adapter = new MultiViewTypeAdapter(list, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), OrientationHelper.VERTICAL, true);

        RecyclerView mRecyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        //Store url in variable.
        mSendUrl = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_messaging_base))
                .appendPath(getString(R.string.ep_messaging_send))
                .build()
                .toString();


        //Store arguments in variables.



    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        if (getArguments() != null) {
            mEmail = getArguments().getString("email_token_123");
            mJwToken = getArguments().getString("jwt_token");
            mChatID = getArguments().getString("chat_id");
        }
        list = new ArrayList();

        setChatHistory();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_fragment2, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onChatFragmentInteraction(uri);
            //if image...open it?
        }
    }

    /**
     * Helper method used to handle sending a message.
     * @param theButton Button that was clicked to send message.
     */
    private void handleSendClick(final View theButton) {
        String msg = mMessageInputEditText.getText().toString();
        JSONObject messageJson = new JSONObject();
        //Build message for web service.
        try {
            messageJson.put("email", mEmail);
            messageJson.put("message", msg);
            messageJson.put("chatId", mChatID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Send message to web service.
        new SendPostAsyncTask.Builder(mSendUrl, messageJson)
                .onPostExecute(this::endOfSendMsgTask)
                .onCancelled(error -> Log.e("ERROR", error))
                .addHeaderField("authorization", mJwToken)
                .build().execute();
    }

    private void setChatHistory() {
        String getAll = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_messaging_base))
                .appendPath(getString(R.string.ep_messaging_getAll))
                .build()
                .toString();
        JSONObject messageJson = new JSONObject();
        //Build message for web service.
        try {
            messageJson.put("chatId", mChatID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new SendPostAsyncTask.Builder(getAll, messageJson)
                .onPostExecute(this::getAllHistory)
                .onCancelled(error -> Log.e("ERROR", error))
                .addHeaderField("authorization", mJwToken)
                .build().execute();
    }

    /**
     * Helper method used to clear the input field.
     * @param result JSON object returned from the web service.
     */
    private void endOfSendMsgTask(final String result) {
        try {
            //This is the result from the web service
            JSONObject res = new JSONObject(result);
            if(res.has("success") && res.getBoolean("success")) {
                //The web service got our message. Time to clear out the input EditText
                mMessageInputEditText.setText("");
                //its up to you to decide if you want to send the message to the output here
                //or wait for the message to come back from the web service.
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //output the chat history result
    private void getAllHistory(final String result) {
        try {
            //This is the result from the web service
            JSONObject res = new JSONObject(result);
            if(res.has("messages")) {
                JSONArray chatHistoryArray = res.getJSONArray("messages");
                //Log.e("history: ", "  " + res.get("messages"));
                for (int i = chatHistoryArray.length() -1; i >= 0 ; i--) {
                    Log.e("MESSAGEIS" ," " + chatHistoryArray.getString(i));
                    int data = chatHistoryArray.getJSONObject(i).getString("email").equals(mEmail) ? 1 : 0;
                    list.add(new ChatModel(ChatModel.TEXT_TYPE, chatHistoryArray.getJSONObject(i).getString("messages"), data));
                }
                finalizeChat();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnChatFragmentInteraction) {
            mListener = (OnChatFragmentInteraction) context;
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnChatFragmentInteraction {
        // TODO: Update argument type and name
        void onChatFragmentInteraction(Uri uri);
    }
}
