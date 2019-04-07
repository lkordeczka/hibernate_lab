package pl.edu.agh.ki.mwo.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Table(name="students")
public class Student implements java.io.Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column
	private String name;
	
	@Column
	private String surname;
	
	@Column
	private String pesel;
	
	@Column
//	private String classId;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public Student() {		
	}
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private SchoolClass schoolClass;

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPesel() {
		return pesel;
	}

	public void setPesel(String pesel) {
		this.pesel = pesel;
	}
	
// getter method to retrieve the SchoolClass inf
    
	@JsonIgnore
    public SchoolClass getSchoolClass() {
        return schoolClass;
    }
	
	@JsonIgnore
    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }
	
    public Long getSchoolClassId(){
        return schoolClass.getId();
    }
    
    public String getSchoolClassProfile(){
        return schoolClass.getProfile();
    }



}
