package com.example.project.addstudent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    // 1. GET Endpoint: Retrieves all students
    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // 2. POST Endpoint: Adds a new student (Your existing working code)
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addStudent(@RequestBody Student student) {
        Student savedStudent = studentRepository.save(student);
        Map<String, Object> response = new HashMap<>();
        response.put("id", savedStudent.getId());
        // Returning 201 Created status
        return new ResponseEntity<>(response, HttpStatus.CREATED); 
    }

    // 3. PUT Endpoint: Updates an existing student
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(
        @PathVariable Integer id, // Corrected: Removed ** around Integer
        @RequestBody Student studentDetails) {

        // 1. Find the existing student by ID
        Optional<Student> studentOptional = studentRepository.findById(id);

        if (studentOptional.isPresent()) {
            Student existingStudent = studentOptional.get();

            // 2. Update the fields of the existing student
            existingStudent.setFirstName(studentDetails.getFirstName());
            existingStudent.setLastName(studentDetails.getLastName());
            existingStudent.setDateOfBirth(studentDetails.getDateOfBirth());
            existingStudent.setYearOfRegistration(studentDetails.getYearOfRegistration());
            existingStudent.setAddressDetails(studentDetails.getAddressDetails());
            existingStudent.setContactDetails(studentDetails.getContactDetails());
            existingStudent.setStream(studentDetails.getStream());

            // 3. Save the updated student back to the database
            Student updatedStudent = studentRepository.save(existingStudent);
            
            // 4. Return the updated student and HTTP 200 OK
            return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
        } else {
            // 5. If the student is not found, return HTTP 404 Not Found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // 4. DELETE Endpoint
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Integer id) { // Corrected: Removed ** around Integer
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        }
    }
}
