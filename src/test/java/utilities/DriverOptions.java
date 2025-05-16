package utilities;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DriverOptions {
	public static WebDriver driver;
	public static WebDriver getDriver(){
		if (driver == null) {
			String browser = System.getProperty("browser", "chrome");
			switch (browser.toLowerCase()) {
				case "chrome":
					ChromeOptions optionsChrome = new ChromeOptions();
					optionsChrome.addArguments("--start-maximized");
					optionsChrome.addArguments("--disable-notifications");
					optionsChrome.addArguments("--ignore-certificate-errors");
					driver = new ChromeDriver(optionsChrome);
					break;
				case "firefox":
					driver = new FirefoxDriver();
					break;
				case "edge":
					EdgeOptions optionsEdge = new EdgeOptions();
					optionsEdge.addArguments("--start-maximized");
					optionsEdge.addArguments("--ignore-certificate-errors");
					optionsEdge.addArguments("--disable-notifications");
					driver = new EdgeDriver(optionsEdge);
					break;
				default:
					throw new IllegalStateException("Geçersiz tarayıcı seçimi: " + browser);
			}
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
			driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
		}
		return driver;
	}

	public static void quitDriver() {
		if (driver != null) {
			driver.quit();
			driver = null;
		}
	}
}





