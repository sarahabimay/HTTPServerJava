package server;

public class CommandLineArguments {
    public static int currentPort = 5000;
    public static String currentPublicClassPath = "/Users/sarahjohnston/Sarah/CobSpec/public/";

    public void process_arguments(String[] args) {
        for (int i = 0; i < args.length; i++) {
            portNumber(i, args);
            publicClassPath(i, args);
        }
    }

    public int portNumber() {
        return currentPort;
    }

    public String publicClassPath() {
        return currentPublicClassPath;
    }

    private static void publicClassPath(int index, String[] args) {
        if (args[index].equals("-d")) {
            currentPublicClassPath = args[index + 1];
        }
    }

    private static void portNumber(int index, String[] args) {
        if (args[index].equals("-p")) {
            currentPort = Integer.parseInt(args[index + 1]);
        }
    }
}
