
INSERT INTO usuarios (username, password, full_name, imagen, token, enabled) VALUES ('lopezcon1@hotmail.com', '{bcrypt}$2a$10$EXZCJlb.Jk.Lo.4LFDROkujvBlUVrbjRz4r4oz/.hSnoramwDVt0y', 'john lopez', 'b64b193d-3cc3-4ee9-ae33-b2033dbdceb9.jpeg', null, true);
INSERT INTO usuarios (username, password, full_name, imagen, token, enabled) VALUES ('lopezcon2@hotmail.com', '{bcrypt}$2a$10$EXZCJlb.Jk.Lo.4LFDROkujvBlUVrbjRz4r4oz/.hSnoramwDVt0y', 'Usuario dos', 'b64b193d-3cc3-4ee9-ae33-b2033dbdceb9.jpeg', null, true);
INSERT INTO usuarios (username, password, full_name, imagen, token, enabled) VALUES ('lopezcon3@hotmail.com', '{bcrypt}$2a$10$EXZCJlb.Jk.Lo.4LFDROkujvBlUVrbjRz4r4oz/.hSnoramwDVt0y', 'Usuario tres', 'b64b193d-3cc3-4ee9-ae33-b2033dbdceb9.jpeg', null, true);
INSERT INTO usuarios (username, password, full_name, imagen, token, enabled) VALUES ('lopezcon4@hotmail.com', '{bcrypt}$2a$10$EXZCJlb.Jk.Lo.4LFDROkujvBlUVrbjRz4r4oz/.hSnoramwDVt0y', 'Usuario cuatro', 'b64b193d-3cc3-4ee9-ae33-b2033dbdceb9.jpeg', null, true);

INSERT INTO restaurantes (id_usuario, nombre, ciudad, provincia, telefono, imagen) VALUES (1, 'Pizzeria Da Ernesto', 'Jaén', 'Jaén', '953 26 67 28', '9be027ea-7150-4b85-a4a0-3cbde5448864.jpeg');
INSERT INTO restaurantes (id_usuario, nombre, ciudad, provincia, telefono, imagen) VALUES (1, 'Restaurante Casa Pepe', 'Jaén', 'Jaén', '953 23 10 29', 'e5cc0dc6-4d0f-461a-8319-15519037d3f2.jpeg');
INSERT INTO restaurantes (id_usuario, nombre, ciudad, provincia, telefono, imagen) VALUES (2, 'Panaceite Centro', 'Jaén', 'Jaén', '953 24 06 30', '5e7ef194-9452-4054-80fa-153b444f89e3.jpeg');


INSERT INTO menus (id_restaurante, nombre, descripcion, precio,imagen) VALUES (1, 'Menú del Día', 'Menú especial del día con platos variados', 12.99,'d4ac2c7d-2df2-4aaf-92fa-c10cb75935ba.jpeg');
INSERT INTO menus (id_restaurante, nombre, descripcion, precio,imagen) VALUES (1, 'Menú Infantil', 'Menú para niños con platos divertidos y nutritivos', 7.50, '472a8fb9-2f1d-46f0-b820-10c290888f0e.jpeg');
INSERT INTO menus (id_restaurante, nombre, descripcion, precio,imagen) VALUES (2, 'Menú Degustación', 'Menú con una selección de platos gourmet', 25.99, '332de2e5-eb84-4792-baae-2adfe22deb7e.jpeg');

INSERT INTO calificaciones (puntuacion, id_restaurante, id_usuario, comentario) VALUES ('5', '1', '1', 'Excelente servicio, volveré pronto.');
INSERT INTO calificaciones (puntuacion, id_restaurante, id_usuario, comentario) VALUES ('4', '2', '2', 'La comida estuvo deliciosa, pero el servicio fue un poco lento.');
INSERT INTO calificaciones (puntuacion, id_restaurante, id_usuario, comentario) VALUES ('3', '1', '3', 'El ambiente era agradable, pero la comida podría mejorar.');
INSERT INTO calificaciones (puntuacion, id_restaurante, id_usuario, comentario) VALUES ('2', '3', '1', 'No quedé satisfecho con la calidad de la comida.');
INSERT INTO calificaciones (puntuacion, id_restaurante, id_usuario, comentario) VALUES ('1', '1', '2', 'Experiencia terrible, no recomendaría este restaurante a nadie.');
INSERT INTO calificaciones (puntuacion, id_restaurante, id_usuario, comentario) VALUES ('5', '2', '3', 'Increíble atención al cliente, el personal fue muy amable.');
INSERT INTO calificaciones (puntuacion, id_restaurante, id_usuario, comentario) VALUES ('4', '1', '1', 'Buen ambiente y comida deliciosa, definitivamente regresaré.');
INSERT INTO calificaciones (puntuacion, id_restaurante, id_usuario, comentario) VALUES ('3', '3', '2', 'Nada especial, esperaba más de este restaurante.');
INSERT INTO calificaciones (puntuacion, id_restaurante, id_usuario, comentario) VALUES ('2', '1', '3', 'No recomendaría este lugar, la calidad no coincide con el precio.');
INSERT INTO calificaciones (puntuacion, id_restaurante, id_usuario, comentario) VALUES ('1', '3', '1', 'Desastroso, tuve una mala experiencia desde que llegué.');



INSERT INTO reservas (numero_personas, fechayhora, id_restaurante, id_usuario) VALUES ('5', '2024-05-15 18:30:20', 1, 1);
INSERT INTO reservas (numero_personas, fechayhora, id_restaurante, id_usuario) VALUES ('5', '2024-05-15 19:15:00', 2, 2);
INSERT INTO reservas (numero_personas, fechayhora, id_restaurante, id_usuario) VALUES ('5', '2024-05-15 22:50:00', 3, 3);
INSERT INTO reservas (numero_personas, fechayhora, id_restaurante, id_usuario) VALUES ('5', '2024-05-15 15:20:00', 1, 1);


INSERT INTO usuario_roles (roles, usuario_id) VALUES ('0', '1');
INSERT INTO usuario_roles (roles, usuario_id) VALUES ('1', '2');
INSERT INTO usuario_roles (roles, usuario_id) VALUES ('1', '3');
INSERT INTO usuario_roles (roles, usuario_id) VALUES ('1', '4');