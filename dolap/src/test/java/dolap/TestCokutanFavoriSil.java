package dolap;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMap;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;

public class TestCokutanFavoriSil {
	public static URL url;
	public static DesiredCapabilities capabilities;
	public static AndroidDriver driver;

	// 1
	@BeforeSuite
	public void setupAppium() throws MalformedURLException {
		// 2
		final String URL_STRING = "http://127.0.0.1:4723/wd/hub";
		url = new URL(URL_STRING);
//3
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
		capabilities.setCapability("unicodeKeyboard", true);
		capabilities.setCapability("resetKeyboard", true);
		// 4
		driver = new AndroidDriver(url, capabilities);
		imlicitlyWait();
	}

	private void imlicitlyWait() {
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
	}

	/*
	 * Test Name: testBasicNoTitle() Given: Application is installed and launched
	 * And: User is on the home page When: User selects button "BASIC (NO TITLE)"
	 * Then: User should see popup with text
	 * "This app wants to access your location", "DISAGREE" and "AGREE" When: User
	 * selects "AGREE" Then: Popup should be dismissed
	 */
	@Test(enabled = true)
	public void testBasicNoTitle() throws Exception {

		driver.findElement(By.id("com.dolap.android:id/bottomNavMyAccount")).click();
		if (!Arrays.asList("@wpcgl", "@dolap1616214080")
				.contains(driver.findElement(By.id("com.dolap.android:id/userNameTitle")).getText())) {
			throw new Exception("I am not the one !!");
		}
		driver.findElement(By.id("com.dolap.android:id/bottomNavHomePage")).click();

		WebElement element = cokutanbArat();

		doProductCleanupJobs(element);
	}

	private void doProductCleanupJobs(WebElement element) {
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

	private void handleTwoListings(WebElement element) {

		clickByCoordinate(150, 900);
		cleanupProduct();
		driverBack();

		wait20Seconds(element);

		clickByCoordinate(700, 900);
		cleanupProduct();
		driverBack();
	}

	private void driverBack() {
		driver.navigate().back();
	}

	private void cleanupProduct() {

		try {
			WebElement fav = driver.findElement(By.id("com.dolap.android:id/imageViewFavorite"));
			wait20Seconds(fav);
			fav.click();
		} catch (NoSuchElementException ex) {
		}

		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		WebElement comment = findCommentBar();

		comment.click();

		//implicitlyWait();
		deleteCommentIfExists();

		imlicitlyWait();
		driverBack();
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
				
			}
		} catch (NoSuchElementException ex) {
		}
		
	}


	private void wait20Seconds(WebElement element) {
			WebDriverWait wait = new WebDriverWait(driver, 3);
			wait.until(ExpectedConditions.visibilityOf(element));
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

	public static void clickByCoordinate(int x, int y) {
		try {
			// [96,1195][1080,1339]
			TouchAction touchAction = new TouchAction(driver);
			// touchAction.press(PointOption.point(x,y)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(100))).release().perform();
			// touchAction.tap(PointOption.point(x,y)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(100))).release().perform();
			touchAction.tap(PointOption.point(x, y)).perform();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

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

}
