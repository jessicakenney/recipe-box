package dao;

import models.RecipeCard;
import models.Vegetable;

import java.util.List;

public interface VegetableDao {

  //Create
  void add(Vegetable vegetable);
  //join
  void addVegetableToRecipeCard(Vegetable vegetable, RecipeCard recipeCard);

  //Read
  List<Vegetable> getAll();
  Vegetable findById(int id);
  List<RecipeCard> getAllRecipeCardsForAVegetable(int id);

  //update
  //omit for now

  //delete
  //void deleteById(int id);


}
