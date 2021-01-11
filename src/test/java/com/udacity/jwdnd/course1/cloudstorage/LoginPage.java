package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    @FindBy(id = "inputUsername")
    private WebElement usernameField;

    @FindBy(id = "inputPassword")
    private WebElement passwordField;

    @FindBy(id = "buttonLogin")
    private WebElement submitButton;

    private final WebDriverWait webDriverWait;

    public LoginPage(WebDriver webDriver) {
        this.webDriverWait = new WebDriverWait(webDriver, 10);
        PageFactory.initElements(webDriver, this);
    }

    public void login(String username, String password) {
        webDriverWait.until(ExpectedConditions.visibilityOf(usernameField));
        webDriverWait.until(ExpectedConditions.visibilityOf(passwordField));
        webDriverWait.until(ExpectedConditions.elementToBeClickable(submitButton));
        this.usernameField.sendKeys(username);
        this.passwordField.sendKeys(password);
        this.submitButton.submit();
    }
}
