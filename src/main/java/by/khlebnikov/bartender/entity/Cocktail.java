package by.khlebnikov.bartender.entity;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Date;

@XmlRootElement
public class Cocktail {
    private int id;
    private String name;
    private String recipe;
    private String baseDrink;
    private String type;
    private String uri;
    private ArrayList<Portion> ingredientList;
    private Date creationDate;

    /*no-arg constructor is used by Jesrsey, etc.*/
    public Cocktail() { }

    public Cocktail(String name,
                    String recipe,
                    String baseDrink,
                    String type,
                    String uri,
                    ArrayList<Portion> ingredientList) {
        this.name = name;
        this.recipe = recipe;
        this.baseDrink = baseDrink;
        this.type = type;
        this.uri = uri;
        this.ingredientList = ingredientList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public String getBaseDrink() {
        return baseDrink;
    }

    public void setBaseDrink(String baseDrink) {
        this.baseDrink = baseDrink;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public ArrayList<Portion> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(ArrayList<Portion> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cocktail cocktail = (Cocktail) o;

        if (id != cocktail.id) return false;
        if (name != null ? !name.equals(cocktail.name) : cocktail.name != null) return false;
        if (recipe != null ? !recipe.equals(cocktail.recipe) : cocktail.recipe != null) return false;
        if (baseDrink != null ? !baseDrink.equals(cocktail.baseDrink) : cocktail.baseDrink != null) return false;
        if (type != null ? !type.equals(cocktail.type) : cocktail.type != null) return false;
        if (uri != null ? !uri.equals(cocktail.uri) : cocktail.uri != null) return false;
        return ingredientList != null ? ingredientList.equals(cocktail.ingredientList) : cocktail.ingredientList == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (recipe != null ? recipe.hashCode() : 0);
        result = 31 * result + (baseDrink != null ? baseDrink.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (uri != null ? uri.hashCode() : 0);
        result = 31 * result + (ingredientList != null ? ingredientList.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Cocktail{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", recipe='" + recipe + '\'' +
                ", baseDrink='" + baseDrink + '\'' +
                ", type='" + type + '\'' +
                ", uri='" + uri + '\'' +
                ", ingredientList=" + ingredientList +
                '}';
    }
}
