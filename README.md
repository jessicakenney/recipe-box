# Recipe Box API 

##### Epicodus Section: Java Week4 : Advanced Java Topics

### By Jessica Sheridan

## Table of Contents

- [Description](#description)
- [Test Plan](#test-plan)
    - [httpie](#httpie)
- [API Documentation](#api documentation)
    - [Root](#Root)
    - [Recipe Cards](#Recipe Cards)
    - [Tag: Vegetable](#Tag: Vegetable)
    - [Tag: Meal](#Tag: Meal)
- [Setup](#setup)
- [Support](#support)
- [License](#license)
- [Links](#links)


## Description

The Recipe Box API backend server with Spark is ready for a client app to keep track of all your personal favorite 
recipes from the internet or family favorites tucked into your personal recipe binder. The most powerful Recipe 
Box is one that allows for custom tagging for searching to find just the right recipe in a pinch. Current tag 
categories include Vegetables (search your database for recipes with this week's CSA delivery!) and Meals (looking for a brunch ideas?). 

	* Database is populated for example in main.
	* Future Tags will be included in next release.

## Test Plan 

Complete Dao Coverage
Verification: Run: Sql2oRecipeCardDaoTest, and Sql2oTagDaoTest

| Behavior      | Input | Output |
| ------------- | ------------- | ------------- |
| addingRecipeSetsId | recipeCard | 1 |
| existingRecipeCardsCanBeFoundById | 1 | recipeCard|
| getAll_allRecipeCardsAreFound | recipeCard1,recipeCard2 | 2|
| update_correctlyUpdates | recipeCard.getName | newName|
| deleteById | recipeCard | 0|
| ------------- | ------------- | ------------- |
| addTagtoRecipeCard | |  |
| getAllRecipeCardsForATag | |  |
| getAllTagsForARecipeCard | |  |
| ------------- | ------------- | ------------- |

## HTTPIE output 
![Alt text](src/main/resources/public/images/httpie.png)



## API Documentation
This documentation will introduce the API endpoints that you can use HTTP requests to retrieve data (and
post data).

# Root
The Root url provides information on all available resources within the API currently:

![Alt text](src/main/resources/public/images/httpie-root.png)

Url To add New Tag to Vegetables or Meals
	localhost:4567/vegetables/new
	localhost:4567/meals/new
	
# Recipe Cards
  Endpoints:
	[create]
	localhost:4567/recipecards/new 						--Add new recipeCard
	localhost:4567/recipecards/:recipeCardId/vegetables/:vegetableId/new	--Add specific vegetable tag to recipeCard
	localhost:4567/recipecards/:recipeCardId/meals/:mealId/new		--Add specific meal tag to recipeCard

	[retrieve]
	localhost:4567/recipecards				-- get all recipeCard resources
	localhost:4567/recipecards/:id				--specific recipeCard resource
	localhost:4567/recipecards/:recipeCardId/vegetables	-- get All vegetable tags for a recipeCard
	localhost:4567/recipecards/:recipeCardId/meal		-- get All meal tags for a recipeCard

  Example:

![Alt text](src/main/resources/public/images/recipecards.png)
![Alt text](src/main/resources/public/images/recipecard-2.png)
![Alt text](src/main/resources/public/images/getAllRecipesForAVegetable.png)

  Attributes:
	| name      | String | Name of recipe |
	| url      | String | location of recipe on the internet|
	| image      | String | location of an image of the recipe|
	| notes      | String | personal spot to add a note about the recipe| 
	| rating      | int | how much did you like it | 
	| id      | int | recipe number | 


# Tag: Vegetable 
  Endpoints:
	localhost:4567/vegetables
  Example:
![Alt text](src/main/resources/public/images/getAllVegetablesforRecipeCard-2.png)
![Alt text](src/main/resources/public/images/vegetables.png)

  Attributes:
	| name      | String | Name of tag |
	| id      | int | tag id|


# Tag: Meal 
  Endpoints:
	localhost:4567/meals
  Example:
  Attributes:
	| name      | String | Name of tag |
	| id      | int | tag id|


## Setup
git clone https://github.com/jessicakenney/recipe-box.git
bring up url: localhost:4567


## Support 
email with any questions: jessicakenney@yahoo.com,

## Known Issues/Bugs

## Technologies Used
Java,Handlebars,Postman


