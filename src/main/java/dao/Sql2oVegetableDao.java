package dao;

import models.Vegetable;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

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

  }

