import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by noellemachin on 2/27/16.
 */

public class RecipeTracker {
    public static HashMap<String, User> userMap = new HashMap<>();
    public static ArrayList<Recipe> recipes = new ArrayList<>();

    public static void main(String[] args) {

        Spark.init();
        Spark.get(
                "/",
                ((request, response) -> {
                    User user = getUserFromSession(request.session());
                    HashMap map = new HashMap();
                    map.put("user", user);
                    map.put("recipes", recipes);
                    if (user == null) {
                        return new ModelAndView(map, "login.html");
                    }
                    else {
                        return new ModelAndView(map, "home.html");
                    }
                }),
                new MustacheTemplateEngine()
        );
        Spark.get(
                "/add",
                ((request, response) -> {
                    User user = getUserFromSession(request.session());
                    return new ModelAndView(user, "add.html");
                }),
                new MustacheTemplateEngine()
        );
        Spark.post(
                "/login",
                ((request, response) -> {
                    String name = request.queryParams("loginName");
                    String pass = request.queryParams("password");

                    if (userMap.get(name).passWord.equals(pass)) {
                        Session session = request.session();
                        session.attribute("userName", name);
                    }
                    response.redirect("/");

                    return "";
                })
        );
        Spark.post(
                "/createAccount",
                ((request, response) -> {
                    String newUser = request.queryParams("newUser");
                    String newPassword = request.queryParams("newPassword");
                    User user = new User(newUser, newPassword);
                    user.setUserName(newUser);
                    user.setPassWord(newPassword);
                    userMap.put(newUser, user);

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
                    User user = getUserFromSession(request.session());
                    String recipeName = request.queryParams("recipeName");
                    String ingredients = request.queryParams("ingredients");
                    String prep = request.queryParams("prep");
                    Recipe recipe = new Recipe (recipeName, ingredients, prep);
                    recipe.setAuthor(user.getUserName());
                    user.recipeList.add(recipe);
                    recipes.add(recipe);
                    response.redirect("/");
                    return "";
                })
        );
    }
    static User getUserFromSession(Session session) {
        String name = session.attribute("userName");
        return userMap.get(name);
    }
}
