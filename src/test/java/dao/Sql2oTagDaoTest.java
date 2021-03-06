package dao;

import com.sun.org.apache.regexp.internal.RE;
import models.RecipeCard;
import models.Tag;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

  public class Sql2oTagDaoTest {

    private Sql2oTagDao vegetableDao;
    private RecipeCardDao recipeCardDao;
    private Connection conn;

    public Tag getTestVegetable() {
      String name = "Corn";
      return new Tag(name);
    }

    public Tag getTestVegetable2() {
      String name = "Kale";
      return new Tag(name);
    }

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
      vegetableDao = new Sql2oTagDao(sql2o, "vegetables");
      recipeCardDao = new Sql2oRecipeCardDao(sql2o);
      conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
      conn.close();
    }

    @Test
    public void addingVegetableSetsId() throws Exception {
      Tag vegetable = getTestVegetable();
      vegetableDao.add(vegetable);
      assertEquals(1, vegetable.getId());
    }

    @Test
    public void existingVegetablesCanBeFoundById() throws Exception {
      Tag vegetable = getTestVegetable();
      vegetableDao.add(vegetable);
      Tag foundVegetable = vegetableDao.findById(vegetable.getId());
      assertEquals(vegetable, foundVegetable);
    }

    @Test
    public void getAll_allVegetablesAreFound() throws Exception {
      Tag vegetable = getTestVegetable();
      Tag anotherVegetable = getTestVegetable2();
      vegetableDao.add(vegetable);
      vegetableDao.add(anotherVegetable);
      Tag[] testVegetables = {vegetable, anotherVegetable};
      assertEquals(Arrays.asList(testVegetables), vegetableDao.getAll());
    }

    @Test
    public void getAll_noVegetablesAreFound() throws Exception {
      int number = vegetableDao.getAll().size();
      assertEquals(0, number);
    }

    //includes both getAllRecipeCardsForAVegetable and getAllVegetablesForARecipeCard
    @Test
    public void addVegetableToRecipeCard_getAllRecipeCardsForAVegetable() throws Exception {
      Tag vegetable = getTestVegetable();
      Tag anotherVegetable = getTestVegetable2();
      RecipeCard recipeCard = getTestRecipeCard();
      RecipeCard recipeCard2 = getTestRecipeCard2();
      vegetableDao.add(vegetable);
      vegetableDao.add(anotherVegetable);
      Tag tomato = new Tag("tomato");
      vegetableDao.add(tomato);
      recipeCardDao.add(recipeCard);
      recipeCardDao.add(recipeCard2);
      vegetableDao.addTagToRecipeCard(vegetable, recipeCard);
      vegetableDao.addTagToRecipeCard(tomato, recipeCard);
      vegetableDao.addTagToRecipeCard(anotherVegetable, recipeCard2);
      vegetableDao.addTagToRecipeCard(tomato, recipeCard2);
      List<RecipeCard>testRecipes = vegetableDao.getAllRecipeCardsForATag(vegetable.getId());
      assertEquals( recipeCard, testRecipes.get(0));
      List<RecipeCard>test2Recipes= vegetableDao.getAllRecipeCardsForATag(anotherVegetable.getId());
      assertEquals( recipeCard2, test2Recipes.get(0));
      List<RecipeCard>test3Recipes= vegetableDao.getAllRecipeCardsForATag(tomato.getId());
      assertEquals( 2, test3Recipes.size());
      List<Tag> testVegetables = recipeCardDao.getAllTagsForARecipeCard(recipeCard.getId(),"vegetables");
      Tag[] vegesExpected = { vegetable, tomato };
      assertEquals ( Arrays.asList(vegesExpected), testVegetables);
    }
  }
