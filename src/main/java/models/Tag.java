package models;

public class Tag {

  private String name;
  private int id;

  public Tag(String name) {
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

    Tag tag = (Tag) o;

    if (id != tag.id) return false;
    return name.equals(tag.name);
  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + id;
    return result;
  }
}
