package server;

import configuration.Configuration;
import exceptions.ServerErrorHandler;
import request.RequestParser;
import routeActions.URIProcessor;
import router.RouteProcessor;
import router.RoutesFactory;

import java.io.IOException;
import java.net.ServerSocket;

public class HttpServerMain {

    private static final int NUMBER_OF_THREADS = 200;

    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        CommandLineArguments arguments = commandLineArguments(args);
        try {
            ServerErrorHandler errorHandler = new ServerErrorHandler();
            HttpServer server = new HttpServer(
                    new HttpServerSocket(new ServerSocket(arguments.portNumber())),
                    new ExecutorServiceCreator(NUMBER_OF_THREADS),
                    new RequestParser(errorHandler),
                    new RouteProcessor(
                            new RoutesFactory(new URIProcessor(arguments.publicClassPath()), configuration),
                            configuration,
                            errorHandler));
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

    private static CommandLineArguments commandLineArguments(String[] args) {
        CommandLineArguments arguments = new CommandLineArguments();
        arguments.process_arguments(args);
        return arguments;
    }
}
