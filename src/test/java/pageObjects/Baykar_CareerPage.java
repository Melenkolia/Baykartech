package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import utilities.CommonMethods;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class Baykar_CareerPage extends CommonMethods {

    public WebDriver driver;
    SoftAssert softAssert = new SoftAssert();

    public Baykar_CareerPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);

    }

    @FindBy(how = How.XPATH, using = "//*[@id=\"search\"]")
    WebElement searchBox;

    public void scrollDownPageUntilSearchBox(){

        scrollIntoView(searchBox);
    }

    public void scrollDownForUntilSearchBox(String birimAdi) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            List<WebElement> birimEtiketleri = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                    By.cssSelector("ul#myUL2 li label")));

            System.out.println("Sayfadaki Birimler:");
            birimEtiketleri.forEach(birim -> System.out.println(" - " + birim.getText().trim()));

            boolean flag = false;

            for (WebElement birim : birimEtiketleri) {
                String text = birim.getText().trim();
                if (text.equalsIgnoreCase(birimAdi) || text.toLowerCase().contains(birimAdi.toLowerCase())) {
                    System.out.println("Filtre uygulanıyor: " + text);
                    birim.click();
                    flag = true;

                    Thread.sleep(1500);
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Birim filtresi sırasında beklenmeyen bir hata oluştu: " + e.getMessage());
        }
    }

    public void positionSearch(String keyword) throws InterruptedException {
        WebElement searchBox = driver.findElement(By.xpath("//*[@id=\"search\"]"));
        searchBox.clear();
        searchBox.sendKeys(keyword);
        Thread.sleep(1500);
    }

    public void departmentPositionVerify(String position) {
        List<WebElement> pozisyonlar = driver.findElements(By.cssSelector("div.workBox h3"));

        Assert.assertFalse(pozisyonlar.isEmpty(), "Hiç pozisyon listelenmedi!");

        List<String> basliklar = pozisyonlar.stream()
                .map(element -> element.getText().toLowerCase())
                .collect(Collectors.toList());

        boolean enAzBirTaneVar = basliklar.stream()
                .anyMatch(baslik -> baslik.contains(position.toLowerCase()));

        softAssert.assertTrue(enAzBirTaneVar,
                "Hiçbir başlık '" + position + "' kelimesini içermiyor. Mevcut başlıklar: " + basliklar);

        softAssert.assertAll();
    }
}
