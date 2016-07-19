package org.barakahchicago.barakah.test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import org.barakahchicago.barakah.model.Article;
import org.barakahchicago.barakah.dao.ArticleDAO;
import org.barakahchicago.barakah.dao.BarakahDbHelper;

import java.util.ArrayList;

/**
 * Created by bevuk on 11/29/2015.
 */
public class ArticleDAOTest extends AndroidTestCase {

    ArticleDAO articleDAO;
    Context context;
    BarakahDbHelper dbHelper;
    SQLiteDatabase db;
    private Article testArticle, testArticle2, testArticle3;
    private ArrayList<Article> testArticles;


    public ArticleDAOTest() {
        super();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        testArticle = Article.getTestInstance();

        testArticle2 = Article.getTestInstance();
        testArticle2.setTitle("test2");
        testArticle2.setId("2");
        testArticle2.setDate_created("2017-01-28 00:00:00");
        testArticle2.setEnd_publish("2016-01-29 00:00:00");

        testArticle3 = Article.getTestInstance();
        testArticle3.setTitle("test3");
        testArticle3.setId("3");
        testArticle3.setEnd_publish("2018-02-28 00:00:00");
        testArticle3.setDate_created("2016-02-29 00:00:00");

        testArticles = new ArrayList<Article>();

        testArticles.add(testArticle);
        testArticles.add(testArticle2);
        testArticles.add(testArticle3);

//passig null to database name. to use memory database
        dbHelper = BarakahDbHelper.newInstanceForTest(getContext(), null);
        db = dbHelper.getWritableDatabase();
        articleDAO = new ArticleDAO(getContext(), dbHelper, db);

    }

    public void testPreconditions() {
        assertNotNull(articleDAO);
        assertEquals(3, testArticles.size());
    }

    public void testAdd() {

        assertEquals(testArticles.size(), articleDAO.add(testArticles));


    }

    public void testGetAll() {
        assertEquals(testArticles.size(), articleDAO.add(testArticles));
        assertEquals(testArticles.size(), articleDAO.getAll().size());
    }

    public void testCallingGetAllOnAnEmptyDb_ShouldReturn() {
        assertEquals(0, articleDAO.getAll().size());
    }

    public void testWhenAnEmptyListIsAdded_ShouldReturnZero() {
        assertEquals(0, articleDAO.add(new ArrayList<Article>()));
    }

    public void testUpdate() {
        articleDAO.add(testArticles);
        String updatedTitle = "title 1 updated";
        testArticles.get(0).setTitle(updatedTitle);

        assertEquals(testArticles.size(), articleDAO.update(testArticles));

        //we already know the first article is the earliest.
        assertEquals(updatedTitle, articleDAO.getPublishedArticles().get(0).getTitle());

    }

    public void testWhenUpdateWithEmptyList_ShouldReturnZero() {
        assertEquals(0, articleDAO.update(new ArrayList<Article>()));
    }

    public void testGetPublishedArticles() {
        //changing start date of one of the test articles to earlier date than now.
        testArticles.get(2).setEnd_publish("2010-02-28 00:00:00");
        articleDAO.add(testArticles);

        assertEquals(testArticles.size() - 1, articleDAO.getPublishedArticles().size());


    }

    public void testWhenThereAreNoPublishedArticles_ShouldReturnAnEmptyList() {
        //setting start Date of all events to an old date
        testArticles.get(0).setEnd_publish("2010-02-28 00:00:00");
        testArticles.get(1).setEnd_publish("2010-02-28 00:00:00");
        testArticles.get(2).setEnd_publish("2010-02-28 00:00:00");
        articleDAO.add(testArticles);

        assertEquals(0, articleDAO.getPublishedArticles().size());
    }

    public void testAddOrUpdate() {
        articleDAO.add(testArticles);

        Article oldArticle = Article.getTestInstance();

        Article newArticle = Article.getTestInstance();
        newArticle.setId("4");

        Article oldButUpdatedArticle = Article.getTestInstance();
        // Changing from 2015-12-30 00:00:00 to "2016-01-01 00:00:00"
        String updatedDate = "2016-01-01 00:00:00";
        String updatedTitle = "update title";
        oldButUpdatedArticle.setLast_updated(updatedDate);
        oldButUpdatedArticle.setTitle(updatedTitle);

        ArrayList<Article> newList = new ArrayList<>();
        newList.add(newArticle);
        newList.add(oldArticle);
        newList.add(oldButUpdatedArticle);

        //only two rows sholuld be affected one is adde another is updated
        assertEquals(2, articleDAO.addOrUpdate(newList));
        assertEquals(testArticles.size() + 1, articleDAO.getPublishedArticles().size());
        assertEquals(updatedTitle, articleDAO.getPublishedArticles().get(0).getTitle());

    }

    public void testWhenAddOrUpdateIsCalledWithAnEmptyList_ShouldReturnZero() {
        assertEquals(0, articleDAO.addOrUpdate(new ArrayList<Article>()));
    }

    public void testWhenAddOrUpdateIsCalledOnAnEmptyDB_ShouldAddAll() {
        assertEquals(testArticles.size(), articleDAO.addOrUpdate(testArticles));
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        dbHelper.close();
        db.close();
    }
}
