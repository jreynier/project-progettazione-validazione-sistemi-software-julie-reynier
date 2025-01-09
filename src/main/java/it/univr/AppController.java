package it.univr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;

import java.time.YearMonth;
import java.util.*;

@Controller
public class AppController {

    @Autowired
    private ResearcherRepository researcherRepository;
    @Autowired
    private  ProjectRepository projectRepository;
    @Autowired
    private HoursRepository hourRepository;

    @RequestMapping("/")
    public String index(){
        return "login";
    }



    @RequestMapping("/login")
    public String handleLogin(@RequestParam("email") String email,
                              @RequestParam("password") String password) {
        // Check if a researcher exists with the given username
        Researcher researcher = researcherRepository.findByEmail(email);

        if (researcher!= null && researcher.getPassword().equals(password)) {
            // Redirect to the researcher's page
            return "redirect:/researcher?id=" + researcher.getId();
        } else {
            // reload login page
            return "login";
        }
    }

    @RequestMapping("/researcher")
    public String researcherPage(
            @RequestParam(name="id") Long id,
            Model model) {
        Optional<Researcher> researcher = researcherRepository.findById(id);

        if (researcher.isPresent()) {
            model.addAttribute("researcher", researcher.get());
            model.addAttribute("projectsAsPI", researcher.get().getProjectsAsPI());
            model.addAttribute("projects", researcher.get().getProjects());
            return "researcher";
        } else {
            model.addAttribute("error", "Researcher not found.");
            return "_error";
        }
    }

    // Manage Project Page
    @RequestMapping("/project")
    public String projectPage(
            @RequestParam(name="id") Long id,
            Model model) {
        Optional<Project> result = projectRepository.findById(id);
        if (result.isPresent()){
            Project project = result.get();
            List<Researcher> researchers = project.getResearchers(); // researchers that work on this project

            // Map each researcher to their unapproved hours for this project
            Map<Researcher, List<Hours>> hoursMap = new HashMap<>();
            for (Researcher researcher : researchers) {
                List<Hours> hoursList = new ArrayList<>();
                for (Hours hour : researcher.getHours()){
                    if (!hour.isApproved() && hour.getProject().getId().equals(id)){
                        hoursList.add(hour);
                    }
                }
                hoursMap.put(researcher, hoursList);
            }
            model.addAttribute("project", result.get());
            model.addAttribute("researcher", result.get().getProjectInvestigator());
            model.addAttribute("hoursByResearcher", hoursMap);
            return "project";
        }
        else{
            model.addAttribute("error", "Project not found.");
            return "_error";
        }
    }

    // Set the hour to approve
    @RequestMapping ("/approve")
    public String approve(
            @RequestParam (name="id") Long id,
            Model model) {
        Optional<Hours> result = hourRepository.findById(id);
        if(result.isPresent()){
            result.get().setApproved(true);
            hourRepository.save(result.get());
            model.addAttribute("project", result.get().getProject());
            return "redirect:/project?id=" + result.get().getProject().getId();
        } else {
            model.addAttribute("error", "Hour not found.");
            return "_error";
        }
    }

    // Delete the hour from the repository, the hours of the researcher and the hours of the project
    @RequestMapping ("/reject")
    public String reject(
            @RequestParam (name="id") Long id,
            Model model) {
        Optional<Hours> result = hourRepository.findById(id);
        if(result.isPresent()){
            result.get().getResearcher().getHours().remove(result.get());
            result.get().getProject().getHours().remove(result.get());
            hourRepository.delete(result.get());
            return "redirect:/project?id=" + result.get().getProject().getId();
        } else {
            model.addAttribute("error", "Hour not found.");
            return "_error";
        }
    }



    @RequestMapping("/add-hours")
    public String addHoursPage(
            @RequestParam(name="rid") Long rid,
            @RequestParam(name="pid") Long pid, Model model) {
        Optional<Project> project = projectRepository.findById(pid);
        Optional<Researcher> researcher = researcherRepository.findById(rid);
        if (project.isPresent() && researcher.isPresent()) {
            model.addAttribute("project", project.get());
            model.addAttribute("researcher", researcher.get());
            return "add-hours";
        }
        else {
            model.addAttribute("error", "Project or researcher not found.");
            return "_error";
        }
    }

