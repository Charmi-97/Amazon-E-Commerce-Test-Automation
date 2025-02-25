package com.amazon.tests;

import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class AmazonTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    @BeforeClass
    public void openBrowser() {
    	System.setProperty("webdriver.edge.driver", "C:\\Users\\Admin\\Downloads\\edgedriver_win64\\msedgedriver.exe"); // Set EdgeDriver path 
        driver = new EdgeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30)); // Increased timeout
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Implicit wait
        js = (JavascriptExecutor) driver;
    }

    @Test
    public void searchProduct() {
        driver.get("https://www.amazon.com");

        // Search for the product
        WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));
        searchBox.sendKeys("Latitude 3550 15 Business AI Laptop");
        searchBox.submit();

        // Wait for search results and locate the product
        By productLocator = By.xpath("//div[contains(@data-component-type, 's-search-result')]//span[contains(text(), 'Latitude 3550')]");
        WebElement product = wait.until(ExpectedConditions.elementToBeClickable(productLocator));
        product.click(); // Click on the product

        // Wait for product page load
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("titleSection")));

        // Handle popups (if any)
        handlePopups();

        // Change quantity to 3
        WebElement quantity = driver.findElement(By.id("quantity"));
        Select select = new Select(quantity);
        select.selectByValue("3");

        // Click "Add to Cart"
        WebElement addToCart = wait.until(ExpectedConditions.elementToBeClickable(By.id("add-to-cart-button")));
        js.executeScript("arguments[0].click();", addToCart);

        // Wait for confirmation message
        WebElement cartConf = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sw-atc-details-single-container")));
        Assert.assertTrue(cartConf.isDisplayed(), "Add to cart failed.");
    }

    private void handlePopups() {
        try {
            WebElement closePopup = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("attach-close_sideSheet-link")));
            closePopup.click();
        } catch (Exception ignored) {
        }
    }

    @AfterClass
    public void closeBrowser() {
        driver.quit();
    }
}
