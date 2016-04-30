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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bukkit.Location;

/**
 *
 * @author Kolatat Thangkasemvathana
 */
public class Economy extends PluginComponent {

    private final Map<Set<Location>, Double> exportPenaltyCache = new HashMap<>();

    private final Set<Market> markets = new HashSet<>();

    public Economy(ECPlugin plugin) {
        super(plugin);
    }

    public double getExportPenalty(Location a, Location b) {
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
