package graph.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of the Metrics interface.
 * Tracks operation counters and execution time.
 */
public class MetricsImpl implements Metrics {
    private final Map<String, Long> counters;
    private long startTime;
    private long endTime;
    private boolean timerRunning;

    public MetricsImpl() {
        this.counters = new HashMap<>();
        this.startTime = 0;
        this.endTime = 0;
        this.timerRunning = false;
    }

    @Override
    public void reset() {
        counters.clear();
        startTime = 0;
        endTime = 0;
        timerRunning = false;
    }

    @Override
    public void startTimer() {
        startTime = System.nanoTime();
        timerRunning = true;
    }

    @Override
    public void stopTimer() {
        if (timerRunning) {
            endTime = System.nanoTime();
            timerRunning = false;
        }
    }

    @Override
    public long getElapsedTimeNanos() {
        if (timerRunning) {
            return System.nanoTime() - startTime;
        }
        return endTime - startTime;
    }

    @Override
    public double getElapsedTimeMillis() {
        return getElapsedTimeNanos() / 1_000_000.0;
    }

    @Override
    public void incrementCounter(String counterName) {
        incrementCounter(counterName, 1);
    }

    @Override
    public void incrementCounter(String counterName, long amount) {
        counters.put(counterName, counters.getOrDefault(counterName, 0L) + amount);
    }

    @Override
    public long getCounter(String counterName) {
        return counters.getOrDefault(counterName, 0L);
    }

    @Override
    public void printMetrics() {
        System.out.println(getMetricsReport());
    }

    @Override
    public String getMetricsReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Metrics Report ===\n");
        sb.append(String.format("Execution Time: %.3f ms (%.0f ns)\n", 
                               getElapsedTimeMillis(), (double) getElapsedTimeNanos()));
        sb.append("Operation Counters:\n");
        for (Map.Entry<String, Long> entry : counters.entrySet()) {
            sb.append(String.format("  %s: %d\n", entry.getKey(), entry.getValue()));
        }
        return sb.toString();
    }
}
