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
package th.in.mihome.economyCraft.database;

import org.bukkit.entity.Player;

/**
 *
 * @author Kolatat Thangkasemvathana
 */
public class Transaction {

    private final TransactionType type;
    private final Player buyer;
    private final Player seller;
    private final int amount;
    private final long time;

    public Transaction(TransactionType type, Player buyer, Player seller, int amount, long time) {
        this.type = type;
        if (amount < 0) {
            amount = -amount;
            this.buyer = seller;
            this.seller = buyer;
        } else {
            this.buyer = buyer;
            this.seller = seller;
        }
        this.amount = amount;
        this.time = time;
    }

    public Transaction(TransactionType type, Player buyer, Player seller, int amount) {
        this(type, buyer, seller, amount, System.currentTimeMillis());
    }

    /**
     * @return the amount
     */
    public int getAmount() {
        return amount;
    }

    /**
     * @return the buyer
     */
    public Player getBuyer() {
        return buyer;
    }

    /**
     * @return the seller
     */
    public Player getSeller() {
        return seller;
    }

    /**
     * @return the time
     */
    public long getTime() {
        return time;
    }

    /**
     * @return the type
     */
    public TransactionType getType() {
        return type;
    }

    public String getReference() {
        return String.format("TRF%03d%024d", getType().ordinal(), hashCode());
    }

}
