package HospitalManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
	private Connection connection;
	private Scanner scan;

	public Patient(Connection connection, Scanner scan) {
		this.connection = connection;
		this.scan = scan;
	}
	public void addPatient() {
		System.out.println("Enter The Patient Details:");
		String Name=scan.next();
		System.out.println("Enter patient Age:");
		int age=scan.nextInt();
		System.out.println("Enter patient Gender");
		String Gender=scan.next();
		try {
			String query="INSERT INTO patients(name,age,gender) VALUES(?,?,?)";
			PreparedStatement ps=connection.prepareStatement(query);
			ps.setString(1,Name);
			ps.setInt(2, age);
			ps.setString(3, Gender);
			int affectedrows=ps.executeUpdate();
			if(affectedrows>0) {
				System.out.println("Patient Added");
			}else {
				System.out.println("Failed");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void viewpatient() {
		String query="SELECT * FROM patients";
		try {
			PreparedStatement ps=connection.prepareStatement(query);
			ResultSet rs=ps.executeQuery();
			System.out.println("Patients");
			System.out.println("_____******_____");
			System.out.println("Patients id |Name  |Age |Gender |");
			while(rs.next()) {
				int id=rs.getInt("id");
				String name=rs.getString("name");
				int age=rs.getInt("age");
				String gender=rs.getString("gender");
				System.out.printf("|%-12s|%-8s|%-8s|%-10s|\n",id,name,age,gender);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public boolean getPatientId(int id) {
		String query="SELECT * FROM patients where id=?";
		try {
			PreparedStatement ps=connection.prepareStatement(query);
			ps.setInt(1,id);
			ResultSet r=ps.executeQuery();
			if(r.next())
				return true;
			else
				return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
