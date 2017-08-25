package dao;

import models.RecipeCard;
import models.Vegetable;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.ArrayList;
import java.util.List;

  public class Sql2oVegetableDao implements VegetableDao {
    private final Sql2o sql2o;

    public Sql2oVegetableDao(Sql2o sql2o) {
      this.sql2o = sql2o;
    }

    @Override
    public void add(Vegetable vegetable) {
      String sql = "INSERT INTO vegetables (name) VALUES (:name)";
      try (Connection con = sql2o.open()) {
        int id = (int) con.createQuery(sql)
                .bind(vegetable)
                .executeUpdate()
                .getKey();
        vegetable.setId(id);
      } catch (Sql2oException ex) {
        System.out.println(ex);
      }
    }

    @Override
    public List<Vegetable> getAll(){
      try(Connection con = sql2o.open()){
        return con.createQuery("SELECT * FROM vegetables")
                .executeAndFetch(Vegetable.class);
      }
    }

    @Override
    public Vegetable findById(int id) {
      try (Connection con = sql2o.open()) {
        return con.createQuery("SELECT * FROM vegetables WHERE id = :id")
                .addParameter("id", id)
                .executeAndFetchFirst(Vegetable.class);
      }
    }

//------------Join RecipeCard and Vegetables---------//
    @Override
    public void addVegetableToRecipeCard(Vegetable vegetable, RecipeCard recipeCard){
      String sql = "INSERT INTO recipecards_vegetables (recipeCardId, vegetableId) VALUES (:recipeCardId, :vegetableId)";
      try (Connection con = sql2o.open()) {
        con.createQuery(sql)
                .addParameter("recipeCardId", recipeCard.getId())
                .addParameter("vegetableId", vegetable.getId())
                .executeUpdate();
      } catch (Sql2oException ex){
        System.out.println(ex);
      }
    }

    @Override
    public List<RecipeCard> getAllRecipeCardsForAVegetable(int vegetableId) {
      ArrayList<RecipeCard> recipeCards = new ArrayList<>();
      //select all recipecard id's that exist for specific vegetable id in join table.
      String joinQuery = "SELECT recipeCardId FROM recipecards_vegetables WHERE vegetableId = :vegetableId";

      try (Connection con = sql2o.open()) {
        List<Integer> allRecipeCardIds = con.createQuery(joinQuery)
                .addParameter("vegetableId", vegetableId)
                .executeAndFetch(Integer.class); //getting recipe Ids
        for (Integer recipeCardId : allRecipeCardIds){
          // now grab all the recipeCards using ids
          String recipeCardQuery = "SELECT * FROM recipecards WHERE id = :recipeCardId";
          recipeCards.add(
                  con.createQuery(recipeCardQuery)
                          .addParameter("recipeCardId", recipeCardId)
                          .executeAndFetchFirst(RecipeCard.class));
        }
      } catch (Sql2oException ex){
        System.out.println(ex);
      }
      return recipeCards;
    }


  }

