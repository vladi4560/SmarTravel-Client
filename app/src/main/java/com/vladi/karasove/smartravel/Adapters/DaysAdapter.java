package com.vladi.karasove.smartravel.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.vladi.karasove.smartravel.CallBacks.CallBackItemClick;
import com.vladi.karasove.smartravel.Helpers.DataManger;
import com.vladi.karasove.smartravel.Objects.DayTrip;
import com.vladi.karasove.smartravel.R;

import java.util.List;

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.MyViewHolder> {

    private Context mContext;
    private List<DayTrip> dayTripList;
    private CallBackItemClick callBackItemClick;
    private DataManger dataManger = DataManger.getInstance();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DayTrip dayTrip = getItemPosition(getAdapterPosition());
                    dataManger.setCurrentDayTrip(dayTrip);
                    callBackItemClick.itemClick();
                }
            });
        }
    }

    public DaysAdapter(Context mContext, List<DayTrip> dayTripList,CallBackItemClick callBackItemClick) {
        this.mContext = mContext;
        this.dayTripList = dayTripList;
        this.callBackItemClick = callBackItemClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_card_day, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        DayTrip dayTrip = dayTripList.get(position);
        holder.title.setText(dayTrip.getTitle());
    }

    public DayTrip getItemPosition(int position){
        return dayTripList.get(position);
    }

    @Override
    public int getItemCount() {
        return dayTripList.size();
    }
}