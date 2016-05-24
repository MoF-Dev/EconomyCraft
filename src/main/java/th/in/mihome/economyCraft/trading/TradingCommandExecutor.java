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

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import th.in.mihome.economyCraft.*;

/**
 *
 * @author Kolatat Thangkasemvathana
 */
public class TradingCommandExecutor extends AbstractCommandExecutor {

    public TradingCommandExecutor(ECPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            Player player;
            Market market;
            ItemStack item;
            int value;
            int quote_id;
            switch (Commands.getCommand(command)) {
                case BID:
                    player = requirePlayer(sender);
                    market = Place.requireValidNearest(player, plugin.getEconomy().getMarkets());
                    item = getItemStackFromStringArgs(args[1], args[2]);
                    value = parseMonetaryValue(args[3], false);
                    market.list(new Quote(player, item, value, market, Quote.Side.BID));
                    player.sendMessage("Bid listed.");
                    return true;
                case OFFER:
                    player = requirePlayer(sender);
                    market = Place.requireValidNearest(player, plugin.getEconomy().getMarkets());
                    item = player.getInventory().getItemInMainHand();
                    int amount = Integer.MAX_VALUE;
                    if (args.length > 2) {
                        amount = Integer.parseInt(args[2]);
                    }
                    if (amount <= 0 || item == null) {
                        throw new InvalidArgumentException("You must offer some item!");
                    }
                    item.setAmount(Math.min(item.getAmount(), amount));
                    value = parseMonetaryValue(args[1], false);
                    // TODO warn player if this is a dum move?
                    ensureGoods(player, item);
                    market.list(new Quote(player, item, value, market, Quote.Side.OFFER));
                    return true;
                case BUY:
                    player = requirePlayer(sender);
                    market = Place.requireValidNearest(player, plugin.getEconomy().getMarkets());
                    item = getItemStackFromStringArgs(args[1], args[2]);
                    value = parseMonetaryValue(args[3], false);
                    market.buy(player, item, value);
                    return true;
                case REMOVE_QUOTE:
                    player = requirePlayer(sender);
                    market = Place.requireValidNearest(player, plugin.getEconomy().getMarkets());
                    quote_id = Integer.parseInt(args[1]);
                    market.remove(quote_id);
                    return true;
                case LIST_QUOTES:
                    player = requirePlayer(sender);
                    market = Place.requireValidNearest(player, plugin.getEconomy().getMarkets());
                    for (Quote quote : market.getQuotesFor(player)) {
                        player.sendMessage(String.format("#%d - %s (%d:%d) %s %d at %s\n.",
                                quote.getId(),
                                quote.getItem().getType(),
                                quote.getItem().getTypeId(),
                                quote.getItem().getDurability(),
                                quote.getSide(),
                                quote.getQuantity(),
                                plugin.getEconomy().getEngine().format(quote.getValue() / 100.0)));
                    }
            }
        } catch (UnfulfilledRequirementException ex) {
            sender.sendMessage(ex.getMessage());
            return true;
        }
        return false;
    }

}
