package dao;

import models.RecipeCard;
import models.Tag;

import java.util.List;

public interface TagDao {

  //Create
  void add(Tag tag);
  //join
  void addTagToRecipeCard(Tag tag, RecipeCard recipeCard);

  //Read
  List<Tag> getAll();
  Tag findById(int id);
  List<RecipeCard> getAllRecipeCardsForATag(int id);

  //update
  //omit for now

  //delete
  //void deleteById(int id);


}
