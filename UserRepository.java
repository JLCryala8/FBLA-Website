/* Repository interface for MongoDB operations */

package com.example.demo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String>{
    List<User> findByNameContainingIgnoreCase(String name);
    List<User> findByItemNameContainingIgnoreCase(String itemName);
    List<User> findByDay(int day);
    List<User> findByMonth(int month);
    List<User> findByYear(int year);
    List<User> findByCategory(String category);
    List<User> findByLocation(String location);
    List<User> findByIsFound(boolean isFound);
    Optional<User> findByUserNumber(Long userNumber);
    List<User> findByUserId(int userId);
    User findTopByOrderByUserNumberDesc();
}
