package by.khlebnikov.bartender.entity;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class CatalogParameter {
    private List<String> drinkTypeList;
    private List<String> baseDrinkList;
    private List<String> ingredientList;

    public CatalogParameter() { }

    public CatalogParameter(List<String> drinkTypeList, List<String> baseDrinkList, List<String> ingredientList) {
        this.drinkTypeList = drinkTypeList;
        this.baseDrinkList = baseDrinkList;
        this.ingredientList = ingredientList;
    }

    public List<String> getDrinkTypeList() {
        return drinkTypeList;
    }

    public void setDrinkTypeList(List<String> drinkTypeList) {
        this.drinkTypeList = drinkTypeList;
    }

    public List<String> getBaseDrinkList() {
        return baseDrinkList;
    }

    public void setBaseDrinkList(List<String> baseDrinkList) {
        this.baseDrinkList = baseDrinkList;
    }

    public List<String> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<String> ingredientList) {
        this.ingredientList = ingredientList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CatalogParameter that = (CatalogParameter) o;

        if (drinkTypeList != null ? !drinkTypeList.equals(that.drinkTypeList) : that.drinkTypeList != null)
            return false;
        if (baseDrinkList != null ? !baseDrinkList.equals(that.baseDrinkList) : that.baseDrinkList != null)
            return false;
        return ingredientList != null ? ingredientList.equals(that.ingredientList) : that.ingredientList == null;
    }

    @Override
    public int hashCode() {
        int result = drinkTypeList != null ? drinkTypeList.hashCode() : 0;
        result = 31 * result + (baseDrinkList != null ? baseDrinkList.hashCode() : 0);
        result = 31 * result + (ingredientList != null ? ingredientList.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CatalogParameter{" +
                "drinkTypeList=" + drinkTypeList +
                ", baseDrinkList=" + baseDrinkList +
                ", ingredientList=" + ingredientList +
                '}';
    }
}