    @RequestMapping("/request-hours")
    public String requestHours(
            @RequestParam(name="rid") Long rid,
            @RequestParam(name="pid") Long pid,
            @RequestParam(name="day") LocalDate date,
            @RequestParam(name="hours") int hours,
            Model model) {
        Optional<Project> project = projectRepository.findById(pid);
        Optional<Researcher> researcher = researcherRepository.findById(rid);
        if (project.isEmpty() || researcher.isEmpty()) {
            model.addAttribute("error", "Project or researcher not found.");
            return "_error";
        }
        // Create the new hour :
        Hours h = new Hours();
        h.setHoursWorked(hours);
        h.setDate(date);
        h.setApproved(false);
        h.setProject(project.get());
        h.setResearcher(researcher.get());
        // Save :
        hourRepository.save(h);
        project.get().addHours(h);
        researcher.get().addHours(h);
        return "redirect:/researcher?id="+rid;
    }

    public int getDaysInMonth(int year, int month){
        YearMonth yearMonth = YearMonth.of(year, month);
        return yearMonth.lengthOfMonth();
    }

    @RequestMapping("/generate-report")
    public String reportPage(
            @RequestParam(name = "id") Long id,
            @RequestParam(name = "month") int month,
            @RequestParam(name = "year") int year,
            Model model
    ){
        model.addAttribute("month",month);
        model.addAttribute("year",year);
        Optional<Researcher> result = researcherRepository.findById(id);
        if(result.isPresent()){
            Researcher researcher = result.get();
            model.addAttribute("researcher",researcher);

            int daysInMonth = getDaysInMonth(year, month);

            // Create the map of the projects associated to a list of hours worked for each day of the month
            Map<Project, List<Integer>> projectHoursMap = new HashMap<>();

            List<Integer> days = new ArrayList<>();          // list of the days in the mont (1..31)
            List<Integer> totalPerDay = new ArrayList<>();   // total of hours worked for each day
            for (int i = 0; i < daysInMonth; i++) {
                days.add(i+1);
                totalPerDay.add(0);
            }
            totalPerDay.add(0); // the grand total
            // Filter the hours of the researcher for the month and year required
            for (Hours hour : researcher.getHours()){
                if(hour.getDate().getMonthValue()==month && hour.getDate().getYear()==year){
                    if (!hour.isApproved() || researcher.isDayOff(hour.getDate())){
                        continue;
                    }
                    LocalDate date = hour.getDate();
                    Project project = hour.getProject();
                    if(!projectHoursMap.containsKey(project)){      // if the project is not in the Map :
                        List<Integer> listHours = new ArrayList<>(); // we create a new list that will contain th number of hours worked for each day
                        for (int i = 0; i<=daysInMonth; i++) { // last element will be the total for the month
                            listHours.add(0);
                        }
                        projectHoursMap.put(project, listHours);
                    }
                    // Add the hours worked :
                    projectHoursMap.get(project).set(date.getDayOfMonth()-1,projectHoursMap.get(project).get(date.getDayOfMonth()-1)+hour.getHoursWorked());
                    projectHoursMap.get(project).set(daysInMonth,projectHoursMap.get(project).get(daysInMonth)+hour.getHoursWorked());  // the total for the project
                    totalPerDay.set(date.getDayOfMonth()-1, totalPerDay.get(date.getDayOfMonth()-1)+hour.getHoursWorked());             // the total for the day
                    totalPerDay.set(daysInMonth, totalPerDay.get(daysInMonth)+hour.getHoursWorked());                                   //grand total
                    for (LocalDate dayOff : researcher.getDaysOff()){
                        if (dayOff.getYear()==year && dayOff.getMonthValue()==month){
                            projectHoursMap.get(project).set(date.getDayOfMonth()-1, -1);
                        }
                    }
                }
            }
            for (LocalDate dayOff : researcher.getDaysOff()){
                if (dayOff.getYear()==year && dayOff.getMonthValue()==month){
                    totalPerDay.set(dayOff.getDayOfMonth()-1, -1);
                }
            }

            model.addAttribute("projectHoursMap", projectHoursMap);
            model.addAttribute("days", days);
            model.addAttribute("totalePerDay", totalPerDay);
            return "report";

        }
        model.addAttribute("error", "Researcher not found.");
        return "_error";
    }

