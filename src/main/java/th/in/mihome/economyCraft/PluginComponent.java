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

import java.util.logging.Level;

/**
 *
 * @author Kolatat Thangkasemvathana
 */
public abstract class PluginComponent {

    protected final ECPlugin plugin;

    public PluginComponent(ECPlugin plugin) {
        this.plugin = plugin;
    }

    protected void info(String format, Object... args) {
        log(Level.INFO, format, args);
    }

    protected void warning(String format, Object... args) {
        log(Level.WARNING, format, args);
    }

    protected void severe(String format, Object... args) {
        log(Level.SEVERE, format, args);
    }

    protected void log(Level level, String format, Object... args) {
        plugin.getLogger().log(level, String.format("[%s] %s", getClass().getSimpleName(), String.format(format, args)));
    }

    protected void logException(Throwable ex, Level level) {
        logException(ex, level, "No message.");
    }

    protected void logException(Throwable ex, Level level, String msg) {
        plugin.getLogger().log(level, String.format("[%s]: %s", getClass().getSimpleName(), msg), ex);
    }

}
