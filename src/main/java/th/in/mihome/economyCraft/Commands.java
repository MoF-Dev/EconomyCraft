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

import org.bukkit.command.Command;

/**
 *
 * @author Kolatat Thangkasemvathana
 */
public enum Commands {
    DEPOSIT("deposit"),
    BID("market_bid"),
    OFFER("market_offer"),
    REMOVE_QUOTE("remove_quote"),
    LIST_QUOTES("list_market_quotes"),
    WITHDRAW("withdraw"),
    BUY("market_buy");

    public static Commands getCommand(Command cmd) {
        for (Commands cmds : values()) {
            if (cmds.name.equalsIgnoreCase(cmd.getName())) {
                return cmds;
            }
        }
        return null;
    }

    private final String name;

    private Commands(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
