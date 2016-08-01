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
import th.in.mihome.economyCraft.*;
import th.in.mihome.economyCraft.trading.Market;

/**
 *
 * @author Kolatat Thangkasemvathana
 */
public abstract class TradingCommand extends PlayerCommand {

    public TradingCommand(ECPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onPlayerCommand(Player sender, Command command, String label, String[] args) {
        try {
            Market market = Place.requireValidNearest(sender, plugin.getMarkets());
            return onTradingCommand(sender, market, command, label, args);
        } catch (UnfulfilledRequirementException ex) {
            logAndTellSender(sender, UnfulfilledRequirementException.DEFAULT_LOG_LEVEL, ex, this);
            return false;
        }
    }

    public abstract boolean onTradingCommand(Player sender, Market market, Command command, String label, String[] args);

}
