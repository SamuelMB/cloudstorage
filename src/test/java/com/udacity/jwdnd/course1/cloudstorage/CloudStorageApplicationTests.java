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
import java.util.concurrent.TimeUnit;

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

		Assertions.assertEquals(loginUrl, driver.getCurrentUrl());
	}

	@Test
	public void createNewNote() throws InterruptedException {
		String loginUrl = "http://localhost:" + this.port + "/login";

		driver.get(loginUrl);

		LoginPage loginPage = new LoginPage(driver);

		loginPage.login("usertest", "usertest1234");

		NotesPage notesPage = new NotesPage(driver);
		notesPage.createNote("Test", "This is a test");
	}
}
