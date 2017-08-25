import com.google.gson.Gson;
import dao.Sql2oRecipeCardDao;
import dao.Sql2oVegetableDao;
import models.ApiException;
import models.RecipeCard;
import models.Vegetable;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.post;

public class App {

  public static void main(String[] args) {
    Sql2oRecipeCardDao recipeCardDao;
    Sql2oVegetableDao vegetableDao;
    Connection conn;
    Gson gson = new Gson();

    String connectionString = "jdbc:h2:~/jadle.db;INIT=RUNSCRIPT from 'classpath:db/create.sql'"; //check me!

    Sql2o sql2o = new Sql2o(connectionString, "", "");
    recipeCardDao = new Sql2oRecipeCardDao(sql2o);
    vegetableDao = new Sql2oVegetableDao(sql2o);
    conn = sql2o.open();

    //----------RecipeCard API EndPoints----------//

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
    post("//:foodTypeId/restaurants/:restaurantId", "application/json", (req,res)-> {
      Vegetable vegetable = gson.fromJson(req.body(), Vegetable.class);
      vegetableDao.add(vegetable);
      int restaurantId = Integer.parseInt(req.params("restaurantId"));
      RecipeCard restaurant = recipeCardDao.findById(restaurantId);
      vegetableDao.addVegetableToRecipeCard(vegetable,restaurant);
      res.status(201);
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
    get("/vegetables/:id/recipecards", "application/json", (req, res) -> {
      int vegetableId = Integer.parseInt(req.params("id"));
      Vegetable vegetableToFind = vegetableDao.findById(vegetableId);
      if (vegetableToFind == null) {
        throw new ApiException(404, String.format("No vegetable with id: %d exists", vegetableId));
      }

      return gson.toJson(vegetableToFind);
    });




    //Filter
    after((req,res)->{
      res.type("application/json");

    });

  }
}
