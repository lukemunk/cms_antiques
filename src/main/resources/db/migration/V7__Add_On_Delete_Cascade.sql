ALTER TABLE User
    DROP CONSTRAINT Fk_User_Address;

ALTER TABLE User
    ADD CONSTRAINT Fk_User_Address
        FOREIGN KEY(address_id) REFERENCES Address(id)
            ON DELETE CASCADE;

ALTER TABLE Item
    DROP CONSTRAINT Fk_Item_Category_Category_id;

ALTER TABLE Item
    ADD CONSTRAINT Fk_Item_Category_Category_id
        FOREIGN KEY(category_id) REFERENCES Category(id)
            ON DELETE CASCADE;

ALTER TABLE Item_Image
    DROP CONSTRAINT Fk_Item_Image_Item_id;

ALTER TABLE Item_Image
    ADD CONSTRAINT Fk_Item_Image_Item_id
        FOREIGN KEY(item_id) REFERENCES Item(id)
            ON DELETE CASCADE;

ALTER TABLE Post
    DROP CONSTRAINT Fk_Post_User_id;

ALTER TABLE Post
    ADD CONSTRAINT Fk_Post_User_id
        FOREIGN KEY(user_id) REFERENCES User(id)
            ON DELETE CASCADE;

ALTER TABLE Post
    DROP CONSTRAINT Fk_Post_Item_id;

ALTER TABLE Post
    ADD CONSTRAINT Fk_Post_Item_id
        FOREIGN KEY(item_id) REFERENCES Item(id)
            ON DELETE CASCADE;

ALTER TABLE Classified
    DROP CONSTRAINT Fk_Classified_User_id;

ALTER TABLE Classified
    ADD CONSTRAINT Fk_Classified_User_id
        FOREIGN KEY(user_id) REFERENCES User(id)
            ON DELETE CASCADE;

ALTER TABLE Classified
    DROP CONSTRAINT Fk_Classified_Item_id;

ALTER TABLE Classified
    ADD CONSTRAINT Fk_Classified_Item_id
        FOREIGN KEY(item_id) REFERENCES Item(id)
            ON DELETE CASCADE;

ALTER TABLE Role_Permission
    DROP CONSTRAINT Fk_Role_PermissionJunctionTable_Role_id;

ALTER TABLE Role_Permission
    ADD CONSTRAINT Fk_Role_PermissionJunctionTable_Role_id
        FOREIGN KEY(role_id) REFERENCES Role(id)
            ON DELETE CASCADE;

ALTER TABLE Role_Permission
    DROP CONSTRAINT Fk_Role_PermissionJunctionTable_Permission_id;

ALTER TABLE Role_Permission
    ADD CONSTRAINT Fk_Role_PermissionJunctionTable_Permission_id
        FOREIGN KEY(permission_id) REFERENCES Permission(id)
            ON DELETE CASCADE;

ALTER TABLE User_Role
    DROP CONSTRAINT Fk_User_RoleJunctionTable_User_id;

ALTER TABLE User_Role
    ADD CONSTRAINT Fk_User_RoleJunctionTable_User_id
        FOREIGN KEY(user_id) REFERENCES User(id)
            ON DELETE CASCADE;

ALTER TABLE User_Role
    DROP CONSTRAINT Fk_User_RoleJunctionTable_Role_id;

ALTER TABLE User_Role
    ADD CONSTRAINT Fk_User_RoleJunctionTable_Role_id
        FOREIGN KEY(role_id) REFERENCES Role(id)
            ON DELETE CASCADE;