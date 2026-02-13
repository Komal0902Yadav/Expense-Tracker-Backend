package com.example.ExpenseTracker2.Repository;

import com.example.ExpenseTracker2.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
