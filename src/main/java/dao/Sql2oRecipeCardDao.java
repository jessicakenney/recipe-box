package dao;

import models.RecipeCard;
import models.Tag;
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
        int id = (int) con.createQuery(sql)
                .bind(recipeCard)
                .throwOnMappingFailure(false)
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
               .throwOnMappingFailure(false)
               .executeAndFetch(RecipeCard.class);
     }
   }

   @Override
   public RecipeCard findById(int id) {
     try (Connection con = sql2o.open()) {
       return con.createQuery("SELECT * FROM recipecards WHERE id = :id")
               .addParameter("id", id)
               .throwOnMappingFailure(false)
               .executeAndFetchFirst(RecipeCard.class);
     }
   }

  @Override
  public void update(int id, String name, String url,String image, String notes, int rating) {
    String sql = "UPDATE recipecards SET (name,url,image,notes,rating)=(:name,:url,:image,:notes,:rating) WHERE id=:id";
    try (Connection con = sql2o.open()) {
      con.createQuery(sql)
              .addParameter("id", id)
              .addParameter("name", name)
              .addParameter("url", url)
              .addParameter("image", image)
              .addParameter("notes", notes)
              .addParameter("rating", rating)
              .executeUpdate();
    } catch (Sql2oException ex) {
      System.out.println(ex);
    }
  }

  @Override
  //will want to delete from all joined tables
  public void deleteById(int id) {
    String sql = "DELETE from recipecards WHERE id=:id";
    String deleteJoinVege = "DELETE from recipecards_vegetables WHERE recipeCardid = :recipeCardId";
    String deleteJoinMeal = "DELETE from recipecards_meals WHERE recipeCardid = :recipeCardId";
    try (Connection con = sql2o.open()) {
      con.createQuery(sql)
              .addParameter("id", id)
              .executeUpdate();
      con.createQuery(deleteJoinVege)
              .addParameter("recipeCardId",id)
              .executeUpdate();
      con.createQuery(deleteJoinMeal)
              .addParameter("recipeCardId",id)
              .executeUpdate();
    } catch (Sql2oException ex) {
      System.out.println(ex);
    }
  }

  @Override
  public List<Tag> getAllTagsForARecipeCard(int recipeCardId, String tableName) {

    ArrayList<Tag> tags = new ArrayList<>();
    //select all tag id's that exist for specific Recipe in join table.
    String joinQuery = "SELECT tagId FROM recipecards_"+tableName+" WHERE recipeCardId = :recipeCardId";
    try (Connection con = sql2o.open()) {
      List<Integer> allTagIds = con.createQuery(joinQuery)
              .addParameter("recipeCardId", recipeCardId)
              .executeAndFetch(Integer.class);
      for (Integer tagId : allTagIds){
        // now grab all the tags using ids
        String recipeCardQuery = "SELECT * FROM "+tableName+" WHERE id = :tagId";
        tags.add(
                con.createQuery(recipeCardQuery)
                        .addParameter("tagId", tagId)
                        .executeAndFetchFirst(Tag.class));
      }
    } catch (Sql2oException ex){
      System.out.println(ex);
    }
    return tags;
  }



 }
