package it.univr;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ProjectTest extends BaseTest{
    @Test
    public void testProject() {
        driver.get("http://localhost:8080/project?id=1");
        ProjectPage projectPage = new ProjectPage(driver);
        assertEquals("Manage project : Smartitude", projectPage.title());
        assertEquals(3, projectPage.numberOfResearchers());
        assertTrue(projectPage.researcherIsFound("Jean Martin"));
        assertTrue(projectPage.researcherIsFound("John Smith"));
    }

    @Test
    public void testProjectNotFound() {
        driver.get("http://localhost:8080/project?id=20");
        ProjectPage projectPage = new ProjectPage(driver);
        assertEquals("UniPro : Error", projectPage.title());
    }

    @Test
    public void generateReport(){
        driver.get("http://localhost:8080/project?id=1");
        ProjectPage projectPage = new ProjectPage(driver);
        ReportProjectPage reportProjectPage = projectPage.goToReport("Jean Martin", 1, 2020);
        assertEquals("1/2020", reportProjectPage.title());
    }

    @Test
    public void approveHour(){
        driver.get("http://localhost:8080/project?id=3");
        ProjectPage projectPage = new ProjectPage(driver);
        projectPage.approveHour(LocalDate.of(2021,2,10),1, "John Smith");
        assertEquals("Manage project : iNest", projectPage.title());
    }

    @Test
    public void approveHourNotFound(){
        driver.get("http://localhost:8080/approve?id=20");
        PageObject pageObject = new PageObject(driver);
        assertEquals("UniPro : Error", pageObject.title());
    }

    @Test
    public void rejectHour(){
        driver.get("http://localhost:8080/project?id=3");
        ProjectPage projectPage = new ProjectPage(driver);
        projectPage.rejectHour(LocalDate.of(2021,1,2),1, "John Smith");
        assertEquals("Manage project : iNest", projectPage.title());
    }

    @Test
    public void rejectHourNotFound(){
        driver.get("http://localhost:8080/reject?id=20");
        PageObject pageObject = new PageObject(driver);
        assertEquals("UniPro : Error", pageObject.title());
    }
}
