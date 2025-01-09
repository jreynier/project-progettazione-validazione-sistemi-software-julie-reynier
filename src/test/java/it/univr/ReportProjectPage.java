package it.univr;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class ReportProjectPage  extends PageObject{
    public ReportProjectPage(WebDriver driver) {
        super(driver);
    }
    @FindBy(tagName = "h1")
    private WebElement title;

    @FindBy(xpath = "//table/thead/tr/th")
    private List<WebElement> headers;

    @FindBy(xpath = "//table/tbody/tr")
    private List<WebElement> rows;

    @FindBy(linkText = "Back")
    WebElement backLink;

    public String title(){
        return title.getText();
    }

    public int numberOfColumns(){
        return headers.size();
    }

    public int numberOfRows(){
        return rows.size();
    }

    // project 0 : this project
    // project 1 : projects of the same agency
    // project 2 : other activities
    // project 3 : total
    public String getElementForProjectAndDay(int project, int day) {
        if (day<1 || day>numberOfColumns()-1){
            throw new IllegalArgumentException("Need a valid day");
        }
        WebElement row = rows.get(project);
        List<WebElement> cells = row.findElements(By.xpath("./td"));
        return cells.get(day).getText();
    }

    public ResearcherPage back(){
        backLink.click();
        return new ResearcherPage(driver);
    }
}