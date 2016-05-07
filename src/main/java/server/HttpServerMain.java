package server;

import routeActions.RouteAction;
import routeActions.URIProcessor;
import router.Route;
import router.RouteProcessor;
import router.Router;
import router.RoutesFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.Map;

public class HttpServerMain {

    private static final int NUMBER_OF_THREADS = 200;

    public static void main(String[] args) {
        CommandLineArguments arguments = commandLineArguments(args);
        try {
            HttpServer server = new HttpServer(
                    new HttpServerSocket(new ServerSocket(arguments.portNumber())),
                    new ExecutorServiceCreator(NUMBER_OF_THREADS),
                    new RouteProcessor(new Router(routeActions()), new URIProcessor(arguments.publicClassPath())));

            startServer(server);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startServer(HttpServer server) {
        while(true) {
            server.serverUp();
        }
    }

    private static CommandLineArguments commandLineArguments(String[] args) {
        CommandLineArguments arguments = new CommandLineArguments();
        arguments.process_arguments(args);
        return arguments;
    }

    private static Map<Route, List<RouteAction>> routeActions() {
        return new RoutesFactory().routeActions();
    }
}
