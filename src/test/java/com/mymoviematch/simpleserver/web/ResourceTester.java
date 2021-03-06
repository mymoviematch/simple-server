package com.mymoviematch.simpleserver.web;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import com.mymoviematch.simpleserver.db.DbContext;
import com.mymoviematch.simpleserver.conf.ConfigurationLoader;

import java.io.IOException;
import java.io.InputStream;


public class ResourceTester {

    protected WebServer webServer;

    @Before
    public void setUp() throws Exception {
        ConfigurationLoader confLoader = new ConfigurationLoader();
        confLoader.init();
        DbContext dbContext = new DbContext();
        dbContext.init(confLoader.getConfiguration());
        webServer = new WebServer();
        webServer.init(confLoader.getConfiguration());
        webServer.start();
    }


    @After
    public void tearDown() throws Exception {
        webServer.stop();
    }


    protected String readResource(String name) throws IOException {
        try (InputStream is = getClass().getResourceAsStream(name)) {
            return IOUtils.toString(is);
        }
    }
}
