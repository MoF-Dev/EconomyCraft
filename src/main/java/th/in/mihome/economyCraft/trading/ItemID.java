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

import java.util.Objects;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Kolatat Thangkasemvathana
 */
public class ItemID {

    private short durability;
    private Material material;
    private ItemMeta meta;

    public ItemID(Material material) {
        this(material, (short) 0);
    }

    public ItemID(Material material, short durability) {
        this(material, durability, null);
    }

    public ItemID(Material material, short durability, ItemMeta meta) {
        this.material = material;
        this.durability = durability;
        this.meta = meta;
    }

    public ItemID(ItemStack itemStack) {
        material = itemStack.getType();
        durability = itemStack.getDurability();
        meta = itemStack.getItemMeta();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ItemID other = (ItemID) obj;
        if (this.getDurability() != other.getDurability()) {
            return false;
        }
        if (this.getMaterial() != other.getMaterial()) {
            return false;
        }
        if (!Objects.equals(this.meta, other.meta)) {
            return false;
        }
        return true;
    }
    /**
     * @return the durability
     */
    public short getDurability() {
        return durability;
    }

    /**
     * @return the material
     */
    public Material getMaterial() {
        return material;
    }


    /**
     * @return the meta
     */
    public ItemMeta getMeta() {
        return meta;
    }
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.getMaterial());
        hash = 47 * hash + this.getDurability();
        hash = 47 * hash + Objects.hashCode(this.getMeta());
        return hash;
    }

}
