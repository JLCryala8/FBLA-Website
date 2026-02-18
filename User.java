/* User entity representing a lost or found item report */
package com.example.demo;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "users")
public class User {
    @Id
    private String ID; // ID to auto-increment for MongoDB
    

    @Field("userNumber") // New field for user number
    private Long userNumber; // New field for user number
    private String name;
    private int userId;
    private String itemName;
    private int day;
    private int month;
    private int year;
    private String category;
    private String location;
    private boolean isFound;
    private byte[] image;
    private String imageType;


    public User(){}

    public void setUserNumber(Long userNumber){
        this.userNumber = userNumber;
    }

    public Long getUserNumber(){
        return userNumber;
    }

    public String getId(){
        return ID;
    }

    public void setId(String id){
        this.ID = id;
    }

    public void setUserId(String userId){
        this.userId = Integer.parseInt(userId); // Convert String to int for userId
    }

    public int getUserId(){
        return userId;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
    
    public String getItemName(){
        return itemName;
    }

    public void setItemName(String itemName){
        this.itemName = itemName;
    }
    
    public int getDay(){
        return day;
    }

    public void setDay(int day){
        this.day = day;
    }

    public int getMonth(){
        return month;
    }

    public void setMonth(int month){
        this.month = month;
    }

    public int getYear(){
        return year;
    }

    public void setYear(int year){
        this.year = year;
    }

    public String getCategory(){
        return category;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public String getLocation(){
        return location;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public boolean getIsFound(){
        return isFound;
    }

    public void setIsFound(boolean isFound){
        this.isFound = isFound;
    }

    public byte[] getImage(){ 
        return image; 
    }

    public void setImage(byte[] image){ 
        this.image = image; 
    }

    public String getImageType(){ 
        return imageType;
    }

    public void setImageType(String imageType){ 
        this.imageType = imageType; 
    }

}