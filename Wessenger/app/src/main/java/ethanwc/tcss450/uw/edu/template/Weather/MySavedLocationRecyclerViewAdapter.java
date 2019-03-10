
package ethanwc.tcss450.uw.edu.template.Weather;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import ethanwc.tcss450.uw.edu.template.model.location;
import ethanwc.tcss450.uw.edu.template.R;
import ethanwc.tcss450.uw.edu.template.Weather.SavedLocationFragment.OnLocationListFragmentInteractionListener;
//import DummyContent.DummyItem;

import java.util.Collections;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link location} and makes a call to the
 * specified {@link OnLocationListFragmentInteractionListener}.
 */
public class MySavedLocationRecyclerViewAdapter extends RecyclerView.Adapter<MySavedLocationRecyclerViewAdapter.ViewHolder> {

    private final List<location> mValues;
    private final OnLocationListFragmentInteractionListener mListener;
    private final boolean mChange;

    public MySavedLocationRecyclerViewAdapter(List<location> items, OnLocationListFragmentInteractionListener listener, boolean change) {
        mValues = items;
        mListener = listener;
        mChange = change;
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

        Collections.sort(mValues);
        holder.mItem = (location) mValues.get(position);
        holder.mCity.setText( mValues.get(position).getNickname() );
//        holder.mState.setText( mValues.get(position).getState() );
        holder.mZip.setText( mValues.get(position).getZip() );



        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {

                    mListener.onLocationListFragmentInteraction(holder.mItem);
                }
            }
        });

        holder.mRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onLocationListRemoveFragmentInteraction(holder.mItem);
                }
            }
        });

        if (mChange) {
            holder.mRemove.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {

        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        //public final TextView mIdView;
        public final TextView mCity;
        public final TextView mZip;
        public ImageButton mRemove;
        location mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            //mIdView = (TextView) view.findViewById(R.id.item_number);
            mCity = view.findViewById(R.id.textview_locations_city);

            mZip = view.findViewById(R.id.textview_locations_zip);
            mRemove = view.findViewById(R.id.button_location_remove);

        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mZip.getText() + "'";
        }
    }
}
