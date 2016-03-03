package com.theironyard;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by noellemachin on 2/27/16.
 */

public class RecipeTracker {

    public static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS users (user_id IDENTITY, user_name VARCHAR, password VARCHAR)");
        stmt.execute("CREATE TABLE IF NOT EXISTS recipes (recipe_id IDENTITY, recipe_name VARCHAR, ingredients VARCHAR, " +
                "prep VARCHAR, prep_time VARCHAR, recipe_user_id INT)");
}
//    Create an insertUser method, which creates a new record in the users table.
    public static void insertUser(Connection conn, String userName, String password) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (NULL, ?, ?)");
        stmt.setString(1, userName);
        stmt.setString(2, password);
        stmt.execute();
}
    public static void insertRecipe(Connection conn, String recipeName, String ingredients, String prep, String prepTime, int recipeUserId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO recipes VALUES (NULL, ?, ?, ?, ?, ?)");
        stmt.setString(1, recipeName);
        stmt.setString(2, ingredients);
        stmt.setString(3, prep);
        stmt.setString(4, prepTime);
        stmt.setInt(5, recipeUserId);
        stmt.execute();
    }

//Create a selectUser method, which returns a com.theironyard.User object for the given username.
    public static User selectUser(Connection conn, String userName) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE user_name = ?");
        stmt.setString(1, userName);
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
            int id = results.getInt("user_id");
            String password = results.getString("password");
            return new User(id, userName, password);
        }
        return null;
    }
    public static Recipe selectRecipe(Connection conn, int recipeId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM recipes INNER JOIN users ON recipe_user_id = user_id WHERE recipe_id = ?");
        stmt.setInt(1, recipeId);
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
//            int recipeId = results.getInt("recipe_id");
            String recipeName = results.getString("recipe_name");
            String ingredients = results.getString("ingredients");
            String prep = results.getString("prep");
            String prepTime = results.getString("prep_time");
            int recipeUserId = results.getInt("recipe_user_id");

            Recipe recipe = new Recipe (recipeId, recipeUserId, recipeName, ingredients, prep, prepTime);
            return recipe;
        }
        return null;
    }
    // only included conn in the args and excluded the WHERE condition in preparestatement. might need to edit later**
    public static ArrayList selectRecipes (Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM recipes INNER JOIN users ON recipe_user_id = user_id");
        ArrayList<Recipe> allRecipes = new ArrayList<>();
        ResultSet results = stmt.executeQuery();

        while (results.next()) {
            int recipeId = results.getInt("recipe_id");
            String recipeName = results.getString("recipe_name");
            String ingredients = results.getString("ingredients");
            String prep = results.getString("prep");
            String prepTime = results.getString("prep_time");
            int recipeUserId = results.getInt("recipe_user_id");
            Recipe recipe = new Recipe(recipeId, recipeUserId, recipeName, ingredients, prep, prepTime);
            allRecipes.add(recipe);
        }
        return allRecipes;
    }
    public static ArrayList selectRecipesForUser (Connection conn, int userId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM recipes INNER JOIN users ON recipe_user_id = user_id WHERE user_id = ?");
        stmt.setInt(1, userId);
        ArrayList<Recipe> allRecipes = new ArrayList<>();
        ResultSet results = stmt.executeQuery();
        while (results.next()) {
            int recipeId = results.getInt("recipe_id");
            String recipeName = results.getString("recipe_name");
            String ingredients = results.getString("ingredients");
            String prep = results.getString("prep");
            String prepTime = results.getString("prep_time");
            int recipeUserId = results.getInt("recipe_user_id");
            Recipe recipe = new Recipe(recipeId, recipeUserId, recipeName, ingredients, prep, prepTime);
            allRecipes.add(recipe);
        }
        return allRecipes;
    }
    public static void updateRecipe (Connection conn, int recipeId, String recipeName, String ingredients, String prep, String prepTime) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("UPDATE recipes SET recipe_name = ?, ingredients = ?, prep = ?, prep_time = ? WHERE recipe_id = ?");
        stmt.setInt(5, recipeId);
        stmt.setString(1, recipeName);
        stmt.setString(2, ingredients);
        stmt.setString(3, prep);
        stmt.setString(4, prepTime);
        stmt.execute();
    }
    public static void deleteRecipe (Connection conn, int recipeId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM recipes WHERE recipe_id = ?");
        stmt.setInt(1, recipeId);
        stmt.execute();
    }
    public static void main(String[] args) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTables(conn);
        Spark.init();
        Spark.get(
                "/",
                ((request, response) -> {
                    User user = getUserFromSession(request.session(), conn);
                    HashMap map = new HashMap();

                    map.put("user", user);

                    if (user == null) {
                        map.put("recipes", selectRecipes(conn));
                        return new ModelAndView(map, "login.html");
                    }
                    else {
                        map.put("recipes", selectRecipesForUser(conn, user.id));
                        return new ModelAndView(map, "home.html");
                    }
                }),
                new MustacheTemplateEngine()
        );
        Spark.get(
                "/add",
                ((request, response) -> {
                    User user = getUserFromSession(request.session(), conn);
                    return new ModelAndView(user, "add.html");
                }),
                new MustacheTemplateEngine()
        );
        Spark.get(
                "/edit",
                ((request, response) -> {
                    int id = Integer.valueOf(request.queryParams("recipeId"));
                    Recipe recipe = selectRecipe(conn, id);
                    return new ModelAndView(recipe, "edit.html");
                }),
                new MustacheTemplateEngine()
        );
        Spark.post(
                "/login",
                ((request, response) -> {
                    String name = request.queryParams("loginName");
                    String pass = request.queryParams("password");
                    User user = selectUser(conn, name);
                    if (user == null) {
                        response.redirect("/");
                    }
                    else if (user.password.equalsIgnoreCase(pass)) {
                        Session session = request.session();
                        session.attribute("userName", name);
                        response.redirect("/");
                    }
                    else {
                        response.redirect("/");
                    }
                    return "";
                })
        );
        Spark.post(
                "/createAccount",
                ((request, response) -> {
                    String newUser = request.queryParams("newUser");
                    String newPassword = request.queryParams("newPassword");
                    insertUser(conn, newUser, newPassword);

                    Session session = request.session();
                    session.attribute("userName", newUser);
                    response.redirect("/");
                    return "";
                })
        );
        Spark.post(
                "/logout",
                ((request, response) -> {
                    Session session = request.session();
                    session.invalidate();
                    response.redirect("/");
                    return "";
                })
        );
        Spark.post(
                "/addRecipe",
                ((request, response) -> {
                    User user = getUserFromSession(request.session(), conn);
                    String recipeName = request.queryParams("recipeName");
                    String ingredients = request.queryParams("ingredients");
                    String prep = request.queryParams("prep");
                    String prepTime = request.queryParams("prepTime");

                    insertRecipe(conn, recipeName, ingredients, prep, prepTime, user.id);
                    response.redirect("/");
                    return "";
                })
        );
        Spark.post(
                "/edit",
                ((request, response) -> {
                    String recipeName = request.queryParams("recipeName");
                    String ingredients = request.queryParams("ingredients");
                    String prep = request.queryParams("prep");
                    String prepTime = request.queryParams("prepTime");
                    int id = Integer.valueOf(request.queryParams("recipeId"));
                    updateRecipe(conn, id, recipeName, ingredients, prep, prepTime);
                    response.redirect("/");
                    return "";
                })
        );
        Spark.post(
                "/delete",
                ((request, response) -> {
                    int recipeId = Integer.valueOf(request.queryParams("recipeId"));
                    deleteRecipe(conn, recipeId);
                    response.redirect("/");
                    return "";
                })
        );
    }
    static User getUserFromSession(Session session, Connection conn) throws SQLException {
        String name = session.attribute("userName");
        return selectUser(conn, name);
    }
}
