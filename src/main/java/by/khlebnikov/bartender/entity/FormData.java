package by.khlebnikov.bartender.entity;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This object represents the data of the search form of the catalog page
 */
@XmlRootElement
public class FormData implements Serializable {

    // Constants ----------------------------------------------------------------------------------
    private static final long serialVersionUID = 1L;

    // Properties ---------------------------------------------------------------------------------
    private ArrayList<String> drinkType;
    private ArrayList<String> baseDrink;
    private ArrayList<String> ingredient;

    // Constructors -------------------------------------------------------------------------------
    public FormData() {
    }

    public FormData(ArrayList<String> drinkType, ArrayList<String> baseDrink, ArrayList<String> ingredient) {
        this.drinkType = drinkType;
        this.baseDrink = baseDrink;
        this.ingredient = ingredient;
    }

    // Getters and Setters ------------------------------------------------------------------------
    public ArrayList<String> getDrinkType() {
        return drinkType;
    }

    public void setDrinkType(ArrayList<String> drinkType) {
        this.drinkType = drinkType;
    }

    public ArrayList<String> getBaseDrink() {
        return baseDrink;
    }

    public void setBaseDrink(ArrayList<String> baseDrink) {
        this.baseDrink = baseDrink;
    }

    public ArrayList<String> getIngredient() {
        return ingredient;
    }

    public void setIngredient(ArrayList<String> ingredient) {
        this.ingredient = ingredient;
    }

    // Object overrides ---------------------------------------------------------------------------

    /**
     * Compares this FormData to another one
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FormData formData = (FormData) o;

        if (drinkType != null ? !drinkType.equals(formData.drinkType) : formData.drinkType != null) return false;
        if (baseDrink != null ? !baseDrink.equals(formData.baseDrink) : formData.baseDrink != null) return false;
        return ingredient != null ? ingredient.equals(formData.ingredient) : formData.ingredient == null;
    }

    /**
     * Returns the hashcode for this FormData
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int result = drinkType != null ? drinkType.hashCode() : 0;
        result = 31 * result + (baseDrink != null ? baseDrink.hashCode() : 0);
        result = 31 * result + (ingredient != null ? ingredient.hashCode() : 0);
        return result;
    }

    /**
     * Returns the String representation of this FormData.
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "FormData{" +
                "drinkType=" + drinkType +
                ", baseDrink=" + baseDrink +
                ", ingredient=" + ingredient +
                '}';
    }
}
