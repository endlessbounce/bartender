package by.khlebnikov.bartender.entity;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * This class represents the Portion model. All cocktails have a list of Portions.
 * A Portion is an ingredient and its amount for a given cocktail.
 */
@XmlRootElement
public class Portion implements Serializable {

    // Constants ----------------------------------------------------------------------------------
    private static final long serialVersionUID = 1L;

    // Properties ---------------------------------------------------------------------------------
    private int id;
    private String ingredientName;
    private String amount;

    // Constructors -------------------------------------------------------------------------------
    public Portion() {
    }

    public Portion(String ingredientName, String amount) {
        this.ingredientName = ingredientName;
        this.amount = amount;
    }

    // Getters and Setters ------------------------------------------------------------------------
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    // Object overrides ---------------------------------------------------------------------------

    /**
     * Compares this Portion to another one.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Portion portion = (Portion) o;

        if (id != portion.id) return false;
        if (ingredientName != null ? !ingredientName.equals(portion.ingredientName) : portion.ingredientName != null)
            return false;
        return amount != null ? amount.equals(portion.amount) : portion.amount == null;
    }

    /**
     * Returns the hashcode for this Portion.
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (ingredientName != null ? ingredientName.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }

    /**
     * Returns the String representation of this Portion.
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Ingredient'" + ingredientName + '\'' +
                ": amount='" + amount + '\'';
    }
}
