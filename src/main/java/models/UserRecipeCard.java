package models;


public class UserRecipeCard extends RecipeCard {

  private String ingredients;
  private String directions;
  private String yield;
  private String totalTime;
  private String textImage;

  public UserRecipeCard(String name,String notes,int rating, String ingredients, String directions, String yield, String totalTime, String textImage) {
        super(name,"user","user",notes,rating);
        this.ingredients = ingredients;
        this.directions = directions;
        this.yield = yield;
        this.totalTime = totalTime;
        this.textImage = textImage;
  }

  public String getIngredients() {
    return ingredients;
  }

  public void setIngredients(String ingredients) {
    this.ingredients = ingredients;
  }

  public String getDirections() {
    return directions;
  }

  public void setDirections(String directions) {
    this.directions = directions;
  }

  public String getYield() {
    return yield;
  }

  public void setYield(String yield) {
    this.yield = yield;
  }

  public String getTotalTime() {
    return totalTime;
  }

  public void setTotalTime(String totalTime) {
    this.totalTime = totalTime;
  }

  public String getTextImage() {
    return textImage;
  }

  public void setTextImage(String textImage) {
    this.textImage = textImage;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    UserRecipeCard that = (UserRecipeCard) o;

    if (!ingredients.equals(that.ingredients)) return false;
    if (!directions.equals(that.directions)) return false;
    if (yield != null ? !yield.equals(that.yield) : that.yield != null) return false;
    if (totalTime != null ? !totalTime.equals(that.totalTime) : that.totalTime != null) return false;
    return textImage != null ? textImage.equals(that.textImage) : that.textImage == null;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + ingredients.hashCode();
    result = 31 * result + directions.hashCode();
    result = 31 * result + (yield != null ? yield.hashCode() : 0);
    result = 31 * result + (totalTime != null ? totalTime.hashCode() : 0);
    result = 31 * result + (textImage != null ? textImage.hashCode() : 0);
    return result;
  }
}
