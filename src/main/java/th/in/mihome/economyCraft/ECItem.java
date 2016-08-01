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

import java.util.Objects;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Kolatat Thangkasemvathana
 */
public class ECItem {

    private final int id;
    private final int durability;
    private final String displayName;
    private final String minecraftName;
    private final int pathWeight;
    private final ItemStack mcItem;

    ECItem(int id, int durability, String displayName, String minecraftName, int pathWeight) {
        this.id = id;
        this.displayName = displayName;
        this.minecraftName = minecraftName;
        this.pathWeight = pathWeight;
        mcItem = new ItemStack(Material.getMaterial(minecraftName));
        // item.setDurability((short) durability); // should be implicit in minecraftName
        this.durability = durability==-1?mcItem.getDurability():durability;
        assert(this.durability==mcItem.getDurability());
    }

    /**
     * @return the durability
     */
    public int getDurability() {
        return durability;
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
     * @return the material
     */
    public Material getMaterial() {
        return mcItem.getType();
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

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ECItem){
            return ((ECItem) obj).mcItem.isSimilar(mcItem);
        } else if(obj instanceof ItemStack){
            return ((ItemStack) obj).isSimilar(mcItem);
        } else if(obj instanceof Material){
            return ((Material) obj).equals(mcItem.getType());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        ItemStack singleItem = new ItemStack(this.mcItem);
        singleItem.setAmount(1);
        hash = 53 * hash + Objects.hashCode(singleItem);
        return hash;
    }

}
