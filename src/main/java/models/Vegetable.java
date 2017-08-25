package models;

public class Vegetable {

  private String name;
  private int id;

  public Vegetable(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Vegetable vegetable = (Vegetable) o;

    if (id != vegetable.id) return false;
    return name.equals(vegetable.name);
  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + id;
    return result;
  }
}
