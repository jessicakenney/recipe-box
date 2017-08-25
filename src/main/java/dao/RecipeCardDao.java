package dao;

import models.RecipeCard;

import java.util.List;

public interface RecipeCardDao {

    //Create
    void add(RecipeCard recipeCard);
    //void addVegetableToRecipeCard (RecipeCard recipeCard, String vegetable);

    //Read
    RecipeCard findById(int id);
    List<RecipeCard> getAll();
    //List<RecipeCard> getAllRecipeCardsByVegetable(String vegetable);

    //Update
    //void update(int id, Type1 var1, Type2 var2);

    //Delete
    //void deleteById(int id);
 }
