import com.google.gson.Gson;
import dao.Sql2oRecipeCardDao;
import dao.Sql2oTagDao;
import models.ApiException;
import models.RecipeCard;
import models.Tag;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class App {

  public static void main(String[] args) {

    //----------DataBase SetUp----------//
    Sql2oRecipeCardDao recipeCardDao;
    Sql2oTagDao vegetableDao;
    Sql2oTagDao mealDao;
    Connection conn;
    Gson gson = new Gson();

    String connectionString = "jdbc:h2:~/recipe-box.db;INIT=RUNSCRIPT from 'classpath:db/create.sql'"; //check me!

    Sql2o sql2o = new Sql2o(connectionString, "", "");
    recipeCardDao = new Sql2oRecipeCardDao(sql2o);
    vegetableDao = new Sql2oTagDao(sql2o,"vegetables");
    mealDao = new Sql2oTagDao(sql2o,"meals");
    conn = sql2o.open();

    populateRecipeBox(recipeCardDao,vegetableDao,mealDao);

    //----------RecipeCard API EndPoints----------//

    //get:Delete recipe card
    get("/recipecards/:id/delete", "application/json", (req, res) -> {
      int recipeCardId = Integer.parseInt(req.params("id"));
      recipeCardDao.deleteById(recipeCardId);
      return gson.toJson(recipeCardDao.findById(recipeCardId--));
    });

    // Enter new Recipe cards
    post("/recipecards/new", "application/json", (req, res) -> {
      RecipeCard recipeCard = gson.fromJson(req.body(), RecipeCard.class);
      recipeCardDao.add(recipeCard);
      res.status(201);
      return gson.toJson(recipeCard);
    });

    // Get All Recipe cards
    get("/recipecards", "application/json", (req, res) -> {
      return gson.toJson(recipeCardDao.getAll());
    });

    // Get Specific Recipe card by ID
    get("/recipecards/:id", "application/json", (req, res) -> {
      int recipeCardId = Integer.parseInt(req.params("id"));
      RecipeCard recipeCardToFind = recipeCardDao.findById(recipeCardId);
      if (recipeCardToFind == null) {
        throw new ApiException(404, String.format("No recipeCard with id: %d exists", recipeCardId));
      }
      return gson.toJson(recipeCardToFind);
    });

    //----------Add Tags to Recipe Card[Join]----------//
    post("/recipecards/:recipeCardId/vegetables/:vegetableId/new", "application/json", (req,res)-> {
      int vegetableId = Integer.parseInt(req.params("vegetableId"));
      int recipeCardId = Integer.parseInt(req.params("recipeCardId"));
      Tag vegetable = vegetableDao.findById(vegetableId);
      vegetableDao.addTagToRecipeCard(vegetable,recipeCardDao.findById(recipeCardId));
      res.status(201);
      return gson.toJson(vegetable);
    });

    post("/recipecards/:recipeCardId/meals/:mealId/new", "application/json", (req,res)-> {
      int mealId = Integer.parseInt(req.params("mealId"));
      int recipeCardId = Integer.parseInt(req.params("recipeCardId"));
      Tag meal = mealDao.findById(mealId);
      mealDao.addTagToRecipeCard(meal,recipeCardDao.findById(recipeCardId));
      res.status(201);
      return gson.toJson(meal);
    });


    //----------Vegetable Tag API EndPoints----------//

    post("/vegetables/new", "application/json", (req, res) -> {
      Tag vegetable = gson.fromJson(req.body(), Tag.class);
      vegetableDao.add(vegetable);
      res.status(201);
      return gson.toJson(vegetable);
    });

    // Get All Vegetable categories
    get("/vegetables", "application/json", (req, res) -> {
      return gson.toJson(vegetableDao.getAll());
    });

    // Get All Recipes for a specific Vegetable Tag
    get("/recipecards/vegetables/:id/index", "application/json", (req, res) -> {
      int vegetableId = Integer.parseInt(req.params("id"));
      if (vegetableDao.findById(vegetableId) == null) {
        throw new ApiException(404, String.format("No Tag with id: %d exists", vegetableId));
      }
      List<RecipeCard> recipeCards = vegetableDao.getAllRecipeCardsForATag(vegetableId);
      return gson.toJson(recipeCards);
    });

    // Get All Vegetable Tags for a Recipe Card
    get("/recipecards/:recipeCardId/vegetables", "application/json", (req, res) -> {
      int recipeCardId = Integer.parseInt(req.params("recipeCardId"));
      List<Tag> vegetables = recipeCardDao.getAllTagsForARecipeCard(recipeCardId, "vegetables");
      return gson.toJson(vegetables);
    });


    //----------Meal Tag API EndPoints----------//

    post("/meals/new", "application/json", (req, res) -> {
      Tag meal = gson.fromJson(req.body(), Tag.class);
      mealDao.add(meal);
      res.status(201);
      return gson.toJson(meal);
    });

    // Get All Meal categories
    get("/meals", "application/json", (req, res) -> {
      return gson.toJson(mealDao.getAll());
    });

    // Get All Recipes for a specific Meal Tag
    get("/recipecards/meals/:id/index", "application/json", (req, res) -> {
      int mealId = Integer.parseInt(req.params("id"));
      if (mealDao.findById(mealId) == null) {
        throw new ApiException(404, String.format("No Tag with id: %d exists", mealId));
      }
      List<RecipeCard> recipeCards = mealDao.getAllRecipeCardsForATag(mealId);
      return gson.toJson(recipeCards);
    });

    // Get All Meal Tags for a Recipe Card
    get("/recipecards/:recipeCardId/meals", "application/json", (req, res) -> {
      int recipeCardId = Integer.parseInt(req.params("recipeCardId"));
      List<Tag> meals = recipeCardDao.getAllTagsForARecipeCard(recipeCardId, "meals");
      return gson.toJson(meals);
    });

    // Get All Available Resources at Root
    get("/", "application/json", (req, res) -> {
      Map<String, String> rootIndex = new HashMap<>();
      rootIndex.put("recipeCards","localhost:4567/recipecards");
      rootIndex.put("vegetables","localhost:4567/vegetables");
      rootIndex.put("meals","localhost:4567/meals");
      return gson.toJson(rootIndex);
    });

    exception(ApiException.class, (errorObjectThatWeMade, req, res) -> {
      ApiException error = (ApiException) errorObjectThatWeMade;
      Map<String, Object> jsonMap = new HashMap<>();
      jsonMap.put("status", error.getStatusCode());
      jsonMap.put("errorMessage", error.getMessage());
      res.type("application/json");
      res.status(error.getStatusCode());
      res.body(gson.toJson(jsonMap));
    });

    //Filter
    after((req,res)->{
      res.type("application/json");

    });

  }


  public static void populateRecipeBox( Sql2oRecipeCardDao recipeCardDao,Sql2oTagDao vegetableDao, Sql2oTagDao mealDao) {

//--------------------Recipe Cards--------------------//
    String name = "Corn Chowder";
    String url = "https://smittenkitchen.com/2017/08/corn-chowder-with-chile-lime-and-cotija/";
    String image = "https://smittenkitchendotcom.files.wordpress.com/2017/08/corn-chowder-with-chile-lime-cotija.jpg?w=750";
    String notes = "make this weekend";
    int rating = 2;
    recipeCardDao.add(new RecipeCard(name, url, image, notes, rating));

    name = "Greens and Cheese Quesadillas";
    url = "https://food52.com/recipes/72397-greens-stuffed-cheese-stuffed-quesadilla";
    image="NA";
    notes="judes favorite";
    rating = 4;
    recipeCardDao.add(new RecipeCard(name, url,image,notes,rating));

    name = "Toro Bravo's Radicchio Salad";
    url = "https://food52.com/recipes/25448-toro-bravo-s-radicchio-salad-with-manchego-vinaigrette";
    image = "https://images.food52.com/v7oiqWwmFNCsRjJJcZbzxA3VcK4=/753x502/85bf3d00-a6b0-42e3-88e4-882b1bbeb3f6--2013-1126_genius_radicchio-salad-020.jpg ";
    notes = "";
    rating = 5;
    recipeCardDao.add(new RecipeCard(name, url, image, notes, rating));

    name = "Coffee and Walnut Layer Cake";
    url = "https://www.nigella.com/recipes/coffee-and-walnut-layer-cake";
    image = "https://www.nigella.com/assets/uploads/recipes/public-thumbnail/coffee-and-walnut-layer-cake-563891d0e8063.jpg";
    notes = "Yes please!";
    rating = 5;
    recipeCardDao.add(new RecipeCard(name, url, image, notes, rating));

  //--------------------Vegetable Tags --------------------//
    vegetableDao.add(new Tag("kale"));    //1
    vegetableDao.add(new Tag("corn"));    //2
    vegetableDao.add(new Tag("radicchio"));//3
    vegetableDao.add(new Tag("carrots"));//4
    vegetableDao.add(new Tag("potatoes"));//5
    vegetableDao.add(new Tag("tomatoes"));//6

    //--------------------Meal Tags --------------------//
    mealDao.add(new Tag("sweets"));    //1
    mealDao.add(new Tag("side"));    //2
    mealDao.add(new Tag("breakfast"));//3
    mealDao.add(new Tag("main"));//4
    mealDao.add(new Tag("brunch"));//5
    mealDao.add(new Tag("salad"));    //6

  //--------------------Join (many-2-many)--------------------//
    vegetableDao.addTagToRecipeCard(vegetableDao.findById(2),recipeCardDao.findById(1));
    vegetableDao.addTagToRecipeCard(vegetableDao.findById(5),recipeCardDao.findById(1));
    vegetableDao.addTagToRecipeCard(vegetableDao.findById(1),recipeCardDao.findById(2));
    vegetableDao.addTagToRecipeCard(vegetableDao.findById(3),recipeCardDao.findById(3));
    vegetableDao.addTagToRecipeCard(vegetableDao.findById(1),recipeCardDao.findById(3));

    mealDao.addTagToRecipeCard(mealDao.findById(4),recipeCardDao.findById(1));
    mealDao.addTagToRecipeCard(mealDao.findById(4),recipeCardDao.findById(2));
    mealDao.addTagToRecipeCard(mealDao.findById(6),recipeCardDao.findById(3));
    mealDao.addTagToRecipeCard(mealDao.findById(1),recipeCardDao.findById(4));
  }

}
