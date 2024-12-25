package Outpatient.example.Intership_Backend.Service;

import Outpatient.example.Intership_Backend.Entity.Patient;
import Outpatient.example.Intership_Backend.Entity.Payment;
import Outpatient.example.Intership_Backend.Repository.AppointmentRepository;
import Outpatient.example.Intership_Backend.Repository.PatientRepository;
import Outpatient.example.Intership_Backend.Repository.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Value("${stripe.secret.key}")
    private String stripeApiKey;

    //change here
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PatientRepository patientRepository; // Assuming you have this to fetch patient by email

    @Autowired
    private AppointmentRepository appointmentRepository; // Assuming you have this to fetch appointment by ID
    //change here

    public String createCheckoutSession(String appointmentId, double chargePerVisit) {
        Stripe.apiKey = stripeApiKey;

        try {
            SessionCreateParams params = SessionCreateParams.builder()
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("http://localhost:8080/appointments/payment-success?appointmentId=" + appointmentId)
                    .setCancelUrl("http://localhost:8080/appointments/payment-cancelled")
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("inr")
                                                    .setUnitAmount((long) (chargePerVisit * 100)) // Use chargePerVisit
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Appointment Booking Fee")
                                                                    .build())
                                                    .build())
                                    .setQuantity(1L)
                                    .build())
                    .build();

            Session session = Session.create(params);
            return session.getUrl();
        } catch (Exception e) {
            throw new RuntimeException("Stripe payment initiation failed", e);
        }
    }
//right code
//    public boolean verifyPayment(String sessionId) {
//        try {
//            Session session = Session.retrieve(sessionId);
//            // Check the payment status of the session (e.g., succeeded)
//            return session.getPaymentStatus().equals("paid");
//        } catch (Exception e) {
//            return false;
//        }
//    }
//right code
public boolean verifyPayment(String sessionId) {
    try {
        Session session = Session.retrieve(sessionId);
        return "paid".equalsIgnoreCase(session.getPaymentStatus());
    } catch (Exception e) {
        return false;
    }
}
//
    public void savePaymentDetails(double amount, String appointmentId, String doctorEmail, String patientEmail) {
        // Fetch the patient by their email
        Patient patient = patientRepository.findByEmail(patientEmail);

        if (patient == null) {
            throw new RuntimeException("Patient not found");
        }

        // Create a new Payment record
        Payment payment = new Payment();
        payment.setAmount(amount); // Set the payment amount
        payment.setAppointmentId(Long.parseLong(appointmentId)); // Set the appointment ID
        payment.setDoctorEmail(doctorEmail); // Set the doctor's email
        payment.setPatient(patient); // Link the patient with the payment

        // Save the payment record in the database
        paymentRepository.save(payment);
    }



}
//till right code

//@Service
//public class PaymentService {
//
//    @Value("${stripe.secret.key}")
//    private String stripeApiKey;
//
//    @Autowired
//    private AppointmentRepository appointmentRepository;
//
//    @Autowired
//    private DoctorRepository doctorRepository;
//
//    @Autowired
//    private PaymentRepository paymentRepository;
//
//    @Autowired
//    private PatientRepository patientRepository;

   // public String createCheckoutSession(String appointmentId, double chargePerVisit) {
//        Stripe.apiKey = stripeApiKey;
//
//        try {
//            SessionCreateParams params = SessionCreateParams.builder()
//                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
//                    .setMode(SessionCreateParams.Mode.PAYMENT)
//                    .setSuccessUrl("http://localhost:8080/appointments/payment-success?appointmentId=" + appointmentId + "&sessionId={CHECKOUT_SESSION_ID}")
//                    .setCancelUrl("http://localhost:8080/appointments/payment-cancelled")
//                    .addLineItem(
//                            SessionCreateParams.LineItem.builder()
//                                    .setPriceData(
//                                            SessionCreateParams.LineItem.PriceData.builder()
//                                                    .setCurrency("inr")
//                                                    .setUnitAmount((long) (chargePerVisit * 100))  // Charge in cents
//                                                    .setProductData(
//                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
//                                                                    .setName("Appointment Booking Fee")
//                                                                    .build())
//                                                    .build())
//                                    .setQuantity(1L)
//                                    .build())
//                    .build();
//
//            // Create session
//            Session session = Session.create(params);
//            return session.getUrl();
//        } catch (Exception e) {
//            throw new RuntimeException("Stripe payment initiation failed", e);
//        }
//    }
//        public String createCheckoutSession(String appointmentId, double chargePerVisit){
//       // public String createCheckoutSession(String appointmentId, double chargePerVisit) {
//            Stripe.apiKey = stripeApiKey;
//
//            try {
//                // Set up Stripe Checkout session parameters
//                SessionCreateParams params = SessionCreateParams.builder()
//                        .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
//                        .setMode(SessionCreateParams.Mode.PAYMENT)
//                        .setSuccessUrl("http://localhost:8080/appointments/payment-success?appointmentId=" + appointmentId + "&sessionId={CHECKOUT_SESSION_ID}")
//                        .setCancelUrl("http://localhost:8080/appointments/payment-cancelled")
//                        .addLineItem(
//                                SessionCreateParams.LineItem.builder()
//                                        .setPriceData(
//                                                SessionCreateParams.LineItem.PriceData.builder()
//                                                        .setCurrency("inr")
//                                                        .setUnitAmount((long) (chargePerVisit * 100))  // Charge in cents
//                                                        .setProductData(
//                                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
//                                                                        .setName("Appointment Booking Fee")
//                                                                        .build())
//                                                        .build())
//                                        .setQuantity(1L)
//                                        .build())
//                        .build();
//
//                // Create Stripe session
//                Session session = Session.create(params);
//
//                // Return the session ID to the frontend (not the URL)
//                return session.getId();  // Return session ID instead of URL
//            } catch (Exception e) {
//                throw new RuntimeException("Stripe payment initiation failed", e);
//            }
//        }
//
//    public boolean verifyPayment(String sessionId) {
//        try {
//            // Retrieve session using session ID
//            Session session = Session.retrieve(sessionId);
//
//            // Check payment status (whether it's paid)
//            return session.getPaymentStatus().equals("paid");
//        } catch (Exception e) {
//            throw new RuntimeException("Payment verification failed", e);
//        }
//    }
//
//        public void savePaymentDetails(double amount, String appointmentId, String doctorEmail, String patientEmail) {
//        // Fetch the patient by their email
//        Patient patient = patientRepository.findByEmail(patientEmail);
//
//        if (patient == null) {
//            throw new RuntimeException("Patient not found");
//        }
//
//        // Create a new Payment record
//        Payment payment = new Payment();
//        payment.setAmount(amount); // Set the payment amount
//        payment.setAppointmentId(Long.parseLong(appointmentId)); // Set the appointment ID
//        payment.setDoctorEmail(doctorEmail); // Set the doctor's email
//        payment.setPatient(patient); // Link the patient with the payment
//
//        // Save the payment record in the database
//        paymentRepository.save(payment);
//    }

//}


