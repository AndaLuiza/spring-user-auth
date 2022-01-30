# spring-user-auth demo
### User Authentication application with SpringBoot and Spring Security with JWT

- Provides an authentication mechanism based on user credentials saved in db
- Password is encoded with Spring default encoder
- When authenticated, new JWT token is generated
- All the other requests besides /login must provide this Bearer token in the Authorization Header
- Role-based authorization
- 2 users and 3 roles are saved into db at application startup
- All endpoints are defined in the Controller: POST /login, POST /logout, GET /currentUser
- Repository contains a postman collection + environment with several requests for all endpoints covering multiple scenarios

Run the app locally using `mvn install` and then `mvn spring-boot:run` or directly from Intellij after importing the project.

Send the requests from the postman collection.

Sending, for example, POST http://localhost:8080/api/auth/login with:

`{
"username": "user1",
"password": "test1"
}`

will return the authenticated user details and the generated JWT token:

`{
"token": "jwt_token",
"type": "Bearer",
"username": "user1",
"email": "test1@email.com",
"roles": [
"ROLE_ADMIN",
"ROLE_USER"
]
}`

###### - Java: 10
###### - Spring Boot
###### - Spring Security
###### - Spring JPA
###### - H2 in-memory database
###### - jjwt
###### - lombok
