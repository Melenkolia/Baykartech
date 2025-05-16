package cucumberOptions;

import org.testng.annotations.DataProvider;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
		features = {"src/test/java/features/Checkout.feature"},
		glue = {"stepDefinitions"},
		plugin = { "pretty",
		"io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
        "html:target/site/cucumber-pretty",
        "html:target/site/cucumber.html",
        "json:target/cucumber.json",
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"},
		dryRun = false,monochrome=true)
//		tags = "@PlaceOrder"

public class TestNGTestRunner extends AbstractTestNGCucumberTests{
	@Override
	@DataProvider(parallel = true)
	public Object [][] scenarios()
	{
		return super.scenarios();
	}

}
