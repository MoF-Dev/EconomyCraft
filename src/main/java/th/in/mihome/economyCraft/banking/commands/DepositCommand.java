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
public class DepositCommand extends BankingCommand {

    public DepositCommand(ECPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onBankingCommand(Player sender, Bank bank, Command command, String label, String[] args) {
        try {
            PlayerInventory inventory = sender.getInventory();
            
            // maybe need to add the damage data and others.
            ItemStack itemPassed = getItemStackFromStringArgs(
                    extractString(args, 0, "Missing item."),
                    extractString(args, 1, "Missing amount"));
            ItemStack leftOver = inventory.removeItem(itemPassed).get(0);
            int amountRemoved = itemPassed.getAmount() - (leftOver == null ? 0 : leftOver.getAmount());
            // Removes item from inventory
            
            if (!publishDeposit(sender, itemPassed, amountRemoved, bank)) {
                // not success so return items removed
                itemPassed.setAmount(amountRemoved);
                ItemStack errorStack = inventory.addItem(itemPassed).get(0);
                if (errorStack != null) {
                    // we've got items we cant return - LET THE GOVT TAKE IT
                }
            }
            return true;
        } catch (InvalidArgumentException ex) {
            logAndTellSender(sender, Level.INFO, ex, this);
            return false;
        }
    }
    
    private boolean publishDeposit(Player player, ItemStack item, int amount, Bank bank) {
        // TODO: implement mee senpaii~~!
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
