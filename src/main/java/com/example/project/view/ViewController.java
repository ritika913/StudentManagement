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
import java.util.Optional; // Added for Optional usage

// FIX: Changed base path to /api/views to prevent conflict with StudentController
@RestController
@RequestMapping("/api/views") 
@CrossOrigin(origins = "*")
public class ViewController {

    @Autowired
    private ViewRepository repository;

    // Optional setter for testing, but typically not needed for production code
    public void setRepository(ViewRepository repository) {
        this.repository = repository;
    }

    // GET /api/views - Retrieves all students (as View objects)
    @GetMapping
    public List<View> getAllViews() {
        return repository.findAll();
    } 
    
    // GET /api/views/{id} - Retrieves a single student by ID
    @GetMapping("/{id}")
    public ResponseEntity<View> getViewById(@PathVariable int id) {
        return repository.findById(id)
                .map(view -> ResponseEntity.ok().body(view)) // changed 'student' to 'view' for clarity
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/views - Adds a new student (as View object)
    // FIX: Removed the non-standard "/add-view" path segment
    @PostMapping
    public ResponseEntity<String> addView(@RequestBody View view) {
        repository.save(view);
        return new ResponseEntity<>("Student added successfully", HttpStatus.CREATED);
    }

    // PUT /api/views/{id} - Updates an existing student
    // FIX: Changed @PostMapping to @PutMapping to follow standard REST convention
    @PutMapping("/{id}")
    public ResponseEntity<String> updateView(@PathVariable int id, @RequestBody View updatedView) {
        Optional<View> viewOptional = repository.findById(id);
        
        if (!viewOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found");
        }
        
        View view = viewOptional.get();
        
        // Update fields
        view.setFirstName(updatedView.getFirstName());
        view.setLastName(updatedView.getLastName());
        view.setDateOfBirth(updatedView.getDateOfBirth());
        view.setYearOfRegistration(updatedView.getYearOfRegistration());
        view.setAddressDetails(updatedView.getAddressDetails());
        view.setContactDetails(updatedView.getContactDetails());
        view.setStream(updatedView.getStream());

        repository.save(view);
        return ResponseEntity.ok("Student updated successfully");
    }
    
    // DELETE /api/views/{id} - Deletes a student
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteView(@PathVariable int id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok("Student deleted");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found");
        }
    }
    
    // GET /api/views/export-pdf - PDF Export functionality
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
