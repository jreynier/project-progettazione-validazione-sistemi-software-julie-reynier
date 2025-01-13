package it.univr;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.Assert.*;

public class LoginTest extends BaseTest{
    @Test
    public void loginTestOK() {
        driver.get("http://localhost:8080/");
        LoginPage loginPage = new LoginPage(driver);
        assertEquals("UniPro : Login", loginPage.title());
        PageObject returnPage = loginPage.login("researcher1@univr.it", "123456");
        assertTrue(returnPage instanceof ResearcherPage);
        ResearcherPage researcher = (ResearcherPage) returnPage;
        assertEquals("Welcome, Jean Martin", researcher.title());
    }

    @Test
    public void loginTestKO() {
        driver.get("http://localhost:8080/");
        LoginPage loginPage = new LoginPage(driver);
        assertEquals("UniPro : Login", loginPage.title());
        PageObject returnPage = loginPage.login("researcher1@univr.it", "123457");
        assertTrue(returnPage instanceof LoginPage);
        LoginPage loginPage2 = (LoginPage) returnPage;
        assertEquals("UniPro : Login", loginPage2.title());
    }

    @Test
    public void loginTestKO2() {
        driver.get("http://localhost:8080/");
        LoginPage loginPage = new LoginPage(driver);
        assertEquals("UniPro : Login", loginPage.title());
        PageObject returnPage = loginPage.login("nonexisting@univr.it", "123457");
        assertTrue(returnPage instanceof LoginPage);
        LoginPage loginPage2 = (LoginPage) returnPage;
        assertEquals("UniPro : Login", loginPage2.title());
    }
}
