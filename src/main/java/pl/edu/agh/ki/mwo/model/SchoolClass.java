package pl.edu.agh.ki.mwo.model;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
@Entity
@Table(name="schoolClasses")
public class SchoolClass implements java.io.Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column
	private int startYear;
	
	@Column
	private int currentYear;
	
	@Column
	private String profile;
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="class_id")
	private Set<Student> students;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private School school;
	
	
	public SchoolClass() {
		students = new HashSet<Student>();
	}
	public Set<Student> getStudents() {
		return students;
	}

	public void setStudents(Set<Student> students) {
		this.students = students;
	}

	public void addStudent(Student newStudent) {
		students.add(newStudent);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getStartYear() {
		return startYear;
	}

	public void setStartYear(int startYear) {
		this.startYear = startYear;
	}

	public int getCurrentYear() {
		return currentYear;
	}

	public void setCurrentYear(int currentYear) {
		this.currentYear = currentYear;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	// getter method to retrieve the SchoolClass inf
    
	@JsonIgnore
    public School getSchool() {
        return school;
    }
	
	@JsonIgnore
    public void setSchool(SchoolClass schoolClass) {
        this.school = school;
    }
	
    public Long getSchoolId(){
        return school.getId();
    }
    
    public String getSchoolName(){
        return school.getName();
    }
    
    public String getSchoolAdress(){
        return school.getAddress();
    }
	
	public String toString() {
		return "Class: " + profile + " (Started: " + getStartYear() + ", Current year: " + getCurrentYear() + ")";
	}

	public void removeStudent(Student student) {
		students.remove(student);		
	}
}