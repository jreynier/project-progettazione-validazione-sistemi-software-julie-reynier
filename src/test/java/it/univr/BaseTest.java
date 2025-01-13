package it.univr;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class BaseTest {

    @Autowired
    ResearcherRepository researcherRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    HoursRepository hoursRepository;

    protected WebDriver driver;
    @Before
    public void setUp() {

        WebDriverManager manager = WebDriverManager.firefoxdriver();
        if (driver == null)
            driver = manager.create();
        // create the database :
        Researcher  researcher1;
        Researcher  researcher2;
        Researcher  researcher3;
        if (researcherRepository.findByEmail("researcher1@univr.it") == null) {
            researcher1 = new Researcher("Jean", "Martin", "researcher1@univr.it", "123456","654321");
        } else {
            return;
        }
        if (researcherRepository.findByEmail("researcher2@univr.it") == null) {
         researcher2 = new Researcher("Mario", "Rossi", "researcher2@univr.it", "234567", "765432");
        }else {
            return;
        }
        if (researcherRepository.findByEmail("researcher3@univr.it") == null) {
            researcher3 = new Researcher("John", "Smith", "researcher3@univr.it", "345678","876543");
        } else {
            return;
        }

        Project project1;
        Project project2;
        Project project3;
        if (projectRepository.findByName("Smartitude") == null) {
            project1 = new Project("Smartitude","Ministero della Ricerca", "073");
        }else {
            return;
        }
        if (projectRepository.findByName("NeuroPuls") == null) {
            project2 = new Project("NeuroPuls", "Unione Europea", "1293");
        }else {
            return;
        }
        if (projectRepository.findByName("iNest") == null) {
            project3 = new Project("iNest", "Ministero della Ricerca", "097");
        }else {
            return;
        }

        Hours alreadyApproved = new Hours(2, LocalDate.of(2020,1,1),true, researcher3, project3);
        Hours alreadyApproved2 = new Hours(2, LocalDate.of(2020,1,2),true, researcher3, project3);
        Hours alreadyApproved3 = new Hours(2, LocalDate.of(2020,1,1),true, researcher3, project1);
        Hours alreadyApproved4 = new Hours(2, LocalDate.of(2020,1,3),true, researcher3, project2);

        Hours notApproved = new Hours(2, LocalDate.of(2020,1,3),false, researcher3, project1);
        Hours toApprove = new Hours(1, LocalDate.of(2021,2,10),false, researcher3, project3);
        Hours toReject = new Hours(1, LocalDate.of(2021,1,2),false, researcher3, project3);

        researcher3.addDaysOff(LocalDate.of(2020,1,30));

        project1.addResearcher(researcher1);
        project1.addResearcher(researcher3);
        project2.addResearcher(researcher2);
        project2.addResearcher(researcher3);
        project3.addResearcher(researcher3);

        project1.setProjectInvestigator(researcher2);
        project2.setProjectInvestigator(researcher1);
        project3.setProjectInvestigator(researcher1);

        researcher1.addProject(project1);
        researcher2.addProject(project2);
        researcher3.addProject(project3);
        researcher3.addProject(project2);
        researcher3.addProject(project1);

        researcher1.addProjectsAsPI(project2);
        researcher1.addProjectsAsPI(project3);
        researcher2.addProjectsAsPI(project1);

        project3.addHours(alreadyApproved);
        project3.addHours(toApprove);
        project3.addHours(toReject);
        project1.addHours(notApproved);
        project3.addHours(alreadyApproved2);
        project1.addHours(alreadyApproved3);
        project2.addHours(alreadyApproved4);
        researcher3.addHours(alreadyApproved2);
        researcher3.addHours(alreadyApproved);
        researcher3.addHours(alreadyApproved3);
        researcher3.addHours(toApprove);
        researcher3.addHours(toReject);
        researcher3.addHours(notApproved);
        researcher3.addHours(alreadyApproved4);

        researcherRepository.save(researcher1);
        researcherRepository.save(researcher2);
        researcherRepository.save(researcher3);

        projectRepository.save(project1);
        projectRepository.save(project2);
        projectRepository.save(project3);

        hoursRepository.save(alreadyApproved);
        hoursRepository.save(toApprove);
        hoursRepository.save(toReject);

        hoursRepository.save(notApproved);
        hoursRepository.save(alreadyApproved2);
        hoursRepository.save(alreadyApproved4);
        hoursRepository.save(alreadyApproved3);

    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }


}