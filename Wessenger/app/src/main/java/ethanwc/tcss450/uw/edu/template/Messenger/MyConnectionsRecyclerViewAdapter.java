package ethanwc.tcss450.uw.edu.template.Messenger;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    MyConnectionsRecyclerViewAdapter(List<Connection> items, OnConnectionListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_connections, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mUsernameView.setText(mValues.get(position).getUsername());
        holder.mEmailView.setText(mValues.get(position).getEmail());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onConnectionListFragmentInteraction(holder.mItem);
                }
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
        Connection mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mUsernameView = (TextView) view.findViewById(R.id.textview_connections_email);
            mEmailView = (TextView) view.findViewById(R.id.textview_connections_username);
        }
    }
}
