package com.example.dropshippingcourier;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.widget.ImageView;

public class ParcelAdapter extends RecyclerView.Adapter<ParcelAdapter.ViewHolder> {
    private List<Parcel> items;

    public ParcelAdapter(List<Parcel> items) {
        this.items = items;
    }
    public void updateList(List<Parcel> newList) {
        items = newList;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_parcel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Parcel item = items.get(position); // Use existing 'items' list

        // Bind data to views
        holder.tvTrackingNumber.setText(item.getTrackingNumber());
        holder.tvStatus.setText(item.getStatus());
        holder.tvScanTime.setText(item.getScanTime());
        holder.tvStoreName.setText(item.getStoreName());
        holder.tvStoreAddress.setText(item.getStoreAddress());
        holder.tvCustomerName.setText(item.getCustomerName());
        holder.tvCustomerAddress.setText(item.getCustomerAddress());
        holder.tvEstimatedDelivery.setText(item.getEstimatedDelivery());
        holder.tvParcelDetails.setText(item.getItemCount() + " items, " + item.getWeight());

        // Update status background color
        int statusColor = getStatusColor(item.getStatus());
        GradientDrawable statusBg = (GradientDrawable) holder.tvStatus.getBackground().mutate();
        statusBg.setColor(statusColor);
        holder.accordionHeader.setOnClickListener(v -> {
            if (holder.eventsContainer.getVisibility() == View.VISIBLE) {
                holder.eventsContainer.setVisibility(View.GONE);
                holder.ivExpandCollapse.setImageResource(R.drawable.ic_expand_more);
            } else {
                holder.eventsContainer.setVisibility(View.VISIBLE);
                holder.ivExpandCollapse.setImageResource(R.drawable.ic_expand_less);
                populateScanEvents(holder, item.getScanEvents());
            }
        });

        // Set latest event
        if (item.getScanEvents() != null && !item.getScanEvents().isEmpty()) {
            ScanEvent latest = item.getScanEvents().get(0);
            holder.tvLatestEvent.setText(latest.getLocation() + ", " + latest.getTime());
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void populateScanEvents(ViewHolder holder, List<ScanEvent> events) {
        // Clear existing views
        holder.eventsContainer.removeAllViews();

        // Skip first event since it's already shown in the header
        for (int i = 1; i < events.size(); i++) {
            ScanEvent event = events.get(i);

            LayoutInflater inflater = LayoutInflater.from(holder.itemView.getContext());
            View eventView = inflater.inflate(R.layout.item_scan_event, holder.eventsContainer, false);

            TextView tvTime = eventView.findViewById(R.id.tv_event_time);
            TextView tvLocation = eventView.findViewById(R.id.tv_event_location);
            TextView tvDescription = eventView.findViewById(R.id.tv_event_description);

            tvTime.setText(event.getTime());
            tvLocation.setText(event.getLocation());
            tvDescription.setText(event.getDescription());

            holder.eventsContainer.addView(eventView);
        }
    }

    private int getStatusColor(String status) {
        switch (status.toLowerCase()) {
            case "pending": return Color.parseColor("#FF9800");
            case "in transit": return Color.parseColor("#2196F3");
            case "out for delivery": return Color.parseColor("#FF5722");
            case "delivered": return Color.parseColor("#4CAF50");
            default: return Color.parseColor("#9E9E9E");
        }
    }



    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvTrackingNumber;
        final TextView tvStatus;
        final TextView tvScanTime;
        final TextView tvStoreName;
        final TextView tvStoreAddress;
        final TextView tvCustomerName;
        final TextView tvCustomerAddress;
        final TextView tvEstimatedDelivery;
        final TextView tvParcelDetails;
        final LinearLayout eventsContainer, accordionHeader;
        final TextView tvLatestEvent;
        final ImageView ivExpandCollapse;



        ViewHolder(View view) {
            super(view);
            // Initialize all text views
            tvTrackingNumber = view.findViewById(R.id.tv_tracking_number);
            tvStatus = view.findViewById(R.id.tv_status);
            tvScanTime = view.findViewById(R.id.tv_scan_time);
            tvStoreName = view.findViewById(R.id.tv_store_name);
            tvStoreAddress = view.findViewById(R.id.tv_store_address);
            tvCustomerName = view.findViewById(R.id.tv_customer_name);
            tvCustomerAddress = view.findViewById(R.id.tv_customer_address);
            tvEstimatedDelivery = view.findViewById(R.id.tv_estimated_delivery);
            tvParcelDetails = view.findViewById(R.id.tv_parcel_details);
            eventsContainer = view.findViewById(R.id.events_container);
            accordionHeader = view.findViewById(R.id.accordion_header);
            tvLatestEvent = view.findViewById(R.id.tv_latest_event);
            ivExpandCollapse = view.findViewById(R.id.iv_expand_collapse);


        }
    }
}