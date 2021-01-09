package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;

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
	public void signUpNewUserAndLoginNewUserAndVerifyRedirectHomePageAndLogout() {
		String signupUrl = "http://localhost:" + this.port + "/signup";
		String loginUrl = "http://localhost:" + this.port + "/login";
		String homeUrl = "http://localhost:" + this.port + "/home";
		driver.get(signupUrl);

		WebElement firstName = driver.findElement(By.name("firstName"));
		firstName.sendKeys("User");

		WebElement lastName = driver.findElement(By.name("lastName"));
		lastName.sendKeys("Test");

		WebElement username = driver.findElement(By.name("username"));
		username.sendKeys("userTest");

		WebElement password = driver.findElement(By.name("password"));
		password.sendKeys("userTest12345");

		WebElement signup = driver.findElement(By.xpath("//button[text()=\"Sign Up\"]"));
		signup.click();

		WebElement alertSuccessfulSignUp = driver.findElement(By.className("alert-dark"));
		String successfulMessageText = alertSuccessfulSignUp.getText();

		Assertions.assertEquals("You successfully signed up! Please continue to the login page.", successfulMessageText);

		driver.get(loginUrl);

		WebElement usernameLogin = driver.findElement(By.name("username"));
		usernameLogin.sendKeys("userTest");

		WebElement passwordLogin = driver.findElement(By.name("password"));
		passwordLogin.sendKeys("userTest12345");

		WebElement loginButton = driver.findElement(By.xpath("//button[text()=\"Login\"]"));
		loginButton.click();

		Assertions.assertEquals(homeUrl, driver.getCurrentUrl());

		WebElement logout = driver.findElement(By.xpath("//button[text()=\"Logout\"]"));
		logout.click();

		Assertions.assertEquals(loginUrl, driver.getCurrentUrl());
	}
}
