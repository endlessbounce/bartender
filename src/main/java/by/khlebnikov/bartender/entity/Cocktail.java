package by.khlebnikov.bartender.entity;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class represents the Cocktail model.
 */
@XmlRootElement
public class Cocktail implements Serializable {

    // Constants ----------------------------------------------------------------------------------
    private static final long serialVersionUID = 1L;

    // Properties ---------------------------------------------------------------------------------
    private int id;
    private String name;
    private String recipe;
    private String baseDrink;
    private String type;
    private String uri;
    private String slogan;
    private ArrayList<Portion> ingredientList;
    private Date creationDate;

    // Constructors -------------------------------------------------------------------------------
    public Cocktail() {
    }

    public Cocktail(String name, String recipe, String baseDrink,
                    String type, String uri, ArrayList<Portion> ingredientList) {
        this.name = name;
        this.recipe = recipe;
        this.baseDrink = baseDrink;
        this.type = type;
        this.uri = uri;
        this.ingredientList = ingredientList;
    }

    // Getters and Setters ------------------------------------------------------------------------
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    // Object overrides ---------------------------------------------------------------------------

    /**
     * The cocktail ID is unique for each Cocktail. So this should compare Cocktail by ID only.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cocktail cocktail = (Cocktail) o;

        return id == cocktail.id;
    }

    /**
     * The cocktail ID is unique for each Cocktail. Cocktail with same ID should return same hashcode.
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return id;
    }

    /**
     * Returns the String representation of this Cocktail.
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Cocktail{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", recipe='" + recipe + '\'' +
                ", baseDrink='" + baseDrink + '\'' +
                ", type='" + type + '\'' +
                ", uri='" + uri + '\'' +
                ", slogan='" + slogan + '\'' +
                ", ingredientList=" + ingredientList +
                ", creationDate=" + creationDate +
                '}';
    }
}
