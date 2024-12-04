package app.finance.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.finance.api.Model.UserModel;
import app.finance.api.Repository.IUserRepository;
import at.favre.lib.crypto.bcrypt.BCrypt;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// User for the user controller
@RestController
@CrossOrigin(origins = "*") // Allow requests from any origin
@RequestMapping("/Users") // API route
public class UserController {

    // dependency injection for the repositories
    @Autowired
    private IUserRepository userRepository;

    // dependency injection for the repositories
    @Autowired
    private JdbcTemplate jdbcTemplate;


    // Create a new user
    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody UserModel userModel){
        try {
            // Check if user already exists
            var user = this.userRepository.findByEmail(userModel.getEmail());
            if(user != null){
                // Return bad request
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
            }

            // Hash the password
            var passwordHashed = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
            // Set the password
            userModel.setPassword(passwordHashed);
            // Save the user
            var userCreated = this.userRepository.save(userModel);

            // Execute the SQL file with default categories for the user
            executeSqlFile("src/main/resources/default_categories.sql", userCreated.getUid());

            return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating user");
        }
    }

    // Login a user
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserModel userModel){
        try {
            // Find the user by email
            var user = this.userRepository.findByEmail(userModel.getEmail());
            // If user not found
            if(user == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
            }

            // Verify the password
            var passwordVerified = BCrypt.verifyer().verify(userModel.getPassword().toCharArray(), user.getPassword());
            if(!passwordVerified.verified){// If password is invalid
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid password");
            }

            // Return the user
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error logging in user");
        }
    }

    // Get all users
    @GetMapping("/accounts")
    public ResponseEntity<List<UserModel>> getMethodName() {
        try {
            // Get all users
            var users = this.userRepository.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(users);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    // Get a user by ID
    @GetMapping("/accounts/{id}")
    public ResponseEntity<Optional<UserModel>> getMethodName(@PathVariable int id) {
        try {
            // Get the user by ID
            var user = this.userRepository.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    // Execute SQL file
    private void executeSqlFile(String filePath, int userId) throws IOException {
        // Read the SQL file
        String sql = new String(Files.readAllBytes(Paths.get(filePath)));
        // Replace the user ID
        sql = sql.replace("{userId}", String.valueOf(userId));
        // Execute the SQL commands
        Stream.of(sql.split(";"))
                .filter(command -> !command.trim().isEmpty())
                .forEach(command -> jdbcTemplate.execute(command));
    }

}