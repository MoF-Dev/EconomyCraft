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

import java.util.logging.Level;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import th.in.mihome.economyCraft.ECPlugin;
import th.in.mihome.economyCraft.InvalidArgumentException;
import th.in.mihome.economyCraft.UnfulfilledRequirementException;
import th.in.mihome.economyCraft.trading.Market;
import th.in.mihome.economyCraft.trading.Quote;

import static th.in.mihome.economyCraft.AbstractCommandExecutor.ensureGoods;
import th.in.mihome.economyCraft.ECItem;

/**
 *
 * @author Kolatat Thangkasemvathana
 */
public class OfferCommand extends TradingCommand {

    public OfferCommand(ECPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onTradingCommand(Player sender, Market market, Command command, String label, String[] args) {
        try {
            ItemStack mcItem = sender.getInventory().getItemInMainHand();
            if (mcItem == null) {
                throw new InvalidArgumentException("You must offer some item!");
            }
            int amount = extractInt(args, 1, null);
            if (amount <= 0) {
                amount = mcItem.getAmount();
            }
            mcItem.setAmount(Math.min(mcItem.getAmount(), amount));
            int value = extractMonetaryValue(args, 0, false, "Missing order price.");

            ECItem item = plugin.getItem(mcItem);

            // TODO warn player if this is a dum move?
            // Okay to use ItemStack since this directly deals with player's inventory.
            ensureGoods(sender, mcItem);
            market.list(new Quote(sender, item, mcItem.getAmount(), value, market, Quote.Side.OFFER));
            sender.sendMessage("Order listed.");
            return true;
        } catch (UnfulfilledRequirementException ex) {
            logAndTellSender(sender, UnfulfilledRequirementException.DEFAULT_LOG_LEVEL, ex, market);
            return false;
        }
    }

}
