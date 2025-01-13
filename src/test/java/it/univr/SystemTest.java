package it.univr;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class SystemTest extends BaseTest {

    public void addHours(LocalDate date, int hours, String researcherEmail, String password,String projectName) {
        driver.get("http://localhost:8080/");
        LoginPage loginPage = new LoginPage(driver);
        PageObject page = loginPage.login(researcherEmail, password);
        ResearcherPage researcherPage = (ResearcherPage) page;
        AddHoursPage addHoursPage = researcherPage.clickAddHours(projectName);
        researcherPage = addHoursPage.requestHours(date, hours);
    }

    public void approveHour(LocalDate date, int hours, String researcherEmail, String password,String projectName, String researcherName) {
        driver.get("http://localhost:8080/");
        LoginPage loginPage = new LoginPage(driver);

        PageObject page = loginPage.login(researcherEmail, password);
        ResearcherPage researcherPage = (ResearcherPage) page;

        ProjectPage projectPage = researcherPage.clickManageProject(projectName);
        projectPage.approveHour(date,hours, researcherName);
    }

    @Test
    public void testScenarioSubmitHour() {
        driver.get("http://localhost:8080/");
        LoginPage loginPage = new LoginPage(driver);
        assertEquals("UniPro : Login", loginPage.title());

        PageObject page = loginPage.login("researcher2@univr.it", "234567");
        assertTrue(page instanceof  ResearcherPage);
        assertEquals("Welcome, Mario Rossi", page.title());
        ResearcherPage researcherPage = (ResearcherPage) page;
        AddHoursPage addHoursPage = researcherPage.clickAddHours("NeuroPuls");
        researcherPage = addHoursPage.requestHours(LocalDate.of(2025,1, 13), 5);
        assertEquals("Welcome, Mario Rossi", researcherPage.title());
        boolean found = false;
        for (Hours hours : hoursRepository.findByResearcher(researcherRepository.findByEmail("researcher2@univr.it"))){
            if (hours.getDate().equals(LocalDate.of(2025,1,13)) && hours.getHoursWorked() == 5){
                assertFalse(hours.isApproved());
                found = true;
            }
        }
        assertTrue(found);
    }

    @Test
    public void testScenarioApproveHour() {
        addHours(LocalDate.of(2025, 1, 14), 3, "researcher2@univr.it", "234567", "NeuroPuls");
        driver.get("http://localhost:8080/");
        LoginPage loginPage = new LoginPage(driver);
        assertEquals("UniPro : Login", loginPage.title());

        PageObject page = loginPage.login("researcher1@univr.it", "123456");
        assertTrue(page instanceof  ResearcherPage);
        assertEquals("Welcome, Jean Martin", page.title());
        ResearcherPage researcherPage = (ResearcherPage) page;

        ProjectPage projectPage = researcherPage.clickManageProject("NeuroPuls");
        assertEquals("Manage project : NeuroPuls", projectPage.title());
        projectPage.approveHour(LocalDate.of(2025,1,14),3, "Mario Rossi");
        ReportProjectPage reportProjectPage = projectPage.goToReport("Mario Rossi", 1, 2025);
        assertEquals("1/2025", reportProjectPage.title());
        assertEquals("3",reportProjectPage.getElementForProjectAndDay(0, 14));
        boolean found = false;
        for (Hours hours : hoursRepository.findByResearcher(researcherRepository.findByEmail("researcher2@univr.it"))){
            if (hours.getDate().equals(LocalDate.of(2025,1,14)) && hours.getHoursWorked() == 3){
                assertTrue(hours.isApproved());
                found = true;
            }
        }
        assertTrue(found);
    }

    @Test
    public void testScenarioRejectHour() {
        addHours(LocalDate.of(2025, 1, 15), 3, "researcher2@univr.it", "234567", "NeuroPuls");
        driver.get("http://localhost:8080/");
        LoginPage loginPage = new LoginPage(driver);
        assertEquals("UniPro : Login", loginPage.title());

        PageObject page = loginPage.login("researcher1@univr.it", "123456");
        assertTrue(page instanceof  ResearcherPage);
        assertEquals("Welcome, Jean Martin", page.title());
        ResearcherPage researcherPage = (ResearcherPage) page;
        ProjectPage projectPage = researcherPage.clickManageProject("NeuroPuls");
        assertEquals("Manage project : NeuroPuls", projectPage.title());
        projectPage.rejectHour(LocalDate.of(2025,1,15),3, "Mario Rossi");
        ReportProjectPage reportProjectPage = projectPage.goToReport("Mario Rossi", 1, 2025);
        assertEquals("1/2025", reportProjectPage.title());
        assertEquals("0",reportProjectPage.getElementForProjectAndDay(0, 15));
        boolean found = false;
        for (Hours hours : hoursRepository.findByResearcher(researcherRepository.findByEmail("researcher2@univr.it"))){
            if (hours.getDate().equals(LocalDate.of(2025,1,15)) && hours.getHoursWorked() == 3){
                assertFalse(hours.isApproved());
                found = true;
            }
        }
        assertFalse(found);
    }

    @Test
    public void testScenarioAskDayOff() {
        driver.get("http://localhost:8080/");
        LoginPage loginPage = new LoginPage(driver);
        assertEquals("UniPro : Login", loginPage.title());

        PageObject page = loginPage.login("researcher1@univr.it", "123456");
        assertTrue(page instanceof  ResearcherPage);
        assertEquals("Welcome, Jean Martin", page.title());
        ResearcherPage researcherPage = (ResearcherPage) page;

        researcherPage.askDayOff(LocalDate.of(2025,1,16));
        ReportPage reportPage = researcherPage.generateReport(1,2025);
        assertEquals("1/2025", reportPage.title());
        assertEquals("X", reportPage.getElementForProjectAndDay("Total", 16));
        boolean found = false;
        for (LocalDate date : researcherRepository.findByEmail("researcher1@univr.it").getDaysOff()){
            if (date.equals(LocalDate.of(2025, 1, 16))) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    public void testScenarioReport() {
        addHours(LocalDate.of(2025,1,30),6,"researcher2@univr.it","234567", "NeuroPuls");
        approveHour(LocalDate.of(2025,1,30),6,"researcher1@univr.it","123456", "NeuroPuls","Mario Rossi");
        driver.get("http://localhost:8080/");
        LoginPage loginPage = new LoginPage(driver);
        assertEquals("UniPro : Login", loginPage.title());

        PageObject page = loginPage.login("researcher2@univr.it", "234567");
        assertTrue(page instanceof  ResearcherPage);
        assertEquals("Welcome, Mario Rossi", page.title());
        ResearcherPage researcherPage = (ResearcherPage) page;

        ReportPage reportPage = researcherPage.generateReport(1,2025);
        assertEquals("1/2025", reportPage.title());
        assertEquals("6", reportPage.getElementForProjectAndDay("NeuroPuls", 30));
        assertEquals("6", reportPage.getElementForProjectAndDay("Total", 30));
    }

    @Test
    public void testScenarioReportProject() {
        addHours(LocalDate.of(2025,1,28),4,"researcher2@univr.it","234567", "NeuroPuls");
        approveHour(LocalDate.of(2025,1,28),4,"researcher1@univr.it","123456", "NeuroPuls","Mario Rossi");
        driver.get("http://localhost:8080/");
        LoginPage loginPage = new LoginPage(driver);
        assertEquals("UniPro : Login", loginPage.title());

        PageObject page = loginPage.login("researcher1@univr.it", "123456");
        assertTrue(page instanceof  ResearcherPage);
        assertEquals("Welcome, Jean Martin", page.title());
        ResearcherPage researcherPage = (ResearcherPage) page;

        ProjectPage projectPage = researcherPage.clickManageProject("NeuroPuls");
        assertEquals("Manage project : NeuroPuls", projectPage.title());
        ReportProjectPage reportProjectPage = projectPage.goToReport("Mario Rossi",1, 2025);
        assertEquals("1/2025", reportProjectPage.title());
        assertEquals("4", reportProjectPage.getElementForProjectAndDay(0, 28));
        assertEquals("4", reportProjectPage.getElementForProjectAndDay(3, 28));
    }
}
