package pack;

import java.sql.Connection;
import java.sql.SQLException;

public class Foo {

    private Connection connection = null;

    public void testLinear() throws SQLException {
        String sql;
        String a = "SELECT", b = "bbb";
        a += " * ";
        String from = "FROM";
        String c = b + b;
        if (true) {
//            if (false) {
//                sql = a + "1";
//            } else {
//                sql = "2";
//            }
            sql = a + "bbb";
        } else {
            sql = a + "aaa";
        }
        connection.prepareStatement(sql);
    }
//    public void testLoop() throws SQLException {
//        String sql;
//        String a = "SELECT", b = "bbb";
//        a += " * ";
//        String from = "FROM";
//        String c = b + b;
//        while ((a += "1") != null) {
//            a += "2";
//        }
//        sql = a;
//        connection.prepareStatement(sql);
//    }
}
