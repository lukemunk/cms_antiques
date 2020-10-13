CREATE TABLE User(
id BINARY(16) NOT NULL,
first_name VARCHAR(100) NOT NULL,
last_name VARCHAR(100) NOT NULL,
username VARCHAR(100) NOT NULL,
password VARCHAR(100) NOT NULL,
email VARCHAR(255) NOT NULL,
phone_number VARCHAR(25) NULL,
address_id BINARY(16) NULL,
created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
modified_on TIMESTAMP DEFAULT 0 ON UPDATE CURRENT_TIMESTAMP,
expired_on TIMESTAMP,
credentialsExpired_on TIMESTAMP,
locked TINYINT(1) DEFAULT 0,
enabled TINYINT(1) DEFAULT 1,
CONSTRAINT Pk_User PRIMARY KEY(id)
)ENGINE=INNODB;

CREATE TABLE Role(
id BINARY(16) NOT NULL,
name VARCHAR(50) NOT NULL,
created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
modified_on TIMESTAMP DEFAULT 0 ON UPDATE CURRENT_TIMESTAMP,
CONSTRAINT Pk_Role PRIMARY KEY(id)
)ENGINE=INNODB;

CREATE TABLE Permission(
id BINARY(16) NOT NULL,
name VARCHAR(50) NOT NULL,
created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
modified_on TIMESTAMP DEFAULT 0 ON UPDATE CURRENT_TIMESTAMP,
CONSTRAINT Pk_Permission PRIMARY KEY(id)
)ENGINE=INNODB;

CREATE TABLE Category(
id BINARY(16) NOT NULL,
name VARCHAR(255) NOT NULL,
created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
modified_on TIMESTAMP DEFAULT 0 ON UPDATE CURRENT_TIMESTAMP,
CONSTRAINT Pk_Category PRIMARY KEY(id)
)ENGINE=INNODB;

CREATE TABLE Item(
id BINARY(16) NOT NULL,
name VARCHAR(255) NOT NULL,
category_id BINARY(16) NOT NULL,
created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
modified_on TIMESTAMP DEFAULT 0 ON UPDATE CURRENT_TIMESTAMP,
CONSTRAINT Pk_Item PRIMARY KEY(id)
)ENGINE=INNODB;

CREATE TABLE Post(
id BINARY(16) NOT NULL,
title VARCHAR(255),
story TEXT NULL,
text_file_path VARCHAR(255) NULL,
user_id BINARY(16) NOT NULL,
item_id BINARY(16) NOT NULL,
created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
modified_on TIMESTAMP DEFAULT 0 ON UPDATE CURRENT_TIMESTAMP,
CONSTRAINT Pk_Post PRIMARY KEY(id)
)ENGINE=INNODB;

CREATE TABLE Classified(
id BINARY(16) NOT NULL,
title VARCHAR(255),
description TEXT NOT NULL,
price VARCHAR(15) NOT NULL,
user_id BINARY(16) NOT NULL,
item_id BINARY(16) NOT NULL,
created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
modified_on TIMESTAMP DEFAULT 0 ON UPDATE CURRENT_TIMESTAMP,
CONSTRAINT Pk_Classified PRIMARY KEY(id)
)ENGINE=INNODB;

CREATE TABLE User_Role(
user_id BINARY(16) NOT NULL,
role_id BINARY(16) NOT NULL,
CONSTRAINT Pk_User_Role PRIMARY KEY(user_id,role_id)
)ENGINE=INNODB;

CREATE TABLE Role_Permission(
role_id BINARY(16) NOT NULL,
permission_id BINARY(16) NOT NULL,
CONSTRAINT Pk_Role_Permission PRIMARY KEY(role_id,permission_id)
)ENGINE=INNODB;

CREATE TABLE Item_Image(
id BINARY(16) NOT NULL,
file_path VARCHAR(255) NOT NULL,
description VARCHAR(255) NULL,
item_id BINARY(16) NOT NULL,
created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
modified_on TIMESTAMP DEFAULT 0 ON UPDATE CURRENT_TIMESTAMP,
CONSTRAINT Pk_Item_Image PRIMARY KEY(id)
)ENGINE=INNODB;

CREATE TABLE Address(
id BINARY(16) NOT NULL,
city_id BINARY(16) NOT NULL,
created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
modified_on TIMESTAMP DEFAULT 0 ON UPDATE CURRENT_TIMESTAMP,
CONSTRAINT Pk_Address PRIMARY KEY(id)
)ENGINE=INNODB;

CREATE TABLE City(
id BINARY(16) NOT NULL,
postal_code VARCHAR(10) NOT NULL,
state_id BINARY(16) NOT NULL,
created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
modified_on TIMESTAMP DEFAULT 0 ON UPDATE CURRENT_TIMESTAMP,
CONSTRAINT Pk_City PRIMARY KEY(id)
)ENGINE=INNODB;

CREATE TABLE State(
id BINARY(16)  NOT NULL,
name VARCHAR(255) NOT NULL,
created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
modified_on TIMESTAMP DEFAULT 0 ON UPDATE CURRENT_TIMESTAMP,
CONSTRAINT Pk_State PRIMARY KEY(id)
)ENGINE=INNODB;

ALTER TABLE User
ADD CONSTRAINT Fk_User_Address
FOREIGN KEY(address_id) REFERENCES Address(id);

ALTER TABLE Item
ADD CONSTRAINT Fk_Item_Category_Category_id
FOREIGN KEY(category_id) REFERENCES Category(id);

ALTER TABLE Item_Image
ADD CONSTRAINT Fk_Item_Image_Item_id
FOREIGN KEY(item_id) REFERENCES Item(id);

ALTER TABLE Post
ADD CONSTRAINT Fk_Post_User_id
FOREIGN KEY(user_id) REFERENCES User(id);

ALTER TABLE Post
ADD CONSTRAINT Fk_Post_Item_id
FOREIGN KEY(item_id) REFERENCES Item(id);

ALTER TABLE Classified
ADD CONSTRAINT Fk_Classified_User_id
FOREIGN KEY(user_id) REFERENCES User(id);

ALTER TABLE Classified
ADD CONSTRAINT Fk_Classified_Item_id
FOREIGN KEY(item_id) REFERENCES Item(id);

ALTER TABLE Role_Permission
ADD CONSTRAINT Fk_Role_PermissionJunctionTable_Role_id
FOREIGN KEY(role_id) REFERENCES Role(id);

ALTER TABLE Role_Permission
ADD CONSTRAINT Fk_Role_PermissionJunctionTable_Permission_id
FOREIGN KEY(permission_id) REFERENCES Permission(id);

ALTER TABLE User_Role
ADD CONSTRAINT Fk_User_RoleJunctionTable_User_id
FOREIGN KEY(user_id) REFERENCES User(id);

ALTER TABLE User_Role
ADD CONSTRAINT Fk_User_RoleJunctionTable_Role_id
FOREIGN KEY(role_id) REFERENCES Role(id);
