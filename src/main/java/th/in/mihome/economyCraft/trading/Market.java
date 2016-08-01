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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import th.in.mihome.economyCraft.ECItem;
import th.in.mihome.economyCraft.ECPlugin;
import th.in.mihome.economyCraft.Place;
import th.in.mihome.economyCraft.database.Transaction;
import th.in.mihome.economyCraft.database.TransactionResult;
import th.in.mihome.economyCraft.database.TransactionType;

/**
 *
 * @author Kolatat Thangkasemvathana
 */
public class Market extends Place {

    HashMap<ECItem, QuoteMatcher> matchers = new HashMap<>();

    public Market(ECPlugin plugin, ResultSet rs) throws SQLException {
        this(plugin,
                rs.getInt("id"),
                rs.getString("name"),
                new Location(plugin.getServer().getWorld(rs.getString("world")),
                        rs.getDouble("x"),
                        rs.getDouble("y"),
                        rs.getDouble("z")),
                rs.getString("address"));
    }

    public Market(ECPlugin plugin, int id, String name, Location location, String address) {
        super(plugin, id, name, location, address);
    }

    public ECEconomy getEconomy() {
        return plugin.getEconomy();
    }

    @Override
    public double getRadius() {
        return plugin.config.MARKET_RADIUS;
    }

    public boolean isValid() {
        return location.getBlock().getType() == plugin.config.MARKET_CORNERSTONE;
    }

    public Quote seeBest(ECItem type, Quote.Side side) {
        QuoteMatcher matcher = matchers.get(type);
        if (matcher == null) {
            return null;
        }
        switch (side) {
            case BID:
                return matcher.getBids().peek();
            case OFFER:
                return matcher.getOffers().peek();
            default:
                return null;
        }
    }

    public void list(Quote q) {
        QuoteMatcher matcher = matchers.get(q.getItem());
        if (matcher == null) {
            matcher = plugin.getEconomy().newMatcher(this);
            matchers.put(q.getItem(), matcher);
        }
        switch (q.getSide()) {
            case BID:
                matcher.getBids().add(q);
                break;
            case OFFER:
                matcher.getOffers().add(q);
                break;
        }
    }

    public void unlist(Quote q) {
        QuoteMatcher matcher = matchers.get(q.getItem());
        if (matcher != null) {
            switch (q.getSide()) {
                case BID:
                    matcher.getBids().remove(q);
                    break;
                case OFFER:
                    matcher.getOffers().remove(q);
                    break;
            }
        }
    }

    public void match() {
        for (Entry<ECItem, QuoteMatcher> itemEntry : matchers.entrySet()) {
            if (itemEntry.getValue().getBids().isEmpty() || itemEntry.getValue().getOffers().isEmpty()) {
                continue;
            }
            Quote tmpQuote;
            ArrayList<Quote> bids = new ArrayList<>();
            ArrayList<Quote> offers = new ArrayList<>();
            for (Market market : plugin.getMarkets()) {
                tmpQuote = market.seeBest(itemEntry.getKey(), Quote.Side.BID);
                if (tmpQuote != null) {
                    bids.add(tmpQuote);
                }
                tmpQuote = market.seeBest(itemEntry.getKey(), Quote.Side.OFFER);
                if (tmpQuote != null) {
                    offers.add(tmpQuote);
                }
            }
            Quote bestBid = Collections.max(bids, (q1, q2) -> itemEntry.getValue().compare(q1, q2, this));
            Quote bestOffer = Collections.max(offers, (q1, q2) -> itemEntry.getValue().compare(q1, q2, this));
            assert (bestBid.getItem() == bestOffer.getItem());
            bestBid.getMarket().unlist(bestBid);
            bestOffer.getMarket().unlist(bestOffer);

            int bid = bestBid.getValue();
            int xBid = bestBid.getValue(this);
            int offer = bestOffer.getValue();
            int xOffer = bestOffer.getValue(this);
            int tp = Math.abs(offer - xOffer);

            if (offer + tp >= bid) {
                // theres a match
                int quant = Math.min(bestBid.getQuantity(), bestOffer.getQuantity());
                int realAmount = offer * quant;
                int realTp = tp * quant;
                Transaction t = new Transaction(TransactionType.PURCHASE, bestBid.getTrader(), bestOffer.getTrader(), realAmount);
                TransactionResult tr = plugin.getDb().process(t);
                if (tr.isSuccess()) {
                    plugin.getEconomy().getCentralLogistics().deliver(bestOffer.getMarket().getLocation(), bestBid.getMarket().getLocation(), bestBid.getTrader(), realTp);

                }

            }
        }
    }

    public void buy(Player player, ECItem item, int quantity, int value) {
        // TODO: implement mee senpaii~~!
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void remove(Player player, int quoteId) {
        // TODO: implement mee senpaii~~!
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Iterable<Quote> getQuotesFor(Player player) {
        // TODO: implement mee senpaii~~!
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
