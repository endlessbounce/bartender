package by.khlebnikov.bartender.service;

import by.khlebnikov.bartender.entity.Cocktail;
import by.khlebnikov.bartender.entity.Portion;

import java.util.ArrayList;
import java.util.List;

public class CocktailService {

    public List<Cocktail> getCockatailAll(){
        Portion p = new Portion("sugar", "2 spoons");
        ArrayList l = new ArrayList<>();
        l.add(p);
        Cocktail cocktail1 = new Cocktail("OldCock", "5 mugs of water",
                "", "", "no", l);
        Cocktail cocktail2 = new Cocktail("NewCock", "2 mugs of water",
                "", "", "yes", l);
        List<Cocktail> list = new ArrayList<>();
        list.add(cocktail1);
        list.add(cocktail2);
        return list;
    }
}
