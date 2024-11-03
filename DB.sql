

CREATE TABLE `users` (
  `id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `date_of_birth` date,
  `password` varchar(50) NOT NULL,
  `isactive` bool
);

CREATE TABLE `roles` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(50) NOT NULL
);

CREATE TABLE `role_user` (
  `role_id` int,
  `user_id` int
);

CREATE TABLE `categories` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `image_url` varchar(250) NOT NULL,
  `isactive` bool
);

CREATE TABLE `products` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `category_id` int,
  `name` varchar(250) NOT NULL,
  `current_price` double,
  `description` varchar(250) NOT NULL,
  `status` bool,
  `weight` double,
  `length` double,
  `width` double,
  `height` double,
  `discount_limit` double,
  `shelf_life_days` int,
  `recipe_id` int NOT NULL
);

create table disposed_products
(
    id                int auto_increment
        primary key,
    disposed_quantity int                                                   not null,
    product_batch_id  int                                                   null,
    reason            enum ('EXPIRED', 'DAMAGED', 'QUALITY_ISSUE', 'OTHER') not null,
    note              text                                                  null,
    staff_id          int                                                   null,
    date_disposed     datetime default CURRENT_TIMESTAMP                    null,
    constraint disposed_products_ibfk_1
        foreign key (product_batch_id) references product_batches (id),
    constraint disposed_products_ibfk_2
        foreign key (staff_id) references users (id)
);

CREATE TABLE `images` (
  `id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `product_id` int,
  `url` varchar(250)
);

CREATE TABLE `product_histories` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `price` double NOT NULL,
  `effective_date` timestamp DEFAULT (now())
);

CREATE TABLE `promotions` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` varchar(250) NOT NULL,
  `start_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `isactive` bool,
  `discount` int,
  `created_at` datetime,
  `updated_at` datetime
);

CREATE TABLE `promotion_details` (
  `promotion_id` int,
  `product_batch_id` int
);

CREATE TABLE `units` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(50) NOT NULL
);

CREATE TABLE `ingredients` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `unit_id` int,
  `warning_limits` double NOT NULL
);

CREATE TABLE `recipes` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(100) NOT NULL
);

CREATE TABLE `recipe_details` (
  `recipe_id` int,
  `ingredient_id` int,
  `quantity` double NOT NULL
);

CREATE TABLE `import_ingredients` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `user_id` int,
  `import_date` datetime,
  `total_amount` double,
  `created_at` datetime,
  `updated_at` datetime
);

CREATE TABLE `import_ingredient_details` (
  `import_ingredient_id` int,
  `ingredient_id` int,
  `quantity` double,
  `unit_price` double
);

CREATE TABLE `daily_productions` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `production_date` datetime NOT NULL
);

CREATE TABLE `product_batches` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `product_id` int,
  `daily_production_id` int,
  `expiration_date` datetime NOT NULL,
  `current_discount` int,
  `quantity` int
);

CREATE TABLE `daily_product_inventories` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `product_batch_id` int,
  `inventory_date` datetime NOT NULL,
  `quantity` int
);

CREATE TABLE `payment_methods` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(100),
  `description` varchar(250)
);

CREATE TABLE `bills` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `user_id` int,
  `total_amount` double,
  `payment_method_id` int,
  `table_id` int,
  `created_at` datetime,
  `status` enum(pending,completed,canceled)
);

CREATE TABLE `bill_detail` (
  `bill_id` int,
  `product_batch_id` int,
  `quantity` int,
  `price` double
);

CREATE TABLE `areas` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(50)
);

CREATE TABLE `tables` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `area_id` int,
  `seat` int,
  `name` varchar(50),
  `status` int,
  `isactive` bool
);

CREATE TABLE `export_ingredients` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `sender_id` int,
  `recieve_id` int,
  `daily_production_id` int,
  `export_date` datetime,
  `total_amount` double,
  `created_at` datetime
);

CREATE TABLE `export_ingredient_details` (
  `export_ingredient_id` int,
  `ingredient_id` int,
  `quantity` double
);

ALTER TABLE `role_user` ADD FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`);

ALTER TABLE `role_user` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `products` ADD FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`);

ALTER TABLE `products` ADD FOREIGN KEY (`recipe_id`) REFERENCES `recipes` (`id`);



ALTER TABLE `images` ADD FOREIGN KEY (`product_id`) REFERENCES `products` (`id`);

ALTER TABLE `product_histories` ADD FOREIGN KEY (`product_id`) REFERENCES `products` (`id`);

ALTER TABLE `promotion_details` ADD FOREIGN KEY (`promotion_id`) REFERENCES `promotions` (`id`);

ALTER TABLE `promotion_details` ADD FOREIGN KEY (`product_batch_id`) REFERENCES `product_batches` (`id`);

ALTER TABLE `ingredients` ADD FOREIGN KEY (`unit_id`) REFERENCES `units` (`id`);

ALTER TABLE `recipe_details` ADD FOREIGN KEY (`recipe_id`) REFERENCES `recipes` (`id`);

ALTER TABLE `recipe_details` ADD FOREIGN KEY (`ingredient_id`) REFERENCES `ingredients` (`id`);

ALTER TABLE `import_ingredients` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `import_ingredient_details` ADD FOREIGN KEY (`import_ingredient_id`) REFERENCES `import_ingredients` (`id`);

ALTER TABLE `import_ingredient_details` ADD FOREIGN KEY (`ingredient_id`) REFERENCES `ingredients` (`id`);

ALTER TABLE `product_batches` ADD FOREIGN KEY (`product_id`) REFERENCES `products` (`id`);

ALTER TABLE `product_batches` ADD FOREIGN KEY (`daily_production_id`) REFERENCES `daily_productions` (`id`);

ALTER TABLE `daily_product_inventories` ADD FOREIGN KEY (`product_batch_id`) REFERENCES `product_batches` (`id`);

ALTER TABLE `bills` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `bills` ADD FOREIGN KEY (`payment_method_id`) REFERENCES `payment_methods` (`id`);

ALTER TABLE `bills` ADD FOREIGN KEY (`table_id`) REFERENCES `tables` (`id`);

ALTER TABLE `bill_detail` ADD FOREIGN KEY (`bill_id`) REFERENCES `bills` (`id`);

ALTER TABLE `bill_detail` ADD FOREIGN KEY (`product_batch_id`) REFERENCES `product_batches` (`id`);

ALTER TABLE `tables` ADD FOREIGN KEY (`area_id`) REFERENCES `areas` (`id`);

ALTER TABLE `export_ingredients` ADD FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`);

ALTER TABLE `export_ingredients` ADD FOREIGN KEY (`recieve_id`) REFERENCES `users` (`id`);

ALTER TABLE `export_ingredients` ADD FOREIGN KEY (`daily_production_id`) REFERENCES `daily_productions` (`id`);

ALTER TABLE `export_ingredient_details` ADD FOREIGN KEY (`export_ingredient_id`) REFERENCES `export_ingredients` (`id`);

ALTER TABLE `export_ingredient_details` ADD FOREIGN KEY (`ingredient_id`) REFERENCES `ingredients` (`id`);
