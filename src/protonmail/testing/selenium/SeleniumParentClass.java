package protonmail.testing.selenium;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumParentClass {

	public static String DRIVER_PATH_COMMON = "drivers";
	public static String DRIVER_PATH_CHROME = DRIVER_PATH_COMMON + "\\chromedriver.exe";
	public static String DRIVER_PATH_FIREFOX = DRIVER_PATH_COMMON + "\\geckodriver.exe";
	public static String DRIVER_PATH_IED = DRIVER_PATH_COMMON + "\\IEDriverServer.exe";
	public static String DRIVER_SELECTED = "chrome";

	public static String USERNAME = "SeleniumTests";
	public static String PASSWORD = "A1b2c3d4@";
	public static String PROTONMAIL_URL_COMMON = "https://beta.protonmail.com";
	public static String PROTONMAIL_URL_LOGIN = PROTONMAIL_URL_COMMON + "/login";
	public static String PROTONMAIL_URL_INBOX = PROTONMAIL_URL_COMMON + "/inbox";

	public static int TIMEOUT_DURATION = 30;

	public static WebDriver WebDriver;
	
	//private static String WatchedLog;
	
	public static void main(String[] args) throws Exception {

		try {

		} catch(Throwable e) {
			
			e.printStackTrace();
			System.out.println(" === SeleniumParentClass.main() - call to takeScreenShot()");
			String className = new Exception().getStackTrace()[0].getClassName();
			takeScreenShot( className );
			
		}

	}
	
	public static int generateRandomInt(int minNumber, int maxNumber) {
		
		Random r = new Random();
		int randomInt = r.nextInt(maxNumber + 1 - minNumber);

		return minNumber + randomInt;
		
	}
	
	@Before
	public void setUp() {
	// https://javacodehouse.com/blog/junit-tutorial/
		
		System.out.println( "[ INFO ] Executing " +  "Stickers.setUp()" );

		if (DRIVER_SELECTED.contains("chrome")) {
			ChromeOptions options = new ChromeOptions();
			System.setProperty("webdriver.chrome.driver", DRIVER_PATH_CHROME);
			options.setPageLoadStrategy(PageLoadStrategy.EAGER);
			options.addArguments("--disable-notifications");
			WebDriver = new ChromeDriver( options );
		}
		else if (DRIVER_SELECTED.contains("gecko") || DRIVER_SELECTED.contains("firefox")) {
			FirefoxOptions options = new FirefoxOptions();
			System.setProperty("webdriver.gecko.driver", DRIVER_PATH_FIREFOX);
			options.setPageLoadStrategy(PageLoadStrategy.EAGER);
			options.addArguments("--disable-notifications");
			WebDriver = new FirefoxDriver( options );
		}
		else if (DRIVER_SELECTED.startsWith("IE")) {
			InternetExplorerOptions options = new InternetExplorerOptions();
			System.setProperty("webdriver.ie.driver", DRIVER_PATH_IED);
			options.setPageLoadStrategy(PageLoadStrategy.EAGER);
			//options.addArguments("--disable-notifications");
			WebDriver = new InternetExplorerDriver( options );
		} else {
			System.err.println("\n\n");
			System.err.println("[ SYNTAX ERROR ] - >>>DRIVER_SELECTED=" + DRIVER_SELECTED + "<<<");
			System.err.println("[ SYNTAX ERROR ] - DRIVER_SELECTED variable must be equal to chrome or gecko / firefox or ie !");
			System.err.println("[ SYNTAX ERROR ] - Please modify DRIVER_SELECTED value prior to running the java class again.\n\n");
			System.exit(1);
		}

		//return driver;

	}

	@After
	public void tearDown() {
		// https://javacodehouse.com/blog/junit-tutorial/

		System.out.println( "[ INFO ] Executing " + "Stickers.tearDown()" );

		System.out.println("[ INFO ] TearDown() : selenium WebDriver closed.");
		WebDriver.close();
		System.out.println("[ INFO ] TearDown() : selenium WebDriver quitted.");
		WebDriver.quit();

	}

	public static void actionsMoveToWebElementClickBuildPerform(String xpath) {

		WebElement webElement = null;
		Actions actions = null;

		webElement = WebDriver.findElement(By.xpath( xpath ));
		actions = new Actions(WebDriver);
		actions.moveToElement(webElement).click().build().perform();
		//actions.moveToElement(webElement).click().build().perform();
		//actions.moveToElement(webElement).click().build().perform();

	}

	public static void actionsMoveToWebElementClickBuildPerform(String xpath, int idx) {

		WebElement webElement = null;
		Actions actions = null;

		webElement = WebDriver.findElements(By.xpath( xpath )).get( idx );
		actions = new Actions(WebDriver);
		actions.moveToElement(webElement).click().build().perform();
		//actions.moveToElement(webElement).click().build().perform();
		//actions.moveToElement(webElement).click().build().perform();

	}

	public static WebElement findElementsByXpathAndGetOneByIndex(String xpath, int idx) {

		WebElement webElement = WebDriver.findElements(By.xpath( xpath )).get( idx );

		return webElement;

	}

	public static void findElementsByXpathGetOneByIndexAndClick(String xpath, int idx) {

		WebDriver.findElements(By.xpath( xpath )).get( idx ).click();

	}

	public static String findElementsByXpathGetOneByIndexAndGetText(String xpath, int idx) {

		String text = WebDriver.findElements(By.xpath( xpath )).get( idx ).getText();
		
		return text;

	}
	
	public static int findElementsByXpathAndGetSize(String xpath) {

		int size = WebDriver.findElements(By.xpath( xpath )).size();

		return size;

	}

	public static String getDateNow() {

		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		return dateFormat.format(date);

	}

	public static void selectAllInboxEmails() throws InterruptedException {

		// other path 1 : "//button/*[text()='Select options']"
		// other path 2 : "//button/*[text()='Select All']"
		Thread.sleep(2000);
		actionsMoveToWebElementClickBuildPerform("//*[contains(@*, 'checkbox')]", 1);

	}

	public static void takeScreenShot( String className ){

		//String className = new Exception().getStackTrace()[0].getClassName();
		String fileName = "screenshots\\" + className + "_" + getDateNow() + ".png";
		System.err.println("\n\n");
		System.err.println("[ ERROR ] Taking a screenshot of last page where failure occurred !");
		System.err.println("[ ERROR ] fileName=" + fileName);
		
		byte[] bytes=((TakesScreenshot) WebDriver).getScreenshotAs(OutputType.BYTES);

		try (FileOutputStream fos = new FileOutputStream(fileName)) {
			try {
				fos.write(bytes);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void trashEmailsOfCurrentFolder () throws InterruptedException {

		selectAllInboxEmails();

		// move to trash
		WebDriver.findElement(By.xpath("//button[@*='Move to trash']")).click();

	}

	public static WebElement waitUntilElementToBeClickable( String xpath ) {

		WebDriverWait wait = new WebDriverWait(WebDriver, TIMEOUT_DURATION);
		WebElement webElement = null;

		webElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( xpath )));

		return webElement;

	}

	public static void waitUntilElementToBeClickableAndClick( String xpath ) {

		WebDriverWait wait = new WebDriverWait(WebDriver, TIMEOUT_DURATION);

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath( xpath ))).click();

	}

	public static String waitUntilElementToBeClickableAndGetText( String xpath ) {

		WebDriverWait wait = new WebDriverWait(WebDriver, TIMEOUT_DURATION);
		String text = "";

		text = wait.until(ExpectedConditions.elementToBeClickable(By.xpath( xpath ))).getText();

		return text;

	}


	// htps://www.edgewordstraining.co.uk/2018/12/18/junit-test-status-testwatcher/
	// https://stackoverflow.com/questions/18008241/run-a-testwatcher-before-the-after

}
