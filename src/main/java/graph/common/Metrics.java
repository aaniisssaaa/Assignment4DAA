package graph.common;

/**
 * Interface for collecting performance metrics during algorithm execution.
 */
public interface Metrics {
    /**
     * Resets all counters and timers to initial state.
     */
    void reset();

    /**
     * Starts the timer for measuring execution time.
     */
    void startTimer();

    /**
     * Stops the timer and records elapsed time.
     */
    void stopTimer();

    /**
     * Gets the elapsed time in nanoseconds.
     * @return execution time in nanoseconds
     */
    long getElapsedTimeNanos();

    /**
     * Gets the elapsed time in milliseconds.
     * @return execution time in milliseconds
     */
    double getElapsedTimeMillis();

    /**
     * Increments a named counter.
     * @param counterName the name of the counter
     */
    void incrementCounter(String counterName);

    /**
     * Increments a named counter by a specific amount.
     * @param counterName the name of the counter
     * @param amount the amount to increment
     */
    void incrementCounter(String counterName, long amount);

    /**
     * Gets the value of a named counter.
     * @param counterName the name of the counter
     * @return the counter value
     */
    long getCounter(String counterName);

    /**
     * Prints all metrics to console.
     */
    void printMetrics();

    /**
     * Returns a formatted string with all metrics.
     * @return formatted metrics string
     */
    String getMetricsReport();
}
