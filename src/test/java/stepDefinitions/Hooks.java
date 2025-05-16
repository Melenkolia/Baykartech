package stepDefinitions;

import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import org.openqa.selenium.WebDriver;
import utilities.DriverOptions;

public class Hooks extends DriverOptions {

	static WebDriver driver;

	@BeforeAll
	public static void setUp() {
		driver = getDriver();
		driver.get("https://kariyer.baykartech.com/tr/");
		System.out.println("The page title is: " + driver.getTitle());
	}

	@AfterAll
	public static void tearDown() {
//		Driver.quitDriver();
	}
}
