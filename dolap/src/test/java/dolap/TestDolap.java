package dolap;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMap;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;

public class TestDolap {
	public static URL url;
	public static DesiredCapabilities capabilities;
	public static AndroidDriver<MobileElement> driver;

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
		driver = new AndroidDriver<MobileElement>(url, capabilities);
		driver.manage().timeouts().implicitlyWait(7, TimeUnit.SECONDS);
	}

	@Test(enabled = true)
	public void testBasicNoTitle() throws Exception {
		
		driver.findElement(By.id("com.dolap.android:id/bottomNavMyAccount")).click();
		if(! driver.findElement(By.id("com.dolap.android:id/accountName")).getText().equals("wpcgl")) {
			throw new Exception("I am not the one !!");
		}
		driver.findElement(By.id("com.dolap.android:id/bottomNavHomePage")).click();

		MobileElement element = cokutanbArat();

		doProductJobs(element);

	}

	private void doProductJobs(MobileElement element) {
		boolean endOfPage = false;
		String previousPageSource = driver.getPageSource();
		while (!endOfPage) {
			handleTwoListings(element);
			wait20Seconds(element);
			// scrolToOtherDual();
			swipeVertical(0.1, 0.59, 0.5, 3000);
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


	private MobileElement cokutanbArat() {
		MobileElement element = driver.findElement(By.id("com.dolap.android:id/textViewSearchBar"));
		element.click();
		element.sendKeys("@cokutanb");
		driver.executeScript("mobile: performEditorAction", ImmutableMap.of("action", "Search"));
		return element;
	}

	private void handleTwoListings(MobileElement element) {

		clickByCoordinate(200, 1000);
		loveProduct();
		driverBack();

		wait20Seconds(element);

		clickByCoordinate(700, 1000);
		loveProduct();
		driverBack();
	}

	private void loveProduct() {

		driver.findElement(By.id("com.dolap.android:id/imageViewFavorite")).click();
		swipeVertical(0.1, 0.8, 0.5, 5000);
		swipeVertical(0.1, 0.8, 0.5, 5000);
		driver.findElement(By.id("com.dolap.android:id/buttonProductCommentsNavigator")).click();
		MobileElement editTextComment = driver.findElement(By.id("com.dolap.android:id/editTextComment"));
		editTextComment.click();

		editTextComment.sendKeys("Biraz indirim olur mu? "
				+ new SimpleDateFormat("yyyy/MM/dd HH:mm").format(Calendar.getInstance().getTime()));

		driver.findElement(By.id("com.dolap.android:id/buttonSend")).click();

		driver.findElement(By.id("com.dolap.android:id/imageViewBackButton")).click();

	    

		try {
			MobileElement buttonBid = driver.findElement(By.id("com.dolap.android:id/buttonBid"));
			if (buttonBid.isDisplayed()) {
				buttonBid.click();
				try {
					MobileElement buttonBidProduct = driver.findElement(By.id("com.dolap.android:id/buttonBidProduct"));
					if (buttonBidProduct.isDisplayed()) {
						buttonBidProduct.click();
					}
				} catch (NoSuchElementException ex) {
					
				}
				driverBack();
			}
		} catch (NoSuchElementException ex) {
		}
	}

	private void driverBack() {
		driver.navigate().back();
	}

	private void wait20Seconds(MobileElement element) {
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.visibilityOf(element));
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
