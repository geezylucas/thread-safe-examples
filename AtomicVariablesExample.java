import java.util.concurrent.atomic.AtomicInteger;

public class AtomicVariablesExample {

    private final AtomicInteger counter = new AtomicInteger(0);
    // private int counter = 0;

    /*
     * public synchronized void increment() {
     * counter++;
     * }
     */

    public void increment() {
        counter.incrementAndGet();
    }

    public int getCounter() {
        return counter.get();
    }

    public static void main(String[] args) throws InterruptedException {
        final AtomicVariablesExample atomicVariablesExample = new AtomicVariablesExample();

        Runnable task = () -> {
            for (int i = 0; i < 1000; i++) {
                atomicVariablesExample.increment();
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Counter value: " + atomicVariablesExample.getCounter());
    }
}