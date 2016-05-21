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

import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Kolatat Thangkasemvathana
 */
public class ECItem {
    private final int id;
    private final int damage;
    private final String displayName;
    private final String minecraftName;
    private final int pathWeight;
    private final ItemStack mcItem;

    public ECItem(int id, int damage, String displayName, String minecraftName, int pathWeight) {
        this.id = id;
        this.damage = damage;
        this.displayName = displayName;
        this.minecraftName = minecraftName;
        this.pathWeight = pathWeight;
        mcItem = new ItemStack(id, 1, (short) damage);
    }

    /**
     * @return the damage
     */
    public int getDamage() {
        return damage;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the mcItem
     */
    public ItemStack getMcItem() {
        return mcItem.clone();
    }

    /**
     * @return the minecraftName
     */
    public String getMinecraftName() {
        return minecraftName;
    }

    /**
     * @return the pathWeight
     */
    public int getPathWeight() {
        return pathWeight;
    }
    
}
