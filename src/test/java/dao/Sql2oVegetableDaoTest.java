package dao;

import models.Vegetable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.Arrays;

import static org.junit.Assert.*;

  public class Sql2oVegetableDaoTest {

    private Sql2oVegetableDao vegetableDao;
    //private RecipeCardDao recipeCardDao;
    private Connection conn;

    public Vegetable getTestVegetable() {
      String name = "Kale";
      return new Vegetable(name);
    }

    public Vegetable getTestVegetable2() {
      String name = "Corn";
      return new Vegetable(name);
    }

    @Before
    public void setUp() throws Exception {
      String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
      Sql2o sql2o = new Sql2o(connectionString, "", "");
      vegetableDao = new Sql2oVegetableDao(sql2o);
      //recipeCardDao = new Sql2oRecipeCardDao(sql2o);
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
  }
