package net.danh.storage.Database;

import net.danh.storage.Manager.Data;
import net.danh.storage.Storage;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Level;

import static net.danh.storage.Manager.Files.getconfigfile;

public abstract class Database {
    Storage main;
    Connection connection;
    public String table = "PlayerData";

    public Database(Storage instance) {
        main = instance;
    }

    public abstract Connection getSQLConnection();

    public abstract void load();

    public void initialize() {
        connection = getSQLConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE player = ?");
            ResultSet rs = ps.executeQuery();
            close(ps, rs);

        } catch (SQLException ex) {
            Storage.get().getLogger().log(Level.SEVERE, "Unable to retrieve connection", ex);
        }
    }

    // These are the methods you can use to get things out of your database. You of course can make new ones to return different things in the database.
    // This returns the number of people the player killed.
    public String getData(String type, String player) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE player = '" + player + "';");

            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("player").equalsIgnoreCase(player.toLowerCase())) { // Tell database to search for the player you sent into the method. e.g. getTokens(sam) It will look for sam.
                    return rs.getString(type.toLowerCase()); // Return the players amount of kills. If you wanted to get total (just a random number for an example for you guys) You would change this to total!
                }
            }
        } catch (SQLException ex) {
            Storage.get().getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                Storage.get().getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return null;
    }

    public Integer getMaterial(String player, String material, Boolean max) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE player = '" + player + "';");

            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("player").equalsIgnoreCase(player.toLowerCase())) {
                    JSONParser parser = new JSONParser();
                    Object obj;
                    if (!max) {
                        obj = parser.parse(rs.getString(2));
                    } else {
                        obj = parser.parse(rs.getString(3));
                    }
                    JSONObject jsonObject = (JSONObject) obj;
                    JSONArray amount = (JSONArray) jsonObject.get(material);
                    if (amount != null) {
                        return BigDecimal.valueOf(Long.parseLong(String.valueOf(amount))).intValue();
                    } else {
                        return 0;
                    }
                }
            }
        } catch (SQLException ex) {
            Storage.get().getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                Storage.get().getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return 0;
    }

    public JSONObject getAmountMaterial(Player p) {
        JSONObject jsonObject = new JSONObject();
        for (String item : Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks")).getKeys(false)) {
            jsonObject.put(item, Data.getStorage(p, item));
        }
        return jsonObject;
    }

    public JSONObject getMaxAmountMaterial(Player p) {
        JSONObject jsonObject = new JSONObject();
        for (String item : Objects.requireNonNull(getconfigfile().getConfigurationSection("Blocks")).getKeys(false)) {
            jsonObject.put(item, Data.getMaxStorage(p, item));
        }
        return jsonObject;
    }

    // Now we need methods to save things to the database
    public void save(Player player) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("REPLACE INTO " + table + " (player,material,max_storage,smelt,pickup) VALUES(?,?,?,?,?)");
            ps.setString(1, player.getName().toLowerCase());
            ps.setString(2, getAmountMaterial(player).toJSONString());
            ps.setString(3, getMaxAmountMaterial(player).toJSONString());
            ps.setString(4, String.valueOf(Data.autoSmelt(player)));
            ps.setString(5, String.valueOf(Data.autoPick(player)));
            ps.executeUpdate();
        } catch (SQLException ex) {
            Storage.get().getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                Storage.get().getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
    }


    public void close(PreparedStatement ps, ResultSet rs) {
        try {
            if (ps != null) ps.close();
            if (rs != null) rs.close();
        } catch (SQLException ex) {
            Error.close(Storage.get(), ex);
        }
    }
}
