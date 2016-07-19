package org.barakahchicago.barakah.adapter;

import java.util.ArrayList;

import org.barakahchicago.barakah.ui.ArticleFragment.OnArticleSelectedListener;
import org.barakahchicago.barakah.util.DateUtil;
import org.barakahchicago.barakah.R;
import org.barakahchicago.barakah.model.Article;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class ArticleAdapter extends
        RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    /*
        Callback used to handle whe user selects an article
    */
    private OnArticleSelectedListener callback;

    /*
        List of articles used with the adapter
     */
    private ArrayList<Article> articles = new ArrayList<Article>();

    /*
        Constructor, initializes a new ArticleaAdapter with list of articles and callback object
     */
    public ArticleAdapter(ArrayList<Article> articles, OnArticleSelectedListener callback) {
        this.articles = articles;
        this.callback = callback;
    }

    public OnArticleSelectedListener getCallback() {
        return callback;
    }

    public ArrayList<Article> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<Article> articles) {
        this.articles = articles;
    }

    @Override
    public int getItemCount() {

        return articles.size();
    }

    /*
        Binds individual views with data.
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if (articles.size() > 0) {
            viewHolder.articleTitle.setText(articles.get(position).getTitle());
            viewHolder.articleDate.setText(DateUtil.getFormattedDate(articles.get(position).getDate_created()));

            if (articles.get(position).getImage() == null || articles.get(position).getImage().equals("")) {

                viewHolder.articleImage.setVisibility(View.GONE);

            } else {
                Ion.with(viewHolder.articleImage).placeholder(R.drawable.ic_launcher).load(this.articles.get(position).getImage()).setCallback(new FutureCallback<ImageView>() {
                    @Override
                    public void onCompleted(Exception e, ImageView imageView) {

                    }
                });
                viewHolder.articleImage.setVisibility(View.VISIBLE);

            }
            viewHolder.position = position;
        }

    }

    /*
     Creates a new view holder by inflating list item view.
    */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.article_item, parent, false);


        ViewHolder viewHolder = new ViewHolder(v, parent.getContext());

        return viewHolder;
    }

    /*
        ViewHolder class used to represent individual row or cards.
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements
            OnClickListener {
        public int position;
        TextView articleTitle;
        TextView articleDate;
        TextView articleAuthor;
        ImageView articleImage;


        /*
           Creates a new view holder
         */
        public ViewHolder(View itemView, Context context) {
            super(itemView);

            articleTitle = (TextView) itemView.findViewById(R.id.article_title);
            articleDate = (TextView) itemView.findViewById(R.id.article_date);
            articleImage = (ImageView) itemView.findViewById(R.id.article_image);

            // setting the context of the MainActivity as callback listener to
            // change fragments
            callback = (OnArticleSelectedListener) context;

            itemView.setOnClickListener(this);
        }

        /*
          sets onClick listener for individual view
         */
        @Override
        public void onClick(View arg0) {

            callback.onClick(articles.get(position));
        }
    }

}
