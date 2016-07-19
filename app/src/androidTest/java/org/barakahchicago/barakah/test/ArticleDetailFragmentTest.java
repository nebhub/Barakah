package org.barakahchicago.barakah.test;

import android.test.ActivityInstrumentationTestCase2;

import org.barakahchicago.barakah.ui.ArticleDetailFragment;

/**
 * Created by bevuk on 12/1/2015.
 */
public class ArticleDetailFragmentTest extends ActivityInstrumentationTestCase2<FragmentContainerActivity> {

    private FragmentContainerActivity activity;
    private ArticleDetailFragment articleDetailFragment;

    public ArticleDetailFragmentTest() {
        super(FragmentContainerActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        activity = getActivity();
        articleDetailFragment = (ArticleDetailFragment) activity.getSupportFragmentManager().findFragmentByTag(ArticleDetailFragment.TAG);


    }

    public void testPreconditions() {
        assertNotNull(activity);
        assertNotNull(articleDetailFragment);

    }

    public void testFragmentArgumentIsPassed() {

        assertNotNull(articleDetailFragment.getArguments().getParcelable(ArticleDetailFragment.PARCELABLE_KEY));
        assertNotNull(articleDetailFragment.getArticle());
    }

    public void testDateIsSetToFragmentViews() {
        assertEquals(articleDetailFragment.getArticle().getTitle(), articleDetailFragment.getTitle().getText());
        assertEquals(articleDetailFragment.getArticle().getAuthor(), articleDetailFragment.getAuthor().getText());

    }

}
