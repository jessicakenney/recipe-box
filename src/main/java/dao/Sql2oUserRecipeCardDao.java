package dao;

import models.RecipeCard;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

public class Sql2oUserRecipeCardDao extends Sql2oRecipeCardDao {
  private final Sql2o sql2o;

  public Sql2oUserRecipeCardDao(Sql2o sql2o){
      super(sql2o);
      this.sql2o=sql2o;
  }

  @Override
  public void add(RecipeCard recipeCard) {
    String sql = "INSERT INTO recipecards (name,url,image,notes,rating,ingredients,directions,yield,totalTime,textImage) VALUES (:name,:url,:image,:notes,:rating,:ingredients,:directions,:yield,:totalTime,:textImage)";
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


  public void update(int id, String name, String url,String image, String notes, int rating, String ingredients, String directions,String yield, String totalTime,String textImage) {
    String sql = "UPDATE recipecards SET (name,url,image,notes,rating,ingredients,directions,yield,totalTime,textImage) = (:name,:url,:image,:notes,:rating,:ingredients,:directions,:yield,:totalTime,:textImage) WHERE id=:id";
    try (Connection con = sql2o.open()) {
      con.createQuery(sql)
              .addParameter("id", id)
              .addParameter("name", name)
              .addParameter("url", url)
              .addParameter("image", image)
              .addParameter("notes", notes)
              .addParameter("rating", rating)
              .addParameter("ingredient", ingredients)
              .addParameter("directions", directions)
              .addParameter("yield", yield)
              .addParameter("totalTime", totalTime)
              .addParameter("textImage", textImage)
              .throwOnMappingFailure(false)
              .executeUpdate();
    } catch (Sql2oException ex) {
      System.out.println(ex);
    }
  }

}
