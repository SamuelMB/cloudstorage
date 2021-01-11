package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void homePageRedirectsToLoginPageWhenNotLogged() {
		String homeUrl = "http://localhost:" + this.port + "/home";
		String loginUrl = "http://localhost:" + this.port + "/login";
		driver.get(homeUrl);
		Assertions.assertEquals(loginUrl, driver.getCurrentUrl());
	}

	@Test
	public void signUpNewUserAndLoginNewUserAndVerifyRedirectHomePageAndLogout() throws InterruptedException {
		String signupUrl = "http://localhost:" + this.port + "/signup";
		String loginUrl = "http://localhost:" + this.port + "/login";
		String homeUrl = "http://localhost:" + this.port + "/home";
		driver.get(signupUrl);

		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("User", "Test", "usertest", "usertest1234");

		WebElement alertSuccessfulSignUp = driver.findElement(By.className("alert-dark"));
		String successfulMessageText = alertSuccessfulSignUp.getText();

		Assertions.assertEquals("You successfully signed up! Please continue to the login page.", successfulMessageText);

		driver.get(loginUrl);

		LoginPage loginPage = new LoginPage(driver);
		loginPage.login("usertest", "usertest1234");

		Assertions.assertEquals(homeUrl, driver.getCurrentUrl());

		HomePage homePage = new HomePage(driver);
		homePage.logout();

		new WebDriverWait(driver, 10)
				.until(ExpectedConditions.titleIs("Login"));

		Assertions.assertEquals(loginUrl, driver.getCurrentUrl());
	}

	@Test
	public void createNewNote() {
		String loginUrl = "http://localhost:" + this.port + "/login";

		driver.get(loginUrl);

		LoginPage loginPage = new LoginPage(driver);

		loginPage.login("usertest", "usertest1234");

		NotesPage notesPage = new NotesPage(driver);
		notesPage.createNote("Test", "This is a test");

		List<Map<String, WebElement>> notes = notesPage.getNotes();

		Map<String, WebElement> note = notes.stream().findFirst().orElse(null);
		Assertions.assertNotNull(note);
		Assertions.assertEquals("Test", note.get("noteTitle").getText());
		Assertions.assertEquals("This is a test", note.get("noteDescription").getText());
	}

	@Test
	public void editNote() {
		String loginUrl = "http://localhost:" + this.port + "/login";

		driver.get(loginUrl);

		LoginPage loginPage = new LoginPage(driver);

		loginPage.login("usertest", "usertest1234");

		NotesPage notesPage = new NotesPage(driver);
		notesPage.editLastCreatedNote("titleEdited", "descriptionEdited");

		List<Map<String, WebElement>> notes = notesPage.getNotes();
		List<Map<String, WebElement>> notesFinded = notes.stream().filter(note -> {
			WebElement noteTitle = note.get("noteTitle");
			WebElement noteDescription = note.get("noteDescription");
			return noteTitle.getText().equals("titleEdited") && noteDescription.getText().equals("descriptionEdited");
		}).collect(Collectors.toList());

		Assertions.assertEquals(1, notesFinded.size());
	}

	@Test
	public void deleteNote() {
		String loginUrl = "http://localhost:" + this.port + "/login";

		driver.get(loginUrl);

		LoginPage loginPage = new LoginPage(driver);

		loginPage.login("usertest", "usertest1234");

		NotesPage notesPage = new NotesPage(driver);
		notesPage.deleteLastCreatedNote();

		List<Map<String, WebElement>> notes = notesPage.getNotes();
		List<Map<String, WebElement>> notesFinded = notes.stream().filter(note -> {
			WebElement noteTitle = note.get("noteTitle");
			WebElement noteDescription = note.get("noteDescription");
			return noteTitle.getText().equals("titleEdited") && noteDescription.getText().equals("descriptionEdited");
		}).collect(Collectors.toList());

		Assertions.assertEquals(0, notesFinded.size());
	}

	@Test
	public void credentialCreate() {
		String loginUrl = "http://localhost:" + this.port + "/login";

		driver.get(loginUrl);

		LoginPage loginPage = new LoginPage(driver);

		loginPage.login("usertest", "usertest1234");

		CredentialsPage credentialsPage = new CredentialsPage(driver);
		credentialsPage.createCredential("http://test.com/login", "testusername", "testpassword");

		List<Map<String, WebElement>> credentials = credentialsPage.getCredentials();
		Map<String, WebElement> credential = credentials.stream().findFirst().orElse(null);
		Assertions.assertNotNull(credential);
		Assertions.assertEquals("http://test.com/login", credential.get("credentialUrl").getText());
		Assertions.assertEquals("testusername", credential.get("credentialUsername").getText());
		Assertions.assertEquals("testpassword", credential.get("credentialPassword").getText());
	}

	@Test
	public void credentialEdit() {
		String loginUrl = "http://localhost:" + this.port + "/login";

		driver.get(loginUrl);

		LoginPage loginPage = new LoginPage(driver);

		loginPage.login("usertest", "usertest1234");

		CredentialsPage credentialsPage = new CredentialsPage(driver);
		credentialsPage.editLastCreatedCredential("http://test.com/loginEdited", "testusernameEdited", "testpasswordEdited");

		List<Map<String, WebElement>> credentials = credentialsPage.getCredentials();
		List<Map<String, WebElement>> credentialsFinded = credentials.stream().filter(credential -> {
			WebElement credentialUrl = credential.get("credentialUrl");
			WebElement credentialUsername = credential.get("credentialUsername");
			WebElement credentialPassword = credential.get("credentialPassword");
			return credentialUrl.getText().equals("http://test.com/loginEdited") &&
					credentialUsername.getText().equals("testusernameEdited") &&
					credentialPassword.getText().equals("testpasswordEdited");
		}).collect(Collectors.toList());

		Assertions.assertEquals(1, credentialsFinded.size());
	}
}
