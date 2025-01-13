package it.univr;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.time.LocalDate;
import java.util.List;

public class ProjectPage  extends PageObject{
    public ProjectPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//ul")
    private WebElement researchers;

    @FindBy(linkText = "Back")
    WebElement backLink;

    public WebElement researcherToFind(String name){
        List<WebElement> elements = researchers.findElements(By.tagName("li"));
        for (WebElement researcher : elements){
            String nameToCompare = researcher.findElement(By.tagName("span")).getText();
            if (nameToCompare.equals(name)) {
                return researcher;
            }
        }
        return null;
    }

    public int numberOfResearchers(){
        return researchers.findElements(By.xpath("./li")).size();
    }

    public boolean researcherIsFound(String name){
        return researcherToFind(name) != null;
    }

    public ReportProjectPage goToReport(String name, int month, int year){
        if(month < 1 || month > 12 || year<2020 || year>2025){
            throw new IllegalArgumentException("Invalid month or year");
        }
        WebElement monthElement = researcherToFind(name).findElement(By.name("month"));
        WebElement yearElement = researcherToFind(name).findElement(By.name("year"));

        monthElement.clear();
        monthElement.sendKeys(String.valueOf(month));
        yearElement.clear();
        yearElement.sendKeys(String.valueOf(year));
        researcherToFind(name).findElement(By.xpath(".//input[@type='submit']")).click();
        return new ReportProjectPage(driver);
    }

    public WebElement hourToFind(LocalDate date, int hour, String name){
        WebElement researcher = researcherToFind(name);
        List<WebElement> hours = researcher.findElements(By.cssSelector("ul > li"));
        for (WebElement hourElement : hours){
            String dateToCompare = hourElement.findElement(By.cssSelector("span:nth-of-type(1)")).getText();
            String hourToCompare = hourElement.findElement(By.cssSelector("span:nth-of-type(2)")).getText();

            // Check if the date and hours match
            if (dateToCompare.equals(date.toString()) && Integer.parseInt(hourToCompare) == hour) {
                return hourElement;
            }
        }
        return null;
    }

    public void approveHour(LocalDate date, int hours, String name){
        if(date.getYear()>2025||date.getYear()<2020||hours<1 || hours>10){
            throw new IllegalArgumentException("Invalid year or number of hours");
        }
        WebElement hour = hourToFind(date, hours, name);
        WebElement approveLink = hour.findElement(By.linkText("Approve"));
        approveLink.click();
    }

    public void rejectHour(LocalDate date, int hours, String name){
        if(date.getYear()>2025||date.getYear()<2020||hours<1 || hours>10){
            throw new IllegalArgumentException("Invalid year or number of hours");
        }
        WebElement hour = hourToFind(date, hours, name);
        WebElement rejectLink = hour.findElement(By.linkText("Reject"));
        rejectLink.click();
    }

    public ResearcherPage back(){
        backLink.click();
        return new ResearcherPage(driver);
    }
}