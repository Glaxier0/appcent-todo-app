# Appcent-todo-app-backend

Backend of todo app project for Appcent Challenge Project with jwt auth, password encoding and unit testing. 

App is deployed on [Heroku](https://glaxier-todo.herokuapp.com/) and ready to use. 

Root url of project is

>https://glaxier-todo.herokuapp.com/

All REST API endpoint documentations can be found at [Swagger-ui](https://glaxier-todo.herokuapp.com/swagger-ui/index.html).

If you wish to read swagger documentation on [Swagger online editor](https://editor.swagger.io/) just copy [Swagger json response](https://glaxier-todo.herokuapp.com/v3/api-docs) and paste it to online editor.

[H2-Console](https://glaxier-todo.herokuapp.com/h2-console/)

[Docker-Hub](https://hub.docker.com/repository/docker/glaxier0/appcent-todo-app)

[Test API using swagger-ui](https://github.com/Glaxier0/appcent-todo-app#test-api-using-swagger-ui)

[Test API using postman](https://github.com/Glaxier0/appcent-todo-app#test-api-using-postman)

### DISCLAIMER
If you try app on heroku, first time loading app will take some time (5-6 secs) because of heroku. So please be patient.

## Build App

There is 2 options when building the app

### Build App locally from Github repository

- Download, clone or pull github repository.

- Open project using your favorite IDE (Intellij IDEA used in this project).

- Maven should automatically install dependencies.

- If you want to change any properties go to [application.properties](https://github.com/Glaxier0/appcent-todo-app/blob/main/src/main/resources/application.properties)
 
- Your app is now ready to next step [Running App](https://github.com/Glaxier0/appcent-todo-app#running-app).

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
- If image is showing there you are ready to next step [Running App](https://github.com/Glaxier0/appcent-todo-app#running-app).

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
There are 2 enpoints that doesnt require Authentication: /users/register and /users/login

- Users should first make a register request to /users/register endpoint
- After registering now make a login request to /users/login endpoint
- If tried with correct credentials login request should return jwt token.
- Now you can use all endpoints with that jwt token
- All you have to do is add "Authorization" header a value like this "Bearer {your_token}"

All REST API endpoint documentations can be found at [Swagger-ui](https://glaxier-todo.herokuapp.com/swagger-ui/index.html).

## Test API Using Postman

- First download [collection](https://github.com/Glaxier0/appcent-todo-app/blob/main/Todo%20App.postman_collection.json).
- After downloading go to the postman and click import button in left up corner.
- Select downloaded file and import as collection.
- Now in the right up corner select "No enviroment" then click eye icon next to it.
- Click add button enviroment button and name it Todo(Production).
- Add a new variable with name url and paste "https://glaxier-todo.herokuapp.com" exactly this to initial value.
- If initial value and current value is "https://glaxier-todo.herokuapp.com/" like this delete last "/" .
- it should be exactly like this "https://glaxier-todo.herokuapp.com" without "/" after .com
- Click save button in the right up corner.
- Now in the left side extend Todo App collection and click Create User > Authorization > Type > No Auth and save it.
- Now click Login User > Authorization > Type > No Auth and save it.
- In login User > Tests copy and paste below code.
```
if (pm.response.code === 200) {
    pm.environment.set('authToken', pm.response.json().token)
}
```
- In Todo App collection click every request (except "Create User" and "Login User") and change their Authorization > Type > Bearer Token
- Now Postman setup is done and you can test API using postman
- All you have to do is check required params to the every request from [Swagger-ui](https://glaxier-todo.herokuapp.com/swagger-ui/index.html).
- Then make requests with required params.

## Test API Using Swagger UI
- Open [Swagger-ui](https://glaxier-todo.herokuapp.com/swagger-ui/index.html).
- First thing to to is make a post request to /users/register.
- Click try it out and make a valid user.
- Now make a login request to /users/login
- Click try it out and make a login request with same email and password with register request
- If you tried login request with correct credentials it should return 200 with token field.
- Copy token value in response.
- Now click Authorize button or click any lock icon in the right side of requests.
- Paste token value to value and click authorize.
- Now you can make requests to all endpoints.
- /users/logout and /users/logoutAll deletes jwt tokens attached to users.
- If you make request to this 2 endpoints you have to login again and get new token.
