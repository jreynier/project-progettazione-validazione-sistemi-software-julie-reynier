package it.univr;


import org.hibernate.query.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


public class LoginPage  extends PageObject{
    @FindBy(tagName = "h1")
    private WebElement title;

    @FindBy(name="email")
    private WebElement email;

    @FindBy(name="password")
    private WebElement password;

    @FindBy(xpath="//input[@type='submit']")
    private WebElement loginButton;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public String title(){
        return title.getText();
    }

    public void enterCredentials(String email, String password){
        this.email.clear();
        this.email.sendKeys(email);
        this.password.clear();
        this.password.sendKeys(password);
    }

    public ResearcherPage submit(){
        this.loginButton.click();
        return new ResearcherPage(driver);
    }

}
