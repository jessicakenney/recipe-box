package dao;

import models.RecipeCard;
import models.UserRecipeCard;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.ConcurrentModificationException;

import static org.junit.Assert.*;

public class Sql2oUserRecipeCardDaoTest {

    private Sql2oUserRecipeCardDao userRecipeCardDao;
    private Connection conn;

    //helpers
    public UserRecipeCard getTestUserRecipeCard() {
        String name = "Coconut Cherry Frozen Yogurt";
        String notes="remember to freeze the icecreammaker vessel first";
        int rating = 4;
        String ingredients = "3 cups Greek Yogurt";
        String directions = "Mix ingredients together";
        String yield = "1 Quart";
        String totalTime = "12 hours";
        String textImage = "NA";
        return new UserRecipeCard(name ,notes ,rating ,ingredients, directions, yield, totalTime, textImage);
    }

    public UserRecipeCard getTestUserRecipeCard2() {
        String name = "Blackberry Scones";
        String notes="make night before and sit in the refrigerator overnight";
        int rating = 4;
        String ingredients = "3 cups Flour ";
        String directions = "Mix ingredients together";
        String yield = "12 scones";
        String totalTime = "30min prep/ 40min baking";
        String textImage = "NA";
        return new UserRecipeCard(name ,notes ,rating ,ingredients, directions, yield, totalTime, textImage);
    }

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        userRecipeCardDao = new Sql2oUserRecipeCardDao(sql2o);
        conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void addingUserRecipeCardSetsId() throws Exception {
        RecipeCard recipeCard = getTestUserRecipeCard();
        userRecipeCardDao.add(recipeCard);
        assertEquals(1, recipeCard.getId());
    }

    @Test
    public void existingRecipeCardsCanBeFoundById() throws Exception {
        RecipeCard recipeCard = getTestUserRecipeCard();
        userRecipeCardDao.add(recipeCard);
        RecipeCard foundRecipeCard = userRecipeCardDao.findById(recipeCard.getId());
        assertEquals(recipeCard.getName(), foundRecipeCard.getName());
    }

    @Test
    public void getAll_allRecipeCardsAreFound() throws Exception {
        RecipeCard userRecipeCard = getTestUserRecipeCard();
        RecipeCard anotherRecipeCard = getTestUserRecipeCard2();
        userRecipeCardDao.add(userRecipeCard);
        userRecipeCardDao.add(anotherRecipeCard);
        int number = userRecipeCardDao.getAll().size();
        assertEquals(2, number);
    }

    @Test
    public void getAll_noRecipeCardsAreFound() throws Exception {
        int number = userRecipeCardDao.getAll().size();
        assertEquals(0, number);
    }

    @Test
    public void update_correctlyUpdates() {
        RecipeCard userRecipeCard = getTestUserRecipeCard();
        userRecipeCardDao.add(userRecipeCard);
        int id = userRecipeCard.getId();
        userRecipeCardDao.update(id, "newName", "newUrl", "newImage", "newNotes", 2);
        RecipeCard updatedRecipeCard = userRecipeCardDao.findById(id);
        assertEquals("newName", updatedRecipeCard.getName());
        assertEquals("newUrl", updatedRecipeCard.getUrl());
        assertEquals("newImage", updatedRecipeCard.getImage());
        assertEquals("newNotes", updatedRecipeCard.getNotes());
        assertEquals(2, updatedRecipeCard.getRating());
    }

    @Test
    public void deleteById_deletesVeryWell() {
        RecipeCard userRecipeCard = getTestUserRecipeCard();
        userRecipeCardDao.add(userRecipeCard);
        userRecipeCardDao.deleteById(userRecipeCard.getId());
        assertEquals(0,userRecipeCardDao.getAll().size());
    }

}