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

import java.util.HashMap;

/**
 * Wrapper for values that can and shall be cached.
 *
 * This class is a wrapper for a computationally expensive function that caches
 * the return value of the function.
 *
 * @author Kolatat Thangkasemvathana
 * @param <T> The type of the returned value.
 */
public class Cacheable<T> implements Function<T> {

    private final HashMap<Object[], MortalValue<T>> valueCacheMap = new HashMap<>();

    private final Function<T> function;
    
    private int TTL;

    @Override
    public T calculate(Object... args) {
        if (function == null) {
            throw new UnsupportedOperationException("Not supported yet.");
        } else {
            return function.calculate(args);
        }
    }

    /**
     * Creates a new cacheable with the specified function.
     * @param function The function used for the calculation of values.
     * @param TTL The TTL of the value cache.
     */
    public Cacheable(Function<T> function, int TTL) {
        this.function = function;
        this.TTL = TTL;
    }
    
    /**
     * Get the TTL.
     * @return The TTL, in milliseconds.
     */
    public int getTTL(){
        return TTL;
    }
    
    /**
     * Set the TTL.
     * @param TTL Set the TTL, in milliseconds.
     */
    public void setTTL(int TTL){
        this.TTL=TTL;
    }

    /**
     * Force the cache to recalculate the value of the function even if it is
     * still fresh.
     *
     * @param args The arguments for the function.
     * @return The result of the function.
     */
    public T forceGet(Object... args) {
        MortalValue<T> value = new MortalValue<>(calculate(args),TTL);
        valueCacheMap.put(args, value);
        return value.getValue();
    }

    /**
     * Get the value of the function.
     *
     * If calculation for the given input has been computed before, and it is
     * not stale, then this method will return the cached output and not perform
     * the calculation. Use {@code forceGet()} to force recalculation.
     *
     * @param args The arguments for the function.
     * @return The result of the function.
     */
    public T get(Object... args) {
        MortalValue<T> value;
        if ((value = valueCacheMap.get(args)) == null || !value.isValid()) {
            return forceGet(args);
        }
        return value.getValue();
    }

    /**
     * Manually override the cache of a calculation.
     *
     * @param value The value to override.
     * @param TTL The valid lifetime of the cache, in milliseconds, that this
     * value shall be fresh for.
     * @param args The arguments for the function.
     */
    public void setWithTTL(T value, int TTL, Object... args) {
        MortalValue<T> v = new MortalValue<>(value, TTL);
        valueCacheMap.put(args, v);
    }
    
    /**
     * Manually override the cache of a calculation.
     *
     * @param value The value to override.
     * @param args The arguments for the function.
     */
    public void set(T value, Object... args){
        setWithTTL(value, TTL, args);
    }

}
