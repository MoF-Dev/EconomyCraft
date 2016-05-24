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
package th.in.mihome.economyCraft.trading;

import java.util.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Location;
import th.in.mihome.economyCraft.ECItem;
import th.in.mihome.economyCraft.ECPlugin;
import th.in.mihome.economyCraft.PluginComponent;

/**
 *
 * @author Kolatat Thangkasemvathana
 */
public class ECEconomy extends PluginComponent {
    
    private Logistics centralLogistics;

    private final Economy engine;

    private final Map<Set<Location>, Double> exportPenaltyCache = new HashMap<>();

    private final Set<Market> markets = new HashSet<>();

    public ECEconomy(ECPlugin plugin, Economy vaultEconomy) {
        super(plugin);
        engine = vaultEconomy;
    }

    /**
     * @return the centralLogistics
     */
    public Logistics getCentralLogistics() {
        return centralLogistics;
    }
    
    public void match(){
        List<Market> markets = new ArrayList<>(this.markets);
        Collections.shuffle(markets);
        markets.stream().forEach((m) -> {
            m.match();
        });
    }

    /**
     * @return the engine
     */
    public Economy getEngine() {
        return engine;
    }

    QuoteMatcher globalMatcher = newMatcher(null);

    public QuoteMatcher newMatcher(Market market) {
        switch (plugin.config.MATCHING_ALGORITHM) {
            case FIFO:
                return new FIFOMatcher(plugin, market);
            default:
                return null;
        }
    }

    public double getExportPenalty(Location a, Location b) {
        /*
            TODO 
            from point a to b, generate fixed coords in grid
            check straight distance between subcoords, count amount of road blocks on line and add to counter
            decrease weight based on amount
            loop through for entire grid
            use dijkstra algorithm to find shortest path.
        */
        
        ECItem i1 = plugin.config.getItemInfo(a.getBlock().getType());
        int weight = i1.getPathWeight();

        Set<Location> key = new HashSet<>();
        key.add(a);
        key.add(b);

        Double exportPenalty = exportPenaltyCache.get(key);
        if (exportPenalty == null) {
            double linear = a.distance(b);
            double sqrt = Math.sqrt(linear);
            double log = Math.log(linear);
            exportPenalty = 1 + linear / plugin.config.TARIFF_LINEAR
                    + sqrt / plugin.config.TARIFF_SQRT
                    + log / plugin.config.TARIFF_LOG;
            exportPenaltyCache.put(key, exportPenalty);
        }

        return exportPenalty;
    }

    /**
     * @return the markets
     */
    public Set<Market> getMarkets() {
        return markets;
    }
}
