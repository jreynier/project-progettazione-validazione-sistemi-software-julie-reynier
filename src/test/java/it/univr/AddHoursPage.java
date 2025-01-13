package it.univr;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.time.LocalDate;

public class AddHoursPage  extends PageObject{
    public AddHoursPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(name="day")
    private WebElement day;

    @FindBy(name = "hours")
    WebElement hours;

    @FindBy(xpath="//input[@type='submit']")
    private WebElement requestAddButton;

    @FindBy(linkText = "Back")
    WebElement backLink;

    public ResearcherPage requestHours(LocalDate day,int hour){
        if(day.getYear()>2025||day.getYear()<2020||hour<1 || hour>10){
            throw new IllegalArgumentException("Invalid year or number of hours");
        }
        this.day.clear();
        this.day.sendKeys(day.toString());
        this.hours.clear();
        this.hours.sendKeys(String.valueOf(hour));
        requestAddButton.click();
        return new ResearcherPage(driver);
    }

    public ResearcherPage back(){
        backLink.click();
        return new ResearcherPage(driver);
    }
}
