package it.univr;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class AddHoursTest extends BaseTest{
    @Test
    public void testAddHours() {
        driver.get("http://localhost:8080/add-hours?rid=1&pid=1");
        AddHoursPage addHoursPage = new AddHoursPage(driver);
        assertEquals("Add hours to the project : Smartitude", addHoursPage.title());
    }

    @Test
    public void testAddHoursProjectNotFound() {
        driver.get("http://localhost:8080/add-hours?rid=1&pid=10");
        AddHoursPage addHoursPage = new AddHoursPage(driver);
        assertEquals("UniPro : Error", addHoursPage.title());
    }

    @Test
    public void testAddHoursResearcherNotFound() {
        driver.get("http://localhost:8080/add-hours?rid=10&pid=1");
        AddHoursPage addHoursPage = new AddHoursPage(driver);
        assertEquals("UniPro : Error", addHoursPage.title());
    }

    @Test
    public void testSubmitHours() {
        driver.get("http://localhost:8080/add-hours?rid=1&pid=1");
        AddHoursPage addHoursPage = new AddHoursPage(driver);
        ResearcherPage researcherPage = addHoursPage.requestHours(LocalDate.of(2022,1,1),3);
        assertEquals("Welcome, Jean Martin", researcherPage.title());
    }

    @Test
    public void testSubmitHoursProjectNotFound() {
        driver.get("http://localhost:8080/request-hours?rid=1&pid=10&day=2022-01-01&hours=3");
        PageObject pageObject = new PageObject(driver);
        assertEquals("UniPro : Error", pageObject.title());
    }

    @Test
    public void testSubmitHoursResearcherNotFound() {
        driver.get("http://localhost:8080/request-hours?rid=10&pid=1&day=2022-01-01&hours=3");
        PageObject pageObject = new PageObject(driver);
        assertEquals("UniPro : Error", pageObject.title());
    }
}
