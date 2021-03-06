package browser;

import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Browser implements WebDriver {

    protected WebDriver driver;
    protected JavascriptExecutor jsExecutor;
    protected TakesScreenshot takesScreenshot;
    private static final int WAIT_FOR_ELEMENT_SECONDS = 20;
    private static final String SCREENSHOTS_NAME_TPL = "Screenshots/src";
    private Logger log = Logger.getLogger(Browser.class);

    public Browser(WebDriver driver) {
        this.jsExecutor = (JavascriptExecutor) driver;
        this.driver = driver;
        this.takesScreenshot = (TakesScreenshot) driver;
    }

    public void get(String url) {
        driver.get(url);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public List<WebElement> findElements(By by) {
        return driver.findElements(by);
    }

    public WebElement findElement(By by) {
        return driver.findElement(by);
    }

    public void acceptAlert() {
        try {
            driver.switchTo().alert().accept();
        } catch (NoAlertPresentException e) {
            log.info("Unable to accept the alert " + e);
        }
    }

    public void selectValueFromDropdownByText(WebElement dropdown, String text){
        Select select = new Select(dropdown);
        select.selectByVisibleText(text);
    }

    public boolean isElementExists(By by) {
        return !driver.findElements(by).isEmpty();
    }

    public String getPageSource() {
        return driver.getPageSource();
    }

    public void close() {
        driver.close();
    }

    public void quit() {
        driver.quit();
    }

    public Set<String> getWindowHandles() {
        return driver.getWindowHandles();
    }

    public String getWindowHandle() {
        return driver.getWindowHandle();
    }

    public TargetLocator switchTo() {
        return driver.switchTo();
    }

    public Navigation navigate() {
        return driver.navigate();
    }

    public Options manage() {
        return driver.manage();
    }

    public Object executeScript(String s, Object... objects) {
        return jsExecutor.executeScript(s, objects);
    }

    public Object executeAsyncScript(String s, Object... objects) {
        return jsExecutor.executeAsyncScript(s, objects);
    }

    public void highlightElement(WebElement element) {
        jsExecutor.executeScript("arguments[0].style.border='3px solid green'", element);
    }

    public void unhighlightElement(WebElement element) {
        jsExecutor.executeScript("arguments[0].style.border='0px'", element);
    }

    @Attachment(value ="Screenshot", type = "image/png")
    public byte[] getScreenshotAsBytes() {
        byte[] screenshot = takesScreenshot.getScreenshotAs(OutputType.BYTES);
        return screenshot;
    }

    public void saveScreenshot() {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            String screenshotName = SCREENSHOTS_NAME_TPL + System.nanoTime();
            File copy = new File(screenshotName + ".png");
            FileUtils.copyFile(screenshot, copy);
            log.info("Saved screenshot:" + screenshotName);
        } catch (IOException e) {
            log.error("Failed to make screenshot " + e.getMessage());
        }
    }

    public void switchToRequiredTabInBrowser(String tabTitle) {
        ArrayList<String> tabs = new ArrayList(driver.getWindowHandles());
        for (String currentTab : tabs) {
            driver.switchTo().window(currentTab);
            if (driver.getTitle().equals(tabTitle)) {
                break;
            }
        }
    }

    public void waitForElementVisible(WebElement element) {
        new WebDriverWait(driver, WAIT_FOR_ELEMENT_SECONDS).ignoring(StaleElementReferenceException.class, WebDriverException.class)
                .until(ExpectedConditions.visibilityOf(element));
    }


    public void waitForElementPresent(WebElement elem) {
        new WebDriverWait(driver, WAIT_FOR_ELEMENT_SECONDS)
                .until(ExpectedConditions.visibilityOf(elem));
    }


    public void waitForElementAndClick(WebElement element) {
        new WebDriverWait(this, WAIT_FOR_ELEMENT_SECONDS).ignoring(StaleElementReferenceException.class, WebDriverException.class)
                .until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }

    public void waitForElementAndClick(By by) {
        WebElement until = new WebDriverWait(this, WAIT_FOR_ELEMENT_SECONDS).ignoring(StaleElementReferenceException.class, WebDriverException.class)
                .until(ExpectedConditions.elementToBeClickable(by));
        this.findElement(by).click();
    }

    public void clickOnElementByJS(WebElement element) {
        JavascriptExecutor ex = (JavascriptExecutor) driver;
        ex.executeScript("arguments[0].click();", element);
    }

    public void scrollUsingJS() {
        JavascriptExecutor ex = (JavascriptExecutor) driver;
        ex.executeScript("window.scrollBy(0,document.body.scrollHeight)");
    }

    public void scrollUsingJSToTheElement(WebElement Element) {
        JavascriptExecutor ex = (JavascriptExecutor) driver;
        ex.executeScript("arguments[0].value=''", Element);
    }

    public void waitForElementVisible(By locator) {
        new WebDriverWait(driver, WAIT_FOR_ELEMENT_SECONDS).ignoring(StaleElementReferenceException.class, WebDriverException.class)
                .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public void waitForElementInvisible(By locator) {
        new WebDriverWait(driver, WAIT_FOR_ELEMENT_SECONDS).ignoring(StaleElementReferenceException.class, WebDriverException.class)
                .until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
}

