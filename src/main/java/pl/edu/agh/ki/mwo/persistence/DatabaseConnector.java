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

	public void deleteSchoolClass(String schoolClassId) {
		String hql = "FROM SchoolClass S WHERE S.id=" + schoolClassId;
		Query query = session.createQuery(hql);
		List<SchoolClass> results = query.list();
		Transaction transaction = session.beginTransaction();
		for (SchoolClass s : results) {
			if (s.getSchool() != null) {
				s.getSchool().removeSchoolClass(s);
			}
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

	public void addOrEditSchoolClass(SchoolClass schoolClass, String schoolId, String action) {
		String hql = "FROM School S WHERE S.id=" + schoolId;
		Query query = session.createQuery(hql);
		List<School> schools = query.list();

		if (action.equals("edit") && schoolClass.getSchool() != null) {
			schoolClass.getSchool().removeSchoolClass(schoolClass);
			schoolClass.setSchool(null);
		}

		Transaction transaction = session.beginTransaction();
		if (!schools.isEmpty()) {
			School school = schools.get(0);
			school.addClass(schoolClass);
			schoolClass.setSchool(school);
		}

		if (action.equals("edit")) {
			session.update(schoolClass);
		} else {
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
			if(s.getSchoolClass() != null) {
				s.getSchoolClass().removeStudent(s);
			}
			session.delete(s);
		}
		transaction.commit();
	}

	public Student returnStudentById(String studentId) {
		String hql = "FROM Student S WHERE S.id=" + studentId;
		Query query = session.createQuery(hql);
    	List<Student> students = query.list();
    	Student student = students.get(0);
    	return student;
	}

	public SchoolClass returnSchoolClassById(String schoolClassId) {
		String hql = "FROM SchoolClass S WHERE S.id=" + schoolClassId;
		Query query = session.createQuery(hql);
		List<SchoolClass> schoolClasses = query.list();
		return schoolClasses.get(0);
	}

	public List<SchoolClass> returnSchoolClassesList() {
		String hql = "FROM SchoolClass S";
		Query query = session.createQuery(hql);
		List<SchoolClass> schoolClasses = query.list();
    	return schoolClasses;
	}

	public List<School> returnSchoolsList() {
		String hql = "FROM School S";
		Query query = session.createQuery(hql);
		List<School> schools = query.list();
		return schools;
	}

	public SchoolClass findCurrentlyStudentLocalisationInClasses(String studentId) {
		List<SchoolClass> schoolClasses = returnSchoolClassesList();
		for (SchoolClass schoolClass : schoolClasses) {
			for(Student student : schoolClass.getStudents()) {
				if (Long.toString(student.getId()).equals(studentId)) {
					return schoolClass;
				}
			}
		}
		return null;
	}

	public School findCurrentlySchoolClassLocalisationInSchool(String schoolClassId) {
		List<School> schools = returnSchoolsList();
		for (School school : schools) {
            for (SchoolClass schoolClass: school.getClasses()){
				if (Long.toString(schoolClass.getId()).equals(schoolClassId)) {
					return school;
				}
			}
		}
		return null;
	}

	public void addOrEditStudent(Student student, String schoolClassId, String action) {
		String hql = "FROM SchoolClass S WHERE S.id=" + schoolClassId;
		Query query = session.createQuery(hql);
		List<SchoolClass> schoolClasses = query.list();

		if(action.equals("edit") && student.getSchoolClass() != null) {
			student.getSchoolClass().removeStudent(student);
			student.setSchoolClass(null);
		}

		Transaction transaction = session.beginTransaction();
		if (schoolClasses.size() > 0) {
			SchoolClass schoolClass = schoolClasses.get(0);
			schoolClass.addStudent(student);
			student.setSchoolClass(schoolClass);
		}

		if (action.equals("edit")) {
			session.update(student);
		} else {
			session.save(student);
		}
		transaction.commit();
	}

}
