package cuidei_api.fiap.cuidei_api.mapper;

import cuidei_api.fiap.cuidei_api.common.swagger.openapi.dto.*;
import cuidei_api.fiap.cuidei_api.entities.Appointment;
import cuidei_api.fiap.cuidei_api.entities.Doctor;
import cuidei_api.fiap.cuidei_api.entities.Nurse;
import cuidei_api.fiap.cuidei_api.entities.Patient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class AppointmentMapper {

    public static AppointmentDtoApi toDto(Appointment a) {
        if (a == null) return null;
        AppointmentDtoApi dto = new AppointmentDtoApi();
        dto.setId(a.getId());
        dto.setDateTime(a.getDateTime());
        dto.setReason(a.getReason());

        if (a.getDoctor() != null) {
            DoctorDtoApi d = new DoctorDtoApi();
            d.setId(a.getDoctor().getId());
            d.setName(a.getDoctor().getName());
            d.setCrm(a.getDoctor().getCrm());
            d.setSpecialty(a.getDoctor().getSpecialty());
            dto.setDoctor(d);
        }

        if (a.getPatient() != null) {
            PatientDtoApi p = new PatientDtoApi();
            p.setId(a.getPatient().getId());
            p.setName(a.getPatient().getName());
            p.setCpf(a.getPatient().getCpf());
            p.setEmail(a.getPatient().getEmail());
            dto.setPatient(p);
        }

        if (a.getNurse() != null) {
            NurseDtoApi n = new NurseDtoApi();
            n.setId(a.getNurse().getId());
            n.setName(a.getNurse().getName());
            n.setRegistrationNumber(a.getNurse().getRegistrationNumber());
            dto.setNurse(n);
        }

        return dto;
    }

    public static List<AppointmentDtoApi> toDtoList(List<Appointment> list) {
        return list.stream().map(AppointmentMapper::toDto).collect(Collectors.toList());
    }

    public static Appointment toEntity(AppointmentDtoApi dto) {

        if (dto == null) return null;

        Appointment a = new Appointment();

        // se vier id, mantém
        if (dto.getId() != null) {
            a.setId(dto.getId());
        }

        // DOCTOR
        if (dto.getDoctor() != null) {
            Doctor d = new Doctor();
            d.setId(dto.getDoctor().getId());
            d.setName(dto.getDoctor().getName());
            d.setCrm(dto.getDoctor().getCrm());
            d.setSpecialty(dto.getDoctor().getSpecialty());
            a.setDoctor(d);
        }

        // PATIENT
        if (dto.getPatient() != null) {
            Patient p = new Patient();
            p.setId(dto.getPatient().getId());
            p.setName(dto.getPatient().getName());
            p.setCpf(dto.getPatient().getCpf());
            p.setEmail(dto.getPatient().getEmail());
            a.setPatient(p);
        }

        // NURSE
        if (dto.getNurse() != null) {
            Nurse n = new Nurse();
            n.setId(dto.getNurse().getId());
            n.setName(dto.getNurse().getName());
            n.setRegistrationNumber(dto.getNurse().getRegistrationNumber());
            a.setNurse(n);
        }

        // DATE TIME
        if (dto.getDateTime() != null) {
            a.setDateTime(dto.getDateTime());
        }

        // REASON
        if (dto.getReason() != null) {
            a.setReason(dto.getReason());
        }

        return a;
    }

    public static Appointment toEntityFromDoctorUpdate(DoctorUpdateAppointmentDtoApi dto) {
        if (dto == null) return null;
        Appointment a = new Appointment();

        if (dto.getPatient() != null) {
            Patient p = new Patient();
            p.setName(dto.getPatient().getName());
            p.setCpf(dto.getPatient().getCpf());
            p.setEmail(dto.getPatient().getEmail());
            a.setPatient(p); // sem id — serviço fará merge com paciente existente
        }

        if (dto.getDateTime() != null) {
            a.setDateTime(LocalDateTime.parse(dto.getDateTime()));
        }

        if (dto.getReason() != null) {
            a.setReason(dto.getReason());
        }

        return a;
    }

}
