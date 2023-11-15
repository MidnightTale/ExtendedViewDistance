package club.tesseract.extendedviewdistance.core.data;

/**
 * Network Traffic
 */
public final class NetworkTraffic {
    private volatile int value = 0;

    /**
     * use
     * @param length number of bytes
     */
    public synchronized void use(int length) {
        value += length;
    }

    /**
     * @return value
     */
    public synchronized int get() {
        return value;
    }

    /**
     * @param length number of bytes
     * @return exceed
     */
    public synchronized boolean exceed(int length) {
        return value >= length;
    }

    /**
     * next tick
     */
    public void next() {
        value = 0;
    }
}
