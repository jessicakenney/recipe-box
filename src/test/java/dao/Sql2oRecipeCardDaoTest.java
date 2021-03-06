package dao;

import models.RecipeCard;
import models.UserRecipeCard;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class Sql2oRecipeCardDaoTest {

  private Sql2oRecipeCardDao recipeCardDao;
  private Connection conn;

  public RecipeCard getTestRecipeCard() {
    String name = "Corn Chowder";
    String url = "https://smittenkitchen.com/2017/08/corn-chowder-with-chile-lime-and-cotija/";
    String image = "https://smittenkitchendotcom.files.wordpress.com/2017/08/corn-chowder-with-chile-lime-cotija.jpg?w=750";
    String notes = "make this weekend";
    int rating = 5;
    return new RecipeCard(name, url, image, notes, rating);
  }

  public RecipeCard getTestRecipeCard2() {
    String name = "Greens and Cheese Quesadillas";
    String url = "https://food52.com/recipes/72397-greens-stuffed-cheese-stuffed-quesadilla";
    String image="NA";
    String notes="judes favorite";
    int rating = 4;
    return new RecipeCard(name, url,image,notes,rating);
  }

  @Before
  public void setUp() throws Exception {
    String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
    Sql2o sql2o = new Sql2o(connectionString, "", "");
    recipeCardDao = new Sql2oRecipeCardDao(sql2o);
    conn = sql2o.open();
  }

  @After
  public void tearDown() throws Exception {
    conn.close();
  }

  @Test
  public void addingRecipeCardSetsId() throws Exception {
    RecipeCard recipeCard = getTestRecipeCard();
    recipeCardDao.add(recipeCard);
    assertEquals(1, recipeCard.getId());
  }

  @Test
  public void existingRecipeCardsCanBeFoundById() throws Exception {
    RecipeCard recipeCard = getTestRecipeCard();
    recipeCardDao.add(recipeCard);
    RecipeCard foundRecipeCard = recipeCardDao.findById(recipeCard.getId());
    assertEquals(recipeCard, foundRecipeCard);
  }

  @Test
  public void getAll_allRecipeCardsAreFound() throws Exception {
    RecipeCard recipeCard = getTestRecipeCard();
    RecipeCard anotherRecipeCard = getTestRecipeCard2();
    recipeCardDao.add(recipeCard);
    recipeCardDao.add(anotherRecipeCard);
    int number = recipeCardDao.getAll().size();
    assertEquals(2, number);
  }

  @Test
  public void getAll_noRecipeCardsAreFound() throws Exception {
    int number = recipeCardDao.getAll().size();
    assertEquals(0, number);
  }

  @Test
  public void update_correctlyUpdates() {
    RecipeCard recipeCard = getTestRecipeCard();
    recipeCardDao.add(recipeCard);
    int id = recipeCard.getId();
    recipeCardDao.update(id, "newName", "newUrl", "newImage", "newNotes", 2);
    RecipeCard updatedRecipeCard = recipeCardDao.findById(id);
    assertEquals("newName", updatedRecipeCard.getName());
    assertEquals("newUrl", updatedRecipeCard.getUrl());
    assertEquals("newImage", updatedRecipeCard.getImage());
    assertEquals("newNotes", updatedRecipeCard.getNotes());
    assertEquals(2, updatedRecipeCard.getRating());
  }

  @Test
  public void deleteById_deletesVeryWell() {
    RecipeCard recipeCard = getTestRecipeCard();
    recipeCardDao.add(recipeCard);
    recipeCardDao.deleteById(recipeCard.getId());
    assertEquals(0,recipeCardDao.getAll().size());
  }

}
