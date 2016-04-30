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
package th.in.mihome.economyCraft;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class BankCommandExecutor extends PluginComponent implements CommandExecutor {

    public BankCommandExecutor(ECPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        switch (Commands.getCommand(cmd)) {
            case DEPOSIT:
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    PlayerInventory inventory = player.getInventory();
                    int amount = Integer.parseInt(args[1]);
                    // maybe need to add the damage data and others.
                    ItemStack itemPassed = new ItemStack(Material.getMaterial(args[0]));

                    while (amount > 0) {
                        ItemStack workingItemStack = null;
                        for (ItemStack eachItemStack : inventory.getContents()) {
                            if (eachItemStack.isSimilar(itemPassed)) {
                                workingItemStack = eachItemStack;
                                break;
                            }
                        }
                        if (workingItemStack == null) {
                            // no item - could already deposit some but just not the specified amount
                            return true;
                        }

                        int actualAmount = Math.min(amount, workingItemStack.getAmount());

                        //add this amount into deposit (SQLLLL)
                        workingItemStack.setAmount(workingItemStack.getAmount() - actualAmount);
                    }
                    return true;
                }
        }
        return false;
    }
}
