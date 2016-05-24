/*
 * The MIT License
 *
 * Copyright 2016 Kolatat Thangkasemvathana.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package th.in.mihome.economyCraft.Utils;

/**
 * Wrapper for a value with timeout.
 *
 * @param <T> The type of value.
 *
 * @author Kolatat Thangkasemvathana
 */
public class MortalValue<T> {

    private final long timeout;
    private final T value;

    /**
     * Creates a new value with timeout.
     *
     * @param value The value.
     * @param TTL The timeout, in milliseconds.
     */
    public MortalValue(T value, int TTL) {
        this.timeout = System.currentTimeMillis() + TTL;
        this.value = value;
    }

    /**
     * @return the timeout
     */
    public long getTimeout() {
        return timeout;
    }

    /**
     * @return the value
     */
    public T getValue() {
        return value;
    }

    /**
     * Check if the value is still valid (fresh).
     *
     * The timeout time is calculated by adding the TTL value to the creation
     * time of the object.
     *
     * @return {@code true}, if the current time is before the timeout time of
     * this value, or {@code false}, if the current time has passed the timeout
     * time.
     */
    public boolean isValid() {
        return getTimeout() > System.currentTimeMillis();
    }
}