INSERT INTO role (name, description) VALUES ('ROLE_ADMIN', 'Administrador del sistema');
INSERT INTO role (name, description) VALUES ('ROLE_USER', 'Usuario estándar');

INSERT INTO users (id, name, email, password, created, modified, last_login, is_active, token, version)
VALUES
  ('b2c2fcd4-d84d-49cd-9185-e93535db30d5', 'Admin', 'admin@admin.com',
   '$2a$10$XURPShlremkq2U/ozwL7uO9wrOJ1sP./VkbY4qNAX2fprZ0p3nxg2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE, NULL, 0),
  ('d6e6f51e-bdf5-41f7-9b5d-712b3b905def', 'User', 'user@user.com',
   '$2a$10$XURPShlremkq2U/ozwL7uO9wrOJ1sP./VkbY4qNAX2fprZ0p3nxg2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE, NULL, 0);

INSERT INTO phones (number, city_code, country_code, user_id)
VALUES
  ('1234567890', '123', '1', 'b2c2fcd4-d84d-49cd-9185-e93535db30d5'),
  ('9876543210', '456', '44', 'd6e6f51e-bdf5-41f7-9b5d-712b3b905def');

INSERT INTO users_roles (users_id, role_id)
VALUES
  ('b2c2fcd4-d84d-49cd-9185-e93535db30d5', (SELECT id FROM role WHERE name = 'ROLE_ADMIN')),
  ('d6e6f51e-bdf5-41f7-9b5d-712b3b905def', (SELECT id FROM role WHERE name = 'ROLE_USER'));