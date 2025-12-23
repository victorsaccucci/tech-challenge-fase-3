package servicehistory_api.services;

import org.springframework.stereotype.Service;
import servicehistory_api.controllers.MedicalRecordController;
import servicehistory_api.entities.MedicalRecord;
import servicehistory_api.repositories.MedicalRecordRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MedicalRecordService {
    
    private final MedicalRecordRepository medicalRecordRepository;
    
    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }
    
    public List<MedicalRecord> getPatientHistory(Long patientId) {
        return medicalRecordRepository.findByPatientIdOrderByRecordDateDesc(patientId);
    }
    
    public MedicalRecord getMedicalRecord(Long id) {
        return medicalRecordRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Registro médico não encontrado"));
    }
    
    public List<MedicalRecord> getDoctorRecords(Long doctorId) {
        return medicalRecordRepository.findByDoctorId(doctorId);
    }
    
    public MedicalRecord createMedicalRecord(Long patientId, Long doctorId, Long appointmentId,
                                           String diagnosis, String treatment, String medications,
                                           String observations, LocalDateTime followUpDate) {
        MedicalRecord record = new MedicalRecord();
        record.setPatientId(patientId);
        record.setDoctorId(doctorId);
        record.setAppointmentId(appointmentId);
        record.setDiagnosis(diagnosis);
        record.setTreatment(treatment);
        record.setMedications(medications);
        record.setObservations(observations);
        record.setFollowUpDate(followUpDate);
        
        return medicalRecordRepository.save(record);
    }
    
    public MedicalRecord updateMedicalRecord(Long id, MedicalRecordController.MedicalRecordInput input) {
        MedicalRecord record = getMedicalRecord(id);
        
        if (input.getDiagnosis() != null) record.setDiagnosis(input.getDiagnosis());
        if (input.getTreatment() != null) record.setTreatment(input.getTreatment());
        if (input.getMedications() != null) record.setMedications(input.getMedications());
        if (input.getObservations() != null) record.setObservations(input.getObservations());
        if (input.getFollowUpDate() != null) record.setFollowUpDate(input.getFollowUpDate());
        
        return medicalRecordRepository.save(record);
    }
}