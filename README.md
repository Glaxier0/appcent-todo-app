# Appcent-todo-app-backend

Backend of todo app project for Appcent Challenge Project with jwt auth, password encoding and unit testing. 

App is deployed on [Heroku](https://glaxier-todo.herokuapp.com/) and ready to use. 

Root url of project is

>https://glaxier-todo.herokuapp.com/

All REST API endpoint documentations can be found at [Swagger-ui](https://glaxier-todo.herokuapp.com/swagger-ui/index.html).

If you wish to read swagger documentation on [Swagger online editor](https://editor.swagger.io/) just copy [Swagger json response](https://glaxier-todo.herokuapp.com/v3/api-docs) and paste it to online editor.

[H2-Console](https://glaxier-todo.herokuapp.com/h2-console/)

[Docker-Hub](https://hub.docker.com/repository/docker/glaxier0/appcent-todo-app)

### DISCLAIMER
If you try app on heroku, first time loading app will take some time (5-6 secs) because of heroku. So please be patient.

## Build App

There is 2 options when building the app

### Build App locally from Github repository

- Download, clone or pull github repository.

- Open project using your favorite IDE (Intellij IDEA used in this project).

- Maven should automatically install dependencies.

- If you want to change any properties go to [application.properties](https://github.com/Glaxier0/appcent-todo-app/blob/main/src/main/resources/application.properties)
 
- Your app is now ready to next step [Running App](https://github.com/Glaxier0/appcent-todo-app/edit/main/README.md#running-app).

### Build App locally from Docker Hub

- You need to have docker installed on your computer or server for this option.

- Run docker pull command to get docker image from [Docker-Hub](https://hub.docker.com/repository/docker/glaxier0/appcent-todo-app).
```
docker pull glaxier0/appcent-todo-app
```
- After image pulled from Docker Hub check images using
```
docker images
```
- If image is showing there you are ready to next step [Running App](https://github.com/Glaxier0/appcent-todo-app/edit/main/README.md#running-app).

## Running tests

There is 29 test case you can test them all with following maven command.
```
mvn clean test 
```
## Running App

If build app from Github repository option selected use this maven command
```
mvn spring-boot:run
```
Or run [TodoApplication.java](https://github.com/Glaxier0/appcent-todo-app/blob/main/src/main/java/com/glaxier/todo/TodoApplication.java) manually from your IDE.

If build app from Docker Hub option selected use this docker command
```
docker run -dp 8080:8080 glaxier0/appcent-todo-app
```

after docker run command check if image is running by
```
docker ps
```
if docker image is shown there, you successfully started app using docker image.


After running app locally you can check avaible endpoints by adding /swagger-ui/index.html to end of your base url.
>http://localhost:8080/swagger-ui/index.html

## How to use the API
There is 2 enpoints that doesnt require Authentication: /users/register and /users/login

- Users should first make a register request to /users/register endpoint
- After registering now make a login request to /users/login endpoint
- If tried with correct credentials login request should return jwt token.
- Now you can use all endpoints with that jwt token
- All you have to do is add "Authorization" header a value like this "Bearer {your_token}"
