package org.barakahchicago.barakah.test;

import android.test.ServiceTestCase;

import org.barakahchicago.barakah.service.BarakahService;

/**
 * Created by bevuk on 11/29/2015.
 */
public class BarakahServiceTest extends ServiceTestCase<BarakahService> {

    public BarakahServiceTest() {
        super(BarakahService.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testStart() {

    }
}
