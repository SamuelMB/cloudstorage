package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CredentialsPage {
    @FindBy(id = "nav-credentials-tab")
    private WebElement credentialsNavButton;

    @FindBy(xpath = "//div[@id='nav-credentials']//button")
    private WebElement addNewCredentialButton;

    @FindBy(id = "credential-url")
    private WebElement credentialUrlField;

    @FindBy(id = "credential-username")
    private WebElement credentialUsernameField;

    @FindBy(id = "credential-password")
    private WebElement credentialPasswordField;

    @FindBy(id = "credentialTable")
    private WebElement credentialsTable;

    @FindBy(id = "credential-save-button")
    private WebElement saveChangesButton;

    private final FluentWait<WebDriver> fluentWait;
    private final JavascriptExecutor javascriptExecutor;

    public CredentialsPage(WebDriver webDriver) {
        this.fluentWait = new FluentWait<>(webDriver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);
        this.javascriptExecutor = (JavascriptExecutor) webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public void createCredential(String url, String username, String password) {
        fluentWait.until(ExpectedConditions.titleIs("Home"));
        fluentWait.until(ExpectedConditions.elementToBeClickable(credentialsNavButton));
        this.javascriptExecutor.executeScript("arguments[0].click();", credentialsNavButton);
        fluentWait.until(ExpectedConditions.elementToBeClickable(addNewCredentialButton));
        this.javascriptExecutor.executeScript("arguments[0].click();", addNewCredentialButton);
        fluentWait.until(ExpectedConditions.visibilityOf(credentialUrlField));
        fluentWait.until(ExpectedConditions.visibilityOf(credentialUsernameField));
        fluentWait.until(ExpectedConditions.visibilityOf(credentialPasswordField));
        fluentWait.until(ExpectedConditions.elementToBeClickable(saveChangesButton));
        credentialUrlField.sendKeys(url);
        credentialUsernameField.sendKeys(username);
        credentialPasswordField.sendKeys(password);
        this.javascriptExecutor.executeScript("arguments[0].click();", saveChangesButton);
    }

    public List<Map<String, WebElement>> getCredentials() {
        fluentWait.until(ExpectedConditions.titleIs("Home"));
        fluentWait.until(ExpectedConditions.elementToBeClickable(credentialsNavButton));
        this.javascriptExecutor.executeScript("arguments[0].click();", credentialsNavButton);
        fluentWait.until(ExpectedConditions.visibilityOf(credentialsTable));
        return credentialsTable.findElement(By.tagName("tbody")).findElements(By.tagName("tr")).stream().map(tr -> {
            List<WebElement> tds = tr.findElements(By.tagName("td"));
            Map<String, WebElement> tableItems = new HashMap<>();
            tableItems.put("credentialEditButton", tds.get(0).findElement(By.tagName("button")));
            tableItems.put("credentialDeleteButton", tds.get(0).findElement(By.tagName("a")));
            tableItems.put("credentialUrl", tr.findElement(By.tagName("th")));
            tableItems.put("credentialUsername", tds.get(1));
            tableItems.put("credentialPassword", tds.get(2));
            return tableItems;
        }).collect(Collectors.toList());
    }

    public void editLastCreatedCredential(String url, String username, String password) {
        List<Map<String, WebElement>> credentials = getCredentials();
        Map<String, WebElement> credentialToEdit = credentials.get(credentials.size() - 1);
        if(credentialToEdit != null) {
            WebElement credentialEditButton = credentialToEdit.get("credentialEditButton");
            this.javascriptExecutor.executeScript("arguments[0].click();", credentialEditButton);
            fluentWait.until(ExpectedConditions.visibilityOf(credentialUrlField));
            fluentWait.until(ExpectedConditions.visibilityOf(credentialUsernameField));
            fluentWait.until(ExpectedConditions.visibilityOf(credentialPasswordField));
            fluentWait.until(ExpectedConditions.elementToBeClickable(saveChangesButton));
            credentialUrlField.clear();
            credentialUrlField.sendKeys(url);
            credentialUsernameField.clear();
            credentialUsernameField.sendKeys(username);
            credentialPasswordField.clear();
            credentialPasswordField.sendKeys(password);
            this.javascriptExecutor.executeScript("arguments[0].click();", saveChangesButton);
        }
    }

    public void deleteLastCreatedNote() {
        List<Map<String, WebElement>> credentials = getCredentials();
        Map<String, WebElement> credentialToDelete = credentials.get(credentials.size() - 1);
        if(credentialToDelete != null) {
            WebElement credentialDeleteButton = credentialToDelete.get("credentialDeleteButton");
            this.javascriptExecutor.executeScript("arguments[0].click();", credentialDeleteButton);
        }
    }
}
