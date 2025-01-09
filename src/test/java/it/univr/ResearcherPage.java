package it.univr;


import org.hibernate.query.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.time.LocalDate;
import java.util.List;


public class ResearcherPage extends PageObject {
    public ResearcherPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(tagName = "h1")
    private WebElement title;

    @FindBy(id="projectsAsPIList")
    WebElement projectsAsPIList;

    @FindBy(id="projectsList")
    WebElement projectsList;

    @FindBy(name="month")
    private WebElement month;

    @FindBy(name="year")
    private WebElement year;

    @FindBy(id="generateButton")
    private WebElement generateButton;

    @FindBy(name="day")
    private WebElement day;

    @FindBy(id="askDayOffButton")
    private WebElement askDayOffButton;

    @FindBy(linkText = "Log out")
    WebElement logoutLink;

    public String title(){
        return title.getText();
    }

    public WebElement projectToFind(String name, WebElement list){
        List<WebElement> projects = list.findElements(By.tagName("li"));
        for (WebElement project : projects) {
            String nameToCompare = project.findElement(By.tagName("span")).getText();
            if (nameToCompare.equals(name)) {
                return project;
            }
        }
        return null;
    }

    public boolean isProjectInListAsPI(String projectName) {
        return projectToFind(projectName, projectsAsPIList) != null;
    }

    public void clickManageProject(String projectName) {
        WebElement manageLink = projectToFind(projectName, projectsAsPIList).findElement(By.tagName("a"));
        manageLink.click();
    }

    public int getProjectsAsPIListSize() {
        List<WebElement> projects = projectsAsPIList.findElements(By.tagName("li"));
        return projects.size();
    }

    public boolean isProjectInList(String projectName) {
        return projectToFind(projectName, projectsList) != null;
    }

    public void clickAddHours(String projectName) {
        WebElement addHourLink = projectToFind(projectName,projectsList).findElement(By.tagName("a"));
        addHourLink.click();
    }

    public int getProjectsListSize() {
        List<WebElement> projects = projectsList.findElements(By.tagName("li"));
        return projects.size();
    }

    public ReportPage generateReport(int month, int year) {
        if(month < 1 || month > 12 || year<2020 || year>2025){
            throw new IllegalArgumentException("Invalid month or year");
        }
        this.month.clear();
        this.month.sendKeys(String.valueOf(month));
        this.year.clear();
        this.year.sendKeys(String.valueOf(year));
        this.generateButton.click();
        return new ReportPage(driver);
    }

    public ResearcherPage askDayOff(LocalDate day) {
        this.day.clear();
        this.day.sendKeys(String.valueOf(day));
        this.askDayOffButton.click();
        return this;
    }

    public LoginPage logout(){
        logoutLink.click();
        return new LoginPage(driver);
    }

    public LoginPage logOut() {
        this.logoutLink.click();
        return new LoginPage(driver);
    }



}
