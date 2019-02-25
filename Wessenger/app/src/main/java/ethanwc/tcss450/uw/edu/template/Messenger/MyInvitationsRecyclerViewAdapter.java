package ethanwc.tcss450.uw.edu.template.Messenger;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ethanwc.tcss450.uw.edu.template.Messenger.InvitationsFragment.OnInvitationListFragmentInteractionListener;
import ethanwc.tcss450.uw.edu.template.R;
import ethanwc.tcss450.uw.edu.template.dummy.DummyContent.DummyItem;
import ethanwc.tcss450.uw.edu.template.model.Connection;

import java.util.Collections;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnInvitationListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyInvitationsRecyclerViewAdapter extends RecyclerView.Adapter<MyInvitationsRecyclerViewAdapter.ViewHolder> {

    private final List<Connection> mValues;
    private final OnInvitationListFragmentInteractionListener mListener;

    public MyInvitationsRecyclerViewAdapter(List<Connection> items, OnInvitationListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    /**
     * OnCreateViewHolder which is used to instantiate the views of each menu item.
     * @param parent ViewGroup.
     * @param viewType int.
     * @return ViewHolder used to hold all the views to be used.
     */
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_invitations, parent, false);
        return new ViewHolder(view);
    }

    /**
     * OnBindViewHolder used to instantiate textfields and create button listeners.
     * @param holder ViewHolder holding the views.
     * @param position Int position in the viewholder.
     */
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        //holder.mIdView.setText(mValues.get(position).id);
        //holder.mContentView.setText(mValues.get(position).content);
        Collections.sort(mValues);
        holder.mItem = mValues.get(position);

        holder.mAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onInvitationAcceptFragmentInteraction(holder.mItem);
                }
            }
        });

        holder.mDeclineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onInvitationDeclineFragmentInteraction(holder.mItem);
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
        final TextView mEmailView;
        final Button mAcceptButton;
        final Button mDeclineButton;
        Connection mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mAcceptButton = view.findViewById(R.id.button_invitations_accept);
            mDeclineButton = view.findViewById(R.id.button_invitations_cancel);
            mEmailView = view.findViewById(R.id.textview_invitations_email);
        }
    }
}
