package pl.edu.agh.ki.mwo.persistence;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import pl.edu.agh.ki.mwo.model.School;
import pl.edu.agh.ki.mwo.model.SchoolClass;
import pl.edu.agh.ki.mwo.model.Student;

public class DatabaseConnector {
	
	protected static DatabaseConnector instance = null;
	
	public static DatabaseConnector getInstance() {
		if (instance == null) {
			instance = new DatabaseConnector();
		}
		return instance;
	}
	
	Session session;

	protected DatabaseConnector() {
		session = HibernateUtil.getSessionFactory().openSession();
	}
	
	public void teardown() {
		session.close();
		HibernateUtil.shutdown();
		instance = null;
	}
	
	public Iterable<School> getSchools() {
		String hql = "FROM School";
		Query query = session.createQuery(hql);
		List schools = query.list();
		
		return schools;
	}
	
	
	public void addSchool(School school) {
		Transaction transaction = session.beginTransaction();
		session.save(school);
		transaction.commit();
	}
	
	public void deleteSchool(String schoolId) {
		String hql = "FROM School S WHERE S.id=" + schoolId;
		Query query = session.createQuery(hql);
		List<School> results = query.list();
		Transaction transaction = session.beginTransaction();
		for (School s : results) {
			session.delete(s);
		}
		transaction.commit();
	}

	public Iterable<SchoolClass> getSchoolClasses() {
		String hql = "FROM SchoolClass";
		Query query = session.createQuery(hql);
		List schoolClasses = query.list();
		
		return schoolClasses;
	}
	
	public void addSchoolClass(SchoolClass schoolClass, String schoolId) {
		String hql = "FROM School S WHERE S.id=" + schoolId;
		Query query = session.createQuery(hql);
		List<School> results = query.list();
		Transaction transaction = session.beginTransaction();
		if (results.size() == 0) {
			session.save(schoolClass);
		} else {
			School school = results.get(0);
			school.addClass(schoolClass);
			session.save(school);
		}
		transaction.commit();
	}
	
	public void deleteSchoolClass(String schoolClassId) {
		String hql = "FROM SchoolClass S WHERE S.id=" + schoolClassId;
		Query query = session.createQuery(hql);
		List<SchoolClass> results = query.list();
		Transaction transaction = session.beginTransaction();
		for (SchoolClass s : results) {
			session.delete(s);
		}
		transaction.commit();
	}

	public Iterable<Student> getStudents() {
		String hql = "FROM Student";
		Query query = session.createQuery(hql);
		List students = query.list();
		
		return students;
	}
	
	public void addStudent(Student student, String schoolClassId) {
		System.out.println("Jestem w addStudent");
		String hql = "FROM SchoolClass S WHERE S.id=" + schoolClassId;
		Query query = session.createQuery(hql);
		List<SchoolClass> schoolClasses = query.list();
		Transaction transaction = session.beginTransaction();
		if (schoolClasses.size() == 0) {
			session.save(student);
		} else {
			SchoolClass schoolClass = schoolClasses.get(0);
			schoolClass.addStudent(student);
			session.save(schoolClass);
		}
		transaction.commit();
	}
	

	public void deleteStudent(String studentId) {
		String hql = "FROM Student S WHERE S.id=" + studentId;
		Query query = session.createQuery(hql);
		List<Student> results = query.list();
		Transaction transaction = session.beginTransaction();
		for (Student s : results) {
			session.delete(s);
		}
		transaction.commit();
	}
	
	public Student returnExistingStudent(String studentId) {
		String hql = "FROM Student S WHERE S.id=" + studentId;
		Query query = session.createQuery(hql);
    	List<Student> students = query.list();
    	Student student = students.get(0);
    	return student;
	}
	
	public List<SchoolClass> returnSchoolClassesList() {
		String hql = "FROM SchoolClass S";
		Query query = session.createQuery(hql);
		List<SchoolClass> schoolClasses = query.list();
    	return schoolClasses;
	}
	
	public SchoolClass findCurrentlyStudentLocalisationInClasses(String studentId) {
		List<SchoolClass> schoolClasses = returnSchoolClassesList();
		for (SchoolClass schoolClass : schoolClasses) {
			for(Student student : schoolClass.getStudents()) {
				System.out.println("Czy " + student.getId() + " == " + studentId);
				if (Long.toString(student.getId()).equals(studentId)) {
					return schoolClass;
				}
			}
		}
		return null;
	}
	
	
	public void assignExistingStudentToTheClass(Student student, String schoolClassId) {
		String hql = "FROM SchoolClass S WHERE S.id=" + schoolClassId;
		Query query = session.createQuery(hql);
		List<SchoolClass> schoolClasses = query.list();
		Transaction transaction = session.beginTransaction();
		if (schoolClasses.size() == 0) {
			session.update(student);
		} else {
			SchoolClass schoolClass = schoolClasses.get(0);
			schoolClass.addStudent(student);
			session.update(schoolClass);
		}
		transaction.commit();
	}
	
	public void editStudent(Student student, String studentId, String schoolClassId) {
				
		session.update(student);		
		
		System.out.println(schoolClassId);
		Transaction transaction = session.beginTransaction();		
		assignExistingStudentToTheClass(student, schoolClassId);
		
	}
	
	
}
