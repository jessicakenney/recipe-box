import com.google.gson.Gson;
import dao.Sql2oRecipeCardDao;
import models.ApiException;
import models.RecipeCard;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.post;

public class App {

  public static void main(String[] args) {
    Sql2oRecipeCardDao recipeCardDao;
    Connection conn;
    Gson gson = new Gson();

    String connectionString = "jdbc:h2:~/jadle.db;INIT=RUNSCRIPT from 'classpath:db/create.sql'"; //check me!

    Sql2o sql2o = new Sql2o(connectionString, "", "");
    recipeCardDao = new Sql2oRecipeCardDao(sql2o);
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

    //----------Ingredient API EndPoints----------//




    //Filter
    after((req,res)->{
      res.type("application/json");

    });

  }
}
