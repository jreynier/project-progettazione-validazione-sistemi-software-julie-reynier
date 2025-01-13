package it.univr;

import org.junit.Test;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ResearcherTest extends BaseTest{
    @Test
    public void testResearcher(){
        driver.get("http://localhost:8080/researcher?id=1");
        ResearcherPage researcherPage = new ResearcherPage(driver);
        assertEquals("Welcome, Jean Martin", researcherPage.title());
        assertEquals(2, researcherPage.getProjectsAsPIListSize());
        assertEquals(3, researcherPage.getProjectsListSize());
        assertTrue(researcherPage.isProjectInList("Smartitude"));
        assertTrue(researcherPage.isProjectInListAsPI("iNest"));
        assertTrue(researcherPage.isProjectInListAsPI("NeuroPuls"));
    }

    @Test
    public void testResearcherNotFound(){
        driver.get("http://localhost:8080/researcher?id=20");
        PageObject researcherPage = new ResearcherPage(driver);
        assertEquals("UniPro : Error", researcherPage.title());
    }

    @Test
    public void testGenerateReport(){
        driver.get("http://localhost:8080/researcher?id=1");
        ResearcherPage researcherPage = new ResearcherPage(driver);
        ReportPage reportPage = researcherPage.generateReport(1,2020);
        assertEquals("1/2020", reportPage.title());
    }

    @Test
    public void testAddHours(){
        driver.get("http://localhost:8080/researcher?id=1");
        ResearcherPage researcherPage = new ResearcherPage(driver);
        AddHoursPage addHoursPage = researcherPage.clickAddHours("Smartitude");
        assertEquals("Add hours to the project : Smartitude", addHoursPage.title());
    }

    @Test
    public void testManageProject(){
        driver.get("http://localhost:8080/researcher?id=1");
        ResearcherPage researcherPage = new ResearcherPage(driver);
        ProjectPage projectPage = researcherPage.clickManageProject("iNest");
        assertEquals("Manage project : iNest", projectPage.title());
    }

    @Test
    public void testRequestDayOff(){
        driver.get("http://localhost:8080/researcher?id=1");
        ResearcherPage researcherPage = new ResearcherPage(driver);
        LocalDate localDate = LocalDate.of(2020, 1, 10);
        ResearcherPage researcherPage1= researcherPage.askDayOff(localDate);
        assertEquals("Welcome, Jean Martin", researcherPage.title());
    }

    @Test
    public void testRequestDayOffResearcherNotFound(){
        driver.get("http://localhost:8080/request-day-off?id=20&day=2020-01-01");
        PageObject page = new PageObject(driver);
        assertEquals("UniPro : Error", page.title());
    }
}
