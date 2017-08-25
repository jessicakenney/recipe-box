SET MODE PostgreSQL;

CREATE TABLE IF NOT EXISTS recipecards (
 id int PRIMARY KEY auto_increment,
 name VARCHAR,
 url VARCHAR,
 image VARCHAR,
 notes VARCHAR,
 rating INT
);

