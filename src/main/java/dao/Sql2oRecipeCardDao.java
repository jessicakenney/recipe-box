package dao;

import models.RecipeCard;
import models.Vegetable;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.ArrayList;
import java.util.List;


public class Sql2oRecipeCardDao implements RecipeCardDao {
    private final Sql2o sql2o;

    public Sql2oRecipeCardDao(Sql2o sql2o) {
      this.sql2o = sql2o;
    }

    @Override
    public void add(RecipeCard recipeCard) {
      String sql = "INSERT INTO recipecards (name,url,image,notes,rating) VALUES (:name,:url,:image,:notes,:rating)";
      try (Connection con = sql2o.open()) { //
        int id = (int) con.createQuery(sql) //make a new variable
                .bind(recipeCard)
                .executeUpdate()
                .getKey();
        recipeCard.setId(id);
      } catch (Sql2oException ex) {
        System.out.println(ex);
      }
    }

   @Override
   public List<RecipeCard> getAll(){
     try(Connection con = sql2o.open()){
       return con.createQuery("SELECT * FROM recipecards")
               .executeAndFetch(RecipeCard.class);
     }
   }

   @Override
   public RecipeCard findById(int id) {
     try (Connection con = sql2o.open()) {
       return con.createQuery("SELECT * FROM recipecards WHERE id = :id")
               .addParameter("id", id)
               .executeAndFetchFirst(RecipeCard.class);
     }
   }

  @Override
  public List<Vegetable> getAllVegetablesForARecipeCard(int recipeCardId) {
    ArrayList<Vegetable> vegetables = new ArrayList<>();
    //select all vegetables id's that exist for specific Recipe in join table.
    String joinQuery = "SELECT vegetableId FROM recipecards_vegetables WHERE recipeCardId = :recipeCardId";
    try (Connection con = sql2o.open()) {
      List<Integer> allVegetableIds = con.createQuery(joinQuery)
              .addParameter("recipeCardId", recipeCardId)
              .executeAndFetch(Integer.class);
      for (Integer vegetableId : allVegetableIds){
        // now grab all the vegetables using ids
        String recipeCardQuery = "SELECT * FROM vegetables WHERE id = :vegetableId";
        vegetables.add(
                con.createQuery(recipeCardQuery)
                        .addParameter("vegetableId", vegetableId)
                        .executeAndFetchFirst(Vegetable.class));
      }
    } catch (Sql2oException ex){
      System.out.println(ex);
    }
    return vegetables;
  }



 }
