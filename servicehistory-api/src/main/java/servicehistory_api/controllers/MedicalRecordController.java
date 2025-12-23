package servicehistory_api.controllers;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import servicehistory_api.entities.MedicalRecord;
import servicehistory_api.services.MedicalRecordService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class MedicalRecordController {
    
    private final MedicalRecordService medicalRecordService;
    
    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }
    
    @QueryMapping
    public List<MedicalRecord> patientHistory(@Argument Long patientId) {
        return medicalRecordService.getPatientHistory(patientId);
    }
    
    @QueryMapping
    public MedicalRecord medicalRecord(@Argument Long id) {
        return medicalRecordService.getMedicalRecord(id);
    }
    
    @QueryMapping
    public List<MedicalRecord> doctorRecords(@Argument Long doctorId) {
        return medicalRecordService.getDoctorRecords(doctorId);
    }
    
    @MutationMapping
    public MedicalRecord createMedicalRecord(@Argument MedicalRecordInput input) {
        return medicalRecordService.createMedicalRecord(
            input.getPatientId(),
            input.getDoctorId(),
            input.getAppointmentId(),
            input.getDiagnosis(),
            input.getTreatment(),
            input.getMedications(),
            input.getObservations(),
            input.getFollowUpDate()
        );
    }
    
    @MutationMapping
    public MedicalRecord updateMedicalRecord(@Argument Long id, @Argument MedicalRecordInput input) {
        return medicalRecordService.updateMedicalRecord(id, input);
    }
    
    public static class MedicalRecordInput {
        private Long patientId;
        private Long doctorId;
        private Long appointmentId;
        private String diagnosis;
        private String treatment;
        private String medications;
        private String observations;
        private LocalDateTime followUpDate;
        
        // Getters and setters
        public Long getPatientId() { return patientId; }
        public void setPatientId(Long patientId) { this.patientId = patientId; }
        public Long getDoctorId() { return doctorId; }
        public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
        public Long getAppointmentId() { return appointmentId; }
        public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }
        public String getDiagnosis() { return diagnosis; }
        public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
        public String getTreatment() { return treatment; }
        public void setTreatment(String treatment) { this.treatment = treatment; }
        public String getMedications() { return medications; }
        public void setMedications(String medications) { this.medications = medications; }
        public String getObservations() { return observations; }
        public void setObservations(String observations) { this.observations = observations; }
        public LocalDateTime getFollowUpDate() { return followUpDate; }
        public void setFollowUpDate(LocalDateTime followUpDate) { this.followUpDate = followUpDate; }
    }
}