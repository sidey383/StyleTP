package me.marti201.styletp.table;

import java.sql.*;
import java.util.UUID;

public class MySQLOffTable extends OffTable{

    private final String url, user, password;

    public MySQLOffTable(String url, String user, String password) throws SQLException {
        this.url = "jdbc:mysql://"+url;
        this.user = user;
        this.password = password;
        try(Connection con = getConnection()) {
            PreparedStatement st = con.prepareStatement("CREATE TABLE IF NOT EXISTS StyleTPTable ( uuid VARCHAR(255) PRIMARY KEY, off BOOL)");
            st.execute();
        }
    }

    @Override
    public boolean isOffInTable(UUID p) throws Exception {
        try(Connection con = getConnection()) {
            PreparedStatement st = con.prepareStatement("SELECT * FROM StyleTPTable WHERE uuid = ?");
            st.setString(1, p.toString());
            ResultSet res = st.executeQuery();
            if(res.next()) {
                return res.getBoolean("off");
            } else {
                return false;
            }
        }
    }

    @Override
    public void setOffInTable(UUID p, boolean off) throws Exception {
        try(Connection con = getConnection()) {
            PreparedStatement st = con.prepareStatement("INSERT INTO StyleTPTable (uuid, off) VALUES (?, ?) ON DUPLICATE KEY UPDATE off=?");
            st.setString(1, p.toString());
            st.setBoolean(2, off);
            st.setBoolean(3, off);
            st.execute();
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

}
