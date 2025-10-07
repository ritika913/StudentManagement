/*package com.example.project.registration;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository extends JpaRepository<Registration, Integer> {
}*/

package com.example.project.registration;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.loginsystem.User;

public interface RegistrationRepository extends JpaRepository<User, Integer> {
    boolean existsByUsername(String username); // <-- Add this line
    Optional<User> findByUsername(String username);
}
