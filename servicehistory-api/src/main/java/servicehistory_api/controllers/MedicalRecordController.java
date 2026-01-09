package servicehistory_api.controllers;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
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
    public List<MedicalRecord> patientHistory(@Argument Long patientId, DataFetchingEnvironment env) {
        String role = env.getGraphQlContext().get("role");
        String username = env.getGraphQlContext().get("username");
        
        if (role == null) {
            throw new RuntimeException("Acesso negado: Token de autenticação obrigatório");
        }
        
        // Médicos e enfermeiros podem ver qualquer histórico
        if ("DOCTOR".equals(role) || "NURSE".equals(role)) {
            return medicalRecordService.getPatientHistory(patientId);
        }
        
        // Pacientes não podem acessar histórico médico diretamente
        if ("PATIENT".equals(role)) {
            throw new RuntimeException("Acesso negado: Pacientes não podem acessar histórico médico");
        }
        
        throw new RuntimeException("Acesso negado: Tipo de usuário não reconhecido");
    }
    
    @QueryMapping
    public MedicalRecord medicalRecord(@Argument Long id, DataFetchingEnvironment env) {
        String role = env.getGraphQlContext().get("role");
        
        if (role == null) {
            throw new RuntimeException("Acesso negado: Token de autenticação obrigatório");
        }
        
        if ("PATIENT".equals(role)) {
            throw new RuntimeException("Acesso negado. Pacientes não podem acessar registros médicos individuais. Use a consulta de histórico do paciente.");
        }
        
        if (!"DOCTOR".equals(role) && !"NURSE".equals(role)) {
            throw new RuntimeException("Acesso negado. Apenas médicos e enfermeiros podem acessar registros médicos.");
        }
        
        return medicalRecordService.getMedicalRecord(id);
    }
    
    @QueryMapping
    public List<MedicalRecord> doctorRecords(@Argument Long doctorId, DataFetchingEnvironment env) {
        String role = env.getGraphQlContext().get("role");
        
        if ("PATIENT".equals(role)) {
            throw new RuntimeException("Pacientes não podem acessar registros de médicos.");
        }
        
        if (!"DOCTOR".equals(role) && !"NURSE".equals(role)) {
            throw new RuntimeException("Acesso negado. Apenas médicos e enfermeiros podem acessar registros de médicos.");
        }
        
        try {
            return medicalRecordService.getDoctorRecords(doctorId);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar registros do médico: " + e.getMessage());
        }
    }
    
    @MutationMapping
    public MedicalRecord createMedicalRecord(@Argument MedicalRecordInput input, DataFetchingEnvironment env) {
        String role = env.getGraphQlContext().get("role");
        
        if (role == null) {
            throw new RuntimeException("Acesso negado: Token de autenticação obrigatório");
        }
        
        if ("PATIENT".equals(role)) {
            throw new RuntimeException("Acesso negado: Pacientes não podem criar registros médicos");
        }
        
        if (!"DOCTOR".equals(role) && !"NURSE".equals(role)) {
            throw new RuntimeException("Acesso negado: Apenas médicos e enfermeiros podem criar registros médicos");
        }
        
        try {
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
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar registro médico: " + e.getMessage());
        }
    }
    
    @MutationMapping
    public MedicalRecord updateMedicalRecord(@Argument Long id, @Argument MedicalRecordInput input, DataFetchingEnvironment env) {
        String role = env.getGraphQlContext().get("role");
        
        if ("PATIENT".equals(role)) {
            throw new RuntimeException("Acesso negado. Pacientes não podem editar registros médicos. Apenas profissionais de saúde podem modificar consultas.");
        }
        
        // Apenas médicos podem editar histórico
        if (!"DOCTOR".equals(role)) {
            throw new RuntimeException("Acesso negado. Apenas médicos podem editar registros médicos.");
        }
        
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