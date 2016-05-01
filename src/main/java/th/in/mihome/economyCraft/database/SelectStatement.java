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
package th.in.mihome.economyCraft.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringJoiner;

/**
 *
 * @author Kolatat Thangkasemvathana
 */
public class SelectStatement {
    private String qualifier = "";
    private String columns[];
    private String from = "";
    private final Statement stmt;

    protected SelectStatement(Statement stmt) {
        this.stmt = stmt;
    }
    
    public SelectStatement distinct(){
        qualifier = "distinct";
        return this;
    }
    
    public SelectStatement all(){
        qualifier = "all";
        return this;
    }
    
    public SelectStatement columns(){
        return columns("*");
    }
    
    public SelectStatement columns(String... columns){
        this.columns=columns;
        return this;
    }
    
    public SelectStatement from(String table){
        from = table;
        return this;
    }
    
    public ResultSet execute() throws SQLException{
        StringJoiner sj = new StringJoiner(", ");
        for(String col : columns){
            sj.add("`"+col+"`");
        }
        String sql = String.format("select %s %s from `%s`", qualifier, sj.toString(), from);
        return stmt.executeQuery(sql);
    }
}
