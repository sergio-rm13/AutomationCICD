package rahulshettyacademy.tests;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.AssertJUnit;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chromium.AddHasLaunchApp;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;
import rahulshettyacademy.TestComponents.BaseTest;
import rahulshettyacademy.pageobjects.CartPage;
import rahulshettyacademy.pageobjects.CheckoutPage;
import rahulshettyacademy.pageobjects.ConfirmationPage;
import rahulshettyacademy.pageobjects.LandingPage;
import rahulshettyacademy.pageobjects.OrderPage;
import rahulshettyacademy.pageobjects.ProductCatalogue;

public class SubmitOrderTest extends BaseTest {
	String productName = "ZARA COAT 3";

	@Test(dataProvider = "getData", groups = { "Purchase" })
	public void submitOrder(HashMap<String, String> input) throws InterruptedException, IOException {
		// TODO Auto-generated method stub

		ProductCatalogue productCatalogue = landingPage.loginApplication(input.get("email"), input.get("password"));

		List<WebElement> products = productCatalogue.getProductList();

		productCatalogue.addProductToCart(input.get("product").toString());
		CartPage cartPage = productCatalogue.goToCartPage();

		Boolean match = cartPage.VerifyProductDisplay(input.get("product"));
		Assert.assertTrue(match);
		CheckoutPage checkoutPage = cartPage.goToCheckout();
		checkoutPage.selectCountry("india");
		ConfirmationPage confirmationPage = checkoutPage.submitOrder();

		Assert.assertTrue(confirmationPage.getConfirmationMessage().equalsIgnoreCase("THANKYOU FOR THE ORDER."));

		/*
		 * WebElement element = driver.findElement(By.cssSelector(".action__submit"));
		 * JavascriptExecutor js2 = (JavascriptExecutor) driver;
		 * js2.executeScript("arguments[0].click();", element);
		 */
	}

	// To verify ZARA COAT 3 is displaying in orders page

	@Test(dependsOnMethods = { "submitOrder" })
	public void OrderHistoryTest() {

		ProductCatalogue productCatalogue = landingPage.loginApplication("sergioruiz@gmail.com", "Temporal1");
		OrderPage orderPage = productCatalogue.goToOrderPage();
		Assert.assertTrue(orderPage.VerifyOrderDisplay(productName));
	}
	
	public String getScreenshot(String testCaseName) throws IOException {
		
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		File file = new File(System.getProperty("user.dir")+"\\reports\\"+ testCaseName + ".png");
		FileUtils.copyFile(source, file);
		return System.getProperty("user.dir")+"\\reports\\"+ testCaseName + ".png";
	}

	
	//Extend Reports
	
	@DataProvider
	public Object[][] getData() throws IOException {

		/*
		 * HashMap<String, String> map1 = new HashMap<String, String>();
		 * map1.put("email", "sergioruiz@gmail.com"); map1.put("password", "Temporal1");
		 * map1.put("product", "ZARA COAT 3");
		 * 
		 * HashMap<String, String> map2 = new HashMap<String, String>();
		 * map2.put("email", "sergioruiz@gmail.com"); map2.put("password", "Temporal1");
		 * map2.put("product", "ADIDAS ORIGINAL");
		 */

		List<HashMap<String, String>> data = getJsonDataToMap(
				System.getProperty("user.dir") + "\\src\\test\\java\\rahulshettyacademy\\data\\PurchaseOrder.json");
		return new Object[][] { { data.get(0) }, { data.get(1) } };
	}

}
