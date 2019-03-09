
package ethanwc.tcss450.uw.edu.template.Weather;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ethanwc.tcss450.uw.edu.template.R;
import ethanwc.tcss450.uw.edu.template.Weather.SavedLocationFragment.OnLocationListFragmentInteractionListener;
//import ethanwc.tcss450.uw.edu.template.dummy.DummyContent.DummyItem;
import ethanwc.tcss450.uw.edu.template.model.location;

import java.util.Collections;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link location} and makes a call to the
 * specified {@link OnLocationListFragmentInteractionListener}.
 */
public class MySavedLocationRecyclerViewAdapter extends RecyclerView.Adapter<MySavedLocationRecyclerViewAdapter.ViewHolder> {

    private final List<location> mValues;
    private final OnLocationListFragmentInteractionListener mListener;

    public MySavedLocationRecyclerViewAdapter(List<location> items, OnLocationListFragmentInteractionListener listener) {
        mValues = items;
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


    }

    @Override
    public int getItemCount() {

        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        //public final TextView mIdView;
        public final TextView mCity;
        public final TextView mState;
        public final TextView mZip;
        location mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            //mIdView = (TextView) view.findViewById(R.id.item_number);
            mCity = view.findViewById(R.id.textview_locations_city);
            mState = view.findViewById(R.id.textview_locations_state);
            mZip = view.findViewById(R.id.textview_locations_zip);

        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mZip.getText() + "'";
        }
    }
}
