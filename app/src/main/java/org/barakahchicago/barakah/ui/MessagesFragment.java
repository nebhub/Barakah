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
import org.barakahchicago.barakah.dao.MessageDAO;
import org.barakahchicago.barakah.util.OnUpdateListener;
import org.barakahchicago.barakah.R;
import org.barakahchicago.barakah.adapter.MessagesAdapter;
import org.barakahchicago.barakah.model.Message;

public class MessagesFragment extends Fragment {

    /*
      Tag used for fragment transaction
     */
    public static final String TAG = "messages";
    /*
      Notification Id for messages
     */
    public static final int MESSAGES_NOTIFIFCATION_ID = 2;

    /*
      Tag used for logging
     */
    private static final String LOG_TAG = "MESSAGE FRAGMENT";

    /*
      TextView Displayed when the recycles view is empty
    */
    private TextView emptyView;

    /*
     A Broadcast receiver used to update view when background download completes
     */
    private BarakahBroadcastReciever barakahBroadcastReciever;

    /*
        Android database helper
     */
    private BarakahDbHelper dbHelper;

    /*

    SQLiteDatabase instance
     */
    private SQLiteDatabase db;

    /*
       An Adapter for the ViewPager
    */
    private MessagesAdapter adapter;

    /*
       RecyclerView used to display list of messages
     */
    private RecyclerView recyclerView;

    /*
      On message item select listener
     */
    private OnMessageSelectedListener callback;

    /*
      A List used to hold messages to be used with the adapter
     */
    private ArrayList<Message> messages;

    /*
     A DAO class used to retrieve messages form local database
     */
    private MessageDAO messageDAO;

    /*
       ProgressBar displayed while background service is running
    */
    private ProgressBar progressBar;

    public MessagesFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (OnMessageSelectedListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        barakahBroadcastReciever = new BarakahBroadcastReciever();
        LocalBroadcastManager
                .getInstance(getActivity().getApplicationContext())
                .registerReceiver(barakahBroadcastReciever,
                        new IntentFilter(BarakahService.BROADCAST_ACTION));
        Log.i(LOG_TAG, "Broadcast reciever registered");


    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager
                .getInstance(getActivity().getApplicationContext())
                .unregisterReceiver(barakahBroadcastReciever);
        Log.i(LOG_TAG, "Broadcast receiver unregistered");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        dbHelper = BarakahDbHelper.newInstance(getContext());
        db = dbHelper.getReadableDatabase();
        messageDAO = new MessageDAO(getContext(), dbHelper, db);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.messages_fragment, container, false);
        emptyView = (TextView) view.findViewById(R.id.message_empty);
        recyclerView = (RecyclerView) view.findViewById(R.id.message_recycler_view);
        recyclerView.setHasFixedSize(false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getActivity());

        recyclerView.setLayoutManager(layoutManager);
        messages = messageDAO.getPublishedMessages();
        if (messages.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
        }
        adapter = new MessagesAdapter(messages, this.callback);
        recyclerView.setAdapter(adapter);

        progressBar = (ProgressBar) view.findViewById(R.id.message_progress_bar);


        return view;
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            hideNotification();
        }
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public MessagesAdapter getAdapter() {
        return adapter;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public MessageDAO getMessageDAO() {
        return messageDAO;
    }

    public void setMessageDAO(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public void updateView() {
        messages = messageDAO.getPublishedMessages();
        if (messages.size() > 0) {
            emptyView.setVisibility(View.GONE);
            adapter.setMessages(messages);
            adapter.notifyDataSetChanged();
        } else {
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    public void hideNotification() {
        NotificationManager notificationManager = (NotificationManager) getContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancel(MESSAGES_NOTIFIFCATION_ID);
        Log.i(LOG_TAG, "Notification hidden");
    }

    public interface OnMessageSelectedListener extends OnUpdateListener {
        public void onClick(Message message);
    }

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
                Toast.makeText(getContext(), getResources().getString(R.string.message_updated),
                        Toast.LENGTH_SHORT).show();
            } else {
                Log.i(LOG_TAG, "Fail Broadcast received");

                //	updateView();
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), getResources().getString(R.string.error_cannot_update),
                        Toast.LENGTH_SHORT).show();
            }

        }

    }
}
