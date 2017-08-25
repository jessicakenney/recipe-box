package dao;

import models.RecipeCard;
import models.Vegetable;

import java.util.List;

public interface RecipeCardDao {

    //Create
    void add(RecipeCard recipeCard);

    //Read
    RecipeCard findById(int id);
    List<RecipeCard> getAll();
    List<Vegetable> getAllVegetablesForARecipeCard(int id);

    //Update
    //void update(int id, Type1 var1, Type2 var2);

    //Delete
    //void deleteById(int id);
 }
