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

import org.bukkit.Location;
import org.bukkit.Material;
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
        switch (Commands.getCommand(command)) {
            case BID:
                Player player = requirePlayer(sender);
                if (player != null) {
                    Location pl = player.getLocation();
                    Market cm = Place.getNearest(pl, plugin.getEconomy().getMarkets());
                    if (cm.isNear(pl)) {
                        short damage = 0;
                        String itemSpecs[] = args[0].split(",");
                        if (itemSpecs.length > 1) {
                            damage = Short.parseShort(itemSpecs[1]);
                        }
                        Material m;
                        try {
                            m = Material.getMaterial(Integer.parseInt(itemSpecs[0]));
                        } catch (NumberFormatException ex) {
                            m = Material.getMaterial(itemSpecs[0]);
                        }
                        ItemStack item = new ItemStack(m, Integer.parseInt(args[1]), damage);
                        int value = (int) (Double.parseDouble(args[2]) * 100);
                        Quote q = new Quote(player, item, value, cm, Quote.Side.BID);
                        cm.list(q);
                    } else {
                        player.sendMessage(new String[]{
                            "You must be in a market to use this command!",
                            String.format("The nearest market is %s, %s, which is %dm from here!",
                            cm.getName(), cm.getAddress(),
                            (int) pl.distance(cm.getLocation()))
                        });

                    }
                }
                break;
            case OFFER:
            case REMOVE_BID:
            case REMOVE_OFFER:
            case LIST_QUOTES:
            default:
                return false;
        }
        return true;
    }

}
