/*
 * The MIT License
 *
 * Copyright 2016 Kolatat Thangkasemvathana <kolatat.t@gmail.com>.
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
 * An ECItem with quantity.
 *
 * @author Kolatat Thangkasemvathana <kolatat.t@gmail.com>
 */
public class ECItemStack {

    private ECItem item;
    private int quantity;

    public ECItemStack(ECItem item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    /**
     * @return the item
     */
    public ECItem getItem() {
        return item;
    }

    /**
     * @param item the item to set
     */
    public void setItem(ECItem item) {
        this.item = item;
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ECItemStack) {
            ECItemStack objIs = (ECItemStack) obj;
            return objIs.equals(item) && objIs.quantity == quantity;
        } else if (obj instanceof ECItem) {
            return ((ECItem) obj).equals(item);
        } else if (obj instanceof ItemStack) {
            ItemStack objIs = (ItemStack) obj;
            return objIs.getType().equals(item.getMaterial()) && objIs.getAmount() == quantity;
        } else if (obj instanceof Material) {
            return ((Material) obj).equals(item.getMaterial());
        }
        return false;
    }

    public ItemStack getMcItemStack() {
        return new ItemStack(item.getMaterial(), quantity, (short) item.getDurability());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.item);
        hash = 89 * hash + this.quantity;
        return hash;
    }

}
