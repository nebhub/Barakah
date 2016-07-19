package org.barakahchicago.barakah.test;

import android.support.v4.app.FragmentManager;
import android.test.ActivityInstrumentationTestCase2;

import org.barakahchicago.barakah.model.Article;
import org.barakahchicago.barakah.dao.ArticleDAO;
import org.barakahchicago.barakah.ui.ArticleFragment;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by bevuk on 12/1/2015.
 */
public class ArticlesFragmentTest extends ActivityInstrumentationTestCase2<FragmentContainerActivity> {

    private FragmentContainerActivity fragmentContainerActivity;
    private FragmentManager fragmentManager;
    private ArrayList<Article> testArticles;
    private Article testArticle, testArticle2;
    private ArticleFragment articleFragment;
    private ArticleDAO articleDAO;

    public ArticlesFragmentTest() {
        super(FragmentContainerActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // startActivity(new Intent(Intent.ACTION_MAIN), null, null);
        fragmentContainerActivity = getActivity();

        fragmentManager = fragmentContainerActivity.getSupportFragmentManager();

        articleFragment = (ArticleFragment) fragmentManager.findFragmentByTag(ArticleFragment.TAG);


        testArticles = new ArrayList<Article>();


        testArticle = Article.getTestInstance();
        testArticle.setTitle("test1");
        testArticle.setId("1");
        testArticle.setDate_created("2015-10-30 00:00:00");
        testArticle.setEnd_publish("2017-13-30 00:00:00");

        testArticle2 = Article.getTestInstance();
        testArticle2.setTitle("test2");
        testArticle2.setId("2");
        testArticle2.setDate_created("2015-10-30 00:00:00");
        testArticle2.setEnd_publish("2015-12-30 00:00:00");


        testArticles.add(testArticle);
        testArticles.add(testArticle2);


    }


    public void testPrecondition() {
        assertNotNull("Activity is null: ", fragmentContainerActivity);
        assertNotNull("fragmentManager is null: ", fragmentManager);
        assertEquals("testArticles items are not added", 2, testArticles.size());


    }

    public void testArticlesAreLoaded() {


        assertNotNull("article data not initialized: ", articleFragment.getArticles());


    }

    public void testAdapterIsNotNull() {

        assertNotNull(articleFragment.getAdapter());
        assertEquals(articleFragment.getArticles().size(), articleFragment.getAdapter().getItemCount());

    }

    public void testRecyclerViewIsLoaded() {
        assertNotNull("Recycler view is not initialized: ", articleFragment.getRecyclerView());
        assertNotNull("Adapter is not set for recycler view: ", articleFragment.getRecyclerView().getAdapter());

    }


    public void testUpdateView() {

        //    eventsFragment.setEvents(testEvents);
        articleDAO = Mockito.mock(ArticleDAO.class);
        when(articleDAO.getPublishedArticles()).thenReturn(testArticles);
        articleFragment.setArticleDAO(articleDAO);

        articleFragment.updateView();

        verify(articleDAO, atLeast(1)).getPublishedArticles();


        assertNotNull("ArticleDAO is not set: ", articleFragment.getArticleDAO());

        //check values are updated
        assertEquals("Articles are not updated", testArticles.size(), articleFragment.getArticles().size());
        assertEquals("Adapter is not updated", testArticles.size(), articleFragment.getAdapter().getItemCount());


    }
}
