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

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addStudent(@RequestBody Student student) {
        Student savedStudent = studentRepository.save(student);
        Map<String, Object> response = new HashMap<>();
        response.put("id", savedStudent.getId());
        return ResponseEntity.ok(response);
    }
}
