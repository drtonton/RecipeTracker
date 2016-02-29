/**
 * Created by noellemachin on 2/27/16.
 */
public class Recipe {
    String recipeName;
    String ingredients;
    String prep;
    String author;

    public Recipe(String recipeName, String ingredients, String prep) {
        this.recipeName = recipeName;
        this.ingredients = ingredients;
        this.prep = prep;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getPrep() {
        return prep;
    }

    public void setPrep(String prep) {
        this.prep = prep;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
}
