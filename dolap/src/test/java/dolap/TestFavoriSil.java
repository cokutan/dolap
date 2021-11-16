package dolap;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMap;

import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.touch.offset.PointOption;

public class TestFavoriSil {
	public static URL url;
	public static DesiredCapabilities capabilities;
	public static AndroidDriver<MobileElement> driver;

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
		//capabilities.setCapability("deviceName", "S7");
		//capabilities.setCapability("deviceId", "192.168.1.21:5555");
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "emulator-5554");
		//capabilities.setCapability(MobileCapabilityType.UDID, "OnePlus5");
		//capabilities.setCapability("platformVersion", "8.0");
		capabilities.setCapability("platformVersion", "7.1.1");
		capabilities.setCapability("appPackage", "com.dolap.android");
		capabilities.setCapability("appActivity", "com.dolap.android.init.ui.SplashActivity");

		capabilities.setCapability("automationName", "UiAutomator1");
	    capabilities.setCapability("noReset", true);
	    capabilities.setCapability("fullReset", false);
	    
		// 4
		driver = new AndroidDriver<MobileElement>(url, capabilities);
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
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

		// Find the button  BASIC (NO TITLE) and click it
		/*driver.findElement(By.id("com.dolap.android:id/button_login")).click();
	
		AndroidElement element = (AndroidElement) driver.findElement(By.id("com.dolap.android:id/edittext_email_username"));
		element.click();
		element.clear();
		driver.executeScript("mobile: performEditorAction", ImmutableMap.of("action", "w"));
		driver.executeScript("mobile: performEditorAction", ImmutableMap.of("action", "p"));
		driver.executeScript("mobile: performEditorAction", ImmutableMap.of("action", "c"));
		driver.executeScript("mobile: performEditorAction", ImmutableMap.of("action", "g"));
		driver.executeScript("mobile: performEditorAction", ImmutableMap.of("action", "l"));

	    element = (AndroidElement) driver.findElement(By.id("com.dolap.android:id/edittext_password"));
		element.click();
		element.clear();
		driver.executeScript("mobile: performEditorAction", ImmutableMap.of("action", "c"));
		driver.executeScript("mobile: performEditorAction", ImmutableMap.of("action", "o"));
		driver.executeScript("mobile: performEditorAction", ImmutableMap.of("action", "k"));
		driver.executeScript("mobile: performEditorAction", ImmutableMap.of("action", "u"));
		driver.executeScript("mobile: performEditorAction", ImmutableMap.of("action", "t"));
		driver.executeScript("mobile: performEditorAction", ImmutableMap.of("action", "a"));
		driver.executeScript("mobile: performEditorAction", ImmutableMap.of("action", "n"));
		driver.executeScript("mobile: performEditorAction", ImmutableMap.of("action", "/3"));
		driver.executeScript("mobile: performEditorAction", ImmutableMap.of("action", "/4"));


		element = (AndroidElement) driver.findElement(By.id("com.dolap.android:id/button_login"));
		element.click();
*/		
		driver.findElement(By.id("com.dolap.android:id/bottomNavMyAccount")).click();
		if(! driver.findElement(By.id("com.dolap.android:id/accountName")).getText().equals("wpcgl")) {
			throw new Exception("I am not the one !!");
		}

		
		driver.findElement(By.id("com.dolap.android:id/myFavoritesNavigationLayout")).click();
		
		while(driver.findElements(By.id("com.dolap.android:id/imageViewLike")).size()>0) {
			MobileElement element = driver.findElement(By.id("com.dolap.android:id/imageViewLike"));
			element.click();

	        driver.navigate().back();
	    	driver.findElement(By.id("com.dolap.android:id/myFavoritesNavigationLayout")).click();

		}
		
		
		
		

	}
	
	public static void clickByCoordinate(int x, int y){
		  try {
			  // [96,1195][1080,1339]
		    TouchAction touchAction = new TouchAction(driver);
		    //touchAction.press(PointOption.point(x,y)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(100))).release().perform();
		    //touchAction.tap(PointOption.point(x,y)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(100))).release().perform();
		    touchAction.tap(PointOption.point(x, y)).perform();
		  }catch (Exception e){
		    e.printStackTrace();
		  }
		}

}
