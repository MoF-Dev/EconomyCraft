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

import com.opencsv.CSVReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import th.in.mihome.economyCraft.options.DatabaseEngine;
import th.in.mihome.economyCraft.options.MatchingAlgorithm;

/**
 *
 * @author Kolatat Thangkasemvathana
 */
public class Configuration {

    public final double BANK_RADIUS;
    public final ConfigurationSection COMMODITIES_TAXING;
    public final DatabaseEngine DATABASE_ENGINE;
    public final double MARKET_RADIUS;
    public final MatchingAlgorithm MATCHING_ALGORITHM;
    public final String MYSQL_DATABASE;
    public final String MYSQL_HOST;
    public final String MYSQL_PASSWORD;
    public final int MYSQL_PORT;
    public final String MYSQL_USER;
    public final int DATABASE_TIMEOUT;
    public final String SQLITE_FILE;
    public final String TABLE_MARKETS;
    public final String TABLE_BANKS;
    public final String TABLE_TRANSACTIONS;
    public final int TARIFF_LINEAR;
    public final int TARIFF_LOG;
    public final int TARIFF_SQRT;
    public final int TAX_GLOBAL_MARKET;
    public final int TAX_LOCAL_MARKET;
    public final int TAX_PRIVATE_MARKET;
    public final int TRADING_QUEUE_INITIAL_SIZE;
    public final Material BANK_CORNERSTONE;
    public final Material MARKET_CORNERSTONE;

    private final HashMap<ItemStack, ECItem> itemDatabase;

    public ECItem getItemInfo(ItemStack is) {
        return itemDatabase.get(is);
    }

    private void readItemDb() {
        try {
            CSVReader reader = new CSVReader(new FileReader("blockdb.csv"));
            String[] nextLine;
            reader.readNext();
            while ((nextLine = reader.readNext()) != null) {
                int pathWeight = Short.MAX_VALUE;
                if (!nextLine[4].isEmpty()) {
                    pathWeight = Integer.parseInt(nextLine[4]);
                }
                ECItem item = new ECItem(
                        Integer.parseInt(nextLine[0]),
                        Integer.parseInt(nextLine[1]),
                        nextLine[2],
                        nextLine[3],
                        pathWeight
                );
                itemDatabase.put(item.getMcItem(), item);
            }
        } catch (IOException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Configuration(FileConfiguration config) {
        itemDatabase = new HashMap<>(300);
        readItemDb();

        TARIFF_LINEAR = config.getInt("economy.tariff.linear");
        TARIFF_LOG = config.getInt("economy.tariff.log");
        TARIFF_SQRT = config.getInt("economy.tariff.sqrt");

        TAX_GLOBAL_MARKET = config.getInt("economy.taxing.income.global_market");
        TAX_LOCAL_MARKET = config.getInt("economy.taxing.income.local_market");
        TAX_PRIVATE_MARKET = config.getInt("economy.taxing.income.private_market");

        COMMODITIES_TAXING = config.getConfigurationSection("economy.taxing.commodities");

        MARKET_RADIUS = config.getDouble("market.radius");
        MARKET_CORNERSTONE = Material.valueOf(config.getString("market.cornerstone"));

        MATCHING_ALGORITHM = MatchingAlgorithm.valueOf(config.getString("exchange.trading.matchingAlgorithm"));
        TRADING_QUEUE_INITIAL_SIZE = config.getInt("exchange.trading.queueInitialSize");

        BANK_RADIUS = config.getDouble("banking.radius");
        BANK_CORNERSTONE = Material.valueOf(config.getString("banking.cornerstone"));

        DATABASE_ENGINE = DatabaseEngine.valueOf(config.getString("database.engine"));
        DATABASE_TIMEOUT = config.getInt("database.timeout");

        MYSQL_HOST = config.getString("database.mysql.host");
        MYSQL_PORT = config.getInt("database.mysql.port");
        MYSQL_DATABASE = config.getString("database.mysql.database");
        MYSQL_USER = config.getString("database.mysql.user");
        MYSQL_PASSWORD = config.getString("database.mysql.password");

        SQLITE_FILE = config.getString("database.sqlite.file");

        TABLE_MARKETS = config.getString("database.tables.markets");
        TABLE_BANKS = config.getString("database.tables.banks");
        TABLE_TRANSACTIONS = config.getString("database.tables.transactions");
    }
}
