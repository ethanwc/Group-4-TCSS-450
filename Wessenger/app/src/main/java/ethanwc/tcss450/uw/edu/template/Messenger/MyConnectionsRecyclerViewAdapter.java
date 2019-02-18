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

import java.util.List;

/**
 * specified {@link OnConnectionListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyConnectionsRecyclerViewAdapter extends RecyclerView.Adapter<MyConnectionsRecyclerViewAdapter.ViewHolder> {

    private final List<Connection> mValues;
    private final OnConnectionListFragmentInteractionListener mListener;
    private int mExpandedPosition = -1;

    MyConnectionsRecyclerViewAdapter(List<Connection> items, OnConnectionListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_connections, parent, false);
        View view2 = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_connections, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {


        final boolean isExpanded = position==mExpandedPosition;
        holder.mItem = mValues.get(position);

        holder.details.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.itemView.setActivated(isExpanded);

        if (isExpanded)
            mExpandedPosition = position;
        holder.mUsernameView.setText(mValues.get(position).getUsername());
        holder.mEmailView.setText(mValues.get(position).getEmail());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1:position;
                notifyItemChanged(mExpandedPosition);
                notifyItemChanged(position);
            }
        });

        holder.mDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onConnectionListFragmentInteraction(holder.mItem);

                }
            }
        });

        holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mUsernameView;
        final TextView mEmailView;
        final LinearLayout details;
        final TextView mDetailsButton;
        final TextView mDeleteButton;
        Connection mItem;



        ViewHolder(View view) {
            super(view);
            mView = view;
            details = view.findViewById(R.id.layout_connections_details);
            mDetailsButton = view.findViewById(R.id.textview_connections_details_detailsbutton);
            mDeleteButton = view.findViewById(R.id.textview_connections_details_deletebutton);
            mUsernameView = (TextView) view.findViewById(R.id.textview_connections_email);
            mEmailView = (TextView) view.findViewById(R.id.textview_connections_username);


        }
    }
}
