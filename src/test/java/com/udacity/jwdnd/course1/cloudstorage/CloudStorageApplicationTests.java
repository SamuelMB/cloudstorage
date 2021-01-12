package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	private final String firstName = "firstName";
	private final String lastName = "lastName";
	private final String username = "username";
	private final String password = "password";
	private String homeUrl;
	private String loginUrl;
	private String signupUrl;
	private String resultUrl;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
		this.homeUrl = "http://localhost:" + this.port + "/home";
		this.loginUrl = "http://localhost:" + this.port + "/login";
		this.signupUrl = "http://localhost:" + this.port + "/signup";
		this.resultUrl = "http://localhost:" + this.port + "/result";
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	private void signUp() {
		driver.get(this.signupUrl);
		SignupPage signupPage = new SignupPage(this.driver);
		signupPage.signup(this.firstName, this.lastName, this.username, this.password);

		WebElement alertSuccessfulSignUp = driver.findElement(By.className("alert-dark"));
		String successfulMessageText = alertSuccessfulSignUp.getText();

		Assertions.assertEquals("You successfully signed up! Please continue to the login page.", successfulMessageText);
	}

	private void login() {
		driver.get(this.loginUrl);
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(this.username, this.password);

		Assertions.assertEquals(this.homeUrl, driver.getCurrentUrl());
	}

	@Test
	@Order(1)
	void homePageRedirectsToLoginPageWhenNotLogged() {
		driver.get(this.homeUrl);
		Assertions.assertEquals(this.loginUrl, driver.getCurrentUrl());
	}

	@Test
	@Order(2)
	void signUpLoginVerifyHomeLogout() throws InterruptedException {
		signUp();
		login();

		HomePage homePage = new HomePage(driver);
		homePage.logout();

		new WebDriverWait(driver, 10)
				.until(ExpectedConditions.titleIs("Login"));

		Assertions.assertEquals(this.loginUrl + "?logout", driver.getCurrentUrl());

		driver.get(this.homeUrl);
		Assertions.assertEquals(this.loginUrl, driver.getCurrentUrl());
	}

	@Test
	@Order(3)
	void createNote() {
		login();

		NotesPage notesPage = new NotesPage(driver);
		notesPage.createNote("Test", "This is a test");

		Assertions.assertEquals(this.resultUrl, driver.getCurrentUrl());

		driver.get(this.homeUrl);
		List<Map<String, WebElement>> notes = notesPage.getNotes();

		Map<String, WebElement> note = notes.stream().findFirst().orElse(null);
		Assertions.assertNotNull(note);
		Assertions.assertEquals("Test", note.get("noteTitle").getText());
		Assertions.assertEquals("This is a test", note.get("noteDescription").getText());
	}

	@Test
	@Order(4)
	void editNote() {
		login();

		NotesPage notesPage = new NotesPage(driver);
		notesPage.editLastCreatedNote("titleEdited", "descriptionEdited");

		Assertions.assertEquals(this.resultUrl, driver.getCurrentUrl());

		driver.get(this.homeUrl);

		List<Map<String, WebElement>> notes = notesPage.getNotes();
		List<Map<String, WebElement>> notesFinded = notes.stream().filter(note -> {
			WebElement noteTitle = note.get("noteTitle");
			WebElement noteDescription = note.get("noteDescription");
			return noteTitle.getText().equals("titleEdited") && noteDescription.getText().equals("descriptionEdited");
		}).collect(Collectors.toList());

		Assertions.assertEquals(1, notesFinded.size());
	}

	@Test
	@Order(5)
	void deleteNote() {
		login();

		NotesPage notesPage = new NotesPage(driver);
		notesPage.deleteLastCreatedNote();

		Assertions.assertEquals(this.resultUrl, driver.getCurrentUrl());

		driver.get(this.homeUrl);

		List<Map<String, WebElement>> notes = notesPage.getNotes();
		List<Map<String, WebElement>> notesFinded = notes.stream().filter(note -> {
			WebElement noteTitle = note.get("noteTitle");
			WebElement noteDescription = note.get("noteDescription");
			return noteTitle.getText().equals("titleEdited") && noteDescription.getText().equals("descriptionEdited");
		}).collect(Collectors.toList());

		Assertions.assertEquals(0, notesFinded.size());
	}

	@Test
	@Order(6)
	void createCredential() {
		login();

		CredentialsPage credentialsPage = new CredentialsPage(driver);
		credentialsPage.createCredential("http://test.com/login", "testusername", "testpassword");

		Assertions.assertEquals(this.resultUrl, driver.getCurrentUrl());

		driver.get(this.homeUrl);

		List<Map<String, WebElement>> credentials = credentialsPage.getCredentials();
		Map<String, WebElement> credential = credentials.stream().findFirst().orElse(null);
		Assertions.assertNotNull(credential);
		Assertions.assertEquals("http://test.com/login", credential.get("credentialUrl").getText());
		Assertions.assertEquals("testusername", credential.get("credentialUsername").getText());
	}

	@Test
	@Order(7)
	void editCredential() {
		login();

		CredentialsPage credentialsPage = new CredentialsPage(driver);
		credentialsPage.editLastCreatedCredential("http://test.com/loginEdited", "testusernameEdited", "testpasswordEdited");

		Assertions.assertEquals(this.resultUrl, driver.getCurrentUrl());

		driver.get(this.homeUrl);

		List<Map<String, WebElement>> credentials = credentialsPage.getCredentials();
		List<Map<String, WebElement>> credentialsFinded = credentials.stream().filter(credential -> {
			WebElement credentialUrl = credential.get("credentialUrl");
			WebElement credentialUsername = credential.get("credentialUsername");
			return credentialUrl.getText().equals("http://test.com/loginEdited") &&
					credentialUsername.getText().equals("testusernameEdited");
		}).collect(Collectors.toList());

		Assertions.assertEquals(1, credentialsFinded.size());
	}

	@Test
	@Order(8)
	void deleteCredential() {
		login();

		CredentialsPage credentialsPage = new CredentialsPage(driver);
		credentialsPage.deleteLastCreatedNote();

		Assertions.assertEquals(this.resultUrl, driver.getCurrentUrl());

		driver.get(this.homeUrl);

		List<Map<String, WebElement>> credentials = credentialsPage.getCredentials();
		List<Map<String, WebElement>> credentialsFinded = credentials.stream().filter(credential -> {
			WebElement credentialUrl = credential.get("credentialUrl");
			WebElement credentialUsername = credential.get("credentialUsername");
			return credentialUrl.getText().equals("http://test.com/loginEdited") &&
					credentialUsername.getText().equals("testusernameEdited");
		}).collect(Collectors.toList());

		Assertions.assertEquals(0, credentialsFinded.size());
	}
}
