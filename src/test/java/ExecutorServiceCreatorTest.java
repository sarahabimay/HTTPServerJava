import org.junit.Test;

import java.util.concurrent.ExecutorService;

import static org.junit.Assert.assertEquals;

public class ExecutorServiceCreatorTest {
    @Test
    public void createAPoolOfThreads() {
        int numberOfThreads = 1;
        ExecutorServiceCreator executorServiceCreator = new ExecutorServiceCreator(numberOfThreads);
        ExecutorService executorService = executorServiceCreator.create();
        assertEquals(false, executorService.isShutdown());
    }
}
