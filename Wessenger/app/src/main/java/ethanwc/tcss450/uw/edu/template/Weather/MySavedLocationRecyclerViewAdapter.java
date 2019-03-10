
package ethanwc.tcss450.uw.edu.template.Weather;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ethanwc.tcss450.uw.edu.template.R;
import ethanwc.tcss450.uw.edu.template.Weather.SavedLocationFragment.OnListFragmentInteractionListener;
//import ethanwc.tcss450.uw.edu.template.dummy.DummyContent.HourlyWeather;
import ethanwc.tcss450.uw.edu.template.model.location;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link location} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MySavedLocationRecyclerViewAdapter extends RecyclerView.Adapter<MySavedLocationRecyclerViewAdapter.ViewHolder> {

    private final List<location> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MySavedLocationRecyclerViewAdapter(List<location> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        System.out.println("from my saved location recyclerview---" + items.size());
        mListener = listener;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_savedlocation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = (location) mValues.get(position);
        holder.mSavedLocation.setText( mValues.get(position).getNickname() );
//        holder.mIdView.setText(mValues.get(position).id);
//        holder.mContentView.setText(mValues.get(position).content);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    System.out.println("view clicked");
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
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
        //public final TextView mIdView;
        public final TextView mSavedLocation;
        location mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            //mIdView = (TextView) view.findViewById(R.id.item_number);
            mSavedLocation = view.findViewById(R.id.textview_savedlocation_prompt);
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mSavedLocation.getText() + "'";
        }
    }
}
