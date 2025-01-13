package it.univr;


import org.hibernate.query.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


public class LoginPage  extends PageObject{
    @FindBy(name="email")
    private WebElement email;

    @FindBy(name="password")
    private WebElement password;

    @FindBy(xpath="//input[@type='submit']")
    private WebElement loginButton;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public PageObject login(String email, String password) {
        this.email.clear();
        this.email.sendKeys(email);
        this.password.clear();
        this.password.sendKeys(password);
        this.loginButton.click();
        if (driver.getCurrentUrl().contains("researcher")) {
            return new ResearcherPage(driver);
        }
        return this;

    }

}
