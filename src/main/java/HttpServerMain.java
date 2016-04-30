import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;

public class HttpServerMain {
    public static void main(String[] args) {
        CommandLineArguments arguments = commandLineArguments(args);
        try {
            HttpServer server = new HttpServer(
                    new HttpServerSocket(new ServerSocket(arguments.portNumber())),
                    new Router(routes()));
            server.serverUp();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static CommandLineArguments commandLineArguments(String[] args) {
        CommandLineArguments arguments = new CommandLineArguments();
        arguments.process_arguments(args);
        return arguments;
    }

    private static List<Route> routes() {
        return new RoutesFactory().routes();
    }
}
