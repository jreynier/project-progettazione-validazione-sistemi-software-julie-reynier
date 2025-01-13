package it.univr;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ReportProjectTest extends BaseTest {
    @Test
    public void testReportProject() {
        driver.get("http://localhost:8080/generate-report-project?rid=3&pid=3&month=1&year=2020");
        ReportProjectPage reportPage = new ReportProjectPage(driver);
        assertEquals("1/2020", reportPage.title());

        assertEquals(4, reportPage.numberOfRows());
        assertEquals(33,reportPage.numberOfColumns());

        assertEquals("2",reportPage.getElementForProjectAndDay(0,1));
        assertEquals("0",reportPage.getElementForProjectAndDay(0,3));
        assertEquals("X",reportPage.getElementForProjectAndDay(0,30));
        assertEquals("4",reportPage.getElementForProjectAndDay(0,32));

        assertEquals("2",reportPage.getElementForProjectAndDay(1,1));
        assertEquals("0",reportPage.getElementForProjectAndDay(1,16));
        assertEquals("X",reportPage.getElementForProjectAndDay(1,30));
        assertEquals("2",reportPage.getElementForProjectAndDay(1,32));

        assertEquals("0",reportPage.getElementForProjectAndDay(2,1));
        assertEquals("2",reportPage.getElementForProjectAndDay(2,3));
        assertEquals("X",reportPage.getElementForProjectAndDay(2,30));
        assertEquals("2",reportPage.getElementForProjectAndDay(2,32));

        assertEquals("4",reportPage.getElementForProjectAndDay(3,1));
        assertEquals("2",reportPage.getElementForProjectAndDay(3,3));
        assertEquals("X",reportPage.getElementForProjectAndDay(3,30));
        assertEquals("8",reportPage.getElementForProjectAndDay(3,32));
    }

    @Test
    public void testReportProjectResearcherNotFound() {
        driver.get("http://localhost:8080/generate-report-project?rid=20&pid=1&month=1&year=2020");
        ReportPage reportPage = new ReportPage(driver);
        assertEquals("UniPro : Error", reportPage.title());
    }

    @Test
    public void testReportProjectProjectNotFound() {
        driver.get("http://localhost:8080/generate-report-project?rid=3&pid=20&month=1&year=2020");
        ReportPage reportPage = new ReportPage(driver);
        assertEquals("UniPro : Error", reportPage.title());
    }
}
