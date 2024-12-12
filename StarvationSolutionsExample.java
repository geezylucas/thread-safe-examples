import java.util.concurrent.locks.ReentrantLock;

// Fair Locks
class FairLockExample {

    private final ReentrantLock fairLock = new ReentrantLock(true); // Pass true for fairness

    public void performTask() {
        try {
            fairLock.lock();
            // Critical section code goes here
            // Ensure this operation does not take too long to avoid impacting other threads
        } finally {
            fairLock.unlock();
        }
    }
}

// Managing Thread Priorities
class PriorityExample {

    public void startThreads() {

        Thread highPriorityThread = new Thread(() -> {
            // High-priority task
        });

        Thread lowPriorityThread = new Thread(() -> {
            // Low-priority task
        });

        // Set priorities carefully
        highPriorityThread.setPriority(Thread.MAX_PRIORITY);
        lowPriorityThread.setPriority(Thread.MIN_PRIORITY);
        lowPriorityThread.start();
        highPriorityThread.start();

        // Consider dynamically adjusting priorities if a thread is starved
    }
}