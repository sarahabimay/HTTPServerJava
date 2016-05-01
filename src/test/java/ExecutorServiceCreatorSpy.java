import java.util.concurrent.ExecutorService;

public class ExecutorServiceCreatorSpy extends ExecutorServiceCreator {
    ExecutorService threadPool;
    private boolean hasThreadPoolBeenCreated = false;
    private ExecutorServiceSpy executorServiceSpy;

    public ExecutorServiceCreatorSpy(int sizeOfThreadPool) {
        super(sizeOfThreadPool);
        this.threadPool = null;
    }

    public ExecutorService create() {
        hasThreadPoolBeenCreated = true;
        return executorServiceSpy;
    }

    public boolean hasThreadPoolBeenCreated() {
        return hasThreadPoolBeenCreated;
    }

    public void setExecutorService(ExecutorServiceSpy executorService) {
        this.executorServiceSpy = executorService;
    }
}
