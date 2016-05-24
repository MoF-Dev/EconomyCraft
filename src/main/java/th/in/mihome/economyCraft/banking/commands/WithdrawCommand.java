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
package th.in.mihome.economyCraft.banking.commands;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import th.in.mihome.economyCraft.ECPlugin;
import th.in.mihome.economyCraft.InvalidArgumentException;
import th.in.mihome.economyCraft.banking.Bank;

/**
 *
 * @author Kolatat Thangkasemvathana
 */
public class WithdrawCommand extends BankingCommand {

    public WithdrawCommand(ECPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onBankingCommand(Player sender, Bank bank, Command command, String label, String[] args) {
        try {
            ItemStack itemPassed = getItemStackFromStringArgs(
                    extractString(args, 0, "Missing item."),
                    extractString(args, 1, "Missing amount"));
            int amount = itemPassed.getAmount();
            PlayerInventory inventory = sender.getInventory();
            ItemStack returnedItem = getItemInBank(itemPassed, bank, sender);
            // NEEDS SERIOUS CHECKING
            if (returnedItem.getAmount() > itemPassed.getAmount()) {
                int amountWithdrawn = Math.min(returnedItem.getAmount(), itemPassed.getAmount());
                ItemStack leftover = new ItemStack(Material.getMaterial(args[0]), returnedItem.getAmount() - itemPassed.getAmount());
                ItemStack itemAdded = new ItemStack(Material.getMaterial(args[0]), amountWithdrawn);
                inventory.addItem(itemAdded);

                if (!publishWithdraw(sender, itemAdded, amountWithdrawn, bank)) {
                    itemPassed.setAmount(amountWithdrawn);
                    inventory.remove(itemPassed);
                }
            } else {
                inventory.addItem(itemPassed);
                if (!publishWithdraw(sender, itemPassed, amount, bank)) {
                    itemPassed.setAmount(amount);
                    inventory.remove(itemPassed);
                }
            }
            return true;
        } catch (InvalidArgumentException ex) {
            logAndTellSender(sender, Level.INFO, ex, this);
            return false;
        }
    }

    private boolean publishWithdraw(Player player, ItemStack item, int amount, Bank bank) {
        // TODO: implement mee senpaii~~!
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
