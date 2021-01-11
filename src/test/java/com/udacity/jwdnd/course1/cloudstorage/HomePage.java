package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {
    @FindBy(xpath = "//div[@id='logoutDiv']//button")
    private WebElement logoutButton;

    private final FluentWait<WebDriver> fluentWait;

    private final JavascriptExecutor javascriptExecutor;

    public HomePage(WebDriver webDriver) {
        this.fluentWait = new FluentWait<>(webDriver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);
        this.javascriptExecutor = (JavascriptExecutor) webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public void logout() {
        fluentWait.until(ExpectedConditions.titleIs("Home"));
        fluentWait.until(ExpectedConditions.elementToBeClickable(logoutButton));
        this.javascriptExecutor.executeScript("arguments[0].click();", logoutButton);
    }
}
