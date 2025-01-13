package it.univr;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ReportTest extends BaseTest{
    @Test
    public void testReport() {
        driver.get("http://localhost:8080/generate-report?id=3&month=1&year=2020");
        ReportPage reportPage = new ReportPage(driver);
        assertEquals("1/2020", reportPage.title());
        assertEquals(33, reportPage.numberOfColumns()); //31 days+first columns+total
        assertEquals(4, reportPage.numberOfRows()); // 2 Projects + headers + total

        assertEquals("2", reportPage.getElementForProjectAndDay("iNest",1));
        assertEquals("0", reportPage.getElementForProjectAndDay("iNest",3));
        assertEquals("2", reportPage.getElementForProjectAndDay("iNest",2));
        assertEquals("X", reportPage.getElementForProjectAndDay("iNest",30));
        assertEquals("4", reportPage.getElementForProjectAndDay("iNest",32));

        assertEquals("2", reportPage.getElementForProjectAndDay("Smartitude",1));
        assertEquals("X", reportPage.getElementForProjectAndDay("Smartitude",30));
        assertEquals("2", reportPage.getElementForProjectAndDay("Smartitude",32));

        assertEquals("0", reportPage.getElementForProjectAndDay("NeuroPuls",1));
        assertEquals("2", reportPage.getElementForProjectAndDay("NeuroPuls",3));
        assertEquals("X", reportPage.getElementForProjectAndDay("NeuroPuls",30));
        assertEquals("2", reportPage.getElementForProjectAndDay("NeuroPuls",32));

        assertEquals("0", reportPage.getElementForProjectAndDay("Total",14));
        assertEquals("4", reportPage.getElementForProjectAndDay("Total",1));
        assertEquals("2", reportPage.getElementForProjectAndDay("Total",2));
        assertEquals("X", reportPage.getElementForProjectAndDay("Total",30));
        assertEquals("8", reportPage.getElementForProjectAndDay("Total",32));
    }

    @Test
    public void testReportResearcherNotFound() {
        driver.get("http://localhost:8080/generate-report?id=10&month=1&year=2020");
        ReportPage reportPage = new ReportPage(driver);
        assertEquals("UniPro : Error", reportPage.title());
    }

}
