package org.barakahchicago.barakah.test;

import org.barakahchicago.barakah.model.Article;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by bevuk on 11/30/2015.
 */
public class ArticleTest {
    private Article article1;
    private Article article2;

    @Before
    public  void setUp(){
        article1 = Article.getTestInstance();
        article2 = Article.getTestInstance();

    }

    @Test
    public void testEquals_ShouleReturnFalse_WhenArticlesHaveTheSameIds(){
        assertTrue(article1.isEqualTo(article2));
    }
    @Test
    public void testEquals_ShouldRetunrFalse_WhenArticlesHaveDifferentIds(){
        article2.setId("10");
        assertFalse(article1.isEqualTo(article2));
    }
    @Test
    public void testEquals_ShouldRetunrFalse_WhenEitherOneOfTheTwoArticlesHaveInvalidId(){
        article2.setId(null);
        assertFalse(article1.isEqualTo(article2));

        article2.setId("");
        assertFalse(article1.isEqualTo(article2));
    }
}
