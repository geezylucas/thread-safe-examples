import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadLocalVariablesExample {

    // private static AtomicInteger counter = new AtomicInteger();
    private static ThreadLocal<Integer> counter = ThreadLocal.withInitial(() -> 0);

    public static void main(String[] args) throws InterruptedException {
        // Use thread pools and never manage threads on you own
        /*
         * ExecutorService executorService = Executors.newFixedThreadPool(10);
         * 
         * for (int i = 0; i < 10; i++) {
         * executorService.submit(() -> System.out
         * .println(Thread.currentThread().getName() + " value: " +
         * counter.incrementAndGet()));
         * }
         * 
         */

        // default value is null
        // since Java 8 you can init the ThreadLocal with a default value
        // ThreadLocal.withInitial(() -> 0);
        // but I left it like this for illustrative purposes
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> {
                Integer value = counter.get();
                counter.set(value + 1);
                System.out.println(Thread.currentThread().getName() + " value: " + value);
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
    }
}
