package net.danh.storage.Database;

import net.danh.storage.Manager.Files;
import net.danh.storage.Storage;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public class SQLite extends Database {

    String dbname;

    public SQLite(Storage instance) {
        super(instance);
        dbname = "PlayerData"; // Set the table name here e.g. player_kills
    }

    public String SQLiteCreateTokensTable = "CREATE TABLE IF NOT EXISTS PlayerData (" + // make sure to put your table name in here too.
            "`player` varchar(32) NOT NULL," + // This creates the different columns you will save data too. varchar(32) Is a string, int = integer
            "`material` TEXT DEFAULT '{}'," +
            "`max_storage` TEXT DEFAULT '{}'," +
            "`smelt` TEXT DEFAULT 'false'," +
            "`pickup` TEXT DEFAULT 'true'," +
            "PRIMARY KEY (`player`)" +  // This is creating 5 columns Player, material, amount, storage, max_storage. Primary key is what you are going to use as your indexer. Here we want to use player so
            ");"; // we can search by player, and get kills and total. If you have somehow were searching kills it would provide total and player.


    // SQL creation stuff, You can leave the blow stuff untouched.
    public Connection getSQLConnection() {
        File dataFolder = new File(Storage.get().getDataFolder(), dbname + ".db");
        if (!dataFolder.exists()) {
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                Storage.get().getLogger().log(Level.SEVERE, "File write error: " + dbname + ".db");
            }
        }
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;
        } catch (SQLException ex) {
            Storage.get().getLogger().log(Level.SEVERE, "SQLite exception on initialize", ex);
        } catch (ClassNotFoundException ex) {
            Storage.get().getLogger().log(Level.SEVERE, "You need the SQLite JDBC library. Google it. Put it in /lib folder.");
        }
        return null;
    }

    public void load() {
        connection = getSQLConnection();
        try {
            Statement s = connection.createStatement();
            s.executeUpdate(SQLiteCreateTokensTable);
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initialize();
    }
}

