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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import th.in.mihome.economyCraft.banking.Bank;
import th.in.mihome.economyCraft.banking.commands.DepositCommand;
import th.in.mihome.economyCraft.banking.commands.WithdrawCommand;
import th.in.mihome.economyCraft.database.Database;
import th.in.mihome.economyCraft.trading.ECEconomy;
import th.in.mihome.economyCraft.trading.Market;
import th.in.mihome.economyCraft.trading.commands.*;

/**
 *
 * @author Kolatat Thangkasemvathana
 */
public class ECPlugin extends JavaPlugin implements Listener {

    public Configuration config;

    private ArrayList<Bank> banks;
    private CentralBank centralBank;
    private Chat chat;
    private Database database;
    private ECEconomy economy;
    private ArrayList<Market> markets;
    private Permission permission;
    private Economy walletProvider;

    /**
     * @return the banks
     */
    public ArrayList<Bank> getBanks() {
        return banks;
    }

    /**
     * @return the centralBank
     */
    public CentralBank getCentralBank() {
        return centralBank;
    }

    public Database getDb() {
        return database;
    }

    public ECItem getItem(ItemStack itemStack) {
        return config.getItemInfo(itemStack.getType());
    }

    public ECItemStack getItemStack(String descriptor, int quantity) {
        return new ECItemStack(getItem(descriptor), quantity);
    }

    public ECItem getItem(String descriptor) {
        String searchParts[] = descriptor.split(":");
        Material material = Material.getMaterial(searchParts[0]);
        ECItem result = null;
        if (material != null) {
            result = config.getItemInfo(material);
        }
        if (result != null) {
            return result;
        }

        boolean searchById = false;
        int itemId = 0;
        try {
            itemId = Integer.parseInt(searchParts[0]);
            searchById = true;
        } catch (NumberFormatException ex) {
        }

        boolean explicitDurability = false;
        int durability = 0;
        if (searchParts.length > 1) {
            try {
                durability = Integer.parseInt(searchParts[1]);
                explicitDurability = true;
            } catch (NumberFormatException ex) {
            }
        }

        for (ECItem item : config.itemDatabase.values()) {
            if (searchById) {
                if (item.getId() == itemId && item.getDurability() == durability) {
                    return item;
                }
            } else if (item.getDisplayName().equalsIgnoreCase(searchParts[0]) || item.getMinecraftName().equalsIgnoreCase(searchParts[0])) {
                // check damage only when explicitly specified, damage is optional if e.g. name="DARK_OAK_shit"
                if (explicitDurability && item.getDurability() != durability) {
                    continue;
                }
                return item;
            }
        }
        return null;
    }

    /**
     * @return the economy
     */
    public ECEconomy getEconomy() {
        return economy;
    }

    /**
     * @return the markets
     */
    public ArrayList<Market> getMarkets() {
        return markets;
    }

    /**
     * @return the walletProvider
     */
    public Economy getWalletProvider() {
        return walletProvider;
    }

    @Override
    public void onDisable() {
        //saveConfig();
    }

    @Override
    public void onEnable() {
        loadConfiguration();
        if (!loadDependencies()) {
            return;
        }
        loadPlaces();
        registerCommands();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.sendMessage("Ayy lmao welcome!");
        getDb().createBankAccount(centralBank.getPlayerId(player));
        if (!getWalletProvider().hasAccount(player)) {
            getWalletProvider().createPlayerAccount(player);
        }
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

    private void loadConfiguration() {
        saveDefaultConfig();
        config = new Configuration(this, getConfig());
    }

    private boolean loadDependencies() {
        if (!setupEconomy()) {
            getLogger().severe("Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return false;
        }
        setupPermissions();
        setupChat();
        if (!setupDatabase()) {
            getLogger().severe("Disabled due to database connection failure!");
            getServer().getPluginManager().disablePlugin(this);
            return false;
        } else {
            try {
                database.getConnection().createStatement().executeUpdate(config.CREATE_TABLE_SQL);
            } catch (SQLException ex) {
                logException(ex, Level.SEVERE);
                return false;
            }
        }
        setupCentralBank();
        return true;
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
                    database.delete_from(config.TABLE_MARKETS).where("id",rs.getString("id")).execute();
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ECPlugin.class.getName()).log(Level.SEVERE, null, ex);
        }

        return raw;
    }

    private void loadPlaces() {
        markets = loadMarkets();
        banks = loadBanks();
    }

    private void logException(Throwable ex, Level level) {
        getLogger().log(level, null, ex);
    }

    private void registerCommandExecutor(CommandExecutor executor, Commands... commands) {
        for (Commands command : commands) {
            try {
                getCommand(command.getName()).setExecutor(executor);
            } catch (NullPointerException ex) {
                getLogger().warning("Command " + command.getName() + " does not exist.");
            }
        }
    }

    private void registerCommands() {
        registerCommandExecutor(new BidCommand(this), Commands.BID);
        registerCommandExecutor(new BuyCommand(this), Commands.BUY);
        registerCommandExecutor(new OfferCommand(this), Commands.OFFER);
        registerCommandExecutor(new ListQuotesCommand(this), Commands.LIST_QUOTES);
        registerCommandExecutor(new RemoveQuoteCommand(this), Commands.REMOVE_QUOTE);

        registerCommandExecutor(new DepositCommand(this), Commands.DEPOSIT);
        registerCommandExecutor(new WithdrawCommand(this), Commands.WITHDRAW);
    }

    private void setupCentralBank() {
        centralBank = new CentralBank(this);
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        if (chat == null) {
            return false;
        }
        chat = rsp.getProvider();
        return chat != null;
    }

    private boolean setupDatabase() {
        database = new Database(this);
        return database.isValid();
    }

    private boolean setupEconomy() {
        Plugin p;
        if ((p = getServer().getPluginManager().getPlugin("Vault")) == null) {
            getLogger().warning("get Vault plugin returned null");
            return false;

        }
        /*for(RegisteredServiceProvider rsp : getServer().getServicesManager().getRegistrations(p)){
            System.out.printf("%s\t%s%n",rsp.getProvider().getClass().getName(), rsp.getService().getName());
        }*/

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            getLogger().severe("Economy RSP not available. Make sure Vault's economy functionality is available.");
            return false;
        }
        walletProvider = rsp.getProvider();
        economy = new ECEconomy(this);
        return true;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        permission = rsp.getProvider();
        return permission != null;
    }

}
