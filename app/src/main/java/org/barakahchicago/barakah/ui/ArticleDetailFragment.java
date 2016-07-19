package org.barakahchicago.barakah.ui;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.barakahchicago.barakah.util.DateUtil;
import org.barakahchicago.barakah.R;
import org.barakahchicago.barakah.model.Article;

public class ArticleDetailFragment extends Fragment {
    /*
        Key used to access data from Intent's argument
     */
    public static final String PARCELABLE_KEY = "ARTICLE";

    /*
        Tag used for fragment transactions
     */
    public static final String TAG = "article_detail";

    /*
       Tag used for logging
     */
    private static final String LOG_TAG = "ARTICLE DETAIL FRAGMENT";

    /*
        Article object
     */
    private Article article;

    /*
        View to display article's title
     */
    private TextView title;

    /*
        View to display article's author
     */
    private TextView author;

    /*
        View to display article's post date
     */
    private TextView datePosted;

    /*
        View to display article's body
     */
    private TextView body;

    /*
        View to display article's image
     */
    private ImageView image;

    /*
        Constructor
    */
    public ArticleDetailFragment() {

    }

    /*
        Creates or returns an instance of ArticleDetailFragment and adds article data in
        the argument. so it can retain its state
    */
    public static ArticleDetailFragment newInstance(Article article) {
        ArticleDetailFragment articleDetailFragment = new ArticleDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARCELABLE_KEY, article);
        articleDetailFragment.setArguments(bundle);

        return articleDetailFragment;

    }

    public Article getArticle() {
        return article;
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getAuthor() {
        return author;
    }

    public TextView getDatePosted() {
        return datePosted;
    }

    public TextView getBody() {
        return body;
    }

    public ImageView getImage() {
        return image;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        article = getArguments().getParcelable(ArticleDetailFragment.PARCELABLE_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.article_detail_fragment, container, false);
        title = (TextView) view.findViewById(R.id.det_article_title);
        author = (TextView) view.findViewById(R.id.det_article_author);
        datePosted = (TextView) view.findViewById(R.id.det_article_date_posted);
        body = (TextView) view.findViewById(R.id.det_article_body);
        image = (ImageView) view.findViewById(R.id.det_article_image);
        title.setText(article.getTitle());
        author.setText(article.getAuthor());
        datePosted.setText(DateUtil.getFormattedDateTime(article.getDate_created()));
        body.setText(article.getBody());

        if (article.getImage() == null || article.getImage().equals("")) {

            image.setVisibility(View.GONE);

        } else {
            Log.i(LOG_TAG, "Image url" + article.getImage());
            Ion.with(image).placeholder(R.drawable.ic_launcher).load(article.getImage()).setCallback(new FutureCallback<ImageView>() {
                @Override
                public void onCompleted(Exception e, ImageView imageView) {
                    if (e == null) {
                        Log.i(LOG_TAG, "Image view is null");
                    }

                }
            });
            image.setVisibility(View.VISIBLE);
        }


        return view;
    }
}
