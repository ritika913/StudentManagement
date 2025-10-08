package com.example.project.addstudent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors; // Required for the stream operations

/**
 * Data Transfer Object (DTO) for student retrieval.
 * This class ensures that only necessary fields are exposed via the GET endpoint.
 */
class StudentResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String course;
    
    // Constructor to map from Student Entity
    public StudentResponse(Student student) {
        this.id = student.getId();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        // Assuming your Student entity has these getters/fields:
        this.email = student.getContactDetails(); // Using contactDetails for email for simplicity
        this.course = student.getStream(); 
    }

    // Getters for JSON serialization
    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getCourse() { return course; }
}

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    /**
     * Endpoint to add a new student.
     * URL: POST /api/students/add
     */
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addStudent(@RequestBody Student student) {
        Student savedStudent = studentRepository.save(student);
        Map<String, Object> response = new HashMap<>();
        response.put("id", savedStudent.getId());
        
        // Return 201 Created status for resource creation
        return ResponseEntity.status(201).body(response); 
    }
    
    /**
     * Endpoint to get a list of all students.
     * URL: GET /api/students
     */
    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        
        // Map Student entity list to StudentResponse DTO list
        List<StudentResponse> responseList = students.stream()
            .map(StudentResponse::new)
            .collect(Collectors.toList());
            
        return ResponseEntity.ok(responseList);
    }
}
