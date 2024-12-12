import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// problem
class DeadlockProblemExample {

    private final Object lock1 = new Object();
    private final Object lock2 = new Object();

    public void method1() {
        synchronized (lock1) {
            synchronized (lock2) {
                // Critical section
            }
        }
    }

    public void method2() {
        synchronized (lock2) {
            synchronized (lock1) {
                // Critical section
            }
        }
    }

}

// Lock Ordering
class LockOrderingExample {

    private final Object lock1 = new Object();
    private final Object lock2 = new Object();

    public void method1() {

        synchronized (lock1) {
            // Simulate some operations
            synchronized (lock2) {
                // Perform operations that require lock1 and lock2
            }
        }
    }

    public void method2() {
        synchronized (lock1) { // Note: Using the same order as method1
            synchronized (lock2) {
                // Perform operations that require lock1 and lock2
            }
        }
    }
}

// Try-Lock
class TryLockExample {

    private final Lock lock = new ReentrantLock();

    public void performTask() {

        if (lock.tryLock()) {
            try {
                // Perform task that requires the lock
            } finally {
                lock.unlock();
            }
        } else {
            // Perform alternative actions or retry later
        }
    }
}