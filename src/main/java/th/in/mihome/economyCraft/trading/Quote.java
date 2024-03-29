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
package th.in.mihome.economyCraft.trading;

import org.bukkit.entity.Player;
import th.in.mihome.economyCraft.ECItem;

/**
 *
 * @author Kolatat Thangkasemvathana
 */
public class Quote {

    private int id;
    private final Player trader;
    private final ECItem item;
    private final int quantity;
    private final int value;
    private final long time;
    private final Market market;
    private final Side side;

    public Quote(Player trader, ECItem item, int quantity, int value, Market market, Side side) {
        this.trader = trader;
        this.item = item;
        this.quantity = quantity;
        this.value = value;
        this.time = System.currentTimeMillis();
        this.market = market;
        this.side = side;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the item
     */
    public ECItem getItem() {
        return item;
    }

    /**
     * @return the trader
     */
    public Player getTrader() {
        return trader;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @return the time
     */
    public long getTime() {
        return time;
    }

    /**
     * @return the market
     */
    public Market getMarket() {
        return market;
    }

    /**
     * @return the side
     */
    public Side getSide() {
        return side;
    }

    public static enum Side {
        BID, OFFER
    }

    public int getValue(Market forMarket) {
        if (market == forMarket) {
            return getValue();
        }

        double factor = forMarket.getEconomy()
                .getExportPenalty(getMarket().getLocation(), forMarket.getLocation());

        switch (getSide()) {
            case BID:
                return (int) (getValue() * factor);
            case OFFER:
                return (int) (getValue() / factor);
            default:
                // very dangerous error
                return getValue();
        }
    }
}
