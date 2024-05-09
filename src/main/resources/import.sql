
INSERT INTO `usuarios` (`username`, `password`, `full_name`, `imagen`, `token`, `enabled`) VALUES ('lopezcon1@hotmail.com', '{bcrypt}$2a$10$EXZCJlb.Jk.Lo.4LFDROkujvBlUVrbjRz4r4oz/.hSnoramwDVt0y', 'john lopez', 'avatar1.jpg', '', 1);
INSERT INTO usuarios (username, password, full_name, imagen, token, enabled) VALUES ('usuario2@example.com', '{bcrypt}$2a$10$EXZCJlb.Jk.Lo.4LFDROkujvBlUVrbjRz4r4oz/.hSnoramwDVt0y', 'Usuario dos', 'avatar2.jpg', '', 1);
INSERT INTO usuarios (username, password, full_name, imagen, token, enabled) VALUES ('usuario3@example.com', '{bcrypt}$2a$10$EXZCJlb.Jk.Lo.4LFDROkujvBlUVrbjRz4r4oz/.hSnoramwDVt0y', 'Usuario tres', 'avatar3.jpg', '', 1);
INSERT INTO usuarios (username, password, full_name, imagen, token, enabled) VALUES ('usuario4@example.com', '{bcrypt}$2a$10$EXZCJlb.Jk.Lo.4LFDROkujvBlUVrbjRz4r4oz/.hSnoramwDVt0y', 'Usuario cuatro', 'avatar4.jpg', '', 1);

INSERT INTO restaurantes (id_usuario, nombre, ciudad, provincia, telefono, imagen) VALUES (1, 'La Parrilla de Juan', 'Madrid', 'Madrid', '123456789', 'restaurante1.jpg');
INSERT INTO restaurantes (id_usuario, nombre, ciudad, provincia, telefono, imagen) VALUES (1, 'Sabores de María', 'Barcelona', 'Barcelona', '987654321', 'restaurante2.jpg');
INSERT INTO restaurantes (id_usuario, nombre, ciudad, provincia, telefono, imagen) VALUES (2, 'Pizzería Pepito', 'Valencia', 'Valencia', '555666777', 'restaurante3.jpg');


INSERT INTO menus (id_restaurante, nombre, descripcion, precio,imagen) VALUES (1, 'Menú del Día', 'Menú especial del día con platos variados', 12.99,"");
INSERT INTO menus (id_restaurante, nombre, descripcion, precio,imagen) VALUES (1, 'Menú Infantil', 'Menú para niños con platos divertidos y nutritivos', 7.50,"");
INSERT INTO menus (id_restaurante, nombre, descripcion, precio,imagen) VALUES (2, 'Menú Degustación', 'Menú con una selección de platos gourmet', 25.99,"");

INSERT INTO `calificaciones` (`puntuacion`, `id_restaurante`, `id_usuario`, `comentario`) VALUES ('5', '1', '1', 'Excelente servicio, volveré pronto.');
INSERT INTO `calificaciones` (`puntuacion`, `id_restaurante`, `id_usuario`, `comentario`) VALUES ('4', '2', '2', 'La comida estuvo deliciosa, pero el servicio fue un poco lento.');
INSERT INTO `calificaciones` (`puntuacion`, `id_restaurante`, `id_usuario`, `comentario`) VALUES ('3', '1', '3', 'El ambiente era agradable, pero la comida podría mejorar.');
INSERT INTO `calificaciones` (`puntuacion`, `id_restaurante`, `id_usuario`, `comentario`) VALUES ('2', '3', '1', 'No quedé satisfecho con la calidad de la comida.');
INSERT INTO `calificaciones` (`puntuacion`, `id_restaurante`, `id_usuario`, `comentario`) VALUES ('1', '1', '2', 'Experiencia terrible, no recomendaría este restaurante a nadie.');
INSERT INTO `calificaciones` (`puntuacion`, `id_restaurante`, `id_usuario`, `comentario`) VALUES ('5', '2', '3', 'Increíble atención al cliente, el personal fue muy amable.');
INSERT INTO `calificaciones` (`puntuacion`, `id_restaurante`, `id_usuario`, `comentario`) VALUES ('4', '1', '1', 'Buen ambiente y comida deliciosa, definitivamente regresaré.');
INSERT INTO `calificaciones` (`puntuacion`, `id_restaurante`, `id_usuario`, `comentario`) VALUES ('3', '3', '2', 'Nada especial, esperaba más de este restaurante.');
INSERT INTO `calificaciones` (`puntuacion`, `id_restaurante`, `id_usuario`, `comentario`) VALUES ('2', '1', '3', 'No recomendaría este lugar, la calidad no coincide con el precio.');
INSERT INTO `calificaciones` (`puntuacion`, `id_restaurante`, `id_usuario`, `comentario`) VALUES ('1', '3', '1', 'Desastroso, tuve una mala experiencia desde que llegué.');



INSERT INTO `reservas` (`numero_personas`, `fechayhora`, `id_restaurante`, `id_usuario`) VALUES ('5', '2024-05-15 18:30:20', 1, 1);
INSERT INTO `reservas` (`numero_personas`, `fechayhora`, `id_restaurante`, `id_usuario`) VALUES ('5', '2024-05-15 19:15:00', 2, 2);
INSERT INTO `reservas` (`numero_personas`, `fechayhora`, `id_restaurante`, `id_usuario`) VALUES ('5', '2024-05-15 22:50:00', 3, 3);
INSERT INTO `reservas` (`numero_personas`, `fechayhora`, `id_restaurante`, `id_usuario`) VALUES ('5', '2024-05-15 15:20:00', 1, 1);


INSERT INTO `usuario_roles` (`roles`, `usuario_id`) VALUES ('0', '1');
INSERT INTO `usuario_roles` (`roles`, `usuario_id`) VALUES ('1', '2');
INSERT INTO `usuario_roles` (`roles`, `usuario_id`) VALUES ('1', '3');
INSERT INTO `usuario_roles` (`roles`, `usuario_id`) VALUES ('1', '4');