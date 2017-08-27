SET MODE PostgreSQL;

CREATE TABLE IF NOT EXISTS recipecards (
 id int PRIMARY KEY auto_increment,
 name VARCHAR,
 url VARCHAR,
 image VARCHAR,
 notes VARCHAR,
 rating INT
);

CREATE TABLE IF NOT EXISTS vegetables (
 id int PRIMARY KEY auto_increment,
 name VARCHAR
);
CREATE TABLE IF NOT EXISTS meals (
 id int PRIMARY KEY auto_increment,
 name VARCHAR
);

CREATE TABLE IF NOT EXISTS recipecards_vegetables (
 id int PRIMARY KEY auto_increment,
 recipeCardId VARCHAR,
 tagId VARCHAR
);

CREATE TABLE IF NOT EXISTS recipecards_meals (
 id int PRIMARY KEY auto_increment,
 recipeCardId VARCHAR,
 tagId VARCHAR
);

