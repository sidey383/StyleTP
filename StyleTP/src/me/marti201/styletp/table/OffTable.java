package me.marti201.styletp.table;

import org.bukkit.configuration.file.FileConfiguration;
import org.checkerframework.framework.qual.Unused;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class OffTable {

    private static Logger logger = Logger.getLogger("OffTable");

    private final HashMap<UUID, Boolean> isOff = new HashMap<>();

    public static OffTable createOffTable(String url, String type, String login, String password) throws Exception {
        if(type == null)
            return null;
        return switch (type.toLowerCase()) {
            case "mysql" -> new MySQLOffTable(url, login, password);
            case "sqlite" -> new SQLiteOffTable(url);
            default -> null;
        };
    }

    public static OffTable createOffTable(FileConfiguration conf ) throws Exception {
        return createOffTable(conf.getString("database.url"),
                conf.getString("database.type"),
                conf.getString("database.login"),
                conf.getString("database.password"));
    }

    public boolean isOff(UUID p) {
        if(!isOff.containsKey(p))
            try {
                isOff.put(p, isOffInTable(p));
            }catch (Exception e) {
                logger.log(Level.WARNING, "Write Database error" ,e);
                isOff.put(p, false);
            }
        return isOff.get(p);
    }

    public void setOff(UUID p, boolean off) {
        isOff.put(p, off);
        try {
            setOffInTable(p, off);
        }catch (Exception e) {
            logger.log(Level.WARNING, "Read Database error", e);
        }
    }

    public abstract boolean isOffInTable(UUID p) throws Exception;

    public abstract void setOffInTable(UUID p, boolean off) throws Exception;

    public static void setLogger(Logger logger) {
        OffTable.logger = logger;
    }
    
    public static Logger getLogger() {
        return logger;
    }

}
