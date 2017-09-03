package com.deputyshift.UI.Adpater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.deputyshift.R;
import com.deputyshift.UI.Model.ShiftDetailsUIModel;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private int listItemLayout;
    private ArrayList<ShiftDetailsUIModel> mListShiftDetailUIModels;
    Context mContext;
    private final OnItemClickListner listener;

    public RecyclerAdapter(Context context, int layoutId, ArrayList<ShiftDetailsUIModel> itemList, OnItemClickListner listener) {
        listItemLayout = layoutId;
        this.mListShiftDetailUIModels = itemList;
        this.mContext = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(listItemLayout, parent, false);
        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
        TextView ShiftStartTime = holder.mShiftStartTime;
        TextView ShiftEndTime = holder.mShiftEndTime;
        TextView startLat = holder.mStartLat;
        TextView startLong = holder.mStartLong;
        TextView endLat = holder.mEndLat;
        TextView endlong = holder.mEndLong;
        TextView mShiftId = holder.mShiftId;

        ShiftStartTime.setText(!(mListShiftDetailUIModels.get(position).getShiftStartTime()).isEmpty()
                                ? formatDisplayDate(mListShiftDetailUIModels.get(position).getShiftStartTime()):"NA");

        ShiftEndTime.setText((mListShiftDetailUIModels.get(position).getShiftEndTime()) != null
                ? formatDisplayDate(mListShiftDetailUIModels.get(position).getShiftEndTime()):"Shift is in Progress");

        startLat.setText(""+mListShiftDetailUIModels.get(position).getShiftStartLat());
        startLong.setText(""+mListShiftDetailUIModels.get(position).getShiftStartLon());

        endLat.setText(""+mListShiftDetailUIModels.get(position).getShiftEndLat());
        endlong.setText(""+mListShiftDetailUIModels.get(position).getShiftEndLon());


        mShiftId.setText(""+mListShiftDetailUIModels.get(position).getShiftId());

        Picasso.with(mContext).load(mListShiftDetailUIModels.get(position).getImage()).into(holder.mImage);
        holder.bind(mListShiftDetailUIModels.get(position), position, listener);
    }

    @Override
    public int getItemCount() {
        return mListShiftDetailUIModels == null ? 0 : mListShiftDetailUIModels.size();
    }

    private String formatDisplayDate(String date) {
        DateFormat df1 = new SimpleDateFormat("dd-MM-yy'T'HH:mm");
        try {
            Date finalResult = df1.parse(date);
            String finaldate = df1.format(finalResult);
            Log.d("rakesh","finaldate =" +finaldate);
            return finaldate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    static class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView mShiftStartTime;
        public TextView mShiftEndTime;

        public TextView mStartLat;
        public TextView mStartLong;

        public TextView mEndLat;
        public TextView mEndLong;
        public TextView mShiftId;
        public ImageView mImage;

        public ViewHolder(View itemView) {
            super(itemView);

            mImage = (ImageView) itemView.findViewById(R.id.shiftimage);
            mShiftId = (TextView) itemView.findViewById(R.id.shift_id);
            mShiftStartTime = (TextView) itemView.findViewById(R.id.shiftstarttime);
            mShiftEndTime = (TextView) itemView.findViewById(R.id.shiftendtime);
            mStartLat = (TextView) itemView.findViewById(R.id.startLat);
            mStartLong = (TextView) itemView.findViewById(R.id.startLong);

            mEndLat = (TextView) itemView.findViewById(R.id.endLat);
            mEndLong = (TextView) itemView.findViewById(R.id.endLong);

        }

        public void bind(final ShiftDetailsUIModel item, final int position, final OnItemClickListner listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, position);
                }
            });
        }


    }

    public interface OnItemClickListner {
        void onItemClick(ShiftDetailsUIModel item, int position);
    }
}
