
INSERT INTO usuarios (username, password, full_name, imagen, telefono, token, enabled) VALUES ('lopezcon1@hotmail.com', '{bcrypt}$2a$10$EXZCJlb.Jk.Lo.4LFDROkujvBlUVrbjRz4r4oz/.hSnoramwDVt0y', 'john lopez', 'b64b193d-3cc3-4ee9-ae33-b2033dbdceb9.jpeg', '654456765', null, true);
INSERT INTO usuarios (username, password, full_name, imagen, telefono, token, enabled) VALUES ('lopezcon2@hotmail.com', '{bcrypt}$2a$10$EXZCJlb.Jk.Lo.4LFDROkujvBlUVrbjRz4r4oz/.hSnoramwDVt0y', 'Usuario dos', 'b64b193d-3cc3-4ee9-ae33-b2033dbdceb9.jpeg', '765654543', null, true);
INSERT INTO usuarios (username, password, full_name, imagen, telefono, token, enabled) VALUES ('lopezcon3@hotmail.com', '{bcrypt}$2a$10$EXZCJlb.Jk.Lo.4LFDROkujvBlUVrbjRz4r4oz/.hSnoramwDVt0y', 'Usuario tres', 'b64b193d-3cc3-4ee9-ae33-b2033dbdceb9.jpeg', '987876765', null, true);
INSERT INTO usuarios (username, password, full_name, imagen, telefono, token, enabled) VALUES ('lopezcon4@hotmail.com', '{bcrypt}$2a$10$EXZCJlb.Jk.Lo.4LFDROkujvBlUVrbjRz4r4oz/.hSnoramwDVt0y', 'Usuario cuatro', 'b64b193d-3cc3-4ee9-ae33-b2033dbdceb9.jpeg', '675432345', null, true);

INSERT INTO restaurantes (id_usuario, nombre, ciudad, provincia, telefono, imagen, direccion) VALUES (1, 'Pizzeria Da Ernesto', 'Jaén', 'Jaén', '953266728', '1c428a5f-ec22-4ac6-b9fb-038240f31ce6.jpeg', 'P.º de la Estación, 23');
INSERT INTO restaurantes (id_usuario, nombre, ciudad, provincia, telefono, imagen, direccion) VALUES (1, 'Restaurante Casa Pepe', 'Jaén', 'Jaén', '953231029', '55a1ba4d-fdfe-4da8-8c5d-795d383b24f2.jpeg', 'Ctra. Jabalcuz, 45');
INSERT INTO restaurantes (id_usuario, nombre, ciudad, provincia, telefono, imagen, direccion) VALUES (2, 'Panaceite Centro', 'Jaén', 'Jaén', '953240630', '5e7ef194-9452-4054-80fa-153b444f89e3.jpeg', 'C. Bernabé Soriano, 1');


INSERT INTO menus (id_restaurante, nombre, descripcion, precio,imagen) VALUES (1, 'Menú del Día', 'Menú especial del día con platos variados', 12.99,'17f3dd3c-9952-408e-8246-17435a27fbd7.jpeg');
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



INSERT INTO reservas (numero_personas, fechayhora, id_restaurante, id_usuario, nombre_cliente, email_cliente, telefono_cliente, notas) VALUES ('4', '2024-06-15 18:30:20', 1, 1, 'john lopez', 'lopezcon1@hotmail.com', '675675654', 'carnivoro');
INSERT INTO reservas (numero_personas, fechayhora, id_restaurante, id_usuario, nombre_cliente, email_cliente, telefono_cliente, notas) VALUES ('6', '2024-06-15 19:15:00', 2, 2, 'john lopez', 'lopezcon2@hotmail.com', '675675654', 'vegano');
INSERT INTO reservas (numero_personas, fechayhora, id_restaurante, id_usuario, nombre_cliente, email_cliente, telefono_cliente, notas) VALUES ('2', '2024-06-15 22:50:00', 3, 3, 'john lopez', 'lopezcon3@hotmail.com', '675675654', 'alergico');
INSERT INTO reservas (numero_personas, fechayhora, id_restaurante, id_usuario, nombre_cliente, email_cliente, telefono_cliente, notas) VALUES ('8', '2024-06-15 15:20:00', 1, 1, 'john lopez', 'lopezcon4@hotmail.com', '675675654', 'come de todo');


INSERT INTO usuario_roles (roles, usuario_id) VALUES ('0', '1');
INSERT INTO usuario_roles (roles, usuario_id) VALUES ('1', '2');
INSERT INTO usuario_roles (roles, usuario_id) VALUES ('1', '3');
INSERT INTO usuario_roles (roles, usuario_id) VALUES ('2', '4');