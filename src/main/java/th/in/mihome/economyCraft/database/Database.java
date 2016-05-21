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
package th.in.mihome.economyCraft.database;

import java.sql.*;
import java.util.logging.Level;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import th.in.mihome.economyCraft.ECPlugin;
import th.in.mihome.economyCraft.PluginComponent;

/**
 *
 * @author Kolatat Thangkasemvathana
 */
public class Database extends PluginComponent implements AutoCloseable {

    private boolean connected = false;
    private final Connection connection;
    private String transactionSql;
    private Statement selectStmt;

    public Database(ECPlugin plugin) {
        super(plugin);
        compileSql();
        connection = newConnection();
    }

    private void compileSql() {
        transactionSql = String.format("insert into `%s` (`account`,`amount`,`type`,`time`,`reference`) values (?,?,?,?,?)",
                plugin.config.TABLE_TRANSACTIONS);
    }

    @Override
    public void close() throws Exception {
        if (isConnected()) {
            connection.close();
        }
    }

    /**
     * @return the connected
     */
    public boolean isConnected() {
        return connected;
    }

    public boolean isValid() {
        try {
            return connection.isValid(plugin.config.DATABASE_TIMEOUT);
        } catch (SQLException ex) {
            plugin.logException(ex, Level.WARNING, this);
            return false;
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public SelectStatement select() {
        return new SelectStatement(selectStmt);
    }

    /**
     * Process the transaction command.
     * 
     * A transaction involves the transfer of money from one account to another.
     * This method TRIES to ensure that money deducted from the buyer is
     * transferred to the seller successfully. If not, it will attempt to repay
     * the buyer, but may fail. However, the result of the transaction will be
     * marked as successful if and only if the transfer is committed successfully
     * to both the buyer and the seller.
     * 
     * If this transaction involves an exchange of goods or services, the
     * exchange should only be performed if the result of this transaction is
     * indicated as successful.
     * 
     * @param transaction The transaction to process.
     * @return The result of the transaction.
     */
    public TransactionResult process(Transaction transaction) {
        double doubleAmount = transaction.getAmount() / 100;
        Economy econEngine = plugin.getEconomy().getEngine();
        EconomyResponse eeResponse = econEngine.withdrawPlayer(transaction.getBuyer(), doubleAmount);
        int errorCode;
        String errorMessage = "";
        if (eeResponse.transactionSuccess()) {
            eeResponse = econEngine.depositPlayer(transaction.getSeller(), doubleAmount);
            if (eeResponse.transactionSuccess()) {
                //commit
                try {
                    boolean autoCommit = connection.getAutoCommit();
                    PreparedStatement transLog = null;
                    try {
                        connection.setAutoCommit(false);
                        transLog = connection.prepareStatement(transactionSql);
                        transLog.setString(1, transaction.getSeller().getName());
                        transLog.setInt(2, transaction.getAmount());
                        transLog.setString(3, transaction.getType().toString());
                        transLog.setLong(4, transaction.getTime());
                        transLog.setString(5, transaction.getReference());
                        transLog.executeUpdate();
                        transLog.setString(1, transaction.getBuyer().getName());
                        transLog.setInt(2, -transaction.getAmount());
                        transLog.executeUpdate();
                        connection.commit();
                    } catch (SQLException ex) {
                        plugin.logException(ex, Level.SEVERE, this);
                        plugin.getLogger().log(Level.INFO, "Transaction is being rolled back.");
                        connection.rollback();
                    } finally {
                        if (transLog != null) {
                            transLog.close();
                        }
                        connection.setAutoCommit(autoCommit);
                    }
                } catch (SQLException ex) {
                    plugin.logException(ex, Level.SEVERE, this);
                }
                return new TransactionResult();
            } else {
                //repay payee
                errorCode = 5;
                errorMessage += "Could not pay the receipient: " + eeResponse.errorMessage;
                eeResponse = econEngine.depositPlayer(transaction.getBuyer(), doubleAmount);
                if (!eeResponse.transactionSuccess()) {
                    plugin.getLogger().log(Level.SEVERE, "Transaction failed and cannot repay buyer. Free money for die Fuhrer!");
                    errorCode = 6;
                    errorMessage += "Could not repay buyer: " + eeResponse.errorMessage;
                }
            }
        } else {
            //do nothin
            errorCode = 4;
            errorMessage += "Could not get buyer's money: " + eeResponse.errorMessage;
        }
        return new TransactionResult(errorCode, errorMessage);
    }

    private Connection newConnection() {
        Connection conn = null;
        switch (plugin.config.DATABASE_ENGINE) {
            case MYSQL:
                try {
                    conn = DriverManager.getConnection(
                            String.format("jdbc:mysql://%s:%d/%s?autoReconnect=true",
                                    plugin.config.MYSQL_HOST,
                                    plugin.config.MYSQL_PORT,
                                    plugin.config.MYSQL_DATABASE),
                            plugin.config.MYSQL_USER,
                            plugin.config.MYSQL_PASSWORD);
                } catch (SQLException ex) {
                    plugin.logException(ex, Level.SEVERE, this);
                }
                break;
            case SQLITE:
                try {
                    conn = DriverManager.getConnection("jdbc:sqlite:"
                            + plugin.config.SQLITE_FILE);
                } catch (SQLException ex) {
                    plugin.logException(ex, Level.SEVERE, this);
                }
        }

        if (conn != null) {
            try {
                selectStmt = connection.createStatement();
                connected = true;
            } catch (SQLException ex) {
                plugin.logException(ex, Level.SEVERE, this);
            }
        }
        return conn;
    }

}
