/* Controller layer to handle HTTP requests and responses */

package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

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
    public ResponseEntity<?> uploadImage(@PathVariable String id, @RequestParam("file") MultipartFile file) throws IOException {
    java.util.Optional<User> opt = userRepository.findById(id);
    if (opt.isEmpty()) return ResponseEntity.notFound().build();
    
    User u = opt.get();
    u.setImage(file.getBytes());
    u.setImageType(file.getContentType());
    userRepository.save(u);
    return ResponseEntity.ok().build();
    }

    // GET /users/{id}/image
    @GetMapping(value = "/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable String id) {
    var opt = userRepository.findById(id);
    if (opt.isEmpty() || opt.get().getImage() == null) return ResponseEntity.notFound().build();

    User u = opt.get();
    MediaType mt = (u.getImageType() != null) ? MediaType.parseMediaType(u.getImageType()) : MediaType.APPLICATION_OCTET_STREAM;
    return ResponseEntity.ok().contentType(mt).body(u.getImage());
    }

    @PostMapping("/test")
    public String testUpload(@RequestParam("file") MultipartFile file) throws IOException {
        return "Received file: " + file.getOriginalFilename() + " with size: " + file.getSize() + " bytes";
    }
}