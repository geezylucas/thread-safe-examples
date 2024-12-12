import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

// problem
class RaceConditionsProblemExample {

    private int count = 0;

    public void increment() {
        count++; // Non-atomic operation, race condition here
    }

    public int getCount() {
        return count;
    }
}

// Synchronization
class SynchronizedCounter {

    private int count = 0;

    // Synchronized method to control access to the count variable
    public synchronized void increment() {
        count++;
    }

    public synchronized int getCount() {
        return count;
    }
}

// Atomic Variables
class AtomicCounter {

    private final AtomicInteger count = new AtomicInteger(0);

    public void increment() {
        count.incrementAndGet(); // Atomic operation
    }

    public int getCount() {
        return count.get();
    }
}

// Concurrent Collections
class ConcurrentMapExample {

    private final ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

    public void update(String key, Integer value) {
        map.put(key, value); // Thread-safe operation
    }

    public Integer get(String key) {
        return map.get(key); // Thread-safe operation
    }
}