package Outpatient.example.Intership_Backend.Entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Prescription
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    private String patientEmail;

    @ManyToOne
    @JoinColumn(name = "doctor_email")
    private Doctor doctor;


    private String advice;

    private String reason;


}
