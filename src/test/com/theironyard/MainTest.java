package com.theironyard;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by noellemachin on 3/1/16.
 */
public class MainTest {

    public Connection startConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:./text");
        RecipeTracker.createTables(conn);
        return conn;
    }

    public void endConnection (Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("DROP TABLE users");
        stmt.execute("DROP TABLE recipes");
        conn.close();
    }
    @Test
    public void testUser() throws SQLException {
        Connection conn = startConnection();
        RecipeTracker.insertUser(conn, "USER", "");
        User user = RecipeTracker.selectUser(conn, "USER");
        endConnection(conn);
        assertTrue(user != null);
    }
    @Test
    public void testEntries() throws SQLException {
        Connection conn = startConnection();
        RecipeTracker.insertRecipe(conn, "food", "ings", "cook", "time", 94);
        Recipe recipe = RecipeTracker.selectRecipe(conn, 1);
        endConnection(conn);
        assertTrue(recipe != null);
    }
}