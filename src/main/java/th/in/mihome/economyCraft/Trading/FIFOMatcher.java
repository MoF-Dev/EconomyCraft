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
import java.util.concurrent.PriorityBlockingQueue;
import th.in.mihome.economyCraft.ECPlugin;
import th.in.mihome.economyCraft.Market;
import th.in.mihome.economyCraft.Quote;

/**
 *
 * @author Kolatat Thangkasemvathana
 */
public class FIFOMatcher extends QuoteMatcher {

    private final Queue<Quote> bids;
    private final Queue<Quote> offers;

    public FIFOMatcher(ECPlugin plugin) {
        super(plugin);
        this.offers = new PriorityBlockingQueue<>(plugin.config.TRADING_QUEUE_INITIAL_SIZE, (q1, q2) -> -compare(q1, q2));
        this.bids = new PriorityBlockingQueue<>(plugin.config.TRADING_QUEUE_INITIAL_SIZE, (q1, q2) -> -compare(q1, q2));
    }

    @Override
    public int compare(Quote q1, Quote q2, Market m) {
        // quote is assumed to be of same side

        // c is big when q1 is big
        int c = Double.compare(q1.getValue(m), q2.getValue(m));

        switch (q1.getSide()) {
            case BID:
                c = -c; // quote is better if they are sold for cheaper
                break;
            case OFFER:
                break;
        }

        if (c == 0) {
            return Long.compare(q1.getTime(), q2.getTime());
        }

        return c;
    }

    @Override
    public Queue<Quote> getBids() {
        return bids;
    }

    @Override
    public Queue<Quote> getOffers() {
        return offers;
    }

}
