package com.student.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.student.dao.StudentDao;
import com.student.entity.StudentEntity;
import com.student.utils.DBConnectionUtil;

public class StudentDaoImpl implements StudentDao{

	@Override
	public List<StudentEntity> findAllStudents() {
		List<StudentEntity> allStudents = new ArrayList<StudentEntity>();
		
		try {
			Connection con = DBConnectionUtil.getConnection();
			PreparedStatement stmt = con.prepareStatement("select * from student");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				StudentEntity student = new StudentEntity(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),rs.getInt(10), rs.getLong(11), rs.getString(12), rs.getString(13));
				allStudents.add(student);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return allStudents;
	}

	@Override
	public StudentEntity findStudentById(int studentId) {
		StudentEntity student = null;
		try {
			Connection con = DBConnectionUtil.getConnection();
			PreparedStatement stmt = con.prepareStatement("select * from student where student_id=?");
			stmt.setInt(1, studentId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				 student = new StudentEntity(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),rs.getInt(10), rs.getLong(11), rs.getString(12), rs.getString(13));
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	 
		return student;
	}

	@Override
	public StudentEntity findLoginUser(String emailId, String password) {
		StudentEntity student = null;
		try {
			Connection con = DBConnectionUtil.getConnection();
			PreparedStatement stmt = con.prepareStatement("select * from student where email_id=? and password=?");
			stmt.setString(1, emailId);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				 student = new StudentEntity(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),rs.getInt(10), rs.getLong(11), rs.getString(12), rs.getString(13));
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	 
		return student;
	}

}
