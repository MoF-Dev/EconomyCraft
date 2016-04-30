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

import java.util.ArrayList;
import th.in.mihome.economyCraft.banking.BankCommandExecutor;
import java.util.logging.Level;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import th.in.mihome.economyCraft.banking.Bank;
import th.in.mihome.economyCraft.trading.Economy;
import th.in.mihome.economyCraft.trading.Market;

/**
 *
 * @author Kolatat Thangkasemvathana
 */
public class ECPlugin extends JavaPlugin {

    private ECCommandExecutor cmdExecutor;
    private BankCommandExecutor bankCmdExecutor;
    public Configuration config;

    private Economy economy;

    ArrayList<Market> markets;
    ArrayList<Bank> banks;

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
        economy = new Economy(this);
        loadPlaces();

        cmdExecutor = new ECCommandExecutor(this);
        bankCmdExecutor = new BankCommandExecutor(this);
        registerCommandExecutor(cmdExecutor);
        registerCommandExecutor(bankCmdExecutor, Commands.DEPOSIT);
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
    public Economy getEconomy() {
        return economy;
    }

    private ArrayList<Market> loadMarkets() {
        // TODO: implement mee sempaii~~!
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private ArrayList<Bank> loadBanks() {
        // TODO: implement mee sempaii~~!
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
