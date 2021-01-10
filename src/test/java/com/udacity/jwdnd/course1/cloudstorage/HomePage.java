package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {
    @FindBy(xpath = "//div[@id='logoutDiv']//button")
    private WebElement logoutButton;

    private final WebDriverWait webDriverWait;

    private final FluentWait<WebDriver> fluentWait;

    public HomePage(WebDriver webDriver) {
        this.webDriverWait = new WebDriverWait(webDriver, 10);
        this.fluentWait = new FluentWait<>(webDriver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);
        PageFactory.initElements(webDriver, this);
    }

    public void logout() {
        fluentWait.until(ExpectedConditions.titleIs("Home"));
        fluentWait.until(ExpectedConditions.elementToBeClickable(logoutButton));
        logoutButton.click();
    }
}
