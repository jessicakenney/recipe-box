package dao;

import models.RecipeCard;
import models.Tag;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.ArrayList;
import java.util.List;

  public class Sql2oTagDao implements TagDao {
    private final Sql2o sql2o;
    private String tableName;

    public Sql2oTagDao(Sql2o sql2o, String tableName) {
      this.sql2o = sql2o;
      this.tableName = tableName;
    }

    @Override
    public void add(Tag tag) {

      String sql = "INSERT INTO "+tableName+"(name) VALUES (:name)";
      try (Connection con = sql2o.open()) {
        int id = (int) con.createQuery(sql)
                .bind(tag)
                .executeUpdate()
                .getKey();
        tag.setId(id);
      } catch (Sql2oException ex) {
        System.out.println(ex);
      }

    }

    @Override
    public List<Tag> getAll(){
      String sql = "SELECT * FROM "+tableName;
      try(Connection con = sql2o.open()){
        return con.createQuery(sql)
                .executeAndFetch(Tag.class);
      }
    }

    @Override
    public Tag findById(int id) {
      String sql = "SELECT * FROM "+tableName+" WHERE id = :id";
      try (Connection con = sql2o.open()) {
        return con.createQuery(sql)
                .addParameter("id", id)
                .executeAndFetchFirst(Tag.class);
      }
    }

//------------Join RecipeCard and Tags---------//
    @Override
    public void addTagToRecipeCard(Tag tag, RecipeCard recipeCard){
      String sql = "INSERT INTO recipecards_"+tableName+" (recipeCardId, tagId) VALUES (:recipeCardId, :tagId)";
      try (Connection con = sql2o.open()) {
        con.createQuery(sql)
                .addParameter("recipeCardId", recipeCard.getId())
                .addParameter("tagId", tag.getId())
                .executeUpdate();
      } catch (Sql2oException ex){
        System.out.println(ex);
      }
    }

    @Override
    public List<RecipeCard> getAllRecipeCardsForATag(int tagId) {
      ArrayList<RecipeCard> recipeCards = new ArrayList<>();
      //select all recipecard id's that exist for specific tag id in join table.
      String joinQuery = "SELECT recipeCardId FROM recipecards_"+tableName+" WHERE tagId = :tagId";
      try (Connection con = sql2o.open()) {
        List<Integer> allRecipeCardIds = con.createQuery(joinQuery)
                .addParameter("tagId", tagId)
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

