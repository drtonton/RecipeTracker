/**
 * Created by noellemachin on 2/27/16.
 */
public class Recipe {
    String recipeName;
    int numIngredients;
    String cookingNotes;

    public Recipe(String recipeName, int numIngredients, String cookingNotes) {
        this.recipeName = recipeName;
        this.numIngredients = numIngredients;
        this.cookingNotes = cookingNotes;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public int getNumIngredients() {
        return numIngredients;
    }

    public void setNumIngredients(int numIngredients) {
        this.numIngredients = numIngredients;
    }

    public String getCookingNotes() {
        return cookingNotes;
    }

    public void setCookingNotes(String cookingNotes) {
        this.cookingNotes = cookingNotes;
    }
}
