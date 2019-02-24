package ethanwc.tcss450.uw.edu.template.Messenger;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ethanwc.tcss450.uw.edu.template.Messenger.InvitationsFragment.OnListFragmentInteractionListener;
import ethanwc.tcss450.uw.edu.template.R;
import ethanwc.tcss450.uw.edu.template.dummy.DummyContent.DummyItem;
import ethanwc.tcss450.uw.edu.template.model.Connection;

import java.util.Collections;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyInvitationsRecyclerViewAdapter extends RecyclerView.Adapter<MyInvitationsRecyclerViewAdapter.ViewHolder> {

    private final List<Connection> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyInvitationsRecyclerViewAdapter(List<Connection> items, OnListFragmentInteractionListener listener) {
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

        holder.mEmailView.setText(mValues.get(position).getEmail());
//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    mListener.onListFragmentInteraction(holder.mItem);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mEmailView;
        Connection mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mEmailView = view.findViewById(R.id.textview_invitations_email);
        }
    }
}
