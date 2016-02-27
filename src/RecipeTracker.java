import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by noellemachin on 2/27/16.
 */
public class RecipeTracker {
    public static HashMap<String, User> users = new HashMap<>();

    public static void main(String[] args) {
        ArrayList<Recipe> recipeList = new ArrayList<>();

        Spark.init();
        Spark.get(
                "/",
                ((request, response) -> {
                    Session session = request.session();
                    String userName = session.attribute("userName");
                }),
                new MustacheTemplateEngine()
        );



    }

    static User getUserFromSession(Session session) {
        String name = session.attribute("userName");
        return users.get(name);

    }
}
