package by.khlebnikov.bartender.entity;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement
public class FormData {
    private ArrayList<String> drinkType;
    private ArrayList<String> baseDrink;
    private ArrayList<String> ingredient;

    public FormData() { }

    public FormData(ArrayList<String> drinkType, ArrayList<String> baseDrink, ArrayList<String> ingredient) {
        this.drinkType = drinkType;
        this.baseDrink = baseDrink;
        this.ingredient = ingredient;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FormData formData = (FormData) o;

        if (drinkType != null ? !drinkType.equals(formData.drinkType) : formData.drinkType != null) return false;
        if (baseDrink != null ? !baseDrink.equals(formData.baseDrink) : formData.baseDrink != null) return false;
        return ingredient != null ? ingredient.equals(formData.ingredient) : formData.ingredient == null;
    }

    @Override
    public int hashCode() {
        int result = drinkType != null ? drinkType.hashCode() : 0;
        result = 31 * result + (baseDrink != null ? baseDrink.hashCode() : 0);
        result = 31 * result + (ingredient != null ? ingredient.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FormData{" +
                "drinkType=" + drinkType +
                ", baseDrink=" + baseDrink +
                ", ingredient=" + ingredient +
                '}';
    }
}
