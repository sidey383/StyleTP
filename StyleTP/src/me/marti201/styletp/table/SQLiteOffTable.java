package me.marti201.styletp.table;

import me.marti201.styletp.StyleTP;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.UUID;
import java.util.logging.Level;

public class SQLiteOffTable extends OffTable{

    private final String url;

    public SQLiteOffTable(String url) throws SQLException, IOException {
        File f = new File(url);
        getLogger().log(Level.INFO, "Database file " + f.getAbsolutePath());
        if(!f.exists()) {
            getLogger().log(Level.INFO, "Create database file ");
            f.getParentFile().mkdir();
            f.createNewFile();
        }
        this.url = "jdbc:sqlite:"+f.getAbsolutePath();
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
            PreparedStatement st = con.prepareStatement("insert or replace into StyleTPTable (uuid, off) VALUES (?, ?)");
            st.setString(1, p.toString());
            st.setBoolean(2, off);
            st.execute();
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }

}
