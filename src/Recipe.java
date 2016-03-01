/**
 * Created by noellemachin on 2/27/16.
 */
public class Recipe {
    int id;
    String recipeName;
    String ingredients;
    String prep;
    int prepTime;
    String author;

    public Recipe(int id, String recipeName, String ingredients, String prep, int prepTime) {
        this.id = id;
        this.recipeName = recipeName;
        this.ingredients = ingredients;
        this.prep = prep;
        this.prepTime = prepTime;
    }

    public Recipe() {
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

    public int getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }
}
