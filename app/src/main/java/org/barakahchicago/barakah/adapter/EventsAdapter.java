package org.barakahchicago.barakah.adapter;

import java.util.ArrayList;

import org.barakahchicago.barakah.util.DateUtil;
import org.barakahchicago.barakah.ui.EventsFragment.OnEventSelectedListener;
import org.barakahchicago.barakah.R;
import org.barakahchicago.barakah.model.Event;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class EventsAdapter extends
        RecyclerView.Adapter<EventsAdapter.ViewHolder> {
    /*
        Tag used for logging
    */
    private static final String LOG_TAG = "EVENTS ADAPTER";

    /*
        Callback used to handle whe user selects an event
    */
    OnEventSelectedListener callback;

    /*
        List of events used with the adapter
    */
    private ArrayList<Event> events;

    /*
        Constructor, initializes a new EventsAdapter with list of articles and callback object
     */
    public EventsAdapter(ArrayList<Event> events, OnEventSelectedListener callback) {
        this.events = events;

        this.callback = callback;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public OnEventSelectedListener getCallback() {
        return callback;
    }

    public void setCallback(OnEventSelectedListener callback) {
        this.callback = callback;
    }

    @Override
    public int getItemCount() {

        return this.events.size();
    }

    /*
        Binds individual views with data.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (events.size() > 0) {
            holder.title.setText(this.events.get(position).getTitle());


            if (events.get(position).getStart_date().equals(events.get(position).getEnd_date())) {

			/*when event only have start time and date.
             * shows Wed, Jan 3 10:00 AM
			 */
                holder.date.setText(DateUtil.getFormattedDate(this.events.get(position).getStart_date()));
                holder.time.setText(DateUtil.getFormattedTime(this.events.get(position).getStart_date()));
            } else {

			/*for same day different end time events.
             * shows Wed, Jan 3 10:00 AM-11:00 AM
			 *
			 */
                if (DateUtil.getFormattedDate(this.events.get(position).getStart_date()).equals(DateUtil.getFormattedDate(events.get(position).getEnd_date()))) {
                    //if the events have the same date but different end time
                    holder.date.setText(DateUtil.getFormattedDate(this.events.get(position).getStart_date()));
                    holder.time.setText(DateUtil.getFormattedTime(this.events.get(position).getStart_date()) + " - " + DateUtil.getFormattedTime(events.get(position).getEnd_date()));
                } else {

                /* when an event starts and end on a different dates.
                 * only show the date part Wed Jan, 3 10:00 AM - Thr, Jan 4 11:00 AM
				 */
                    holder.date.setText(DateUtil.getFormattedDateTime(this.events.get(position).getStart_date()) + " -");
                    holder.time.setText(DateUtil.getFormattedDateTime(this.events.get(position).getEnd_date()));
                }
            }

            holder.location.setText(this.events.get(position).getLocation());


            //used find the position in the ViewHolder class
            holder.position = position;


            if (events.get(position).getImage() == null || events.get(position).getImage().equals("")) {
                holder.eventImage.setVisibility(View.GONE);
            } else {
                Ion.with(holder.eventImage).placeholder(R.drawable.ic_launcher).load(this.events.get(position).getImage()).setCallback(new FutureCallback<ImageView>() {
                    @Override
                    public void onCompleted(Exception e, ImageView imageView) {
                        if (imageView == null) {
                            Log.i(LOG_TAG, "No darawable loaded");

                        }

                    }
                });
                holder.eventImage.setVisibility(View.VISIBLE);

            }
        } else {

            Log.i(LOG_TAG, "No event to show");
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.event_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(v, parent.getContext());

        return viewHolder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
            OnClickListener {
        int position;
        TextView title;
        TextView date;
        TextView time;
        TextView location;
        ImageView eventImage;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.event_title);
            date = (TextView) itemView.findViewById(R.id.event_date);
            time = (TextView) itemView.findViewById(R.id.event_time);
            location = (TextView) itemView.findViewById(R.id.event_location);
            eventImage = (ImageView) itemView.findViewById(R.id.event_image);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Log.i(LOG_TAG, "events: " + events.get(position).getTitle() + " " + callback.toString());
            callback.onClick(events.get(position));

        }

    }

}
