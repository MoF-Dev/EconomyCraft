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
package th.in.mihome.economyCraft.banking;

import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import th.in.mihome.economyCraft.*;

public class BankCommandExecutor extends AbstractCommandExecutor {

    public BankCommandExecutor(ECPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        switch (Commands.getCommand(cmd)) {
            case DEPOSIT:
                Player player = requirePlayer(sender);
                if (player != null) {
                    deposit(player, args);
                }
                return true;
            default:
                return false;
        }
    }

    private void deposit(Player player, String[] args) {
        int amount = Integer.parseInt(args[1]);
        if (amount <= 0) {
            player.sendMessage("You are guanteen.");//wew
            return;
        }
        // Check for valid amount argument

        Bank bank = getNearestBank(player);
        if (!bank.isNear(player.getLocation())) {
            player.sendMessage("This command may not be performed outside of a bank.");
            return;
        }
        // Check location for valid bank

        PlayerInventory inventory = player.getInventory();
        // maybe need to add the damage data and others.
        ItemStack itemPassed = new ItemStack(Material.getMaterial(args[0]), amount);
        ItemStack leftOver = inventory.removeItem(itemPassed).get(0);
        int amountRemoved = itemPassed.getAmount() - (leftOver == null ? 0 : leftOver.getAmount());
        // Removes item from inventory

        if (!publishDeposit(player, itemPassed, amountRemoved, bank)) {
            // not success so return items removed
            itemPassed.setAmount(amountRemoved);
            ItemStack errorStack = inventory.addItem(itemPassed).get(0);
            if (errorStack != null) {
                // we've got items we cant return - LET THE GOVT TAKE IT
            }
        }
    }

    private ItemStack getItemInBank(ItemStack itemPassed, Bank bank, Player player) {
        // TODO: implement mee senpaii~~!
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void withdraw(Player player, String[] args) {
        int amount = Integer.parseInt(args[1]);
        if (amount <= 0) {
            player.sendMessage("You are guanteen.");//weeew
            return;
        }

        Bank bank = getNearestBank(player);
        if (!bank.isNear(player.getLocation())) {
            player.sendMessage("This command may not be performed outside of a bank.");
            return;
        }
        // Check location for valid nearby bank

        ItemStack itemPassed = new ItemStack(Material.getMaterial(args[0]), amount);
        PlayerInventory inventory = player.getInventory();
        ItemStack returnedItem = getItemInBank(itemPassed, bank, player);
        if (returnedItem.getAmount() > 0) {
            // withdraw then add then return
            if (publishWithdraw(player, returnedItem, returnedItem.getAmount(), bank)) {
                ItemStack errorStack = inventory.addItem(returnedItem).get(0);
                if (errorStack != null) {
                    // redeposit items
                    if (!publishDeposit(player, errorStack, errorStack.getAmount(), bank)) {
                        // not enough room in inventory and cannot redeposit gg no re
                    }
                }
            } else {
                // cannot withdraw
            }
        }
    }

    private Bank getNearestBank(Player player) {
        Bank nearestBank = null;
        double nearestDistance = Double.POSITIVE_INFINITY;
        ArrayList<Bank> banks = plugin.getBanks();
        for (Bank bank : banks) {
            double distance = player.getLocation().distance(bank.getLocation());
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestBank = bank;
            }
        }
        return nearestBank;
    }

    private boolean publishDeposit(Player player, ItemStack item, int amount, Bank bank) {
        // TODO: implement mee senpaii~~!
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private boolean publishWithdraw(Player player, ItemStack item, int amount, Bank bank) {
        // TODO: implement mee senpaii~~!
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
