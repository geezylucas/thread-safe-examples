import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

public class ConcurrentHashMapExample {

    private ConcurrentHashMap<String, Integer> sharedMap = new ConcurrentHashMap<>();

    public void updateData(String key, int value) {
        // Simulating some processing before updating the data
        performProcessing();

        // Updating the shared data in a thread-safe manner
        sharedMap.put(key, value);

        // Simulating some processing after updating the data
        performProcessing();
    }

    public int getData(String key) {
        // Simulating some processing before retrieving the data
        performProcessing();

        // Retrieving the shared data in a thread-safe manner
        int value = sharedMap.getOrDefault(key, 0);

        // Simulating some processing after retrieving the data
        performProcessing();

        return value;
    }

    private void performProcessing() {
        // Simulating some processing time
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // Creating an instance of ConcurrentHashMapExample
        ConcurrentHashMapExample example = new ConcurrentHashMapExample();

        // Creating multiple threads to update and retrieve data concurrently
        Thread updateThread1 = new Thread(() -> {
            example.updateData("A", 10);
        });

        Thread updateThread2 = new Thread(() -> {
            example.updateData("B", 20);
        });

        Thread retrieveThread1 = new Thread(() -> {
            int valueA = example.getData("A");
            System.out.println("Value for key 'A': " + valueA);
        });

        Thread retrieveThread2 = new Thread(() -> {
            int valueB = example.getData("B");
            System.out.println("Value for key 'B': " + valueB);
        });

        // Starting the threads
        updateThread1.start();
        updateThread2.start();
        retrieveThread1.start();
        retrieveThread2.start();

        // Waiting for all threads to finish
        updateThread1.join();
        updateThread2.join();
        retrieveThread1.join();
        retrieveThread2.join();
    }
}
