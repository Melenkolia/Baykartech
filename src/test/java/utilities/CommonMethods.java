package utilities;

import org.apache.commons.io.FileUtils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Random;

import static utilities.DriverOptions.driver;


public class CommonMethods {

    public CommonMethods(WebDriver webDriver) {
    }

    public CommonMethods() {
    }

    public static void pageLoad(WebDriver driver, String urlPath) {
        CommonData commonData = new CommonData();
        driver.get(commonData.getCommonData("url") + urlPath);
    }
    public static void waitInSeconds(int secVal) {
        try {
            Thread.sleep(secVal * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void takeScreenShot() {
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(srcFile, new File("snap" + System.currentTimeMillis() + ".png"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public synchronized static void waitForElementVisibility(WebElement element, int timeOut, String elementName) {
        try {
            Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(timeOut)).pollingEvery(Duration.ofSeconds(1))
                    .ignoring(NoSuchElementException.class);
            wait.until(ExpectedConditions.visibilityOf(element));
        } catch (Exception e) {
            System.out.println("element is not visible - " + elementName);
        }
    }

    public static String genRandNumber() {
        Random r = new Random(System.currentTimeMillis());
        return (String.valueOf((1 + r.nextInt(2)) * 10000 + r.nextInt(10000)));
    }

    public static String genRandText() {

        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder(8);
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        return output;
    }

    public static void handleCaptcha(WebElement element, WebElement captchaCheckbox) throws InterruptedException {
        driver.switchTo().frame(element);
        captchaCheckbox.click();

    }

    public static void hoverToElement(String loc, WebDriver driver) {
        Actions ac = new Actions(driver);
        ac.moveToElement(driver.findElement(By.xpath(loc))).build().perform();
    }

    public static void switchWindow(WebElement handle) {
        String winHandle = driver.getWindowHandle();
        driver.switchTo().window(winHandle);

    }

    public static void assertEqual(String actual, String expected) {
        if (!actual.equals(expected)) {
            throw new AssertionError("Assertion failed! Actual: " + actual + ", Expected: " + expected);
        }
    }

    public static String readExcelData(String filePath, String sheetName, int rowNum, int colNum) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheet(sheetName);
        System.out.println("Sheet Name: " + sheet);


        XSSFRow row = (XSSFRow) sheet.getRow(rowNum);
        XSSFCell cell = (XSSFCell) ((Row) row).getCell(colNum);

        String cellData = getCellValueAsString(cell);

        workbook.close();
        fileInputStream.close();

        System.out.println(cellData);

        return cellData;
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }


        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    protected WebElement waitForVisibility(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static void closeBrowser() {
        driver.quit();
    }

    public static boolean checkIfElementExist(String elementXpath) {
        try {
            if (driver.findElements(By.xpath(elementXpath)).size() != 0) {
                return true;
            } else return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static void click(WebElement elementXpath) {
        elementXpath.click();
    }

//    public WebElement find(WebElement element) {
//        return webDriver.findElement(element);
//    }

    public Boolean isDisplayed(WebElement element) {
        return (element).isDisplayed();
    }

    public void waitForElement(String elementID) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15L));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(elementID)));
    }

    public static void sendKeys(String elementXpath, String text, Boolean tabOut) {
        WebElement element = driver.findElement(By.xpath(elementXpath));
        element.sendKeys(text);
        if (tabOut) {
            element.sendKeys(Keys.TAB);
        }
    }

    public static void sendKeys(String elementXpath, String text) {
        sendKeys(elementXpath, text, false);
    }

    public static void clearTextInputField(String elementXpath) {
        WebElement element = driver.findElement(By.xpath(elementXpath));
        element.clear();
    }

    public static void scrollToBottomOfPage() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,999999999999999)", "");
        waitInSeconds(1);
    }

    public void scrollIntoView(WebElement webElement) {
//         driver.fi((By) webElement);

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'center'});", webElement);

    }

    public void scrollUp(By elementLocator) {
        WebElement element = driver.findElement(elementLocator);

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'start', inline: 'start'});", element);
    }

    public static void doubleClick(WebElement loc, WebDriver driver) {
        waitInSeconds(2);
        Actions actions = new Actions(driver);
        actions.doubleClick(loc);
        waitInSeconds(2);
    }

}
