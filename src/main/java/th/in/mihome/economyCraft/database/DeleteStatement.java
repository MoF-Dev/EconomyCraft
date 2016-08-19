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
package th.in.mihome.economyCraft.database;

import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Kolatat Thangkasemvathana <kolatat.t@gmail.com>
 */
public class DeleteStatement {
    
    String table;
    StringBuilder where = new StringBuilder();
    Statement stmt;

    DeleteStatement(Statement selectStmt, String table) {
        this.stmt=selectStmt;
        this.table=table;
    }

    public DeleteStatement where(String key, String value) {
        if(where.length()!=0) where.append(" and ");
        where.append('`').append(key).append("` = '").append(value).append('\'');
        return this;
    }

    public int execute() throws SQLException {
        String sql = String.format("delete from `%s` where %s", table, stmt);
        //System.out.println(sql);
        return stmt.executeUpdate(sql);
    }
    
}
