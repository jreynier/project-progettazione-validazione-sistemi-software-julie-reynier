package it.univr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.Optional;

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
            @RequestParam(name="id", required=true) Long id) {
        Optional<Project> result = projectRepository.findById(id);
        if (result.isPresent()){
            return "project";
        }
        else
            return "_error";
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
            @RequestParam(name="day", required=true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date,
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

}