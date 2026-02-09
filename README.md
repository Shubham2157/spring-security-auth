# Spring Boot Basic Authentication Implementation

A comprehensive Spring Boot application demonstrating **HTTP Basic Authentication** using Spring Security with a database-backed user authentication system.

## 📋 Overview

This project showcases the implementation of basic authentication in Spring Security with the following features:
- HTTP Basic Authentication
- BCrypt password encryption
- Custom UserDetailsService
- Database-backed user credentials
- Secured and unsecured endpoints

## 🛠️ Technology Stack

- **Spring Boot 3.x** - Web framework
- **Spring Security 6.x** - Authentication and authorization
- **MySQL** - Database
- **JPA/Hibernate** - ORM
- **Maven** - Build tool
- **Java 17+** - Language

## 📁 Project Structure

```
authentication/
├── src/main/java/com/shubham/authentication/
│   ├── AuthenticationApplication.java      # Main Spring Boot application
│   ├── config/
│   │   └── SecurityConfig.java            # Spring Security configuration
│   ├── controller/
│   │   └── MyController.java              # REST API endpoints
│   ├── entity/
│   │   └── Users.java                     # User JPA entity
│   ├── repo/
│   │   └── UserRepo.java                  # User repository (Spring Data JPA)
│   └── service/
│       └── UserService.java               # UserDetailsService implementation
├── src/main/resources/
│   └── application.properties              # Application configuration
└── pom.xml                                 # Maven dependencies
```

## 🔐 Key Components

### 1. **Users Entity** (`Users.java`)
JPA entity representing a user in the database with:
- `id` - Primary key (auto-generated)
- `username` - Unique username
- `password` - BCrypt encrypted password
- `isActive` - User activation status

### 2. **UserService** (`UserService.java`)
Implements Spring Security's `UserDetailsService` interface:
- Loads user credentials from the database
- Converts `Users` entity to Spring Security's `UserDetails`
- Provides authorities/roles to the user

### 3. **SecurityConfig** (`SecurityConfig.java`)
Spring Security configuration with:

**HTTP Security Rules:**
- `/hello` endpoint is **publicly accessible** (no authentication required)
- All other endpoints require **HTTP Basic Authentication**

**Authentication Components:**
- `PasswordEncoder` - BCrypt password encoder for secure password storage
- `AuthenticationManager` - Manages the authentication process
- `DaoAuthenticationProvider` - DAO-based authentication provider
- `UserDetailsService` - Custom user details service

### 4. **Database Configuration**
Located in `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/practice_db
spring.datasource.username=root
spring.datasource.password=root
```

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- IDE (IntelliJ IDEA, VS Code, or Eclipse)

### Installation Steps

1. **Clone/Download the project**
   ```bash
   cd authentication
   ```

2. **Create MySQL Database**
   ```sql
   CREATE DATABASE practice_db;
   
   USE practice_db;
   
   CREATE TABLE user (
       id BIGINT PRIMARY KEY AUTO_INCREMENT,
       username VARCHAR(255) NOT NULL UNIQUE,
       password VARCHAR(255) NOT NULL,
       is_active BOOLEAN NOT NULL
   );
   ```

3. **Add Sample User** (optional)
   ```sql
   INSERT INTO user (username, password, is_active) 
   VALUES ('shubham', '$2a$10/...bcrypt_hash...', true);
   ```

4. **Configure Database Connection**
   Update `application.properties` with your MySQL credentials:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/practice_db?useSSL=false
   spring.datasource.username=root
   spring.datasource.password=your_password
   ```

5. **Build and Run**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

   The application will start on `http://localhost:8080`

## 📡 API Endpoints

### 1. **Public Endpoint** (No Authentication Required)
```
GET /hello
Response: "hello world"
```
**Example:**
```bash
curl http://localhost:8080/hello
```

### 2. **Protected Endpoints** (Requires HTTP Basic Auth)
```
GET /hey
GET /hi
```

**Example with Basic Authentication:**
```bash
curl -u shubham:12345 http://localhost:8080/hey
curl -u shubham:12345 http://localhost:8080/hi
```

