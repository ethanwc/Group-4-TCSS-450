package ethanwc.tcss450.uw.edu.template.Messenger;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import ethanwc.tcss450.uw.edu.template.Messenger.ConnectionsFragment.OnConnectionListFragmentInteractionListener;
import ethanwc.tcss450.uw.edu.template.R;
import ethanwc.tcss450.uw.edu.template.model.Connection;

import java.util.Collections;
import java.util.List;

/**
 * Class which handles creating and populating the list for the recyclerview.
 */
public class MyConnectionsRecyclerViewAdapter extends RecyclerView.Adapter<MyConnectionsRecyclerViewAdapter.ViewHolder> {

    private List<Connection> mValues;
    private final OnConnectionListFragmentInteractionListener mListener;
    private int mExpandedPosition = -1;

    MyConnectionsRecyclerViewAdapter(List<Connection> items, OnConnectionListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    /**
     * OnCreateViewHolder which is used to instantiate the views of each menu item.
     * @param parent ViewGroup.
     * @param viewType int.
     * @return ViewHolder used to hold all the views to be used.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_connections, parent, false);

        return new ViewHolder(view);
    }

    /**
     * OnBindViewHolder used to instantiate textfields and create button listeners.
     * @param holder ViewHolder holding the views.
     * @param position Int position in the viewholder.
     */
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        //Sort and create a list of connections.
        Collections.sort(mValues);
        final boolean isExpanded = position==mExpandedPosition;
        holder.mItem = mValues.get(position);

        //Handle expanding card.
        holder.details.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.itemView.setActivated(isExpanded);

        if (isExpanded)
            mExpandedPosition = position;
        //Set textfields.
        holder.mUsernameView.setText(mValues.get(position).getUsername());
        holder.mEmailView.setText(mValues.get(position).getEmail());

        //Set listener on individual items.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1:position;
                notifyItemChanged(mExpandedPosition);
                notifyItemChanged(position);
            }
        });

        //Set listener on contact details button.
        holder.mDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onConnectionListFragmentInteraction(holder.mItem);

                }
            }
        });

        //Set listener on contact delete button.
        holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onConnectionListDeleteFragmentInteraction(holder.mItem);
                }
            }
        });

        //Set listener on send message button.
        holder.mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Need to handle
            }
        });
    }

    /**
     * Helper method used to return the size of the list.
     * @return Int used to represent the size of the list.
     */
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    /**
     * Internal class which holds the different views for each menu item.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mUsernameView;
        final TextView mEmailView;
        final LinearLayout details;
        final TextView mDetailsButton;
        final TextView mDeleteButton;
        final TextView mSendButton;
        Connection mItem;



        ViewHolder(View view) {
            //Link the views to variables.
            super(view);
            mView = view;
            details = view.findViewById(R.id.layout_connections_details);
            mDetailsButton = view.findViewById(R.id.textview_connections_details_detailsbutton);
            mDeleteButton = view.findViewById(R.id.textview_connections_details_deletebutton);
            mUsernameView = (TextView) view.findViewById(R.id.textview_connections_username);
            mEmailView = (TextView) view.findViewById(R.id.textview_connections_email);
            mSendButton = view.findViewById(R.id.textview_connections_details_messagebutton);



        }
    }
}
