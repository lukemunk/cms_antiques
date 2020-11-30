# cms_antiques
[Running the app](https://via.placeholder.com/15/1589F0/000000?text=+) 

Requires Java IDE or Docker

```dif
1) Docker
 -Build the project by doing a Maven Clean Install in project directory
 -In CMD or Terminal go to Source Directory and run "Docker-Compose up" to start Database and Web App
 -Go to a browser and go to http://localhost:8080/
```
```dif
2) Java IDE
 -Build the project by doing a Maven Clean Install in project directory
 -Open up using preferred Java IDE
 -Open MySQL Database, configure the correct passwords and usernames for the application to access database
 -Run MySQL
 -Run As Java Program
 -Go to a browser and go to http://localhost:8080/
```

After that you can view posts, if you want to create posts, view classifieds, or modify your account you'll need to sign in
The default Admin account is "admin" and "password"

[Accounts/Permissions/Using Website](https://via.placeholder.com/15/c5f015/000000?text=+)

There are Admin accounts as well as User accounts

Admins have permissions to modify and edit any content 
Users have permissions to create posts and classifieds as well as edit any of their content

To create a post or classified go to the post/classified section while logged in and click
"Create New Post/Classified"
It'll take you to a page where you can create the content and save it

To edit a post or classified go to the post/classified you made (or any if you're an admin)
and an Edit option will appear on the post

[Security](https://via.placeholder.com/15/1589F0/000000?text=+)

// To do, add how our security is set up and works

[Support](https://via.placeholder.com/15/1589F0/000000?text=+)
 
-Link to Docker Compose How To Guide
 https://docs.docker.com/compose/gettingstarted/#:~:text=Get%20started%20with%20Docker%20Compose.%201%20Step%201%3A,to%20add%20a%20bind%20mount%20%F0%9F%94%97.%20More%20items

-If you do not have any category or tag options in your database you will not be able to create content
