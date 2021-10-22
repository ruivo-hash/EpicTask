CREATE TABLE TASK(
	id bigint PRIMARY KEY auto_increment,
	title varchar(200),
	description TEXT,
	points int,
	status int DEFAULT 0,
	user_id int
);

CREATE TABLE USER (
	id bigint PRIMARY KEY auto_increment,
	name varchar(200),
	email varchar(200),
	age int,
	password varchar(200),
	githubuser varchar(200)
);

CREATE TABLE role (
	id int primary key auto_increment,
	name varchar(200)
);

CREATE TABLE user_roles(
	user_id int,
	roles_id int
);

INSERT INTO role (name) VALUES 
	('ROLE_ADMIN'), 
	('ROLE_USER');

INSERT INTO user_roles VALUES 
	(1, 1);

INSERT INTO user (name, email, age, password, githubuser) VALUES
	('Mateus Balduino', 'mateus@gmail.com', 20, '$2a$10$wwSLR/hBZDJD53S6LZt/mej..NsXdfVr9SrB37yEERPspbRIWPi.O', 'ruivo-hash'),
	('Ana Komase', 'ana@gmail.com', 20, '$2a$10$wwSLR/hBZDJD53S6LZt/mej..NsXdfVr9SrB37yEERPspbRIWPi.O', 'AnaKomase'),
	('Igo Jeferson', 'igo@fiap.com.br', 28, '$2a$10$wwSLR/hBZDJD53S6LZt/mej..NsXdfVr9SrB37yEERPspbRIWPi.O', 'IgoJeferson');

INSERT INTO task (title, description, points, status, user_id) VALUES 
	('Criar banco de dados','Criar um banco de dados na nuvem com Oracle', 200, 10, 1),
	('API REST','Publicação de API com endpoints da aplicação', 150, 90, 2);
INSERT INTO task (title, description, points, status) VALUES 
	('Protótipo','Criação de protótipo de alta fidelidade', 100, 50);