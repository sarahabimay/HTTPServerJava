import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceCreator {
    private final int numberOfThreads;

    public ExecutorServiceCreator(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    public ExecutorService create() {
        return Executors.newFixedThreadPool(numberOfThreads);
    }
}
