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

import org.bukkit.Location;

/**
 *
 * @author Kolatat Thangkasemvathana
 */
public abstract class Place extends PluginComponent {

    protected Location location;
    protected String address;
    protected String name;

    public Place(ECPlugin plugin, String name, Location location, String address) {
        super(plugin);
        this.location = location;
        this.address = address;
        this.name = name;
    }

    public abstract double getRadius();

    public boolean isNear(Location loc) {
        return getLocation().distance(loc) <= getRadius();
    }

    /**
     * @return the location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
}
