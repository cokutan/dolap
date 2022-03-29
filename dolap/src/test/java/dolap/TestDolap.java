package dolap;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMap;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;

public class TestDolap {
	public static URL url;
	public static DesiredCapabilities capabilities;
	public static AndroidDriver driver;

	// 1
	@BeforeSuite
	public void setupAppium() throws MalformedURLException {
		// 2
		final String URL_STRING = "http://127.0.0.1:4723/wd/hub";
		url = new URL(URL_STRING);
		capabilities = new DesiredCapabilities();

		capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "");
		capabilities.setCapability("platformName", "ANDROID");
		capabilities.setCapability("deviceName", "S7");
		capabilities.setCapability("deviceId", "192.168.1.21:5555");
		capabilities.setCapability("platformVersion", "8.0");
		capabilities.setCapability("appPackage", "com.dolap.android");
		capabilities.setCapability("appActivity", "com.dolap.android.init.ui.SplashActivity");

		capabilities.setCapability("automationName", "UiAutomator1");
		capabilities.setCapability("noReset", true);
		capabilities.setCapability("fullReset", false);

		// 4
		driver = new AndroidDriver(url, capabilities);
		implicitlyWait();
	}

	private void implicitlyWait() {
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
	}

	@Test(enabled = true)
	public void testBasicNoTitle() throws Exception {

		driver.findElement(By.id("com.dolap.android:id/bottomNavMyAccount")).click();
		if (!Arrays.asList("@wpcgl", "@dolap1616214080")
				.contains(driver.findElement(By.id("com.dolap.android:id/userNameTitle")).getText())) {
			throw new Exception("I am not the one !!");
		}
		driver.findElement(By.id("com.dolap.android:id/bottomNavHomePage")).click();

		WebElement element = cokutanbArat();

		doProductJobs(element);

	}

	private void doProductJobs(WebElement element) {
		boolean endOfPage = false;
		String previousPageSource = driver.getPageSource();
		while (!endOfPage) {
			handleTwoListings(element);
			wait20Seconds(element);
			swipeVertical(0.1, 0.487, 0.5, 1000);
			wait20Seconds(element);
			endOfPage = previousPageSource.equals(driver.getPageSource());
			previousPageSource = driver.getPageSource();
		}
	}

	/**
	 * Performs small swipe from the center of screen
	 *
	 * @param dir the direction of swipe
	 * @version java-client: 7.3.0
	 **/
	public static void swipeVertical(double startPercentage, double finalPercentage, double anchorPercentage,
			int duration) {
		Dimension size = driver.manage().window().getSize();
		int anchor = (int) (size.width * anchorPercentage);
		int startPoint = (int) (size.height * startPercentage);
		int endPoint = (int) (size.height * finalPercentage);
		Logger.getGlobal().log(Level.INFO, "start y:" + startPoint + " end y" + endPoint + " x" + anchor);
		new TouchAction(driver).press(PointOption.point(anchor, endPoint))
				.waitAction(WaitOptions.waitOptions(Duration.ofMillis(duration)))
				.moveTo(PointOption.point(anchor, startPoint)).release().perform();
	}

	private WebElement cokutanbArat() {
		WebElement element = driver.findElement(By.id("com.dolap.android:id/textViewSearchBar"));
		element.click();
		element.sendKeys("@cokutanb");
		driver.executeScript("mobile: performEditorAction", ImmutableMap.of("action", "Search"));

		WebElement sort = driver.findElement(By.id("com.dolap.android:id/viewSortArea"));
		sort.click();

		driver.findElement(By.xpath("//android.widget.CheckedTextView[@index='3']")).click();
		return element;
	}

	private void handleTwoListings(WebElement element) {

		clickByCoordinate(150, 900);
		loveProduct();
		driverBack();

		wait20Seconds(element);

		clickByCoordinate(700, 900);
		loveProduct();
		driverBack();
	}

	private void loveProduct() {

		try {
			WebElement fav = driver.findElement(By.id("com.dolap.android:id/imageViewFavorite"));
			wait20Seconds(fav);
			fav.click();

		} catch (NoSuchElementException ex) {

		}
		WebElement comment = null;

		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		comment = findCommentBar();

		comment.click();

		//implicitlyWait();
		deleteCommentIfExists();
		
		WebElement editTextComment = driver.findElement(By.id("com.dolap.android:id/editTextComment"));
		editTextComment.click();

		editTextComment.sendKeys(RandomStringUtils.randomAlphabetic(10));

		driver.findElement(By.id("com.dolap.android:id/buttonSend")).click();

		driver.findElement(By.id("com.dolap.android:id/imageViewBackButton")).click();

	/*	try {
			WebElement buttonBid = driver.findElement(By.id("com.dolap.android:id/buttonBid"));
			if (buttonBid.isDisplayed()) {
				buttonBid.click();
				try {
					WebElement buttonBidProduct = driver.findElement(By.id("com.dolap.android:id/buttonBidProduct"));
					if (buttonBidProduct.isDisplayed()) {
						buttonBidProduct.click();
					}
				} catch (NoSuchElementException ex) {

				}
				driverBack();
			}
		} catch (NoSuchElementException ex) {
		}*/

		implicitlyWait();

	}

	private WebElement findCommentBar() {
		WebElement comment = null;
		try {
			comment = driver.findElement(By.id("com.dolap.android:id/buttonProductCommentsNavigator"));
		} catch (NoSuchElementException ex) {

			while (comment == null) {
				swipeVertical(0.1, 0.5, 0.5, 1000);
				try {
					comment = driver.findElement(By.id("com.dolap.android:id/buttonProductCommentsNavigator"));
				} catch (NoSuchElementException ex2) {

				}
			}
		}
		return comment;
	}

	private void deleteCommentIfExists() {
		try {
			WebElement deleteButton = driver.findElement(By.id("com.dolap.android:id/textViewDelete"));
			if (deleteButton.isDisplayed()) {
				driver.findElement(By.id("com.dolap.android:id/textViewDelete")).click();
				driver.findElement(By.id("com.dolap.android:id/button_action_two")).click();
				//driverBack();
			}
		} catch (NoSuchElementException ex) {
		}
		/*try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}

	private void driverBack() {
		driver.navigate().back();
	}

	private void wait20Seconds(WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, 7);
		try {
			wait.until(ExpectedConditions.visibilityOf(element));
		} catch (TimeoutException ex) {
			driver.navigate().back();
		}
	}

	public static void clickByCoordinate(int x, int y) {
		try {
			// [96,1195][1080,1339]
			TouchAction touchAction = new TouchAction(driver);
			touchAction.tap(PointOption.point(x, y)).perform();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
