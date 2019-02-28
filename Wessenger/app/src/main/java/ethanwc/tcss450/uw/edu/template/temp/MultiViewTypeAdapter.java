package ethanwc.tcss450.uw.edu.template.temp;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ethanwc.tcss450.uw.edu.template.R;

/**
 * Created by anupamchugh on 09/02/16.
 */
public class MultiViewTypeAdapter extends RecyclerView.Adapter {

    private ArrayList<ChatModel>dataSet;
    Context mContext;
    int total_types;

    public static class TextTypeViewHolder extends RecyclerView.ViewHolder {

        TextView txtType;
        CardView cardView;

        public TextTypeViewHolder(View itemView) {
            super(itemView);

            this.txtType = (TextView) itemView.findViewById(R.id.type);
            this.cardView = (CardView) itemView.findViewById(R.id.card_view);
        }



    }

    public static class ImageTypeViewHolder extends RecyclerView.ViewHolder {

        TextView txtType;
        ImageView image;

        public ImageTypeViewHolder(View itemView) {
            super(itemView);

            this.txtType = (TextView) itemView.findViewById(R.id.type);
            this.image = (ImageView) itemView.findViewById(R.id.background);
        }
    }


    public MultiViewTypeAdapter(ArrayList<ChatModel>data, Context context) {
        this.dataSet = data;
        this.mContext = context;
        total_types = dataSet.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case ChatModel.TEXT_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_type, parent, false);
                return new TextTypeViewHolder(view);
            case ChatModel.IMAGE_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_type, parent, false);
                return new ImageTypeViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {

        switch (dataSet.get(position).type) {
            case 0:
                return ChatModel.TEXT_TYPE;
            case 1:
                return ChatModel.IMAGE_TYPE;
            default:
                return -1;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        ChatModel object = dataSet.get(listPosition);
        if (object != null) {
            switch (object.type) {
                case ChatModel.TEXT_TYPE:
                   ((TextTypeViewHolder) holder).txtType.setText(object.text);


                    if (object.data == 1)  ((TextTypeViewHolder) holder).txtType.setGravity(Gravity.RIGHT);
                    else  ((TextTypeViewHolder) holder).txtType.setGravity(Gravity.LEFT);

                    break;
                case ChatModel.IMAGE_TYPE:
                    ((ImageTypeViewHolder) holder).txtType.setText(object.text);
                    ((ImageTypeViewHolder) holder).image.setImageResource(object.data);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}