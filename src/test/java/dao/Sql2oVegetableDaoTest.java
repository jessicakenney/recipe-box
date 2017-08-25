package dao;

import com.sun.org.apache.regexp.internal.RE;
import models.RecipeCard;
import models.Vegetable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

  public class Sql2oVegetableDaoTest {

    private Sql2oVegetableDao vegetableDao;
    private RecipeCardDao recipeCardDao;
    private Connection conn;

    public Vegetable getTestVegetable() {
      String name = "Corn";
      return new Vegetable(name);
    }

    public Vegetable getTestVegetable2() {
      String name = "Kale";
      return new Vegetable(name);
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
      return new RecipeCard(name, url);
    }


    @Before
    public void setUp() throws Exception {
      String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
      Sql2o sql2o = new Sql2o(connectionString, "", "");
      vegetableDao = new Sql2oVegetableDao(sql2o);
      recipeCardDao = new Sql2oRecipeCardDao(sql2o);
      conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
      conn.close();
    }

    @Test
    public void addingVegetableSetsId() throws Exception {
      Vegetable vegetable = getTestVegetable();
      vegetableDao.add(vegetable);
      assertEquals(1, vegetable.getId());
    }

    @Test
    public void existingVegetablesCanBeFoundById() throws Exception {
      Vegetable vegetable = getTestVegetable();
      vegetableDao.add(vegetable);
      Vegetable foundVegetable = vegetableDao.findById(vegetable.getId());
      assertEquals(vegetable, foundVegetable);
    }

    @Test
    public void getAll_allVegetablesAreFound() throws Exception {
      Vegetable vegetable = getTestVegetable();
      Vegetable anotherVegetable = getTestVegetable2();
      vegetableDao.add(vegetable);
      vegetableDao.add(anotherVegetable);
      Vegetable[] testVegetables = {vegetable, anotherVegetable};
      assertEquals(Arrays.asList(testVegetables), vegetableDao.getAll());
    }

    @Test
    public void getAll_noVegetablesAreFound() throws Exception {
      int number = vegetableDao.getAll().size();
      assertEquals(0, number);
    }

    @Test
    public void addVegetableToRecipeCard_getAllRecipeCardsForAVegetable() throws Exception {
      Vegetable vegetable = getTestVegetable();
      Vegetable anotherVegetable = getTestVegetable2();
      RecipeCard recipeCard = getTestRecipeCard();
      RecipeCard recipeCard2 = getTestRecipeCard2();
      vegetableDao.add(vegetable);
      vegetableDao.add(anotherVegetable);
      Vegetable tomato = new Vegetable("tomato");
      vegetableDao.add(tomato);
      recipeCardDao.add(recipeCard);
      recipeCardDao.add(recipeCard2);
      vegetableDao.addVegetableToRecipeCard(vegetable, recipeCard);
      vegetableDao.addVegetableToRecipeCard(tomato, recipeCard);
      vegetableDao.addVegetableToRecipeCard(anotherVegetable, recipeCard2);
      vegetableDao.addVegetableToRecipeCard(tomato, recipeCard2);
      List<RecipeCard>testRecipes = vegetableDao.getAllRecipeCardsForAVegetable(vegetable.getId());
      assertEquals( recipeCard, testRecipes.get(0));
      List<RecipeCard>test2Recipes= vegetableDao.getAllRecipeCardsForAVegetable(anotherVegetable.getId());
      assertEquals( recipeCard2, test2Recipes.get(0));
      List<RecipeCard>test3Recipes= vegetableDao.getAllRecipeCardsForAVegetable(tomato.getId());
      assertEquals( 2, test3Recipes.size());
      List<Vegetable> testVegetables = recipeCardDao.getAllVegetablesForARecipeCard(recipeCard.getId());
      System.out.println(testVegetables.get(0).getName());
      System.out.println(testVegetables.get(1).getName());
      Vegetable[] vegesExpected = { vegetable, tomato };
      assertEquals ( Arrays.asList(vegesExpected), testVegetables);
    }
  }
