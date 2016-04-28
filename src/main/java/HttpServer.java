import java.io.IOException;
import java.net.ServerSocket;
import java.util.Optional;

public class HttpServer {
    private Optional<ServerSocket> serverSocket = Optional.empty();

    public HttpServer(int portNumber) {
        try {
            this.serverSocket = Optional.of(new ServerSocket(portNumber));
        }
        catch(IOException ex){
           this.serverSocket = Optional.empty();
        }
    }

    public void serverUp() {
        if(serverSocket.isPresent()) {
            System.out.println("Start waiting for Requests");
        }
    }

    public Optional<Integer> getLocalPort() {
        return serverSocket.isPresent()? Optional.of(serverSocket.get().getLocalPort()) : Optional.empty();
    }
}
