import com.google.gson.Gson;
import dao.Sql2oRecipeCardDao;
import dao.Sql2oVegetableDao;
import models.ApiException;
import models.RecipeCard;
import models.Vegetable;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class App {

  public static void main(String[] args) {
    Sql2oRecipeCardDao recipeCardDao;
    Sql2oVegetableDao vegetableDao;
    Connection conn;
    Gson gson = new Gson();

    String connectionString = "jdbc:h2:~/recipe-box.db;INIT=RUNSCRIPT from 'classpath:db/create.sql'"; //check me!

    Sql2o sql2o = new Sql2o(connectionString, "", "");
    recipeCardDao = new Sql2oRecipeCardDao(sql2o);
    vegetableDao = new Sql2oVegetableDao(sql2o);
    conn = sql2o.open();

    //-------------------Populate-----------------//
    String name = "Corn Chowder";
    String url = "https://smittenkitchen.com/2017/08/corn-chowder-with-chile-lime-and-cotija/";
    String image = "https://smittenkitchendotcom.files.wordpress.com/2017/08/corn-chowder-with-chile-lime-cotija.jpg?w=750";
    String notes = "make this weekend";
    int rating = 2;
    recipeCardDao.add(new RecipeCard(name, url, image, notes, rating));

    name = "Greens and Cheese Quesadillas";
    url = "https://food52.com/recipes/72397-greens-stuffed-cheese-stuffed-quesadilla";
    recipeCardDao.add(new RecipeCard(name, url));

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

    vegetableDao.add(new Vegetable("kale"));
    vegetableDao.add(new Vegetable("radicchio"));
    vegetableDao.add(new Vegetable("carrots"));
    vegetableDao.add(new Vegetable("potatoes"));
    vegetableDao.add(new Vegetable("tomatoes"));
    vegetableDao.add(new Vegetable("cauliflower"));


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

    // Add Vegetable To RecipeCard -->Join
    get("/recipecards/:recipeCardId/vegetables/:vegetableId/new", "application/json", (req,res)-> {
      int vegetableId = Integer.parseInt(req.params("vegetableId"));
      int recipeCardId = Integer.parseInt(req.params("recipeCardId"));
      Vegetable vegetable = vegetableDao.findById(vegetableId);
      vegetableDao.addVegetableToRecipeCard(vegetable,recipeCardDao.findById(recipeCardId));
      res.status(201);
      //what to return for this post?
      return gson.toJson(vegetable);
    });




    //----------Vegetable API EndPoints----------//

    post("/vegetables/new", "application/json", (req, res) -> {
      Vegetable vegetable = gson.fromJson(req.body(), Vegetable.class);
      vegetableDao.add(vegetable);
      res.status(201);
      return gson.toJson(vegetable);
    });

    // Get All Vegetable categories
    get("/vegetables", "application/json", (req, res) -> {
      return gson.toJson(vegetableDao.getAll());
    });

    // Get All Recipes for a specific Vegetable
    get("/recipecards/vegetables/:id/index", "application/json", (req, res) -> {
      int vegetableId = Integer.parseInt(req.params("id"));
      if (vegetableDao.findById(vegetableId) == null) {
        throw new ApiException(404, String.format("No Vegetable with id: %d exists", vegetableId));
      }
      List<RecipeCard> recipeCards = vegetableDao.getAllRecipeCardsForAVegetable(vegetableId);
      return gson.toJson(recipeCards);
    });

    // Get All Vegetables for a Recipe Card
    get("/recipecards/:recipeCardId/vegetables", "application/json", (req, res) -> {
      int recipeCardId = Integer.parseInt(req.params("recipeCardId"));
      List<Vegetable> vegetables = recipeCardDao.getAllVegetablesForARecipeCard(recipeCardId);
      return gson.toJson(vegetables);
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
}
