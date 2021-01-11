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

public class NotesPage {

    @FindBy(id = "nav-notes-tab")
    private WebElement notesNavButton;

    @FindBy(xpath = "//div[@id='nav-notes']//button")
    private WebElement addNewNoteButton;

    @FindBy(id = "note-title")
    private WebElement noteTitleField;

    @FindBy(id = "note-description")
    private WebElement noteDescriptionField;

    @FindBy(id = "userTable")
    private WebElement notesTable;

    @FindBy(xpath = "//button[text()='Save changes']")
    private WebElement saveChangesButton;

    private final FluentWait<WebDriver> fluentWait;
    private final JavascriptExecutor javascriptExecutor;

    public NotesPage(WebDriver webDriver) {
        this.fluentWait = new FluentWait<>(webDriver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);
        this.javascriptExecutor = (JavascriptExecutor) webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public void createNote(String title, String description) {
        fluentWait.until(ExpectedConditions.titleIs("Home"));
        fluentWait.until(ExpectedConditions.elementToBeClickable(notesNavButton));
        this.javascriptExecutor.executeScript("arguments[0].click();", notesNavButton);
        fluentWait.until(ExpectedConditions.elementToBeClickable(addNewNoteButton));
        this.javascriptExecutor.executeScript("arguments[0].click();", addNewNoteButton);
        fluentWait.until(ExpectedConditions.visibilityOf(noteTitleField));
        fluentWait.until(ExpectedConditions.visibilityOf(noteDescriptionField));
        noteTitleField.sendKeys(title);
        noteDescriptionField.sendKeys(description);
        saveChangesButton.click();
    }

    public List<Map<String, WebElement>> getNotes() {
        fluentWait.until(ExpectedConditions.titleIs("Home"));
        fluentWait.until(ExpectedConditions.elementToBeClickable(notesNavButton));
        this.javascriptExecutor.executeScript("arguments[0].click();", notesNavButton);
        fluentWait.until(ExpectedConditions.visibilityOf(notesTable));
        return notesTable.findElement(By.tagName("tbody")).findElements(By.tagName("tr")).stream().map(tr -> {
            List<WebElement> tds = tr.findElements(By.tagName("td"));
            Map<String, WebElement> tableItems = new HashMap<>();
            tableItems.put("noteEditButton", tds.get(0).findElement(By.tagName("button")));
            tableItems.put("noteDeleteButton", tds.get(0).findElement(By.tagName("a")));
            tableItems.put("noteTitle", tr.findElement(By.tagName("th")));
            tableItems.put("noteDescription", tds.get(1));
            return tableItems;
        }).collect(Collectors.toList());
    }

    public void editLastCreatedNote(String title, String description) {
        List<Map<String, WebElement>> notes = getNotes();
        Map<String, WebElement> noteToEdit = notes.get(notes.size() - 1);
        if(noteToEdit != null) {
            WebElement noteEditButton = noteToEdit.get("noteEditButton");
            this.javascriptExecutor.executeScript("arguments[0].click();", noteEditButton);
            fluentWait.until(ExpectedConditions.visibilityOf(noteTitleField));
            fluentWait.until(ExpectedConditions.visibilityOf(noteDescriptionField));
            noteTitleField.clear();
            noteTitleField.sendKeys(title);
            noteDescriptionField.clear();
            noteDescriptionField.sendKeys(description);
            saveChangesButton.click();
        }
    }

    public void deleteLastCreatedNote() {
        List<Map<String, WebElement>> notes = getNotes();
        Map<String, WebElement> noteToEdit = notes.get(notes.size() - 1);
        if(noteToEdit != null) {
            WebElement noteDeleteButton = noteToEdit.get("noteDeleteButton");
            this.javascriptExecutor.executeScript("arguments[0].click();", noteDeleteButton);
        }
    }
}
