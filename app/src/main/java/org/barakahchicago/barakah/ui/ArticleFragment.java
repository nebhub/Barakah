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

import org.barakahchicago.barakah.dao.ArticleDAO;
import org.barakahchicago.barakah.dao.BarakahDbHelper;
import org.barakahchicago.barakah.service.BarakahService;
import org.barakahchicago.barakah.util.OnUpdateListener;
import org.barakahchicago.barakah.R;
import org.barakahchicago.barakah.adapter.ArticleAdapter;
import org.barakahchicago.barakah.model.Article;

public class ArticleFragment extends Fragment {

    /*
       Tag used for fragment trasaction
     */
    public static final String TAG = "articles";

    /*
      Notification Id for this articles
     */
    public static final int ARTICLE_NOTIFICATION_ID = 1;

    /*
      Tag used for logging
     */
    private static final String LOG_TAG = "ARTICLE FRAGMENT";

    /*
      On article item select listener
     */
    private OnArticleSelectedListener callback;

    /*
        Android database helper
     */
    private BarakahDbHelper dbHelper;

    /*

    SQLiteDatabase
     */
    private SQLiteDatabase db;

    /*
        An Adapter for the ViewPager
     */
    private ArticleAdapter adapter;

    /*
      A List used to hold articles to be used with the adapter
     */
    private ArrayList<Article> articles;

    /*
     A DAO class used to retrieve articles form local database
     */
    private ArticleDAO articleDAO;

    /*
     A Broadcast reciever used to update view when background download completes
     */
    private BarakahBroadcastReciever barakahBroadcastReciever;

    /*
        ProgressBar displayed while background service is running
     */
    private ProgressBar progressBar;

    /*
       RecyclerView userd to diaplay list of articles
     */
    private RecyclerView recyclerView;

    /*
       TextView Displayed when the recycles view is empty
     */
    private TextView emptyView;

    public ArrayList<Article> getArticles() {
        return articles;
    }

    public ArticleDAO getArticleDAO() {
        return articleDAO;
    }

    public void setArticleDAO(ArticleDAO articleDAO) {
        this.articleDAO = articleDAO;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public ArticleAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        dbHelper = BarakahDbHelper.newInstance(getContext());
        db = dbHelper.getReadableDatabase();
        articleDAO = new ArticleDAO(getContext(), dbHelper, db);
        articles = new ArrayList<Article>();
        articles = articleDAO.getPublishedArticles();
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        callback = (OnArticleSelectedListener) context;

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
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(barakahBroadcastReciever, new IntentFilter(BarakahService.BROADCAST_ACTION));
        Log.i(LOG_TAG, "Broadcast receive registered");
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).unregisterReceiver(barakahBroadcastReciever);
        Log.i(LOG_TAG, "Broadcast receiver unregistered");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        //inflater.inflate(R.menu.event_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_update) {
            progressBar.setVisibility(View.VISIBLE);
            callback.onUpdate();

        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.articles_fragment, container, false);
        emptyView = (TextView) view.findViewById(R.id.article_empty);
        recyclerView = (RecyclerView) view
                .findViewById(R.id.article_recycler_view);

        recyclerView.setHasFixedSize(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getActivity());
        articles = articleDAO.getPublishedArticles();
        if (articles.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
        }
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ArticleAdapter(articles, callback);
        recyclerView.setAdapter(adapter);
        progressBar = (ProgressBar) view.findViewById(R.id.article_progress_bar);


        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //hide notification when user view this fragment
            hideNotification();
        }
    }

    /*
        updates the views by re-querying articles data and updating the adapter
     */
    public void updateView() {

        articles = articleDAO.getPublishedArticles();

        if (articles.size() > 0) {
            emptyView.setVisibility(View.GONE);
            adapter.setArticles(articles);
            adapter.notifyDataSetChanged();
        } else {
            emptyView.setVisibility(View.VISIBLE);
        }

    }


    /*
      Hides notifications with id ARTICLE_NOTIFICATION_ID
     */
    public void hideNotification() {
        NotificationManager notificationManager = (NotificationManager) getContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancel(ARTICLE_NOTIFICATION_ID);
        Log.i(LOG_TAG, "Notification hidden");
    }

    public interface OnArticleSelectedListener extends OnUpdateListener {
        public void onClick(Article article);

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
