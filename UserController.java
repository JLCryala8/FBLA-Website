/* Controller layer to handle HTTP requests and responses */

package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;
import java.util.Optional;

// @CrossOrigin(origins = "*") // Allow CORS for all origins (for development purposes)
@RestController // Mark this class as a REST controller to handle HTTP requests
@RequestMapping("/users")
public class UserController {

    // Autowired dependencies for repository and service layers
    @Autowired
    private UserRepository userRepository;

    // Autowired service layer to handle business logic
    @Autowired
    private UserService userService;

    // Autowired MongoTemplate for custom queries and updates
    @Autowired
    private MongoTemplate mongoTemplate;

    // Create a new user with auto-incrementing userNumber and String ID based on userNumber
    @PostMapping
    public User createUser(@RequestBody User user){
        User lastUser = userRepository.findTopByOrderByUserNumberDesc();
        long nextNumber = 1; // Default to 1 if no users exist
        if (lastUser != null) {
            Long lastNumber = lastUser.getUserNumber();
            if (lastNumber != null) {
                nextNumber = lastNumber + 1; // Increment the last user number
            }
        }
        user.setUserNumber(nextNumber); // Set the new user number
        user.setId(String.valueOf(nextNumber)); // Use numeric string as Mongo _id
        return userRepository.save(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id){
        Optional<User> user = userRepository.findById(id); // Use String id directly since MongoDB uses String IDs
        return user.map(ResponseEntity::ok) // Return 200 OK with user if found
            .orElseGet(() -> ResponseEntity.notFound().build()); // Return 404 if user not found
    }

    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/search")
    public List<User> searchUsers(@RequestParam(required = false) String name,
                                  @RequestParam(required = false) String itemName,
                                  @RequestParam(required = false) Integer userId,
                                  @RequestParam(required = false) Integer day,
                                  @RequestParam(required = false) Integer month,
                                  @RequestParam(required = false) Integer year,
                                  @RequestParam(required = false) String category,
                                  @RequestParam(required = false) String location,
                                  @RequestParam(required = false) Boolean isFound) {
        if (name != null) {
            return userRepository.findByNameContainingIgnoreCase(name.toUpperCase());
        } else if (itemName != null) {
            return userRepository.findByItemNameContainingIgnoreCase(itemName.toUpperCase());
        } else if (day != null) {
            return userRepository.findByDay(day);
        } else if (month != null) {
            return userRepository.findByMonth(month);
        } else if (year != null) {
            return userRepository.findByYear(year);
        } else if (category != null) {
            return userRepository.findByCategory(category);
        } else if (location != null) {
            return userRepository.findByLocation(location);
        } else if (isFound != null) {
            return userRepository.findByIsFound(isFound);
        } else {
            return userRepository.findAll();
        }
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id){
        userRepository.deleteById(id); // Use String id directly since MongoDB uses String IDs
    }

    @DeleteMapping
    public void deleteAllUsers(){
        userRepository.deleteAll();
    }
    
    @PutMapping("/{id}")
    public User updateUser(@PathVariable String id, @RequestBody User user){
        int userId = Integer.parseInt(id);
        return userService.updateUser(userId, user);
    }

    // POST /users/{id}/image
    @PostMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@PathVariable String id, @RequestParam("file") MultipartFile file){
        try {
            Query query = new Query(Criteria.where("userNumber").is(Integer.parseInt(id))); // Find user by userNumber
            Update update = new Update()
                .set("image", file.getBytes()) // Store the image bytes
                .set("imageType", file.getContentType()); // Store the content type for later retrieval
            var result = mongoTemplate.updateFirst(query, update, User.class);

            if (result.getMatchedCount() == 0) {
                return ResponseEntity.notFound().build(); // Return 404 if user not found
            }

            return ResponseEntity.ok("Image uploaded successfully");
        } 
        catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to upload image: " + e.getMessage()); // Return 500 error if something goes wrong
        }
    }

    // GET /users/{id}/image
    @GetMapping(value = "/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        var opt = userRepository.findByUserNumber(id);

        if (opt.isEmpty() || opt.get().getImage() == null) // Check if user exists and has an image
        return ResponseEntity.notFound().build();

        User u = opt.get();
        MediaType mt = (u.getImageType() != null) 
            ? MediaType.parseMediaType(u.getImageType()) // Use the stored content type if available
            : MediaType.APPLICATION_OCTET_STREAM; // Default to binary stream if content type is not set

        return ResponseEntity.ok().contentType(mt).body(u.getImage());
    }
}