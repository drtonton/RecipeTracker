import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;
import java.util.HashMap;

/**
 * Created by noellemachin on 2/27/16.
 */
public class RecipeTracker {
    public static HashMap<String, User> userMap = new HashMap<>();

    public static void main(String[] args) {

        Spark.init();
        Spark.get(
                "/",
                ((request, response) -> {
                    User user = getUserFromSession(request.session());
                    if (user == null) {
                        return new ModelAndView(userMap, "login.html");
                    }
                    else {
                        return new ModelAndView(user, "home.html");
                    }
                }),
                new MustacheTemplateEngine()
        );
        Spark.post(
                "/login",
                ((request, response) -> {
                    String name = request.queryParams("loginName");
                    String pass = request.queryParams("passWord");

                    if (userMap.get(name).getPassWord().equals(pass)) {
                        Session session = request.session();
                        session.attribute("userName", name);
                        response.redirect("/");
                    }

                return "";
                }),
                new MustacheTemplateEngine()
        );
        Spark.post(
                "/createAccount",
                ((request, response) -> {
                    String newUser = request.queryParams("newUser");
                    String newPassword = request.queryParams("newPassword");
                    User user = new User(newUser, newPassword);
                    userMap.put(newUser, user);

                    Session session = request.session();
                    session.attribute("userName", newUser);
                    response.redirect("/");
                    return "";
                }),
                new MustacheTemplateEngine()
        );



    }
    static User getUserFromSession(Session session) {
        String name = session.attribute("userName");
        return userMap.get(name);
    }

}
