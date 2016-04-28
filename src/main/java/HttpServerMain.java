public class HttpServerMain {
    public static int currentPort = 5000;
    public static String currentPublicClassPath = "/Users/sarahjohnston/Sarah/CobSpec/public/";

    public static void main(String[] args) {
        serverHostAndPort(args);
        HttpServer server = new HttpServer(currentPort);
        server.serverUp();
    }

    public int portNumber() {
        return currentPort;
    }

    public String publicClassPath() {
        return currentPublicClassPath;
    }

    private static void serverHostAndPort(String[] args) {
        for (int i = 0; i < args.length; i++) {
            portNumber(i, args);
            publicClassPath(i, args);
        }
    }

    private static void publicClassPath(int index, String[] args) {
        if (args[index] == "-d") {
            currentPublicClassPath = args[index + 1];
        }
    }

    private static void portNumber(int index, String[] args) {
        if (args[index] == "-p") {
            currentPort = Integer.parseInt(args[index + 1]);
        }
    }
}
