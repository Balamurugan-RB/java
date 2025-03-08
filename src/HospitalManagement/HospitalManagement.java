package HospitalManagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class HospitalManagement {
	private static final String url = "jdbc:mysql://localhost:3306/hospitalm";
	private static final String username = "root";
	private static final String password = "admin";

	public static void main(String []args) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		Scanner scan=new Scanner(System.in);
		try {
			Connection connection=DriverManager.getConnection(url,username,password);
			System.out.println("Connected db");
			Patient patient=new Patient(connection, scan);
			Doctors doctor=new Doctors(connection);
			while(true) {
				System.out.println("Welcome To LARA Hospital Management");
				System.out.println("1,Add patient");
				System.out.println("2,View patient");
				System.out.println("3,View Doctors");
				System.out.println("4,Book Appointment");
				System.out.println("5,Exit");
				System.out.println("Enter Your Choice");
				int choice=scan.nextInt();
				switch(choice) {
				case 1:patient.addPatient();
				break;
				case 2:patient.viewpatient();
				break;
				case 3:doctor.viewDoctor();
				break;
				case 4:bookappointment(patient, doctor, connection, scan);
				System.err.println();
				break;
				case 5:return;
				default:
					System.out.println("Invalid request");
					break;
				}
			}
		}
				catch (Exception e) {
					e.printStackTrace();
				}
			
	}

	public static void bookappointment(Patient patient,Doctors doctors,Connection connection,Scanner scan) {
		
		System.out.println("Enter The Patient Id:");
		int patientId=scan.nextInt();
		System.out.println("Enter The Patient Name:");
		String patientname=scan.next();
		System.out.println("Enter the Doctor Id:");
		int doctorid=scan.nextInt();
		System.out.println("Enter The Appointment Date(YYYY-MM-DD");
		String appointmentdate=scan.next();
		if(patient.getPatientId(patientId)&&doctors.getDoctorId(doctorid))
		{
			if(checkDoctorAvailability(doctorid,appointmentdate,connection)) {
				String appointmentquery="INSERT INTO appointments(patient_id,patient_name,appointment_date(?,?,?,?)";
				try {
					PreparedStatement p=connection.prepareStatement(appointmentquery);
					p.setInt(1, patientId);
					p.setString(2, patientname);
					p.setInt(3, doctorid);
					p.setString(4, appointmentdate);
					int rowsaffected=p.executeUpdate();
					if(rowsaffected>0) {
						System.out.println("Application Booked");
					}else {
						System.out.println("Failed to Book appointment");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static boolean checkDoctorAvailability(int doctorid,String appointmentdate,Connection connection) {
		try {
		String query="SELECT  COUNT(*) FROM appointments WHERE doctor id=? AND appoinment_date=?";
		PreparedStatement p=connection.prepareStatement(query);
		p.setInt(1, doctorid);
		p.setString(2, appointmentdate);
		ResultSet r=p.executeQuery();
		if(r.next()) {
			int count=r.getInt(1);
			if(count==0) {
				return true;
			}
			else {
				return false;
			}
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
