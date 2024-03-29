package pl.edu.agh.ki.mwo.web.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pl.edu.agh.ki.mwo.model.School;
import pl.edu.agh.ki.mwo.model.SchoolClass;
import pl.edu.agh.ki.mwo.persistence.DatabaseConnector;

@Controller
public class SchoolClassesController {

    @RequestMapping(value = "/SchoolClasses")
    public String listSchoolClass(Model model, HttpSession session) {
        if (session.getAttribute("userLogin") == null)
            return "redirect:/Login";

        model.addAttribute("schoolClasses", DatabaseConnector.getInstance().getSchoolClasses());

        return "schoolClassesList";
    }

    @RequestMapping(value = "/AddSchoolClass")
    public String displayAddSchoolClassForm(Model model, HttpSession session) {
        if (session.getAttribute("userLogin") == null)
            return "redirect:/Login";

        model.addAttribute("schools", DatabaseConnector.getInstance().getSchools());

        return "schoolClassForm";
    }

    @RequestMapping(value = "/EditSchoolClass", method = RequestMethod.POST)
    public String displayEditSchoolClass(@RequestParam(value = "SchoolClassId", required = false) String schoolClassId,
                                         Model model, HttpSession session) {
        if (session.getAttribute("userLogin") == null)
            return "redirect:/Login";

        Iterable<School> schools = DatabaseConnector.getInstance().getSchools();
        model.addAttribute("currentSchool", DatabaseConnector.getInstance().findCurrentlySchoolClassLocalisationInSchool(schoolClassId));
        model.addAttribute("schoolClass", DatabaseConnector.getInstance().returnSchoolClassById(schoolClassId));
        model.addAttribute("schools", schools);

        return "schoolClassForm";
    }

    @RequestMapping(value = "/CreateSchoolClass", method = RequestMethod.POST)
    public String createSchoolClass(@RequestParam(value = "schoolClassStartYear", required = false) String startYear,
                                    @RequestParam(value = "schoolClassCurrentYear", required = false) String currentYear,
                                    @RequestParam(value = "schoolClassProfile", required = false) String profile,
                                    @RequestParam(value = "schoolClassSchool", required = false) String schoolId,
                                    Model model, HttpSession session) {
        if (session.getAttribute("userLogin") == null)
            return "redirect:/Login";

        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setStartYear(Integer.valueOf(startYear));
        schoolClass.setCurrentYear(Integer.valueOf(currentYear));
        schoolClass.setProfile(profile);

        DatabaseConnector.getInstance().addOrEditSchoolClass(schoolClass, schoolId, "add");
        model.addAttribute("schoolClasses", DatabaseConnector.getInstance().getSchoolClasses());
        model.addAttribute("message", "Nowa klasa została dodana");

        return "schoolClassesList";
    }

    @RequestMapping(value = "/SaveEditableSchoolClass", method = RequestMethod.POST)
    public String editSchoolClass(@RequestParam(value = "schoolClassId", required = false) String schoolClassId,
                                  @RequestParam(value = "schoolClassStartYear", required = false) String startYear,
                                  @RequestParam(value = "schoolClassCurrentYear", required = false) String currentYear,
                                  @RequestParam(value = "schoolClassProfile", required = false) String profile,
                                  @RequestParam(value = "schoolClassSchool", required = false) String schoolId,
                                  Model model, HttpSession session) {
        if (session.getAttribute("userLogin") == null)
            return "redirect:/Login";

        SchoolClass schoolClass = DatabaseConnector.getInstance().returnSchoolClassById(schoolClassId);

        schoolClass.setStartYear(Integer.valueOf(startYear));
        schoolClass.setCurrentYear(Integer.valueOf(currentYear));
        schoolClass.setProfile(profile);

        DatabaseConnector.getInstance().addOrEditSchoolClass(schoolClass, schoolId, "edit");
        model.addAttribute("schoolClasses", DatabaseConnector.getInstance().getSchoolClasses());
        model.addAttribute("message", "Grupa została poprawnie edytowana");

        return "schoolClassesList";
    }

    @RequestMapping(value = "/DeleteSchoolClass", method = RequestMethod.POST)
    public String deleteSchoolClass(@RequestParam(value = "schoolClassId", required = false) String schoolClassId,
                                    Model model, HttpSession session) {
        if (session.getAttribute("userLogin") == null)
            return "redirect:/Login";

        DatabaseConnector.getInstance().deleteSchoolClass(schoolClassId);
        model.addAttribute("schoolClasses", DatabaseConnector.getInstance().getSchoolClasses());
        model.addAttribute("message", "Klasa została usunięta");

        return "schoolClassesList";
    }


}