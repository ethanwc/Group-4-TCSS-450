package ethanwc.tcss450.uw.edu.template.Messenger;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ethanwc.tcss450.uw.edu.template.R;
import ethanwc.tcss450.uw.edu.template.model.Message;

/**
 * Class which handles creating and populating the list for the recyclerview.
 */
public class MyConversationRecyclerViewAdapter extends RecyclerView.Adapter<MyConversationRecyclerViewAdapter.ViewHolder> {

    private List<Message> mValues;
    private final ConversationFragment.OnMessageListFragmentInteractionListener mListener;

    public MyConversationRecyclerViewAdapter(List<Message> items, ConversationFragment.OnMessageListFragmentInteractionListener listener) {
        mValues = items;
        if (null == mValues) {
            mValues = new ArrayList<>();
            mValues.add(new Message.Builder("NO MESSAGES LOADED").build());
        }
        mListener = listener;
    }

    /**
     * OnCreatViewHolder which is used to instantiate the vies of each menu item.
     * @param parent ViewGroup.
     * @param viewType int.
     * @return ViewHolder used to hold all the views to be used.
     */
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_conversation, parent, false);
        return new ViewHolder(view);
    }

    /**
     * OnBindViewHolder used to instantiate textfields and create button listener.
     * @param holder ViewHolder holding the views.
     * @param position Int position in the viewholder.
     */
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mUsers.setText(mValues.get(position).getUsers());
        holder.mMessage.setText(mValues.get(position).getMessage());
        holder.mMessage.setMovementMethod(new ScrollingMovementMethod());
        //Create listener for click on message.
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onMessageListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    /**
     * Helper method used to get the size of the list.
     * @return int used to represent the list size.
     */
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mUsers;
        Message mItem;
        final TextView mMessage;

        ViewHolder(View view) {
            super(view);
            mView = view;
//            mIdView = (TextView) view.findViewById(R.id.item_number);
            mUsers = view.findViewById(R.id.textview_conversation_users);
            mMessage = view.findViewById(R.id.textview_conversation_message);

        }
    }
}
