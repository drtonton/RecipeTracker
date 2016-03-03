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
    public void testSingleRecipe() throws SQLException {
        Connection conn = startConnection();
        RecipeTracker.insertRecipe(conn, "food", "ings", "cook", "time", 94);
        Recipe recipe = RecipeTracker.selectRecipe(conn, 1);
        endConnection(conn);
        assertTrue(recipe != null);
    }
    @Test
    public void testRecipeTable() throws SQLException {
        Connection conn = startConnection();
        RecipeTracker.insertRecipe(conn, "test", "test", "test","test", 100);
        RecipeTracker.insertRecipe(conn, "test2", "test2", "test2", "test2", 101);
        ArrayList recipes = RecipeTracker.selectRecipes(conn);
        endConnection(conn);
        assertTrue(recipes != null);
    }
    @Test
    public void testUpdateRecipe() throws SQLException {
        Connection conn = startConnection();
        RecipeTracker.insertRecipe(conn, "test", "test", "test", "test", 1);
        Recipe original = RecipeTracker.selectRecipe(conn, 1);
        RecipeTracker.updateRecipe(conn, 1, "I CHANGED THIS", "test", "test", "test");
        Recipe updated = RecipeTracker.selectRecipe(conn, 1);
        endConnection(conn);
        assertTrue(original != updated);
    }
    @Test
    public void testDeleteRecipe() throws SQLException {
        Connection conn = startConnection();
        RecipeTracker.insertRecipe(conn, "test", "test", "test", "test", 1);
        RecipeTracker.deleteRecipe(conn, 1);
        Recipe recipe = RecipeTracker.selectRecipe(conn, 1);
        assertTrue(recipe == null);
    }
}