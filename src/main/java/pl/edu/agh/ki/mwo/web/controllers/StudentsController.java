package pl.edu.agh.ki.mwo.web.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pl.edu.agh.ki.mwo.model.School;
import pl.edu.agh.ki.mwo.model.SchoolClass;
import pl.edu.agh.ki.mwo.model.Student;
import pl.edu.agh.ki.mwo.persistence.DatabaseConnector;

@Controller
public class StudentsController {

    @RequestMapping(value="/Students")
    public String listStudents(Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";

    	model.addAttribute("students", DatabaseConnector.getInstance().getStudents());
    	
        return "studentsList";    
    }
    
    @RequestMapping(value="/AddStudent")
    public String displayAddStudentForm(Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";
    	
    	Iterable <School> schools = DatabaseConnector.getInstance().getSchools();
    	model.addAttribute("schools", schools);
    	
        return "studentForm";    
    }

    @RequestMapping(value="/CreateStudent", method=RequestMethod.POST)
    public String createStudent(
    		@RequestParam(value="studentName", required=false) String name,
    		@RequestParam(value="schoolClassId", required=false) String schoolClassId,
    		@RequestParam(value="studentSurname", required=false) String surname,
    		@RequestParam(value="studentPesel", required=false) String pesel,
    		Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";
    	
    	Student student = new Student();
    	student.setName(name);
    	student.setSurname(surname);
    	student.setPesel(pesel);

    	DatabaseConnector.getInstance().addOrEditStudent(student, schoolClassId, "add");  
    	model.addAttribute("students", DatabaseConnector.getInstance().getStudents());
    	model.addAttribute("message", "Nowy uczeń został dodany");
         	
    	return "studentsList";
    }
    
    @RequestMapping(value="/DeleteStudent", method=RequestMethod.POST)
    public String deleteStudent(@RequestParam(value="studentId", required=false) String studentId,
    		Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";
    	
    	DatabaseConnector.getInstance().deleteStudent(studentId);    	
       	model.addAttribute("students", DatabaseConnector.getInstance().getStudents());
    	model.addAttribute("message", "Student został usunięty");
         	
    	return "studentsList";
    }

    @RequestMapping(value="/EditStudent", method=RequestMethod.POST)
    public String displayEditStudent(@RequestParam(value="studentId", required=false) String studentId,
    		Model model, HttpSession session) {  	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";
    	
    	SchoolClass schoolClass = DatabaseConnector.getInstance().findCurrentlyStudentLocalisationInClasses(studentId);
    	Iterable <School> schools = DatabaseConnector.getInstance().getSchools();
    	
    	model.addAttribute("currentSchoolClass", DatabaseConnector.getInstance().findCurrentlyStudentLocalisationInClasses(studentId));
    	model.addAttribute("student", DatabaseConnector.getInstance().returnExistingStudent(studentId));
    	model.addAttribute("schools", schools);
         	
    	return "studentForm";
    }
    	
    @RequestMapping(value="/SaveEditableStudent", method=RequestMethod.POST)
    public String editStudent(
    		@RequestParam(value="studentID", required=false) String studentId,
    		@RequestParam(value="studentName", required=false) String name,
    		@RequestParam(value="schoolClassId", required=false) String schoolClassId,
    		@RequestParam(value="studentSurname", required=false) String surname,
    		@RequestParam(value="studentPesel", required=false) String pesel,
    		Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";
    	
    	Student student = DatabaseConnector.getInstance().returnExistingStudent(studentId);

    	student.setName(name);
    	student.setSurname(surname);
    	student.setPesel(pesel);    	
    	
    	DatabaseConnector.getInstance().addOrEditStudent(student, schoolClassId, "edit");
    	    	
       	model.addAttribute("students", DatabaseConnector.getInstance().getStudents());
    	model.addAttribute("message", "Uczeń został poprawnie edytowany");
         	
    	return "studentsList";
    }
}