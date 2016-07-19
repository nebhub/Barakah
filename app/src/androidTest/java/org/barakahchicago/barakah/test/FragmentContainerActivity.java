package org.barakahchicago.barakah.test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.barakahchicago.barakah.model.Article;
import org.barakahchicago.barakah.ui.ArticleDetailFragment;
import org.barakahchicago.barakah.ui.ArticleFragment;
import org.barakahchicago.barakah.model.Event;
import org.barakahchicago.barakah.ui.EventDetailFragment;
import org.barakahchicago.barakah.ui.EventsFragment;
import org.barakahchicago.barakah.model.Message;
import org.barakahchicago.barakah.ui.MessagesFragment;

/**
 * Created by bevuk on 11/23/2015.
 */
public class FragmentContainerActivity extends AppCompatActivity implements EventsFragment.OnEventSelectedListener, ArticleFragment.OnArticleSelectedListener, MessagesFragment.OnMessageSelectedListener {

    public FrameLayout frameLayout;
    private int id = 98 + 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        frameLayout = new FrameLayout(this);
        frameLayout.setId(id);
        // addFragment(new EventsFragment(),EventsFragment.TAG);
        // addFragment(new ArticleFragment(),ArticleFragment.TAG);
        //addFragment(new MessagesFragment(),MessagesFragment.TAG);

        addFragment(EventDetailFragment.newInstance(Event.getTestInstance()), EventDetailFragment.TAG);
        addFragment(ArticleDetailFragment.newInstance(Article.getTestInstance()), ArticleDetailFragment.TAG);
        setContentView(frameLayout, params);
    }

    public void addFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction().add(frameLayout.getId(), fragment, tag).commit();

    }


    @Override
    public void onClick(Event event) {

    }

    @Override
    public void onUpdate() {

    }


    @Override
    public void onClick(Article article) {

    }

    @Override
    public void onClick(Message message) {

    }
}
