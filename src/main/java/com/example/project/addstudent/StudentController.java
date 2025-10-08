package com.example.project.addstudent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/students") // Base path: /api/students
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    // 1. GET Endpoint: Retrieves all students - URL: /api/students
    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // 2. POST Endpoint: Adds a new student - URL: /api/students 
    // CHANGE IS HERE: Changed from @PostMapping("/add") to @PostMapping
    @PostMapping 
    public ResponseEntity<Map<String, Object>> addStudent(@RequestBody Student student) {
        Student savedStudent = studentRepository.save(student);
        Map<String, Object> response = new HashMap<>();
        response.put("id", savedStudent.getId());
        // Returning 201 Created status
        return new ResponseEntity<>(response, HttpStatus.CREATED); 
    }

    // 3. PUT Endpoint: Updates an existing student - URL: /api/students/{id}
    // This method is now cleanly separated from the POST method.
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(
        @PathVariable Integer id, 
        @RequestBody Student studentDetails) {

        Optional<Student> studentOptional = studentRepository.findById(id);

        if (studentOptional.isPresent()) {
            Student existingStudent = studentOptional.get();

            // Update the fields of the existing student
            existingStudent.setFirstName(studentDetails.getFirstName());
            existingStudent.setLastName(studentDetails.getLastName());
            existingStudent.setDateOfBirth(studentDetails.getDateOfBirth());
            existingStudent.setYearOfRegistration(studentDetails.getYearOfRegistration());
            existingStudent.setAddressDetails(studentDetails.getAddressDetails());
            existingStudent.setContactDetails(studentDetails.getContactDetails());
            existingStudent.setStream(studentDetails.getStream());

            Student updatedStudent = studentRepository.save(existingStudent);
            
            return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // 4. DELETE Endpoint - URL: /api/students/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Integer id) { 
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        }
    }
}
