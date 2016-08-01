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

import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.OfflinePlayer;

/**
 * The Central Bank maintains monetary accounts of all individuals.
 *
 * @author Kolatat Thangkasemvathana
 */
public class CentralBank extends PluginComponent {

    public final String CENTRAL_BANK_ACCOUNT_ID = "EC_Central_Bank";

    public CentralBank(ECPlugin plugin) {
        super(plugin);
    }

    public boolean deposit(OfflinePlayer player, int amount) {
        String playerId = getPlayerId(player);
        double dAmount = amount / 100d;
        if (prepareCash(player, amount)) {
            if (plugin.getWalletProvider().withdrawPlayer(player, dAmount).transactionSuccess()) {
                plugin.getDb().updateBankAccount(playerId, amount);
                return true;
            } else {
                warning("MoneyProvider was unable to deduct $.2f from \"%s\" (%s)",
                        dAmount,
                        player.getName(),
                        playerId);
                return false;
            }
        } else {
            // not enough money in wallet
            info("\"%s\" (%s) does not have the necessary cash.",
                    player.getName(),
                    playerId);
            return false;
        }
    }

    public int getAccountBalance(OfflinePlayer player) {
        return getAccountBalance(getPlayerId(player));
    }

    public int getAccountBalance(String accountName) {
        return plugin.getDb().getBankAccount(accountName);
    }

    public OfflinePlayer getPlayer(String name) {
        return plugin.getServer().getOfflinePlayer(name);
    }

    public OfflinePlayer getPlayer(UUID id) {
        return plugin.getServer().getOfflinePlayer(id);
    }

    /**
     * Retrieve a unique key associated with a player. The implementation of
     * what qualifies as a unique key depends on the plugin configuration. By
     * default, this is the player's UUID.
     *
     * @param player The player.
     * @return A unique, persistent key associated with the given player.
     */
    public String getPlayerId(OfflinePlayer player) {
        switch (plugin.config.PLAYER_ID_SOURCE) {
            case NAME:
                return player.getName();
            case UNIQUE_ID:
                return player.getUniqueId().toString();
            default:
                return String.valueOf(player.hashCode());
        }
    }

    public boolean pay(OfflinePlayer from, OfflinePlayer to, int amount, PaymentMethod method) {
        double dAmount = amount / 100d;
        String fromId = getPlayerId(from);
        String toId = getPlayerId(to);
        switch (method) {
            case CASH:
                if (prepareCash(from, amount)) {
                    plugin.getWalletProvider().withdrawPlayer(from, dAmount);
                    plugin.getWalletProvider().depositPlayer(to, dAmount);
                    return true;
                }
                break;
            case EFT:
                if (prepareCredit(fromId, amount)) {
                    plugin.getDb().updateBankAccount(fromId, -amount);
                    plugin.getDb().updateBankAccount(toId, amount);
                    return true;
                }
                break;
            default:
                logException(new UnsupportedOperationException("Not yet implemented."), Level.SEVERE);
                return false;
        }
        return false;
    }

    /**
     * Withdraw money from a player's account into his/her wallet.
     *
     * @param player The player.
     * @param amount The amount in cents.
     * @return {@code true} on success, otherwise {@code false}.
     */
    public boolean withdraw(OfflinePlayer player, int amount) {
        String playerId = getPlayerId(player);
        double dAmount = amount / 100d;
        if (prepareCredit(playerId, amount)) {
            plugin.getDb().updateBankAccount(playerId, -amount);
            if (plugin.getWalletProvider().depositPlayer(player, dAmount).transactionSuccess()) {
                return true;
            } else {
                warning("MoneyProvider was unable to provide \"%s\" (%s) with $.2f.",
                        player.getName(),
                        playerId,
                        dAmount);
                return false;
            }
        } else {
            // not enough money in account
            info("\"%s\" (%s) does not have the necessary funds.",
                    player.getName(),
                    playerId);
            return false;
        }
    }

    private boolean prepareCash(OfflinePlayer player, int amount) {
        return plugin.getWalletProvider().has(player, amount / 100d);
    }

    private boolean prepareCredit(String accountName, int amount) {
        return getAccountBalance(accountName) >= amount;
    }

    public static enum PaymentMethod {

        /**
         * Payment using cash...
         */
        CASH,
        /**
         * Electronic funds transfer (EFT) is the electronic transfer of money
         * from one bank account to another, through computer-based systems and
         * without the direct intervention of bank staff.
         *
         * https://en.wikipedia.org/wiki/Electronic_funds_transfer
         */
        EFT // , CREDIT_CARD, DEBIT_CARD, CHEQUE, wtv see roadmap
    }

}
