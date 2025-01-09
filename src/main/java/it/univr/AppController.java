package it.univr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class AppController {

    @Autowired
    private ResearcherRepository repository;
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
                              @RequestParam("password") String password,
                              Model model) {
        // Check if a researcher exists with the given username
        Researcher researcher = repository.findByEmail(email);

        if (researcher!= null && researcher.getPassword().equals(password)) {
            // Redirect to the researcher's page
            return "redirect:/researcher?id=" + researcher.getId();
        } else {
            // Show error message and reload login page
            model.addAttribute("error", "Invalid username or password.");
            return "login";
        }
    }

    @RequestMapping("/researcher")
    public String researcherPage(
            @RequestParam(name="id", required = true) Long id,
            Model model) {
        Optional<Researcher> researcher = repository.findById(id);

        if (researcher.isPresent()) {
            model.addAttribute("researcher", researcher.get());
            model.addAttribute("projectsAsPI", researcher.get().getProjectsAsPI());
            model.addAttribute("projects", researcher.get().getProjects());
            return "researcher"; // researcher.html template
        } else {
            model.addAttribute("error", "Researcher not found.");
            return "_error"; // error.html template
        }
    }

    @RequestMapping("/project")
    public String projectPage(
            @RequestParam(name="id", required=true) Long id,
            Model model) {
        Optional<Project> result = projectRepository.findById(id);
        if (result.isPresent()){
            Project project = result.get();
            List<Researcher> researchers = project.getResearchers();
            // Map each researcher to their unapproved hours for this project
            Map<Researcher, List<Hours>> hoursMap = new HashMap<>();
            for (Researcher researcher : researchers) {
               List<Hours> hoursList = researcher.getHours().stream()
                    .filter(h -> h.getProject().equals(project) && !h.isApproved())
                    .toList();
               hoursMap.put(researcher, hoursList);
            }
            model.addAttribute("project", result.get());
            model.addAttribute("researcher", result.get().getProjectInvestigator());
            model.addAttribute("hoursByResearcher", hoursMap);
            return "project";
        }
        else
            return "_error";
    }

    @RequestMapping ("/approve")
    public String approve(
            @RequestParam (name="id", required = true) Long id,
            Model model) {
        Optional<Hours> result = hourRepository.findById(id);
        if(result.isPresent()){
            result.get().setApproved(true);
            hourRepository.save(result.get());
            model.addAttribute("project", result.get().getProject());
            return "redirect:/project?id=" + result.get().getProject().getId();
        } else {
            return "_error";
        }
    }

    @RequestMapping ("/reject")
    public String reject(
            @RequestParam (name="id", required = true) Long id,
            Model model) {
        Optional<Hours> result = hourRepository.findById(id);
        if(result.isPresent()){
            hourRepository.delete(result.get());
            return "redirect:/project?id=" + result.get().getProject().getId();
        } else {
            return "_error";
        }
    }



    @RequestMapping("/addhours")
    public String addHoursPage(
            @RequestParam(name="rid", required=true) Long rid,
            @RequestParam(name="pid", required=true) Long pid, Model model) {
        Optional<Project> result = projectRepository.findById(pid);
        Optional<Researcher> result2 = repository.findById(rid);
        if (result.isPresent() && result2.isPresent()) {
            model.addAttribute("project", result.get());
            model.addAttribute("researcher", result2.get());
            return "addhours";
        }
        else
            return "_error";
    }

    @RequestMapping("/requesthours")
    public String requestHours(
            @RequestParam(name="rid", required=true) Long rid,
            @RequestParam(name="pid", required=true) Long pid,
            @RequestParam(name="day", required=true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(name="hours", required=true) int hours,
            Model model) {
        Hours h = new Hours();
        h.setHourWorked(hours);
        h.setDate(date);
        h.setApproved(false);
        h.setProject(projectRepository.findById(pid).get());
        h.setResearcher(repository.findById(rid).get());
        hourRepository.save(h);
        projectRepository.findById(pid).get().addHours(h);
        repository.findById(rid).get().addHours(h);
        return "redirect:/researcher?id="+rid;
    }

    @RequestMapping("/generate")
    public String reportPage(
            @RequestParam(name = "id", required = true) Long id,
            @RequestParam(name = "month", required = true) int month,
            @RequestParam(name = "year", required = true) int year,
            Model model
    ){
        model.addAttribute("month",month);
        model.addAttribute("year",year);
        Optional<Researcher> result = repository.findById(id);
        if(result.isPresent()){
            Researcher researcher = result.get();
            List<Hours> filteredHours = researcher.getHours().stream()
                    .filter(hour-> hour.getDate().getMonthValue()==month && hour.getDate().getYear()==year)
                    .toList();
            model.addAttribute("researcher",researcher);
            Map<Project, List<Integer>> projectHoursMap = new HashMap<>();
            YearMonth yearMonth = YearMonth.of(year, month);
            int daysInMonth = yearMonth.lengthOfMonth();
            List<Integer> days = new ArrayList<>();
            List<Integer> totalePerDay= new ArrayList<>();
            for (int i = 0; i < daysInMonth; i++) {
                days.add(i+1);
                totalePerDay.add(0);
            }
            int totalHours = 0;
            for (Hours hour : filteredHours) {
                if (!hour.isApproved()){
                    continue;
                }
                LocalDate date = hour.getDate();
                Project project = hour.getProject();
                if(!projectHoursMap.containsKey(project)){
                    List<Integer> listHours = new ArrayList<>();
                    for (int i = 0; i<daysInMonth+1; i++) {
                        listHours.add(0);
                    }
                    projectHoursMap.put(project, listHours);
                }
                projectHoursMap.get(project).set(date.getDayOfMonth()-1,projectHoursMap.get(project).get(date.getDayOfMonth()-1)+hour.getHourWorked());
                projectHoursMap.get(project).set(daysInMonth,projectHoursMap.get(project).get(daysInMonth)+hour.getHourWorked());
                totalePerDay.set(date.getDayOfMonth()-1,totalePerDay.get(date.getDayOfMonth()-1)+hour.getHourWorked());
                totalHours += hour.getHourWorked();
            }

            model.addAttribute("projectHoursMap", projectHoursMap);
            model.addAttribute("daysInMonth", days);
            model.addAttribute("totalePerDay", totalePerDay);
            model.addAttribute("totalHours", totalHours);
            return "report";

        }
        return "_error";
    }

    @RequestMapping("/generate-report-project")
    public String projectReportPage(
            @RequestParam(name = "rid", required = true) Long rid,
            @RequestParam(name = "pid", required = true) Long pid,
            @RequestParam(name = "month", required = true) int month,
            @RequestParam(name = "year", required = true) int year,
            Model model
    ){
        model.addAttribute("month",month);
        model.addAttribute("year",year);
        model.addAttribute("project",projectRepository.findById(pid).get());
        model.addAttribute("researcher",repository.findById(rid).get());

        YearMonth yearMonth = YearMonth.of(year, month);
        int daysInMonth = yearMonth.lengthOfMonth();
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

        for (Hours hour : repository.findById(rid).get().getHours()) {
            if (!hour.isApproved()){
                continue;
            }
            if(hour.getProject().getId() == pid){
                hoursWorkedOnProject.set(hour.getDate().getDayOfMonth()-1,hoursWorkedOnProject.get(hour.getDate().getDayOfMonth()-1)+hour.getHourWorked());
                hoursWorkedOnProject.set(daysInMonth,hoursWorkedOnProject.get(daysInMonth)+hour.getHourWorked());
            } else if(hour.getProject().getFundingAgency().equals(projectRepository.findById(pid).get().getFundingAgency())){
                hoursWorkedOnProjectSameAgency.set(hour.getDate().getDayOfMonth()-1,hoursWorkedOnProjectSameAgency.get(hour.getDate().getDayOfMonth()-1)+hour.getHourWorked());
                hoursWorkedOnProjectSameAgency.set(daysInMonth,hoursWorkedOnProjectSameAgency.get(daysInMonth)+hour.getHourWorked());
            } else {
                hoursWorkedOnOther.set(hour.getDate().getDayOfMonth()-1,hoursWorkedOnOther.get(hour.getDate().getDayOfMonth()-1)+hour.getHourWorked());
                hoursWorkedOnOther.set(daysInMonth,hoursWorkedOnOther.get(daysInMonth)+hour.getHourWorked());
            }
            totalWorkedDay.set(hour.getDate().getDayOfMonth()-1, totalWorkedDay.get(hour.getDate().getDayOfMonth()-1)+hour.getHourWorked());
            totalWorkedDay.set(daysInMonth, totalWorkedDay.get(daysInMonth)+hour.getHourWorked());
        }
        for (LocalDate date : repository.findById(rid).get().getDaysOff()){
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
        Researcher r = repository.findById(id).get();
        r.addDaysOff(date);
        repository.save(r);
        model.addAttribute("researcher", r);
        return "/researcher";
    }
}