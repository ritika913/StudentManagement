package com.example.project.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ViewControllerTest {

    private ViewRepository mockRepo;
    private ViewController controller;

    @BeforeEach
    void setUp() {
        mockRepo = mock(ViewRepository.class);
        controller = new ViewController();
        controller.setRepository(mockRepo);

    }

    @Test
    void testGetAllViews() { // Renamed from testGetAllStudents
        List<View> students = List.of(new View());
        when(mockRepo.findAll()).thenReturn(students);

        List<View> result = controller.getAllViews(); // Updated method call
        assertEquals(1, result.size());
    }

    @SuppressWarnings("deprecation")
    @Test
    void testGetViewById_Found() { // Renamed from testGetStudentById_Found
        View student = new View();
        student.setId(1);
        when(mockRepo.findById(1)).thenReturn(Optional.of(student));

        ResponseEntity<View> response = controller.getViewById(1); // Updated method call
        assertEquals(200, response.getStatusCodeValue());
    }

    @SuppressWarnings("deprecation")
    @Test
    void testGetViewById_NotFound() { // Renamed from testGetStudentById_NotFound
        when(mockRepo.findById(999)).thenReturn(Optional.empty());

        ResponseEntity<View> response = controller.getViewById(999); // Updated method call
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testAddView() { // Renamed from testAddStudent
        View student = new View();
        when(mockRepo.save(student)).thenReturn(student);

        ResponseEntity<String> response = controller.addView(student); // Updated method call and return type
        assertEquals(201, response.getStatusCodeValue()); // Controller now returns 201 CREATED
        assertEquals("Student added successfully", response.getBody());
    }

    @Test
    void testUpdateView_Found() { // Renamed from testUpdateStudent_Found
        View existing = new View();
        existing.setId(1);
        View updated = new View();
        updated.setFirstName("Updated");

        when(mockRepo.findById(1)).thenReturn(Optional.of(existing));
        when(mockRepo.save(existing)).thenReturn(existing);

        ResponseEntity<String> response = controller.updateView(1, updated); // Updated method call and return type
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Student updated successfully", response.getBody());
    }

    @Test
    void testUpdateView_NotFound() { // Renamed from testUpdateStudent_NotFound
        when(mockRepo.findById(404)).thenReturn(Optional.empty());

        ResponseEntity<String> response = controller.updateView(404, new View()); // Updated method call and return type
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Student not found", response.getBody());
    }

    @SuppressWarnings("deprecation")
    @Test
    void testDeleteView_Found() { // Renamed from testDeleteStudent_Found
        when(mockRepo.existsById(1)).thenReturn(true);

        ResponseEntity<String> response = controller.deleteView(1); // Updated method call
        assertEquals(200, response.getStatusCodeValue());
    }

    @SuppressWarnings("deprecation")
    @Test
    void testDeleteView_NotFound() { // Renamed from testDeleteStudent_NotFound
        when(mockRepo.existsById(404)).thenReturn(false);

        ResponseEntity<String> response = controller.deleteView(404); // Updated method call
        assertEquals(404, response.getStatusCodeValue());
    }
}
