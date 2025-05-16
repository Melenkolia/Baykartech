package pageObjects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import utilities.CommonMethods;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.openqa.selenium.support.ui.ExpectedConditions.not;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

public class Baykar_MainPage extends CommonMethods {

    public WebDriver driver;
    SoftAssert softAssert = new SoftAssert();
    private WebDriverWait wait;

    public Baykar_MainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);

    }
    @FindBy(how = How.XPATH, using = "//button[contains(text(),'AÇIK POZİSYONLAR')]")
    WebElement baykarLogo;
    @FindBy(how = How.CSS, using = "a[class='nav-link ']")
    WebElement languagesItems;
    @FindBy(how = How.XPATH, using = "//a[contains(text(),'EN')]")
    WebElement languages;
    @FindBy(how = How.XPATH, using = "//button[contains(text(),'AÇIK POZİSYONLAR')]")
    WebElement openPositionButton1;
    @FindBy(how = How.XPATH, using = "//button[contains(text(),'Açık Pozisyonlar')]")
    WebElement openPositionButton2;

    public void verifymainPage() {
        isDisplayed(baykarLogo);
    }
    public void selectENLanguage(){
        click(languagesItems);
    }
    public void verifyLanguages(){
        isDisplayed(languages);
    }
    public void clickOpenPositionButton1(){
        click(openPositionButton1);
        waitInSeconds(1);
    }
    public void clickOpenPositionButton2(){
        click(openPositionButton2);
        waitInSeconds(1);
    }


    public List<String> extractNavbarLinks() {
        List<String> linkList = new ArrayList<>();
        List<String> navText = new ArrayList<>();

        String html = driver.getPageSource();

        Document doc = Jsoup.parse(html);
        Elements navbarLinks = doc.select(".navbar-nav a.nav-link, .navbar-nav .dropdown-item");

        String currentBase = driver.getCurrentUrl();

        for (Element link : navbarLinks) {
            String href = link.attr("href");
            String text = link.text().trim(); // <a> içindeki görünen metin

            if (href != null && !href.equals("#") && !href.startsWith("javascript")) {
                if (!href.startsWith("http")) {
                    if (href.startsWith("/")) {
                        href = currentBase.split("/tr")[0] + href;
                    } else {
                        href = currentBase + href;
                    }
                }
                if (!linkList.contains(href)) {
                    linkList.add(href);
                    navText.add(text);

                }
            }
        }
        return linkList;
    }

    public void verifyAllNavbarLinks() {
        List<String> links = extractNavbarLinks();

        for (String link : links) {
            driver.get(link);
            String title = driver.getTitle();
            System.out.println("Title : " + title);
            waitInSeconds(1);
            try {
                wait.until(not(titleIs("")));
                System.out.println("Sayfa açıldı: " + driver.getTitle() + " | URL: " + link);
            } catch (Exception e) {
                System.out.println("Sayfa açılamadı: " + " | URL: "+ link);
            }
        }

    }

    public void checkNavbarItemsFromMainPage(List<String> expectedItems) {
        try {
            String pageSource = driver.getPageSource();
            Document doc = Jsoup.parse(pageSource);
            Elements links = doc.select(".offcanvas-body a");
            List<String> actualItems = links.stream()
                    .map(Element::text)
                    .map(String::trim)
                    .filter(text -> !text.isEmpty())
                    .distinct()
                    .collect(Collectors.toList());

            List<String> trimmedExpectedItems = expectedItems.stream()
                    .map(String::trim)
                    .collect(Collectors.toList());

            actualItems.forEach(System.out::println);

            softAssert.assertEquals(actualItems.size(), trimmedExpectedItems.size(), "Navbar öğelerinin sayısı eşleşmiyor");

            for (String expected : trimmedExpectedItems) {
                softAssert.assertTrue(actualItems.contains(expected), "Navbar öğesi eksik veya hatalı: " + expected);
            }

            softAssert.assertAll();

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Navbar öğeleri kontrol edilirken hata oluştu: " + e.getMessage());
        }
    }


}