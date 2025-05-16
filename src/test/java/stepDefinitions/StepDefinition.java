package stepDefinitions;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import io.cucumber.java.en.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import pageObjects.Baykar_CareerPage;
import pageObjects.Baykar_MainPage;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static utilities.DriverOptions.getDriver;

public class StepDefinition {
	WebDriver webdriver = getDriver();
	SoftAssert softAssert = new SoftAssert();
	Baykar_MainPage baykarMainPage = PageFactory.initElements(webdriver, Baykar_MainPage.class);
	Baykar_CareerPage baykarCareerPage = PageFactory.initElements(webdriver, Baykar_CareerPage.class);

	@Given("Kullanıcı {string} sitesini ziyaret eder")
	public void kullanici_sitesini_ziyaret_eder(String url) {
		webdriver.get(url);
	}
	@When("navbar üzerindeki tüm bağlantılara sırayla tıklar")
	public void verifymainPage(){

		baykarMainPage.verifymainPage();
	}
	@Given("Kullanıcı Navigation Bar üzerindeki tüm bağlantılara sırayla tıklar")
	public void verifymainPageNavbar(){

		baykarMainPage.verifyAllNavbarLinks();
	}

	@When("kullanıcı dil seçici menüsünü açar")
	public void openLanguageSelector() {
		WebElement languageMenu = webdriver.findElement(By.xpath("//a[contains(text(),'EN')]"));
		languageMenu.click();
	}
	@When("kullanıcı {string} dilini seçer")
	public void selectLanguage(String language) {
		WebElement languageOption = webdriver.findElement(By.linkText(language));
		languageOption.click();
	}
	@Then("Navbar üzerindeki öğeler aşağıdaki metinleri içermelidir:")
		public void checkNavbarItems(List<String> expectedItems) {
			baykarMainPage.checkNavbarItemsFromMainPage(expectedItems);
		}
	@When("Kullanıcı sayfa dilini İngilizce 'EN' olarak değiştirir")
		public void changeLanguage () {
		baykarMainPage.selectENLanguage();
		}
	@When("Kullanıcı varsayılan sayfa dilinin Türkçe 'TR' olduğunu doğrular")
		public void verifyLanguage () {
		baykarMainPage.verifyLanguages();
		}
	@And("Kullanıcı AÇIK POZİSYONLAR butonuna tıklar")
		public void selectOpenPosition1 () {
		baykarMainPage.clickOpenPositionButton1();
	}
	@And("Kullanıcı Açık Pozisyonlar butonuna tıklar")
		public void selectOpenPosition2 () {

		baykarMainPage.clickOpenPositionButton2();
	}

	@When("Sayfadaki birim listesi ve pozisyon isimleri dinamik olarak çekilir")
	public void sayfadakiBirimVePozisyonlariCek() {
		WebDriverWait wait = new WebDriverWait(webdriver, Duration.ofSeconds(10));

		List<WebElement> birimList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
				By.cssSelector("ul#myUL2 li label")));

		Assert.assertFalse(birimList.isEmpty(), "❌ Birim listesi boş geldi!");

		for (WebElement birim : birimList) {
			System.out.println("✅ Birim: " + birim.getText());
		}
	}

	@And("Kullanıcı {string} birimini ve {string} pozisyonunu filtreler")
	public void filtreUygula(String birim, String pozisyon) {
		WebDriverWait wait = new WebDriverWait(webdriver, Duration.ofSeconds(10));

		List<WebElement> birimler = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
				By.cssSelector(".filter-department .filter-label")));

		Optional<WebElement> hedefBirim = birimler.stream()
				.filter(e -> e.getText().trim().equalsIgnoreCase(birim))
				.findFirst();

		Assert.assertTrue(hedefBirim.isPresent(), "❌ İstenen birim bulunamadı: " + birim);

		hedefBirim.get().click();

		WebElement aramaKutusu = wait.until(ExpectedConditions.elementToBeClickable(By.id("positionSearch")));
		aramaKutusu.clear();
		aramaKutusu.sendKeys(pozisyon);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".career-position")));
	}

	@Then("Navbar {string} dilinde aşağıdaki metinleri içermelidir:")
	public void assertNavbarItemsInLanguage(String languageCode, List<String> expectedItems) {
		try {
			changeLanguage(languageCode);
			waitForPageLoad();

			List<String> actualItems = getNavbarTexts();

			List<String> trimmedExpected = expectedItems.stream()
					.map(String::trim)
					.collect(Collectors.toList());

			System.out.println("🔍 Navbar Texts (" + languageCode + "):");
			actualItems.forEach(System.out::println);

			softAssert.assertEquals(actualItems.size(), trimmedExpected.size(),
					"Navbar öğesi sayısı beklenenden farklı!");

			for (String expected : trimmedExpected) {
				softAssert.assertTrue(actualItems.contains(expected),
						"❌ Eksik veya hatalı navbar öğesi: '" + expected + "'");
			}

			softAssert.assertAll();

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("❌ Navbar kontrolü sırasında hata oluştu: " + e.getMessage());
		}
	}

	private void changeLanguage(String langCode) {
		WebElement languageToggle = webdriver.findElement(By.xpath("//a[@href=\"/en/\"]"));
		languageToggle.click();
		waitForPageLoad();
	}

	private List<String> getNavbarTexts() {
		List<WebElement> navElements = webdriver.findElements(By.cssSelector(".offcanvas-body a"));
		return navElements.stream()
				.map(WebElement::getText)
				.map(String::trim)
				.filter(text -> !text.isEmpty())
				.distinct()
				.collect(Collectors.toList());
	}

	private void waitForPageLoad() {
		new WebDriverWait(webdriver, Duration.ofSeconds(10))
				.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
	}

	@When("Kullanıcı {string} birimini filtreler")
	public void positionFiltered(String department) {
		baykarCareerPage.scrollDownForUntilSearchBox(department);
	}

	@When("Kullanıcı pozisyonları {string} ile arar")
	public void positionSearchFromCheckbox(String position) throws InterruptedException {
		baykarCareerPage.positionSearch(position);
	}

	@Then("Listelenen pozisyonların başlığı {string} içermelidir")
	public void verifyPositionAndDepartment(String position) {
		baykarCareerPage.departmentPositionVerify(position);
	}


	@When("Kullanıcı Search Box alanını görene kadar sayfayı aşağa kaydırır")
	public void scrollPage(){
		baykarCareerPage.scrollDownPageUntilSearchBox();
	}

}

