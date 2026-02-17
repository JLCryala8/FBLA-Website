/* Service layer to handle business logic and interact with the repository */

package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    // Service layer to handle business logic and interact with the repository
    @Autowired
    private UserRepository userRepository;

    // CRUD operations
    public User saveUser(User user){
        return userRepository.save(user);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    
    public User getUserById(int id){
        return userRepository.findById(String.valueOf(id)).orElse(null);
    }

    public void deleteUser(int id){
        userRepository.deleteById(String.valueOf(id)); // Convert int id to String for MongoDB
    }

    public void deleteAllUsers(){
        userRepository.deleteAll();
    }

    // Search methods
    public List<User> findById(int id){
        return userRepository.findById(String.valueOf(id)).map(List::of).orElse(List.of());
    }
    public List<User> findByName(String name){
        return userRepository.findByNameContainingIgnoreCase(name.toUpperCase());
    }
    
    public List<User> findByItemName(String itemName){
        return userRepository.findByItemNameContainingIgnoreCase(itemName.toUpperCase());
    }

    public List<User> findByDay(int day){
        return userRepository.findByDay(day);
    }

    public List<User> findByMonth(int month){
        return userRepository.findByMonth(month);
    }

    public List<User> findByYear(int year){
        return userRepository.findByYear(year);
    }

    public List<User> findByCategory(String category){
        return userRepository.findByCategory(category);
    }

    public List<User> findByLocation(String location){
        return userRepository.findByLocation(location);
    }

    public List<User> findByIsFound(boolean isFound){
        return userRepository.findByIsFound(isFound);
    }

    // Combined search method
    public List<User> searchUsers(String name, String itemName, Integer day, Integer month, Integer year, String category, String location, Boolean isFound){
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

    // Update method
    public User updateUser(int id, User user){
        User existingUser = userRepository.findById(String.valueOf(id)).orElse(null);
        if (existingUser != null) {
            existingUser.setName(user.getName().toUpperCase());
            existingUser.setItemName(user.getItemName().toUpperCase());
            existingUser.setDay(user.getDay());
            existingUser.setMonth(user.getMonth());
            existingUser.setYear(user.getYear());
            existingUser.setCategory(user.getCategory());
            existingUser.setLocation(user.getLocation());
            existingUser.setIsFound(user.getIsFound());
            return userRepository.save(existingUser); // Save the updated user
        } else {
            return null;
        }
    }

}