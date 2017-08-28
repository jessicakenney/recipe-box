package models;


public class RecipeCard {
  private String name;
  private String url;
  private String image;
  private String notes;
  private int rating;
  private int id;

  public RecipeCard (String name,String url, String image,String notes,int rating){
    this.name = name;
    this.url = url;
    this.image = image;
    this.notes = notes;
    this.rating = rating;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
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

    RecipeCard that = (RecipeCard) o;

    if (rating != that.rating) return false;
    if (id != that.id) return false;
    if (!name.equals(that.name)) return false;
    if (!url.equals(that.url)) return false;
    if (image != null ? !image.equals(that.image) : that.image != null) return false;
    return notes != null ? notes.equals(that.notes) : that.notes == null;
  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + url.hashCode();
    result = 31 * result + (image != null ? image.hashCode() : 0);
    result = 31 * result + (notes != null ? notes.hashCode() : 0);
    result = 31 * result + rating;
    result = 31 * result + id;
    return result;
  }
}
