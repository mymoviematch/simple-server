package com.mymoviematch.simpleserver.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import com.mymoviematch.simpleserver.conf.Configuration;
import com.mymoviematch.simpleserver.module.ServerModule;
import com.mymoviematch.simpleserver.web.exception.GenericExceptionMapper;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;


public class WebServer implements ServerModule {

    private static final Logger LOGGER = LogManager.getLogger(WebServer.class);

    private String baseUri;

    private Set<Class<?>> classes = new HashSet<>();

    private HttpServer server;

    private boolean initialized = false;


    @Override
    public void init(Configuration configuration) {
        baseUri = "http://" + configuration.webHost + ":" + configuration.webPort + "/";

        initialized = true;
    }


    @Override
    public void start() {
        ResourceConfig rc = new ResourceConfig();

        rc.registerClasses(JacksonFeature.class);
        rc.registerClasses(GenericExceptionMapper.class);

        if (classes != null) {
            rc.registerClasses(classes);
        }

        server = GrizzlyHttpServerFactory.createHttpServer(URI.create(baseUri), rc);

        LOGGER.debug(String.format("Jersey app started with WADL available at %sapplication.wadl", baseUri));
    }


    @Override
    public void stop() {
        server.shutdownNow();
    }


    public String getBaseUri() {
        return baseUri;
    }


    public void addResource(Class resourceClass) {
        if (initialized) {
            throw new RuntimeException("Cannot add resource, web server already initialized.");
        }

        classes.add(resourceClass);
    }


    public void addResources(Set<Class<?>> resourceClasses) {
        if (initialized) {
            throw new RuntimeException("Cannot add resources, web server already initialized.");
        }

        classes.addAll(resourceClasses);
    }
}
