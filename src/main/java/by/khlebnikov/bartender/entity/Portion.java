package by.khlebnikov.bartender.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Portion {
    private int id;
    private String ingredientName;
    private String amount;

    /*no-arg constructor is used by Jesrsey, etc.*/
    public Portion() { }

    public Portion(String ingredientName, String amount) {
        this.ingredientName = ingredientName;
        this.amount = amount;
    }

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

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (ingredientName != null ? ingredientName.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Ingredient'" + ingredientName + '\'' +
                ": amount='" + amount + '\'';
    }
}