    @RequestMapping("/generate-report-project")
    public String projectReportPage(
            @RequestParam(name = "rid") Long rid,
            @RequestParam(name = "pid") Long pid,
            @RequestParam(name = "month") int month,
            @RequestParam(name = "year") int year,
            Model model
    ){
        model.addAttribute("month",month);
        model.addAttribute("year",year);
        Optional<Researcher> researcher = researcherRepository.findById(rid);
        Optional<Project> project = projectRepository.findById(pid);
        if (researcher.isEmpty() || project.isEmpty()) {
            model.addAttribute("error", "Project or researcher not found.");
            return "_error";
        }
        model.addAttribute("project", project.get());
        model.addAttribute("researcher", researcher.get());

        int daysInMonth = getDaysInMonth(year, month);

        // declarations of arrays :
        List<Integer> days = new ArrayList<>();
        List<Integer> hoursWorkedOnProject = new ArrayList<>();
        List<Integer> hoursWorkedOnProjectSameAgency = new ArrayList<>();
        List<Integer> hoursWorkedOnOther= new ArrayList<>();
        List<Integer> totalWorkedDay = new ArrayList<>();
        for (int i = 0; i < daysInMonth; i++) {
            days.add(i+1);
            hoursWorkedOnProject.add(0);
            hoursWorkedOnProjectSameAgency.add(0);
            hoursWorkedOnOther.add(0);
            totalWorkedDay.add(0);
        }

        // last element will contain the totals :
        hoursWorkedOnOther.add(0);
        hoursWorkedOnProjectSameAgency.add(0);
        hoursWorkedOnProject.add(0);
        totalWorkedDay.add(0);

        for (Hours hour : researcher.get().getHours()) {
            if (!hour.isApproved()|| researcher.get().isDayOff(hour.getDate())){
                continue;
            }
            if(hour.getProject().getId().equals(pid)){
                hoursWorkedOnProject.set(hour.getDate().getDayOfMonth()-1,hoursWorkedOnProject.get(hour.getDate().getDayOfMonth()-1)+hour.getHoursWorked());
                hoursWorkedOnProject.set(daysInMonth,hoursWorkedOnProject.get(daysInMonth)+hour.getHoursWorked());
            } else if(hour.getProject().getFundingAgency().equals(project.get().getFundingAgency())){
                hoursWorkedOnProjectSameAgency.set(hour.getDate().getDayOfMonth()-1,hoursWorkedOnProjectSameAgency.get(hour.getDate().getDayOfMonth()-1)+hour.getHoursWorked());
                hoursWorkedOnProjectSameAgency.set(daysInMonth,hoursWorkedOnProjectSameAgency.get(daysInMonth)+hour.getHoursWorked());
            } else {
                hoursWorkedOnOther.set(hour.getDate().getDayOfMonth()-1,hoursWorkedOnOther.get(hour.getDate().getDayOfMonth()-1)+hour.getHoursWorked());
                hoursWorkedOnOther.set(daysInMonth,hoursWorkedOnOther.get(daysInMonth)+hour.getHoursWorked());
            }
            totalWorkedDay.set(hour.getDate().getDayOfMonth()-1, totalWorkedDay.get(hour.getDate().getDayOfMonth()-1)+hour.getHoursWorked());
            totalWorkedDay.set(daysInMonth, totalWorkedDay.get(daysInMonth)+hour.getHoursWorked());
        }
        for (LocalDate date : researcher.get().getDaysOff()){
            if (date.getYear()==year && date.getMonthValue()==month){
                hoursWorkedOnProject.set(date.getDayOfMonth()-1, -1);
                hoursWorkedOnProjectSameAgency.set(date.getDayOfMonth()-1, -1);
                hoursWorkedOnOther.set(date.getDayOfMonth()-1, -1);
                totalWorkedDay.set(date.getDayOfMonth()-1, -1);
            }
        }
        model.addAttribute("hoursWorkedOnProject", hoursWorkedOnProject);
        model.addAttribute("hoursWorkedOnOther", hoursWorkedOnOther);
        model.addAttribute("hoursWorkedOnProjectSameAgency", hoursWorkedOnProjectSameAgency);
        model.addAttribute("days", days);
        model.addAttribute("totalPerDay", totalWorkedDay);
        model.addAttribute("daysInMonth", daysInMonth);
        return "report-project";
    }

    @RequestMapping("request-day-off")
    public String dayOff(
            @RequestParam(name = "id") Long id,
            @RequestParam(name = "day") LocalDate date,
            Model model
    ){
        Optional<Researcher> result = researcherRepository.findById(id);
        if(result.isPresent()){
            result.get().addDaysOff(date);
            researcherRepository.save(result.get());
            model.addAttribute("researcher", result.get());
            return "/researcher";
        } else {
            model.addAttribute("error", "Researcher not found.");
            return "_error";
        }
    }
}