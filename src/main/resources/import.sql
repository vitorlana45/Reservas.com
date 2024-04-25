-- Inserir dados do hotel
INSERT INTO tb_hotel (name, description, location, status) VALUES ('South Paradise', 'O Resort Southern Paradise é o destino perfeito para quem busca conforto e diversão em um paraíso tropical', 'Jericoacoara,CE', 'Disponivel');

-- Inserir dados dos usuários
INSERT INTO tb_users (name, email, password) VALUES ('Alex', 'alex@gmail.com', '2123');
INSERT INTO tb_users (name, email, password) VALUES ('Angela', 'angela@gmail.com', '2123');

-- Associar usuários ao hotel
INSERT INTO TB_HOTEL_USER (hotel_id, user_id) VALUES (1, 1);
INSERT INTO TB_HOTEL_USER (hotel_id, user_id) VALUES (1, 2);

INSERT INTO tb_rooms (rooms_number, hotel_id, check_in, check_out, rented, user_id) VALUES (1, 1, '2024-04-15 09:00:00', '2024-04-20 09:00:00', 1, 1);
INSERT INTO tb_rooms (rooms_number, hotel_id, check_in, check_out, rented, user_id) VALUES (2, 1, '2024-04-17 09:00:00', '2024-04-22 09:00:00', 1, 1);
INSERT INTO tb_rooms (rooms_number, hotel_id,  rented) VALUES (3,1,0);
INSERT INTO tb_rooms (rooms_number, hotel_id,  rented) VALUES (4,1,0);
INSERT INTO tb_rooms (rooms_number, hotel_id,  rented) VALUES (5,1,0);
INSERT INTO tb_rooms (rooms_number, hotel_id,  rented) VALUES (6,1,0);
INSERT INTO tb_rooms (rooms_number, hotel_id,  rented) VALUES (7,1,0);
INSERT INTO tb_rooms (rooms_number, hotel_id,  rented) VALUES (8,1,0);
INSERT INTO tb_rooms (rooms_number, hotel_id,  rented) VALUES (9,1,0);
INSERT INTO tb_rooms (rooms_number, hotel_id,  rented) VALUES (10,1,0);
