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
package th.in.mihome.economyCraft.trading.commands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import th.in.mihome.economyCraft.ECPlugin;
import th.in.mihome.economyCraft.trading.Market;
import th.in.mihome.economyCraft.trading.Quote;

/**
 *
 * @author Kolatat Thangkasemvathana
 */
public class ListQuotesCommand extends TradingCommand {

    public ListQuotesCommand(ECPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onTradingCommand(Player sender, Market market, Command command, String label, String[] args) {
        int count = 0;
        for (Quote quote : market.getQuotesFor(sender)) {
            count++;
            sender.sendMessage(String.format("#%d - %s (%d:%d) %s %d at %s",
                    quote.getId(),
                    quote.getItem().getMaterial(),
                    quote.getItem().getId(),
                    quote.getItem().getDurability(),
                    quote.getSide(),
                    quote.getQuantity(),
                    plugin.getWalletProvider().format(quote.getValue() / 100.0)));
        }
        sender.sendMessage("You have " + count + " quote(s).");
        return true;
    }

}
