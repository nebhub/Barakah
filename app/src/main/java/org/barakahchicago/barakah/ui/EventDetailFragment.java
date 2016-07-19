package org.barakahchicago.barakah.ui;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.barakahchicago.barakah.util.DateUtil;
import org.barakahchicago.barakah.R;
import org.barakahchicago.barakah.model.Event;

public class EventDetailFragment extends Fragment {

    /*
       Key used to access data from Intent's argument
    */
    public static final String PARCELABLE_KEY = "EVENT";

    /*
        Tag used for fragment transactions
     */
    public static final String TAG = "event_detail";

    /*
       Tag used for logging
     */
    private static final String LOG_TAG = "EVENT DETAIL FRAGMENT";

    /*
        Event object
     */
    private Event event;

    /*
        View to display event's title
     */
    private TextView title;

    /*
       View to display event's data
    */
    private TextView date;

    /*
       View to display event's time
    */
    private TextView time;

    /*
       View to display event's location
    */
    private TextView location;

    /*
       View to display event's description
    */
    private TextView description;

    /*
       View to display event's image
    */
    private ImageView image;

    /*
       Constructor
   */
    public EventDetailFragment() {

    }


    /*
        Creates or returns an instance of EventDetailFragment and adds event data in
        the argument. so it can retain its state

    */
    public static EventDetailFragment newInstance(Event event) {

        Bundle bundle = new Bundle();
        bundle.putParcelable(PARCELABLE_KEY, event);
        EventDetailFragment eventDetailFragment = new EventDetailFragment();
        eventDetailFragment.setArguments(bundle);

        return eventDetailFragment;
    }

    public Event getEvent() {
        return event;
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getDate() {
        return date;
    }

    public TextView getTime() {
        return time;
    }

    public TextView getLocation() {
        return location;
    }

    public TextView getDescription() {
        return description;
    }

    public ImageView getImage() {
        return image;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.event = getArguments().getParcelable(PARCELABLE_KEY);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.event_detail_fragment, container, false);

        title = (TextView) view.findViewById(R.id.det_event_title);
        date = (TextView) view.findViewById(R.id.det_event_date);
        time = (TextView) view.findViewById(R.id.det_event_time);
        location = (TextView) view
                .findViewById(R.id.det_event_location);
        description = (TextView) view
                .findViewById(R.id.det_event_description);

        image = (ImageView) view.findViewById(R.id.det_event_image);


        LinearLayout locationLayout = (LinearLayout) view
                .findViewById(R.id.location_box);
        locationLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String address = location.getText().toString();

                if (address != "") {
                    Intent intent = new Intent(Intent.ACTION_VIEW);

                    // endode the address
                    Uri geoLocation = Uri.parse("geo:0,0?q="
                            + Uri.encode(address));

                    intent.setData(geoLocation);
                    if (intent.resolveActivity(getActivity()
                            .getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }

            }
        });

        LinearLayout eventDate = (LinearLayout) view.findViewById(R.id.event_date_box);
        eventDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(Events.CONTENT_URI)
                        .putExtra(Events.TITLE, event.getTitle())
                        .putExtra(Events.EVENT_LOCATION, event.getLocation())
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, DateUtil.getTimeInMillis(event.getStart_date()))
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, DateUtil.getTimeInMillis(event.getEnd_date()));
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }


            }
        });

        if (event != null) {
            title.setText(event.getTitle());
            if (event.getStart_date().equals(event.getEnd_date())) {

				/*when event only have start time and date.
                 * shows Wed, Jan 3 10:00 AM
				 */
                date.setText(DateUtil.getFormattedDate(event.getStart_date()));
                time.setText(DateUtil.getFormattedTime(event.getStart_date()));
            } else {

				/*for same day different end time events.
                 * shows Wed, Jan 3 10:00 AM-11:00 AM
				 * 			
				 */
                if (DateUtil.getFormattedDate(event.getStart_date()).equals(DateUtil.getFormattedDate(event.getEnd_date()))) {
                    //if the events have the same date but different end time
                    date.setText(DateUtil.getFormattedDate(event.getStart_date()));
                    time.setText(DateUtil.getFormattedTime(event.getStart_date()) + " - " + DateUtil.getFormattedTime(event.getEnd_date()));
                } else {
                    /* when an event starts and end on a different dates.
                     * only show the date part Wed Jan, 3 10:00 AM - Thr, Jan 4 11:00 AM
					 */
                    date.setText(DateUtil.getFormattedDateTime(event.getStart_date()) + " -");
                    time.setText(DateUtil.getFormattedDateTime(event.getEnd_date()));
                }
            }
            location.setText(event.getLocation());
            description.setText(event.getDescription());


            if (event.getImage() == null || event.getImage().equals("")) {
                image.setVisibility(View.GONE);
            } else {
                Ion.with(image).placeholder(R.drawable.ic_launcher).load(this.event.getImage()).setCallback(new FutureCallback<ImageView>() {
                    @Override
                    public void onCompleted(Exception e, ImageView imageView) {
                        if (imageView == null) {


                        }

                    }
                });
                image.setVisibility(View.VISIBLE);

            }

        }
        return view;
    }

}
