package it.univr;


import org.hibernate.query.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


public class ResearcherPage extends PageObject {
    public ResearcherPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(tagName = "h1")
    private WebElement title;

    @FindBy(linkText = "See your hours")
    WebElement seeHoursLink;

    @FindBy(linkText = "Log out")
    WebElement logoutLink;

    @FindBy(linkText = "Add new person")
    WebElement addNewPersonLink;

    @FindBy(id="projectsAsPIList")
    WebElement projectsAsPIList;

    @FindBy(id="projectsList")
    WebElement projectsList;

    public String title(){
        return title.getText();
    }

    // TO DO : change return value
    public PageObject seeHours() {
        this.seeHoursLink.click();
        return new PageObject(driver);
    }

    public LoginPage logOut() {
        this.logoutLink.click();
        return new LoginPage(driver);
    }



}
