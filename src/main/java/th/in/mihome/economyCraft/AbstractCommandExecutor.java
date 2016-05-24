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

import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Kolatat Thangkasemvathana
 */
public abstract class AbstractCommandExecutor extends PluginComponent implements CommandExecutor {

    public AbstractCommandExecutor(ECPlugin plugin) {
        super(plugin);
    }

    /**
     * Get the command sender as a player.
     *
     * @param sender The sender of the command.
     * @return The player who sends the command.
     * @throws NonPlayerException if the sender is not an instance of a Player.
     */
    public static Player requirePlayer(CommandSender sender) throws NonPlayerException {
        if (sender instanceof Player) {
            return (Player) sender;
        } else {
            throw new NonPlayerException();
        }
    }

    public static ItemStack ensureGoods(Player player, ItemStack goods) throws UnfulfilledRequirementException {
        int amountTaken = goods.getAmount();
        for (ItemStack leftoverItem : player.getInventory().removeItem(goods).values()) {
            amountTaken -= leftoverItem.getAmount();
        }
        goods.setAmount(amountTaken);
        assert (amountTaken >= 0);
        if (amountTaken == 0) {
            throw new UnfulfilledRequirementException("You do not have the specified goods in your inventtory.");
        }
        return goods;
    }

    public static ItemStack getItemStackFromStringArgs(String material, String amount) throws InvalidArgumentException {
        String[] materialParts = material.split(",");
        if (materialParts.length > 1) {
            return getItemStackFromStringArgs(materialParts[0], amount, materialParts[1]);
        } else {
            return getItemStackFromStringArgs(material, amount, "0");
        }
    }

    public static ItemStack getItemStackFromStringArgs(String material, String amount, String damage) throws InvalidArgumentException {
        Material realMaterial;
        try {
            realMaterial = Material.getMaterial(Integer.parseUnsignedInt(material));
        } catch (NumberFormatException ex) {
            realMaterial = Material.getMaterial(material);
        }
        if (realMaterial == null) {
            throw new InvalidArgumentException("Invalid item ID or name.");
        }
        try {
            int intAmount = Integer.parseUnsignedInt(amount);
            if (intAmount <= 0) {
                throw new InvalidArgumentException("Amount must be 1 or greater.");
            }
            return new ItemStack(realMaterial, intAmount, Short.parseShort(damage));
        } catch (NumberFormatException ex) {
            throw new InvalidArgumentException("Invalid number input.", ex);
        }
    }

    public static int parseMonetaryValue(String input, boolean signed) throws InvalidArgumentException {
        try {
            double value = Double.parseDouble(input);
            if (!signed && value < 0) {
                throw new InvalidArgumentException("Require a positive amount of money,");
            }
            return (int) (value * 100);
        } catch (NumberFormatException ex) {
            throw new InvalidArgumentException("Invalid money input '%s'.", ex, input);
        }
    }
}