**Response:**
```
"hey"
"hi"
```

### 3. **HTTP Basic Auth Header Format**
Credentials are sent as Base64-encoded `Authorization` header:
```
Authorization: Basic base64(username:password)
```

## 🔑 Authentication Flow

```
1. Client sends HTTP request with Authorization header
   ↓
2. Spring Security's BasicAuthenticationFilter intercepts the request
   ↓
3. Credentials are extracted and decoded
   ↓
4. AuthenticationManager delegates to DaoAuthenticationProvider
   ↓
5. UserService.loadUserByUsername() retrieves user from database
   ↓
6. PasswordEncoder verifies the provided password against stored BCrypt hash
   ↓
7. If valid → Request proceeds to controller
   If invalid → 401 Unauthorized response returned
```

## 🛡️ Security Features

- **BCrypt Password Encoding** - Industry-standard password hashing
- **Stateless Authentication** - Each request carries credentials
- **DAO Authentication** - Database-backed user validation
- **HTTP Basic Auth** - Simple but effective authentication mechanism
- **Endpoint-level Security** - Fine-grained access control

## 📝 Configuration Details

### SecurityConfig Beans

```java
// Password Encoder Bean
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}

// User Details Service Bean
@Bean
public UserDetailsService getUserData() {
    return new UserService();
}

// Authentication Manager Bean
@Bean
public AuthenticationManager authenticationManager(UserService userService, PasswordEncoder passwordEncoder) {
    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userService);
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
    return new ProviderManager(daoAuthenticationProvider);
}

// Security Filter Chain Bean
@Bean
public SecurityFilterChain basicAuth(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth ->
            auth
                .requestMatchers("/hello").permitAll()
                .anyRequest().authenticated())
        .httpBasic(Customizer.withDefaults());
    return http.build();
}
```

## 🧪 Testing

### Using Postman
1. Set request type to **GET**
2. URL: `http://localhost:8080/hey`
3. Go to **Authorization** tab
4. Select **Basic Auth** from dropdown
5. Username: `shubham`
6. Password: `12345`
7. Click **Send**

### Using cURL
```bash
# Test public endpoint
curl http://localhost:8080/hello

# Test protected endpoint with valid credentials
curl -u shubham:12345 http://localhost:8080/hey

# Test protected endpoint without credentials (should fail)
curl http://localhost:8080/hey
```

## 🔍 Troubleshooting

| Issue | Solution |
|-------|----------|
| **401 Unauthorized** | Check username/password credentials |
| **Database connection error** | Verify MySQL is running and credentials in `application.properties` are correct |
| **Password encoding mismatch** | Ensure passwords are BCrypt encoded; use the PasswordEncoder bean |
| **User not found** | Insert user record in database or uncomment `userRepo.save(users)` in AuthenticationApplication |

## 📚 Key Concepts

### HTTP Basic Authentication
- Simple authentication mechanism using Base64-encoded credentials
- Sent via `Authorization: Basic base64(username:password)` header
- Should only be used over HTTPS in production

### BCryptPasswordEncoder
- One-way hashing algorithm with built-in salt
- Each hash is unique even for the same password
- Industry standard for password storage

### UserDetailsService
- Spring Security interface for loading user credentials
- Must implement `loadUserByUsername()` method
- Bridges database and Spring Security

## 🚀 Next Steps / Enhancements

- Implement JWT token-based authentication
- Add role-based authorization
- Implement OAuth2/OpenID Connect
- Add rate limiting
- Add audit logging
- Implement session management
- Add HTTPS/TLS encryption

## 📖 Resources

- [Spring Security Documentation](https://spring.io/projects/spring-security)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [HTTP Basic Authentication RFC](https://tools.ietf.org/html/rfc7617)
- [OWASP Authentication Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Authentication_Cheat_Sheet.html)

## 👤 Author
Shubham

## 📄 License
This project is open source and available under the MIT License.

---

**Note:** This implementation is for educational purposes. For production use, consider implementing more secure authentication methods like JWT, OAuth2, or OpenID Connect over HTTPS.
