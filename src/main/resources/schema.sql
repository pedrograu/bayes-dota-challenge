CREATE SCHEMA `bayes`;

CREATE TABLE `bayes`.match(
id BIGINT PRIMARY KEY AUTO_INCREMENT,
time_creation BIGINT NOT NULL
);

CREATE TABLE `bayes`.match_kills(
match_id BIGINT NOT NULL,
hero VARCHAR(100) NOT NULL,
target VARCHAR(100) NOT NULL,
FOREIGN KEY (match_id) REFERENCES `bayes`.match(id)
);

CREATE TABLE `bayes`.match_items(
match_id BIGINT NOT NULL,
hero VARCHAR(100) NOT NULL,
item VARCHAR(100) NOT NULL,
time_purchase BIGINT NOT NULL,
FOREIGN KEY (match_id) REFERENCES `bayes`.match(id)
);

CREATE TABLE `bayes`.match_spells(
match_id BIGINT NOT NULL,
hero VARCHAR(100) NOT NULL,
spell VARCHAR(100) NOT NULL,
FOREIGN KEY (match_id) REFERENCES `bayes`.match(id)
);

CREATE TABLE `bayes`.match_damage(
match_id BIGINT NOT NULL,
hero VARCHAR(100) NOT NULL,
target VARCHAR(100) NOT NULL,
damage BIGINT NOT NULL,
FOREIGN KEY (match_id) REFERENCES `bayes`.match(id)
);