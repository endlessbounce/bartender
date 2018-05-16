CREATE DATABASE `bartender` /*!40100 DEFAULT CHARACTER SET utf8 */;

CREATE TABLE `base_drink` (
  `base_id` tinyint(3) unsigned NOT NULL AUTO_INCREMENT COMMENT 'У каждого базового напитка есть уникальный ID. Т.к. значений планируется быть не более 1-2 десятков, выбираем TINYINT.',
  `base_name` varchar(50) NOT NULL COMMENT 'Базовый напиток имеет название. Название напитка имеет длину не более 5 слов. Выбираем тип VARCHAR(50).',
  `base_name_rus` varchar(50) NOT NULL COMMENT 'Название базового напитка на русском языке',
  PRIMARY KEY (`base_id`),
  UNIQUE KEY `base_name_UNIQUE` (`base_name`),
  UNIQUE KEY `base_name_rus_UNIQUE` (`base_name_rus`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COMMENT='Таблица содержит типы базового напитка в коктейле. Тип напитка необходим для отображения коктейлей по категориям.';

CREATE TABLE `cocktail` (
  `co_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'У каждого коктейля есть уникальный ключ.',
  `co_name` varchar(60) DEFAULT NULL COMMENT 'У каждого коктейля есть название. Максимально длинное название, найденное в каталоге на 1000 коктейлей - 48 символов. Выбираем с небольшим запасом VARCHAR(60).',
  `co_name_lang` varchar(60) DEFAULT NULL COMMENT 'В данной ячейке классический коктейль будет хранить название на русском и других пользовательских языках',
  `co_recipe` blob COMMENT 'У каждого коктейля есть рецепт приготовления. Рецепт коктейля не может занимать по объёму более 1 условной страницы (1800 символов). Пусть на символ условно уйдёт 3 байта, всего 5400 байтов. Так же необходима сортировка и сравнение данных с учетом регистра, поэтому выбран тип BLOB.',
  `co_recipe_lang` blob COMMENT 'В данной ячейке классический коктейль будет хранить рецепт на русском и других пользовательских языках',
  `co_slogan` varchar(255) DEFAULT NULL COMMENT 'Короткий слоган для придания атмосферы сайту',
  `co_slogan_lang` varchar(255) DEFAULT NULL COMMENT 'Короткий слоган для придания атмосферы сайту на других языках',
  `group_id` tinyint(3) unsigned NOT NULL COMMENT 'Каждый коктейль относится к какой-либо группе. Например: Breakfast and Brunch Cocktails, After Dinner Cocktails.',
  `base_id` tinyint(3) unsigned NOT NULL COMMENT 'Каждый коктейль имеет базовый алкогольный напиток. Например: liquor, liqueur, beer or wine.',
  `co_uri` varchar(255) DEFAULT NULL COMMENT 'URI картинки, может быть null. Так же может быть очень длинным, поэтому 255 символов.',
  `co_creation_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Если коктейль создан пользователем, то у него есть дата создания. Если классический - поле с датой равно NULL. Выбран тип DATETIME для удобства работы с ним, т.к. он не зависит от временной зоны.',
  `us_id` int(10) unsigned DEFAULT NULL COMMENT 'Данное приложение предусматривает возможность создания пользователем коктейлей по собственному рецепту. Данный столбец содержит ссылку на ID пользователя (если коктейль был создан пользователем) либо null (если коктейль классический).',
  PRIMARY KEY (`co_id`),
  KEY `fk_cocktails_drink_group1_idx` (`group_id`),
  KEY `fk_cocktails_base_drink1_idx` (`base_id`),
  KEY `fk_cocktail_user1_idx` (`us_id`),
  CONSTRAINT `fk_cocktail_user1` FOREIGN KEY (`us_id`) REFERENCES `user` (`us_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_cocktails_base_drink1` FOREIGN KEY (`base_id`) REFERENCES `base_drink` (`base_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_cocktails_drink_group1` FOREIGN KEY (`group_id`) REFERENCES `drink_group` (`group_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=442 DEFAULT CHARSET=utf8 COMMENT='Таблица хранит перечень классических коктейлей, так же таблица будет хранить созданные пользователями коктейли, которые будут видны только в их профилях.';

CREATE TABLE `combination` (
  `in_id` tinyint(3) unsigned NOT NULL COMMENT 'Внешний ключ на ID ингредиента.',
  `co_id` int(10) unsigned NOT NULL COMMENT 'Внешний ключ на ID коктейля.',
  `com_portion` varchar(50) DEFAULT NULL COMMENT 'Отображает количество / порцию необходимого к добавлению ингредиента',
  `com_portion_lang` varchar(50) DEFAULT NULL COMMENT 'Отображает количество / порцию необходимого к добавлению ингредиента (так же на русском и других пользовательских языках)',
  PRIMARY KEY (`in_id`,`co_id`),
  KEY `fk_ingredient_has_cocktails_cocktails1_idx` (`co_id`),
  KEY `fk_ingredient_has_cocktails_ingredient_idx` (`in_id`),
  CONSTRAINT `fk_ingredient_has_cocktails_cocktails1` FOREIGN KEY (`co_id`) REFERENCES `cocktail` (`co_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_ingredient_has_cocktails_ingredient` FOREIGN KEY (`in_id`) REFERENCES `ingredient` (`in_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Много ингредиентов могут входить в соствав одного коктейля. Много коктейлей могут иметь в составе один и тот же ингредиент.';

CREATE TABLE `drink_group` (
  `group_id` tinyint(3) unsigned NOT NULL AUTO_INCREMENT COMMENT 'У каждой группы напитков есть уникальный  ID. Т.к. значений планируется быть не более 1 десятка, выбираем TINYINT.',
  `group_name` varchar(50) NOT NULL COMMENT 'Каждая группа напитков имеет название. Название обычно состоит из нескольких слов, это до 50 символов. Поэтому выбран тип VARCHAR(50).',
  `group_name_rus` varchar(50) NOT NULL COMMENT 'Название группы напитков на русском языке',
  PRIMARY KEY (`group_id`),
  UNIQUE KEY `group_name_UNIQUE` (`group_name`),
  UNIQUE KEY `group_name_rus_UNIQUE` (`group_name_rus`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='Данная таблица определяет группу коктейля. Будет использована в приложении для отображения коктейлей по группам.';

CREATE TABLE `favourite_cocktail` (
  `co_id` int(10) unsigned NOT NULL COMMENT 'Внешний ключ на ID коктейля',
  `us_id` int(10) unsigned NOT NULL COMMENT 'Внешний ключ на ID пользователя',
  PRIMARY KEY (`co_id`,`us_id`),
  KEY `fk_cocktail_has_user_user1_idx` (`us_id`),
  KEY `fk_cocktail_has_user_cocktail1_idx` (`co_id`),
  CONSTRAINT `fk_cocktail_has_user_cocktail1` FOREIGN KEY (`co_id`) REFERENCES `cocktail` (`co_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_cocktail_has_user_user1` FOREIGN KEY (`us_id`) REFERENCES `user` (`us_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Один коктейль может быть любимым у многих пользователей. У одного пользователя может быть много любимых коктейлей.';

CREATE TABLE `ingredient` (
  `in_id` tinyint(3) unsigned NOT NULL AUTO_INCREMENT COMMENT 'У каждого ингредиента должен быть уникальный ID. Ингредиентов планируется быть от 1 до 2 сотен, выбираем TINYINT.',
  `in_name` varchar(50) NOT NULL COMMENT 'Каждый ингредиент имеет название. Название не привысит 1 строку (условно 50 символов). Поэтому тип выбран VARCHAR(50).',
  `in_name_rus` varchar(50) NOT NULL,
  PRIMARY KEY (`in_id`),
  UNIQUE KEY `in_name_UNIQUE` (`in_name`),
  UNIQUE KEY `in_name_rus_UNIQUE` (`in_name_rus`)
) ENGINE=InnoDB AUTO_INCREMENT=97 DEFAULT CHARSET=utf8 COMMENT='Ингредиенты в данном приложении могут иметь название и описание.';

CREATE TABLE `prospect` (
  `pr_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'У каждого пользователя есть уникальный ID.',
  `pr_name` varchar(50) NOT NULL COMMENT 'У каждого пользователя есть имя. Выберем с запасом VARCHAR(50).',
  `pr_email` varchar(50) NOT NULL COMMENT 'У каждого пользователя есть уникальный электронный адрес,\nбудет использоваться как логин. VARCHAR(50) выглядит как оптимальный вариант в данном случае.',
  `pr_password` blob NOT NULL COMMENT 'Хешированный ключ, представляющий пароль пользователя.  BLOB т.к. ключ это массив типа byte',
  `pr_salt` blob NOT NULL COMMENT 'Salt применённый для созданию ключа данного пользователя. BLOB т.к. salt это массив типа byte',
  `pr_expiration` int(10) unsigned NOT NULL COMMENT 'Хранит время в секундах с 1970.01.01 и до момента, по прошествию которого ссылка с подтверждением регистрации становится недействительной',
  `pr_code` varchar(45) NOT NULL COMMENT 'Код подтверждения, содержится в ссылке как параметр',
  PRIMARY KEY (`pr_id`),
  UNIQUE KEY `pr_email_UNIQUE` (`pr_email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Таблица хранит временных пользователей, совершивших попытку регистрации, но не подтвердивших регистрацию через ссылку, высланную на их email';

CREATE TABLE `user` (
  `us_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'У каждого пользователя есть уникальный ID.',
  `us_name` varchar(50) NOT NULL COMMENT 'У каждого пользователя есть имя. Выберем с запасом VARCHAR(50).',
  `us_email` varchar(50) NOT NULL COMMENT 'У каждого пользователя есть уникальный электронный адрес,\nбудет использоваться как логин. VARCHAR(50) выглядит как оптимальный вариант в данном случае.',
  `us_password` blob NOT NULL COMMENT 'Хешированный ключ, представляющий пароль пользователя.  BLOB т.к. ключ это массив типа byte',
  `us_salt` blob NOT NULL COMMENT 'Salt применённый для созданию ключа данного пользователя. BLOB т.к. salt это массив типа byte',
  `us_registration_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Пользователь имеет дату регистрации. Тип DATETIME позволит не зависеть от временных зон. По умолчанию данное поле заполняется автоматически значением времени в момент внесения записи в таблицу.',
  `us_cookie` varchar(45) DEFAULT NULL COMMENT 'Хранит Cookie если пользователь при входе в систему поставил галочку "оставаться в системе". Генерируется классом UUID, размер строки не превышает 40 символов.',
  PRIMARY KEY (`us_id`),
  UNIQUE KEY `us_email_UNIQUE` (`us_email`),
  UNIQUE KEY `us_id_UNIQUE` (`us_id`),
  UNIQUE KEY `us_cookie_UNIQUE` (`us_cookie`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8 COMMENT='Приложение предусматривает возможность создания пользователем своих коктейлей. Поэтому должна быть таблица, хранящая пользовательские данные для входа в свой профиль, где можно будет получить доступ к понравившимся и созданным пользователем коктейлям. Администратора в приложении нету, т.к. это приложение - выборка лучших классических коктейлей и менятся не будет. Т.к. пользователи не могут обмениваться данными, другой информации о пользователе в таблице нет.';

