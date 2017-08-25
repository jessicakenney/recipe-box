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
    void update(int id, String name, String url, String image,String notes,int rating);

    //Delete
    void deleteById(int id);
 }
