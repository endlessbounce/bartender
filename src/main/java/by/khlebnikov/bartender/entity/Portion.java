package by.khlebnikov.bartender.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Portion {
    private String ingredientName;
    private String amount;

    /*no-arg constructor is used by Jesrsey, etc.*/
    public Portion() { }

    public Portion(String ingredientName, String amount) {
        this.ingredientName = ingredientName;
        this.amount = amount;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Portion that = (Portion) o;

        if (ingredientName != null ? !ingredientName.equals(that.ingredientName) : that.ingredientName != null)
            return false;
        return amount != null ? amount.equals(that.amount) : that.amount == null;
    }

    @Override
    public int hashCode() {
        int result = ingredientName != null ? ingredientName.hashCode() : 0;
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Ingredient'" + ingredientName + '\'' +
                ": amount='" + amount + '\'' +
                '}';
    }
}
