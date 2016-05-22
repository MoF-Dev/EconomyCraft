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

import java.util.Collection;
import org.bukkit.Location;

/**
 *
 * @author Kolatat Thangkasemvathana
 */
public abstract class Place extends PluginComponent {

    protected final String address;
    protected final int id;
    protected final Location location;
    protected final String name;

    public Place(ECPlugin plugin, int id, String name, Location location, String address) {
        super(plugin);
        this.id = id;
        this.location = location;
        this.address = address;
        this.name = name;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @return the location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    public abstract double getRadius();

    public boolean isNear(Location loc) {
        return getLocation().distance(loc) <= getRadius();
    }
    
    /**
     * Get the nearest place to a location.
     * @param <T> The type of place.
     * @param from The location from which you are comparing the distance.
     * @param places A collection of all the places to choose from.
     * @return A place from the collection that has the least distance from from,
     *         or null if the collection is empty.
     */
    public static <T extends Place> T getNearest(Location from, Collection<T> places){
        T nearest = null;
        double nearestDistance = Double.POSITIVE_INFINITY;
        for (T place : places) {
            double distance = from.distance(place.getLocation());
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearest = place;
            }
        }
        return nearest;
    }
    
    /**
     * Get the nearest valid place to a location.
     * @param <T> The type of place.
     * @param from The location from which to compare the distances.
     * @param places A collection of all the places.
     * @return The closest valid place to the given location, or null if there
     *         is no valid place in the collection.
     */
    public static <T extends Place> T getValidNearest(Location from, Collection<T> places){
        T nearest = getNearest(from,places);
        if(nearest.isNear(from)){
            return nearest;
        } else {
            return null;
        }
    }
}
