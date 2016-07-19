package org.barakahchicago.barakah.test;

import android.test.AndroidTestCase;

import org.barakahchicago.barakah.model.Article;
import org.barakahchicago.barakah.adapter.ArticleAdapter;
import org.barakahchicago.barakah.ui.ArticleFragment;

import org.mockito.Mockito;

import java.util.ArrayList;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

/**
 * Created by bevuk on 11/30/2015.
 */
public class ArticleAdapterTest extends AndroidTestCase {


    ArticleAdapter articleAdapter;
    Article testArticle, testArticle2;
    ArrayList<Article> testArticles;
    ArticleFragment.OnArticleSelectedListener listener;

    public ArticleAdapterTest() {
        super();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        listener = Mockito.mock(ArticleFragment.OnArticleSelectedListener.class);

        testArticle = Article.getTestInstance();

        testArticle2 = Article.getTestInstance();
        testArticle.setTitle("test2");
        testArticle.setId("2");
        testArticle.setDate_created("2016-10-30 00:00:00");
        testArticle.setEnd_publish("2016-10-30 00:00:00");


        testArticles = new ArrayList<Article>();
        testArticles.add(testArticle);
        testArticles.add(testArticle2);
        articleAdapter = new ArticleAdapter(testArticles, listener);
    }


    public void testPreconditions() {
        assertNotNull(testArticles);
        assertNotNull(listener);
        assertNotNull(articleAdapter);
    }

    public void testGetItemCount() {
        assertEquals(2, articleAdapter.getItemCount());
    }


    public void testListenerIsSet() {
        assertNotNull(articleAdapter.getCallback());
        listener.onClick(testArticle);
        verify(listener, atLeastOnce()).onClick(testArticle);

    }
}
