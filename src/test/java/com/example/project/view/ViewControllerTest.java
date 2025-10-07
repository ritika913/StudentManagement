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
    void testGetAllStudents() {
        List<View> students = List.of(new View());
        when(mockRepo.findAll()).thenReturn(students);

        List<View> result = controller.getAllStudents();
        assertEquals(1, result.size());
    }

    @SuppressWarnings("deprecation")
	@Test
    void testGetStudentById_Found() {
        View student = new View();
        student.setId(1);
        when(mockRepo.findById(1)).thenReturn(Optional.of(student));

        ResponseEntity<View> response = controller.getStudentById(1);
        assertEquals(200, response.getStatusCodeValue());
    }

    @SuppressWarnings("deprecation")
	@Test
    void testGetStudentById_NotFound() {
        when(mockRepo.findById(999)).thenReturn(Optional.empty());

        ResponseEntity<View> response = controller.getStudentById(999);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testAddStudent() {
        View student = new View();
        when(mockRepo.save(student)).thenReturn(student);

        String result = controller.addStudent(student);
        assertEquals("Student added successfully", result);
    }

    @Test
    void testUpdateStudent_Found() {
        View existing = new View();
        existing.setId(1);
        View updated = new View();
        updated.setFirstName("Updated");

        when(mockRepo.findById(1)).thenReturn(Optional.of(existing));
        when(mockRepo.save(existing)).thenReturn(existing);

        String result = controller.updateStudent(1, updated);
        assertEquals("Student updated successfully", result);
    }

    @Test
    void testUpdateStudent_NotFound() {
        when(mockRepo.findById(404)).thenReturn(Optional.empty());

        String result = controller.updateStudent(404, new View());
        assertEquals("Student not found", result);
    }

    @SuppressWarnings("deprecation")
	@Test
    void testDeleteStudent_Found() {
        when(mockRepo.existsById(1)).thenReturn(true);

        ResponseEntity<String> response = controller.deleteStudent(1);
        assertEquals(200, response.getStatusCodeValue());
    }

    @SuppressWarnings("deprecation")
	@Test
    void testDeleteStudent_NotFound() {
        when(mockRepo.existsById(404)).thenReturn(false);

        ResponseEntity<String> response = controller.deleteStudent(404);
        assertEquals(404, response.getStatusCodeValue());
    }
}
