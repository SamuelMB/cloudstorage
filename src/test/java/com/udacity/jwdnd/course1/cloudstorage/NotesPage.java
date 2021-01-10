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

public class NotesPage {

    @FindBy(id = "nav-notes-tab")
    private WebElement notesNavButton;

    @FindBy(xpath = "//div[@id='nav-notes']//button")
    private WebElement addNewNoteButton;

    @FindBy(id = "note-title")
    private WebElement noteTitleField;

    @FindBy(id = "note-description")
    private WebElement noteDescriptionField;

    @FindBy(xpath = "//button[text()='Save changes']")
    private WebElement saveChangesButton;

    private final FluentWait<WebDriver> fluentWait;

    public NotesPage(WebDriver webDriver) {
        this.fluentWait = new FluentWait<>(webDriver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);
        PageFactory.initElements(webDriver, this);
    }

    public void createNote(String title, String description) throws InterruptedException {
        fluentWait.until(ExpectedConditions.titleIs("Home"));
        fluentWait.until(ExpectedConditions.elementToBeClickable(notesNavButton));
        notesNavButton.click();
        fluentWait.until(ExpectedConditions.elementToBeClickable(addNewNoteButton));
        addNewNoteButton.click();
        fluentWait.until(ExpectedConditions.visibilityOf(noteTitleField));
        fluentWait.until(ExpectedConditions.visibilityOf(noteDescriptionField));
        noteTitleField.sendKeys(title);
        noteDescriptionField.sendKeys(description);
        saveChangesButton.click();
    }
}
