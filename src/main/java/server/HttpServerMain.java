package server;

import configuration.Configuration;
import exceptions.ServerErrorHandler;
import messages.EntityHeaderBuilder;
import request.HTTPResource;
import request.RequestParser;
import routeActions.URIProcessor;
import router.RouteProcessor;
import router.RoutesFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static request.HTTPMethod.*;
import static request.HTTPResource.*;

public class HttpServerMain {

    private static final int NUMBER_OF_THREADS = 200;

    public static void main(String[] args) {

        CommandLineArguments arguments = new CommandLineArguments().process_arguments(args);

        Configuration configuration = initializeConfiguration(arguments);

        runServer(configuration);
    }

    private static void runServer(Configuration configuration) {
        try {
            ServerErrorHandler serverErrorHandler = new ServerErrorHandler();
            HttpServer server = new HttpServer(
                    new HttpServerSocket(serverSocket(configuration)),
                    new ExecutorServiceCreator(NUMBER_OF_THREADS),
                    new RequestParser(serverErrorHandler),
                    routeProcessor(configuration, serverErrorHandler));

            startServer(server);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startServer(HttpServer server) {
        while (true) {
            server.serverUp();
        }
    }

    private static Configuration initializeConfiguration(CommandLineArguments arguments) {
        return new Configuration()
                    .addServerPortNumber(arguments.portNumber())
                    .addPublicDirectory(arguments.publicClassPath())
                    .addAuthorisationCredentials("admin:hunter2")
                    .addResourcesRequiringAuth(asList(LOGS, LOG, REQUESTS, THESE))
                    .addMethodsNotAllowed(methodsNotAllowed());
    }

    private static Map<HTTPResource, List<String>> methodsNotAllowed() {
        Map<HTTPResource, List<String>> methodsNotAllowed = new HashMap<>();
        methodsNotAllowed.put(FILE1, asList(PUT.method()));
        methodsNotAllowed.put(TEXT_FILE, asList(POST.method()));
        methodsNotAllowed.put(OPTIONS_ONE, asList(DELETE.method(), PATCH.method()));
        methodsNotAllowed.put(OPTIONS_TWO, asList(HEAD.method(), POST.method(), PUT.method()));
        return methodsNotAllowed;
    }

    private static ServerSocket serverSocket(Configuration configuration) throws IOException {
        return new ServerSocket(configuration.serverPortNumber());
    }

    private static RouteProcessor routeProcessor(Configuration configuration, ServerErrorHandler serverErrorHandler) {
        return new RouteProcessor(
                new RoutesFactory(resourceProcessor(configuration), configuration, entityHeaderBuilder(configuration)),
                configuration,
                serverErrorHandler);
    }

    private static EntityHeaderBuilder entityHeaderBuilder(Configuration configuration) {
        return new EntityHeaderBuilder(configuration);
    }

    private static URIProcessor resourceProcessor(Configuration configuration) {
        return new URIProcessor(configuration.pathToPublicDirectory());
    }

}
