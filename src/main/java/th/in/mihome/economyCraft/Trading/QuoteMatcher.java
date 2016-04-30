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
package th.in.mihome.economyCraft.Trading;

import java.util.Queue;
import th.in.mihome.economyCraft.ECPlugin;
import th.in.mihome.economyCraft.Market;
import th.in.mihome.economyCraft.PluginComponent;
import th.in.mihome.economyCraft.Quote;

/**
 *
 * @author Kolatat Thangkasemvathana
 */
public abstract class QuoteMatcher extends PluginComponent {

    public QuoteMatcher(ECPlugin plugin) {
        super(plugin);
    }

    public abstract Queue<Quote> getBids();

    public abstract Queue<Quote> getOffers();

    /**
     * Compares the priority of the given {@code Quote} with respect to the
     * {@code Market} they are trading in.
     *
     * This is dependent on the {@code Quote.Side} of the quotes and the
     * matching algorithm. Generally, the quote with the highest priority should
     * be matched first.
     *
     * @param q1 the first {@code Quote} to compare
     * @param q2 the second {@code Quote} to compare
     * @param m the {@code Market} that the quotes are traded in
     *
     * @return the value {@code 0} if {@code q1} has the same priority as
     * {@code q2}; a value less than {@code 0} if {@code q1} has a priority less
     * than {@code q2}; and a value greater than {@code 0} if {@code q1} has a
     * priority greater than {@code q2}.
     */
    public abstract int compare(Quote q1, Quote q2, Market m);

    /**
     * Compares the priority of the given {@code Quote} within the same
     * {@code Market}.
     *
     * @param q1 the first {@code Quote} to compare
     * @param q2 the second {@code Quote} to compare
     *
     * @return the value {@code 0} if {@code q1} has the same priority as
     * {@code q2}; a value less than {@code 0} if {@code q1} has a priority less
     * than {@code q2}; and a value greater than {@code 0} if {@code q1} has a
     * priority greater than {@code q2}.
     */
    public int compare(Quote q1, Quote q2) {
        // assumes same market
        return compare(q1, q2, q1.getMarket());
    }

}
