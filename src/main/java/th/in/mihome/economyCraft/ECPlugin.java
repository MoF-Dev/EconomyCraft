/*
 * The MIT License
 *
 * Copyright 2016 MoF Development.
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

import java.sql.ResultSet;
import th.in.mihome.economyCraft.database.Database;
import java.sql.SQLException;
import java.util.ArrayList;
import th.in.mihome.economyCraft.banking.BankCommandExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import th.in.mihome.economyCraft.banking.Bank;
import th.in.mihome.economyCraft.trading.ECEconomy;
import th.in.mihome.economyCraft.trading.Market;
import th.in.mihome.economyCraft.trading.TradingCommandExecutor;

/**
 *
 * @author Kolatat Thangkasemvathana
 */
public class ECPlugin extends JavaPlugin {

    private Chat chat;

    private MainCommandExecutor mainCmdExecutor;
    private BankCommandExecutor bankCmdExecutor;
    private TradingCommandExecutor tradeCmdExecutor;
    public Configuration config;
    private Database database;

    private ECEconomy economy;
    private Permission permission;
    private Economy vaultEconomy;

    ArrayList<Market> markets;
    private ArrayList<Bank> banks;

    /**
     * @return the banks
     */
    public ArrayList<Bank> getBanks() {
        return banks;
    }

    private void loadDependencies() {
        if (!setupEconomy()) {
            getLogger().severe("Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupPermissions();
        setupChat();
        if (!setupDatabase()) {
            getLogger().severe("Disabled due to database connection failure!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }

    private void registerCommandExecutor(CommandExecutor executor, Commands... commands) {
        for (Commands command : commands) {
            getCommand(command.getName()).setExecutor(executor);
        }
    }

    private void loadConfiguration() {
        config = new Configuration(getConfig());
    }

    private void loadPlaces() {
        markets = loadMarkets();
        banks = loadBanks();
    }

    @Override
    public void onEnable() {
        loadConfiguration();
        loadDependencies();
        loadPlaces();

        mainCmdExecutor = new MainCommandExecutor(this);
        bankCmdExecutor = new BankCommandExecutor(this);
        tradeCmdExecutor = new TradingCommandExecutor(this);
        registerCommandExecutor(mainCmdExecutor, Commands.DEBUG1);
        registerCommandExecutor(bankCmdExecutor, Commands.DEPOSIT);
        registerCommandExecutor(tradeCmdExecutor, Commands.BID, Commands.OFFER,
                Commands.REMOVE_BID, Commands.REMOVE_OFFER, Commands.LIST_QUOTES);
    }

    @Override
    public void onDisable() {
    }

    public void logException(Throwable ex, Level level, PluginComponent source) {
        getLogger().log(level, String.format("From [%s]:", source.getClass().getName()), ex);
    }

    /**
     * @return the economy
     */
    public ECEconomy getEconomy() {
        return economy;
    }

    private ArrayList<Market> loadMarkets() {
        ArrayList<Market> raw = new ArrayList<>();
        try {
            ResultSet rs = database.select().columns().from(config.TABLE_MARKETS).execute();
            while (rs.next()) {
                Market toAdd = new Market(this, rs);
                if (toAdd.isValid()) {
                    raw.add(toAdd);
                } else {
                    // TODO remove from db
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ECPlugin.class.getName()).log(Level.SEVERE, null, ex);
        }

        return raw;
    }

    private ArrayList<Bank> loadBanks() {
        ArrayList<Bank> raw = new ArrayList<>();
        try {
            ResultSet rs = database.select().columns().from(config.TABLE_BANKS).execute();
            while (rs.next()) {
                raw.add(new Bank(this, rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ECPlugin.class.getName()).log(Level.SEVERE, null, ex);
        }

        return raw;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    private boolean setupDatabase() {
        database = new Database(this);
        return database.isValid();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        vaultEconomy = rsp.getProvider();
        economy = new ECEconomy(this, vaultEconomy);
        return true;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        permission = rsp.getProvider();
        return permission != null;
    }

}
