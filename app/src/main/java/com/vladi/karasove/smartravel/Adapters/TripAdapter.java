package com.vladi.karasove.smartravel.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.vladi.karasove.smartravel.CallBacks.CallBackItemClick;
import com.vladi.karasove.smartravel.Helpers.DataManger;
import com.vladi.karasove.smartravel.Objects.Trip;
import com.vladi.karasove.smartravel.R;
import com.vladi.karasove.smartravel.retrofit.Convertor;
import com.vladi.karasove.smartravel.retrofit.UserApi;
import com.vladi.karasove.smartravel.serverObjects.ActivityBoundary;
import com.vladi.karasove.smartravel.serverObjects.InstanceBoundary;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.MyViewHolder> {

    private Context mContext;
    private List<Trip> tripList;
    private CallBackItemClick callBackItemClick;
    private DataManger dataManger = DataManger.getInstance();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count, startDate, endDate;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            startDate = (TextView) view.findViewById(R.id.startDate);
            endDate = (TextView) view.findViewById(R.id.endDate);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }

    public TripAdapter(Context mContext, List<Trip> tripList, CallBackItemClick callBackItemClick) {
        this.mContext = mContext;
        this.tripList = tripList;
        this.callBackItemClick = callBackItemClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_card_trip, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Trip trip = tripList.get(position);
        holder.title.setText(trip.getName());
        holder.count.setText(trip.getNumOfDays() + " Days");
        holder.startDate.setText(trip.getStartDate());
        holder.endDate.setText(" - " + trip.getEndDate());

        // loading album cover using Glide library
        Glide.with(mContext).load(trip.getThumbnail()).into(holder.thumbnail);

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataManger.getCurrentTrip().setName(trip.getName());
                dataManger.getCurrentTrip().setNumOfDays(trip.getNumOfDays());
                dataManger.getCurrentTrip().setDayTripList(trip.getDayTripList());
                dataManger.getCurrentTrip().setThumbnail(trip.getThumbnail());
                dataManger.getCurrentTrip().setStartDate(trip.getStartDate());
                dataManger.getCurrentTrip().setEndDate(trip.getEndDate());
                dataManger.getCurrentTrip().setIsRate(trip.getIsRate());
                callBackItemClick.itemClick();

            }
        });

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow, trip);
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view, Trip trip) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_trip, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(trip));
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        Trip trip;

        public MyMenuItemClickListener(Trip trip) {
            this.trip = trip;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_delete_trip:
                    Toast.makeText(mContext, "delete", Toast.LENGTH_SHORT).show();
                    new MaterialAlertDialogBuilder(mContext)
                            .setTitle("Delete Message")
                            .setMessage("Are you sure you want to delete the " + trip.getName() + " trip?")
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    for (Trip t : dataManger.getUpcomingTripList()) {
                                        if (trip.getId() == t.getId()) {
                                            Log.d("roman", "delete trip " + t.getName() + dataManger.getUpcomingTripList().indexOf(t));
                                            createActivityBoundary(t);
                                            callBackItemClick.itemDelete(dataManger.getUpcomingTripList().indexOf(t));
                                            dataManger.getUpcomingTripList().remove(t);
                                            break;
                                        }
                                    }

                                }
                            })
                            .show();
                    return true;
                default:
            }
            return false;
        }
    }

    private void updateTripInstance(Trip trip) {
        InstanceBoundary instanceBoundary = Convertor.convertTripToInstanceBoundary(trip);
        instanceBoundary.setActive(false);
        UserApi userApi = dataManger.getRetrofitService().getRetrofit().create(UserApi.class);
        userApi.updateInstanceById(instanceBoundary, dataManger.getCurrentUser().getDomain(), dataManger.getMyInstances().get("trip" + trip.getId()), DataManger.CLIENT_MANAGER_DOMAIN, DataManger.CLIENT_MANAGER_EMAIL)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.d("roman", "delete trip " + trip.getId());
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
    }

    private void createActivityBoundary(Trip t) {
        ActivityBoundary activityBoundary = Convertor.convertToActivityBoundary(dataManger.getCurrentUser().getDomain(), dataManger.getMyInstances().get("trip" + t.getId()), dataManger.getCurrentUser().getDomain(), dataManger.getCurrentUser().getEmail(), "deleteTrip");
        Log.d("roman", "activityB:" + activityBoundary.toString());
        UserApi userApi = dataManger.getRetrofitService().getRetrofit().create(UserApi.class);
        userApi.createActivity(activityBoundary)
                .enqueue(new Callback<ActivityBoundary>() {
                    @Override
                    public void onResponse(Call<ActivityBoundary> call, Response<ActivityBoundary> response) {
                        updateTripInstance(t);
                        Log.d("roman", "delete trip ");
                    }

                    @Override
                    public void onFailure(Call<ActivityBoundary> call, Throwable t) {

                    }
                });
    }


    @Override
    public int getItemCount() {
        return tripList.size();
    }
}