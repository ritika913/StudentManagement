package com.example.project.view;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class ViewController {

	public void setRepository(ViewRepository repository) {
	    this.repository = repository;
	}

    @Autowired
    private ViewRepository repository;

    @GetMapping
    public List<View> getAllStudents() {
        return repository.findAll();
    } 
    
    @GetMapping("/{id}")
    public ResponseEntity<View> getStudentById(@PathVariable int id) {
        return repository.findById(id)
                .map(student -> ResponseEntity.ok().body(student))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add-view")
    public String addStudent(@RequestBody View student) {
        repository.save(student);
        return "Student added successfully";
    }

    @PostMapping("/edit/{id}")
    public String updateStudent(@PathVariable int id, @RequestBody View updatedStudent) {
        View student = repository.findById(id).orElse(null);
        if (student == null) {
            return "Student not found";
        }
        
        student.setFirstName(updatedStudent.getFirstName());
        student.setLastName(updatedStudent.getLastName());
        student.setDateOfBirth(updatedStudent.getDateOfBirth());
        student.setYearOfRegistration(updatedStudent.getYearOfRegistration());
        student.setAddressDetails(updatedStudent.getAddressDetails());
        student.setContactDetails(updatedStudent.getContactDetails());
        student.setStream(updatedStudent.getStream());

        repository.save(student);
        return "Student updated successfully";
    }
    
    
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable int id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok("Student deleted");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found");
        }
    }
    
    @GetMapping("/export-pdf")
    public ResponseEntity<byte[]> exportToPDF() {
        try {
            List<View> students = repository.findAll();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            document.add(new Paragraph("Student Information").setBold().setFontSize(16).setMarginBottom(10));

            // Define column headers
            float[] columnWidths = {30F, 70F, 70F, 70F, 40F, 90F, 70F, 60F};
            Table table = new Table(columnWidths);
            table.addHeaderCell("ID");
            table.addHeaderCell("First Name");
            table.addHeaderCell("Last Name");
            table.addHeaderCell("DOB");
            table.addHeaderCell("Year");
            table.addHeaderCell("Address");
            table.addHeaderCell("Contact");
            table.addHeaderCell("Stream");

            // Add student data to table
            for (View student : students) {
                table.addCell(String.valueOf(student.getId()));
                table.addCell(student.getFirstName());
                table.addCell(student.getLastName());
                table.addCell(String.valueOf(student.getDateOfBirth()));
                table.addCell(String.valueOf(student.getYearOfRegistration()));
                table.addCell(student.getAddressDetails());
                table.addCell(student.getContactDetails());
                table.addCell(student.getStream());
            }

            document.add(table);
            document.close();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "students.pdf");

            return new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}