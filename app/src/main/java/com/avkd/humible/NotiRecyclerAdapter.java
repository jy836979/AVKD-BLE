package com.avkd.humible;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NotiRecyclerAdapter extends RecyclerView.Adapter<NotiRecyclerAdapter.ViewHolder> {

    Context mContext;
    List<Notification> items;

    public NotiRecyclerAdapter(Context context, List<Notification> items) {
        this.mContext = context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Notification item = items.get(position);
        holder.tv_device.setText(item.getDeviceName());
        holder.tv_ago.setText(item.getAgo());
        holder.tv_message.setText(item.getMessage());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_device;
        TextView tv_ago;
        TextView tv_message;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_device = itemView.findViewById(R.id.tv_device);
            tv_ago = itemView.findViewById(R.id.tv_ago);
            tv_message = itemView.findViewById(R.id.tv_message);
        }
    }

}
