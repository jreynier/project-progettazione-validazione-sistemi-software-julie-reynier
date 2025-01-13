package it.univr;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class ReportPage  extends PageObject{
    public ReportPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//table/thead/tr/th")
    private List<WebElement> headers;

    @FindBy(xpath = "//table/tbody/tr")
    private List<WebElement> rows;

    @FindBy(linkText = "Back")
    WebElement backLink;

    public int numberOfColumns(){
        return headers.size();
    }

    public int numberOfRows(){
        return rows.size();
    }

    public String getElementForProjectAndDay(String projectName, int day) {
        if (day<1 || day>numberOfColumns()-1){
            throw new IllegalArgumentException("Need a valid day");
        }
        for (WebElement row : rows) {
            // Locate the first cell in the row (project name)
            WebElement firstCell = row.findElement(By.xpath("./td[1]"));
            if (firstCell.getText().equals(projectName)) {
                // Locate the cell in the specified column (1-based index)
                List<WebElement> cells = row.findElements(By.xpath("./td"));
                return cells.get(day).getText();
            }
        }
        throw new IllegalArgumentException("Project not found: " + projectName);
    }


    public ResearcherPage back(){
        backLink.click();
        return new ResearcherPage(driver);
    }
}