package org.barakahchicago.barakah.ui;

import java.util.ArrayList;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.barakahchicago.barakah.dao.BarakahDbHelper;
import org.barakahchicago.barakah.service.BarakahService;
import org.barakahchicago.barakah.dao.EventDAO;
import org.barakahchicago.barakah.util.OnUpdateListener;
import org.barakahchicago.barakah.R;
import org.barakahchicago.barakah.adapter.EventsAdapter;
import org.barakahchicago.barakah.model.Event;

public class EventsFragment extends Fragment {

    /*
       Notification Id for events
      */
    public static final int EVENT_NOTIFICATION_ID = 0;
    /*
       Tag used for fragment transactions
     */
    public static final String TAG = "events";
    /*
       Tag used for logging
     */
    private static final String LOG_TAG = "EVENTS FRAGMENT";

    /*
       RecyclerView used to display list of events
     */
    private RecyclerView recyclerView;

    /*
       ProgressBar displayed while background service is running
    */
    private ProgressBar progressBar;

    /*
        An Adapter for the ViewPager
     */
    private EventsAdapter adapter;

    /*
     A Broadcast receiver used to update view when background download completes
     */
    private BarakahBroadcastReciever barakahBroadcastReciever;

    /*
      On event item select listener
     */
    private OnEventSelectedListener callback;

    /*
        Android database helper
     */
    private BarakahDbHelper dbHelper;

    /*

    SQLiteDatabase instance
     */
    private SQLiteDatabase db;

    /*
      TextView Displayed when the recycles view is empty
    */
    private TextView emptyView;

    /*
      A List used to hold events to be used with the adapter
     */
    private ArrayList<Event> events;

    /*
     A DAO class used to retrieve events form local database
     */
    private EventDAO eventDAO;

    public EventsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        events = new ArrayList<Event>();
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        dbHelper = BarakahDbHelper.newInstance(getContext());
        db = dbHelper.getReadableDatabase();
        eventDAO = new EventDAO(getContext(), dbHelper, db);

    }

    @Override
    public void onResume() {
        super.onResume();
        barakahBroadcastReciever = new BarakahBroadcastReciever();
        LocalBroadcastManager
                .getInstance(getActivity().getApplicationContext())
                .registerReceiver(barakahBroadcastReciever,
                        new IntentFilter(BarakahService.BROADCAST_ACTION));
        Log.i(LOG_TAG, "Broadcast receiver registered");


    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager
                .getInstance(getActivity().getApplicationContext())
                .unregisterReceiver(barakahBroadcastReciever);
        Log.i(LOG_TAG, "Broadcast receiver unregistered");
        barakahBroadcastReciever = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_update) {

            Log.i(LOG_TAG, "Update Clicked");
            progressBar.setVisibility(View.VISIBLE);
            callback.onUpdate();
        }
        return true;
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        callback = (OnEventSelectedListener) context;
        Log.i(LOG_TAG, "Event Fragment attached");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;

        Log.i(LOG_TAG, "Event Fragment detached");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.events_fragment, container, false);
        emptyView = (TextView) view.findViewById(R.id.event_empty);
        recyclerView = (RecyclerView) view
                .findViewById(R.id.event_recycler_view);

        recyclerView.setHasFixedSize(false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getActivity());

        recyclerView.setLayoutManager(layoutManager);
        events = eventDAO.getUpcommingEvents();
        if (events.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
        }
        adapter = new EventsAdapter(events, this.callback);
        recyclerView.setAdapter(adapter);
        progressBar = (ProgressBar) view.findViewById(R.id.event_progress_bar);

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            hideNotification();
        }
    }

    /*
        updates the views by re-querying articles data and updating the adapter
     */
    public void updateView() {

        events = eventDAO.getUpcommingEvents();
        if (events.size() > 0) {
            emptyView.setVisibility(View.GONE);

            adapter.setEvents(events);
            adapter.notifyDataSetChanged();
        } else {
            emptyView.setVisibility(View.VISIBLE);
        }
    }


    /*
      Hides notifications with id EVENT_NOTIFICATION_ID
     */
    public void hideNotification() {
        NotificationManager notificationManager = (NotificationManager) getContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancel(EVENT_NOTIFICATION_ID);
        Log.i(LOG_TAG, "Notification hidden");
    }

    public EventsAdapter getAdapter() {
        return adapter;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public EventDAO getEventDAO() {
        return eventDAO;
    }

    public void setEventDAO(EventDAO eventDAO) {
        this.eventDAO = eventDAO;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public interface OnEventSelectedListener extends OnUpdateListener {
        public void onClick(Event event);

    }

    /*
      A BroadCast Reciever class used to capture status messages from the background service.

     */
    public class BarakahBroadcastReciever extends BroadcastReceiver {

        public BarakahBroadcastReciever() {

        }

        @Override
        public void onReceive(Context context, Intent intent) {

            String status = intent.getStringExtra(BarakahService.STATUS);
            if (status.equals(BarakahService.STATUS_DONE)) {
                Log.i(LOG_TAG, "Broadcast received");

                updateView();
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), getResources().getString(R.string.message_updated), Toast.LENGTH_SHORT)
                        .show();
            } else {
                Log.i(LOG_TAG, "Fail Broadcast received");

                // updateView();
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(),
                        getResources().getString(R.string.error_cannot_update),
                        Toast.LENGTH_SHORT).show();
            }

        }

    }
}
