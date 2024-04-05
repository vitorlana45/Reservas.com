-- Inserir dados do hotel
INSERT INTO tb_hotel (name, description, location) VALUES ('South Paradise', 'O Resort Southern Paradise é o destino perfeito para quem busca conforto e diversão em um paraíso tropical', 'Jericoacoara,CE');

-- Inserir dados dos usuários
INSERT INTO tb_users (name, email, password) VALUES ('Alex', 'alex@gmail.com', '2123');
INSERT INTO tb_users (name, email, password) VALUES ('Angela', 'angela@gmail.com', '2123');
INSERT INTO tb_users (name, email, password) VALUES ('Angela2', 'angela2@gmail.com', '2123');

-- Associar usuários ao hotel
INSERT INTO TB_HOTEL_USER (hotel_id, user_id) VALUES (1, 1);
INSERT INTO TB_HOTEL_USER (hotel_id, user_id) VALUES (1, 2);
INSERT INTO TB_HOTEL_USER (hotel_id, user_id) VALUES (1, 3);

INSERT INTO tb_rooms (rooms_number, hotel_id,  rented,user_id) VALUES (1,1,1,1);
INSERT INTO tb_rooms (rooms_number, hotel_id,  rented, user_id) VALUES (2,1,1,1);
INSERT INTO tb_rooms (rooms_number, hotel_id,  rented) VALUES (3,1,0);
INSERT INTO tb_rooms (rooms_number, hotel_id,  rented) VALUES (4,1,0);

