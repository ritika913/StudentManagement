package com.example.project.addstudent;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
	@MockBean
    private StudentRepository studentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAddStudent() throws Exception {
        Student student = new Student();
        student.setId(1);
        student.setFirstName("Ritika");
        student.setLastName("Rajak");
        student.setDateOfBirth(LocalDate.of(2001, 6, 15));
        student.setYearOfRegistration(2023);
        student.setAddressDetails("Bhopal");
        student.setContactDetails("9999999999");
        student.setStream("CS");

        Mockito.when(studentRepository.save(Mockito.any(Student.class))).thenReturn(student);
        mockMvc.perform(post("/api/students/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk());

    }
}
