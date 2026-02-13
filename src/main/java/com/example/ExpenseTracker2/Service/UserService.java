package com.example.ExpenseTracker2.Service;

import com.example.ExpenseTracker2.Entity.User;
import com.example.ExpenseTracker2.Exception.UserNotFoundException;
import com.example.ExpenseTracker2.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private  final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User creatUser(User user){
        System.out.println("Saving user: " + user.getEmail());
        return userRepository.save(user);
    }

    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
    }
}
