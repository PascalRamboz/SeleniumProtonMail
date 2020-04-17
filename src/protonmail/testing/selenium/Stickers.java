package protonmail.testing.selenium;

import org.junit.Test;
import org.junit.Assert;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;


public class Stickers extends SeleniumParentClass {

	// FOLDER_QTY (MAX on the 14th of April 2020) = 3 
	public static int FOLDER_QTY = 4;
	public static int EMAIL_QTY_PER_FOLDER = 2;

	// LABEL_QTY (MAX on the 14th of April 2020) = 20 
	public static int LABEL_QTY = 21;
	public static int EMAIL_QTY_PER_LABEL = 3;

	public static String FOLDER_PATTERN = "Folder_";
	public static String LABEL_PATTERN = "Label_";
	public static String MULTI_STICKER_PATTERN = "MultiSticker_";
	public static String EMAIL_PATTERN = "Email_";

	// https://javacodehouse.com/blog/junit-tutorial/
	public static void main(String[] args) throws Exception {

		Stickers stick = new Stickers();

		try {

			stick.setUp();
			stick.scenario();
			stick.tearDown();

		} catch(Throwable e) {

			e.printStackTrace();

			System.out.println(" === Stickers.main() - call to takeScreenShot()");
			String className = new Exception().getStackTrace()[0].getClassName();
			stick.takeScreenShot( className );

			stick.tearDown();

		}

	}

	@Test
	public void scenario() throws Exception, InterruptedException {

		try {

			System.out.println("[ INFO ] Executing Stickers.scenario()");

			// https://www.selenium.dev/documentation/en/
			// https://www.guru99.com/first-webdriver-script.html
			// https://beta.protonmail.com/login

			//findElementsByXpathGetOneByIndexAndClick( "//button[text()='Add folder", 0 );

			// ---------------------------------------------------------------
			// LOG IN SECTION 
			
			WebDriver.get( PROTONMAIL_URL_LOGIN );

			waitUntilElementToBeClickable( "//button[@type='submit']" );

			String textOfSubmitButton = findElementsByXpathGetOneByIndexAndGetText( "//button[@type='submit']", 0 );
			String textOfLoginButton = findElementsByXpathGetOneByIndexAndGetText( "//button[@id='login_btn']", 0 );
			Assert.assertEquals( textOfSubmitButton, textOfLoginButton );

			WebDriver.findElement(By.id("username")).sendKeys( USERNAME );
			WebDriver.findElement(By.id("password")).sendKeys( PASSWORD );
			waitUntilElementToBeClickableAndClick( "//button[@type='submit']" );

			// ---------------------------------------------------------------
			// WELCOME PAGE 

			// wait and click on close inner prompt window 
			waitUntilElementToBeClickableAndClick( "//button[@title='Close']" );

			String textOfWelcomePage = findElementsByXpathGetOneByIndexAndGetText( "//h1[@*='Title']", 0 );
			Assert.assertEquals( "Welcome, " + USERNAME + "!", textOfWelcomePage );

			// ---------------------------------------------------------------
			// TRASH PREVIOUS EMAILS INBOX 
			// select and delete all emails
			trashEmailsOfCurrentFolder();

			String textOfInbox = findElementsByXpathGetOneByIndexAndGetText( "//h3[@*='inbox']", 0 );
			Assert.assertEquals( "Your inbox is empty", textOfInbox );

			// ---------------------------------------------------------------
			// DELETE ALL STICKERS (i.e. ALL FOLDERS AND ALL LABELS)
			deleteAllStickers();
			Thread.sleep(2000);
			// ---------------------------------------------------------------

			// ---------------------------------------------------------------
			// FOLDERS SECTION		
			createAllStickersForAGivenType("folder", FOLDER_QTY, FOLDER_PATTERN);

			forEachStickerForEachEmailComposeSendAndAssignASingleStickerToIt(
					"folder", 
					FOLDER_PATTERN, 
					FOLDER_QTY, 
					EMAIL_PATTERN, 
					EMAIL_QTY_PER_FOLDER
					);
			// ---------------------------------------------------------------

			// ---------------------------------------------------------------
			// LABELS SECTION 
			createAllStickersForAGivenType("label", LABEL_QTY, LABEL_PATTERN);

			forEachStickerForEachEmailComposeSendAndAssignASingleStickerToIt(
					"label", 
					LABEL_PATTERN, 
					LABEL_QTY, 
					EMAIL_PATTERN, 
					EMAIL_QTY_PER_LABEL
					);

			int[][] allEmailsToMultiLabelAssignmentArray = createTwoDimensionalArrayOfIntForMultiLabelAssignmentPurpose( LABEL_QTY , true);

			forEachRowOfTwoDimMultiLabelAssignmentArrayComposeSendAndAssignOneOrSeveralStickersToIt(
					MULTI_STICKER_PATTERN,
					"label", 
					LABEL_PATTERN, 
					LABEL_QTY, 
					EMAIL_PATTERN, 
					EMAIL_QTY_PER_LABEL,
					allEmailsToMultiLabelAssignmentArray
					);
			// ---------------------------------------------------------------

			// ---------------------------------------------------------------
			// LOGOUT SECTION
			WebDriver.get( PROTONMAIL_URL_LOGIN );
			
			waitUntilElementToBeClickable( "//button[@type='submit']" );
			
			textOfSubmitButton = findElementsByXpathGetOneByIndexAndGetText( "//button[@type='submit']", 0 );
			textOfLoginButton = findElementsByXpathGetOneByIndexAndGetText( "//button[@id='login_btn']", 0 );
			Assert.assertEquals( textOfSubmitButton, textOfLoginButton );
			// ---------------------------------------------------------------


		} catch(Throwable e) {

			e.printStackTrace();
			System.out.println(" === Stickers.scenario() - call to takeScreenShot()");
			String className = new Exception().getStackTrace()[0].getClassName();
			takeScreenShot( className );

		}

	}

	public static int[][] createTwoDimensionalArrayOfIntForMultiLabelAssignmentPurpose(int size, boolean displayOrNot) {

		int[][] multiLabelAssignmentArray = new int[ size ][ size ];

		for (int row=0; row<=size-1; row++) {
			for (int col=0; col<=size-1; col++) {
				if (col<=row) {
					multiLabelAssignmentArray[row][col] = 1;
				} else {
					multiLabelAssignmentArray[row][col] = 0;
				}
				//System.out.println("multiLabelAssignmentArray[" + row + "][" + col + "]=" + multiLabelAssignmentArray[row][col]);
			}
		}

		String line = "";
		if (displayOrNot == true) {
			for (int row=0; row<=size-1; row++) {
				line = "";
				for (int col=0; col<=size-1; col++) {
					line += multiLabelAssignmentArray[row][col] + "|";
				}
				System.out.println( line );
			}

		}

		return multiLabelAssignmentArray;

	}

	public static void forEachStickerForEachEmailComposeSendAndAssignASingleStickerToIt(String stickerType, String stickerPattern, int stickerQty, String emailPattern, int emailQty) throws InterruptedException {

		String stickerName = "";
		String emailRef = "";
		String emailRecipient= "";
		String emailSubject = "";
		String emailMessage= "";

		// foreach label
		for (int iter_l=1; iter_l<=stickerQty; iter_l++) {

			// go to inbox 
			WebDriver.get(PROTONMAIL_URL_INBOX);

			stickerName = stickerPattern + iter_l;

			// foreach email
			for (int iter_e=1; iter_e<=emailQty; iter_e++) {

				emailRef = stickerPattern + iter_l + " " + emailPattern + iter_e;

				emailRecipient = USERNAME + "@protonmail.com";
				emailSubject = "SUBJECT : " + emailRef;
				emailMessage = "BODY : " + emailRef;

				composeAndSendEmail(emailRecipient, emailSubject, emailMessage);
				clickOnCheckboxOfLastReceivedEmailInbox();
				assignAStickerToSelectedEmail(stickerType, stickerName);
				selectAndDeselectAllEmailsInbox();

			}

			// foreach sticker (folder or label), check email sujects which should be displayed 
			checkEmailSubjectsInsideOfASticker(stickerName, emailQty, emailPattern);

		}

	}

	public static void forEachRowOfTwoDimMultiLabelAssignmentArrayComposeSendAndAssignOneOrSeveralStickersToIt(String multiStickerPattern, String stickerType, String stickerPattern, int stickerQty, String emailPattern, int emailQty, int[][] allEmailsToMultiLabelAssignmentArray) throws InterruptedException {

		int[] currentEmailToMultiLabelAssignmentArray = new int[LABEL_QTY];

		String stickerName = "";
		String emailRef = "";
		String emailRecipient= "";
		String emailSubject = "";
		String emailMessage= "";

		// foreach label
		for (int iter_r=1; iter_r<=stickerQty; iter_r++) {

			// go to inbox 
			WebDriver.get(PROTONMAIL_URL_INBOX);

			stickerName = stickerPattern + iter_r;
			emailRef = multiStickerPattern + iter_r + " " + emailPattern + iter_r;

			emailRecipient = USERNAME + "@protonmail.com";
			emailSubject = "SUBJECT : " + emailRef;
			emailMessage = "BODY : " + emailRef;

			// get multi label one dimensional array for current email 
			currentEmailToMultiLabelAssignmentArray = allEmailsToMultiLabelAssignmentArray[iter_r-1];

			composeAndSendEmail(emailRecipient, emailSubject, emailMessage);
			clickOnCheckboxOfLastReceivedEmailInbox();
			assignOneDimArrayOfSeveralStickersToSelectedEmail(stickerType, currentEmailToMultiLabelAssignmentArray);
			selectAndDeselectAllEmailsInbox();

			// foreach sticker (folder or label), check email sujects which should be displayed 
			checkEmailAssignmentToOneOrSeveralStickers(stickerPattern, emailSubject, currentEmailToMultiLabelAssignmentArray);

		}

	}

	public static void checkEmailSubjectsInsideOfASticker(String stickerName, int emailQtyExpected, String emailPattern) {

		// go to sticker (i.e. folder or label section) 
		findElementsByXpathGetOneByIndexAndClick( "//*[text()='" + stickerName + "']", 0);

		// wait until email subjects appears
		waitUntilElementToBeClickable( "//*[starts-with(@*, 'subject-text')]" );

		// --------------------------------------------------------------------------------
		// quantitative checking
		// if message qty read online is not equal to message qty expected, then issue an error ! 
		int emailQtyReadOnLine = findElementsByXpathAndGetSize( "//*[starts-with(@*, 'subject-text')]" );
		String emailSubject = "";
		if ( emailQtyReadOnLine != emailQtyExpected ) {
			System.err.println("\n\n");
			System.err.println("[ ERROR ] emailQtyReadOnLine != emailQtyExpected !");
			System.err.println("[ ERROR ] emailQtyReadOnLine=" + emailQtyReadOnLine);
			System.err.println("[ ERROR ] emailQtyExpected=" + emailQtyExpected);
			System.err.println("\n");
			for (int m=0; m<=emailQtyReadOnLine-1; m++) {
				emailSubject = findElementsByXpathGetOneByIndexAndGetText( "//*[starts-with(@*, 'subject-text')]", m );
				System.err.println("[ ERROR ] emailSubjectReadOnline(" + m + ")=" + emailSubject);
			}
			//System.exit(2);
		}
		Assert.assertEquals( emailQtyExpected, emailQtyReadOnLine );


		// --------------------------------------------------------------------------------
		// qualitative checking on email subject only (i.e. email message could also be checked to make sure) 
		String emailSubjectReadOnLine = "";
		String emailSubjectExpected = "";

		// foreach email: check its presence in current folder 
		for (int iter_m=1; iter_m<=emailQtyExpected; iter_m++) {

			System.out.println("\n\n");
			emailSubjectReadOnLine = findElementsByXpathGetOneByIndexAndGetText( "//*[starts-with(@*, 'subject-text')]", emailQtyExpected-iter_m );
			System.out.println("emailSubjectReadOnLine=" + emailSubjectReadOnLine);
			emailSubjectExpected = "SUBJECT : " + stickerName + " " + emailPattern + iter_m;
			System.out.println("emailSubjectExpected=" + emailSubjectExpected);

			// if email subject read online differs from email subject expected, then raise an error ! 
			if ( !emailSubjectReadOnLine.equalsIgnoreCase(emailSubjectExpected) == true ) {
				System.err.println("[ ERROR ] emailSubjectReadOnLine != emailSubjectExpected !");
				System.exit(2);
			} else {
				System.out.println("[ INFO ] emailSubjectReadOnLine == emailSubjectExpected");
			}
			Assert.assertEquals( emailSubjectExpected, emailSubjectReadOnLine );

		}

	}

	public static void checkEmailAssignmentToOneOrSeveralStickers(String stickerPattern, String emailSubjectTarget, int[] currentEmailToMultiLabelAssignmentArray) {

		String stickerName = "";
		int emailQtyInLabel = 0;
		String emailSubjectRead = "";

		boolean emailSubjectFound = false; 

		// foreach label, click on it to open all emails and search for email target presence 
		for (int iter_l=1; iter_l<=LABEL_QTY; iter_l++) {

			// if label is assigned, then click on label in left menu to check email subject presence 
			if ( currentEmailToMultiLabelAssignmentArray[iter_l-1] == 1 ) {

				stickerName = stickerPattern + iter_l;
				System.out.println("stickerName=" + stickerName);

				// go to sticker (i.e. folder or label section) 
				findElementsByXpathGetOneByIndexAndClick( "//*[text()='" + stickerName + "']", 0 );

				waitUntilElementToBeClickable( "//*[starts-with(@*, 'subject-text')]" );
				emailQtyInLabel = findElementsByXpathAndGetSize( "//*[starts-with(@*, 'subject-text')]" );

				emailSubjectFound = false;
				for (int e=0; e<=emailQtyInLabel-1; e++) {
					emailSubjectRead = findElementsByXpathGetOneByIndexAndGetText( "//*[starts-with(@*, 'subject-text')]", e );
					if ( emailSubjectRead.equalsIgnoreCase(emailSubjectTarget) == true ) {
						emailSubjectFound = true;
						break;
					}
				}
				if ( emailSubjectFound == false ) {
					System.err.println("[ ERROR ] emailSubjectTarget=" + emailSubjectTarget + " has NOT been assigned to label " + stickerName + " !!!\n");
				} else {
					System.out.println("[ INFO ] emailSubjectTarget=" + emailSubjectTarget + " has been assigned to label " + stickerName + " successfully\n");
				}
				Assert.assertEquals( true, emailSubjectFound );

			}

		}

	}

	public static void composeAndSendEmail(String emailRecipient, String emailSubject, String emailMessage) throws InterruptedException {

		System.out.println( "\n" );
		System.out.println( "emailRecipient=" + emailRecipient );
		System.out.println( "emailSubject=" + emailSubject );
		System.out.println( "emailMessage=" + emailMessage );

		// go to inbox to erase all pre-registered options ! 
		//driver.get(PROTONMAIL_URL_INBOX);

		// wait and click on Compose to write a message 
		waitUntilElementToBeClickableAndClick( "//*[text()='Compose']" );
		Thread.sleep(1000);

		// wait Send button to appear prior to filling email fields 
		waitUntilElementToBeClickable( "//button[text()='Send']" );
		Thread.sleep( 1000 );

		// fill email fields one by one 
		WebDriver.switchTo().activeElement().sendKeys( emailRecipient + Keys.TAB + Keys.TAB ); 
		Thread.sleep( 500 );
		WebDriver.switchTo().activeElement().sendKeys( emailSubject ); 
		Thread.sleep( 500 );
		WebDriver.switchTo().activeElement().sendKeys( Keys.TAB + emailMessage );

		waitUntilElementToBeClickableAndClick( "//button[text()='Send']" );

		// old xpath : //*[@role='alert']
		String textAlert = waitUntilElementToBeClickableAndGetText( "(//*[text()='Message sent'])[1]" );
		//Thread.sleep( 1000 );
		Assert.assertEquals( "Message sent", textAlert );
		Thread.sleep( 3000 );

	}

	public static void selectAndDeselectAllEmailsInbox() throws InterruptedException {

		// workaround - select all inbox emails twice (to avoid any previous email to be selected) 
		// workaround - except if there's just one email (where one single click is needed) 
		System.out.println("findElementsByXpathAndGetSize( \"//*[starts-with(@*, 'item-wrapper')]/*\" )\"=" + findElementsByXpathAndGetSize( "//*[starts-with(@*, 'item-wrapper')]/*" ));
		if ( findElementsByXpathAndGetSize( "//*[starts-with(@*, 'item-wrapper')]/*" ) >= 2 ) {
			selectAllInboxEmails();
		}
		selectAllInboxEmails();

	} 

	public static void clickOnCheckboxOfLastReceivedEmailInbox() {

		// wait until last created message is clickable into the mailbox 
		// old path : "(//*[text()='" + patternToIdentifyEmail + "'])[1]
		waitUntilElementToBeClickable( "(//*[contains(@*, 'selectBoxElement')])[2]" );
		// click on last email 
		findElementsByXpathGetOneByIndexAndClick( "(//*[contains(@*, 'selectBoxElement')])[2]", 0 );

	}

	public static void assignAStickerToSelectedEmail(String stickerType, String stickerName) throws InterruptedException {

		String xpathOfDrodownMenu = "";
		// click on 'Move to' dropdown menu  
		if (stickerType.equalsIgnoreCase("folder")) {
			xpathOfDrodownMenu = "//button[@*='Move to']";
		} else if (stickerType.equalsIgnoreCase("label")) {
			xpathOfDrodownMenu = "//button[@*='Label as']";
		}
		WebDriver.findElement(By.xpath(xpathOfDrodownMenu)).click();
		Thread.sleep(2000);

		// wait until sticker name is clickable into the dropdown folder menu 
		waitUntilElementToBeClickable( "(//*[text()='" + stickerName + "'])[2]" );

		actionsMoveToWebElementClickBuildPerform( "(//*[text()='" + stickerName + "'])[2]", 0 );

		if (stickerType.equalsIgnoreCase("label")) {
			findElementsByXpathGetOneByIndexAndClick( "//button[text()='Apply']", 0 );
		}
		Thread.sleep(2000);

		// old xpath : "(//*[@role='alert'])[1]"
		String textAssignmentResult = waitUntilElementToBeClickableAndGetText( "(//*[contains(@*,'notification-success')])[1]" );
		if ( stickerType.equalsIgnoreCase("folder") == true ) {
			Assert.assertEquals( "1 conversation moved to " + stickerName + ". Undo", textAssignmentResult  );
		}
		if ( stickerType.equalsIgnoreCase("label") == true ) {
			// xpath to be considered : //*[@role='alert']/span
			Assert.assertEquals( "Labels Saved", textAssignmentResult );
		}

	}

	public static void assignOneDimArrayOfSeveralStickersToSelectedEmail(String stickerType, int[] currentEmailToMultiLabelAssignmentArray) throws InterruptedException {

		String xpathOfDrodownMenu = "";
		// click on 'Move to' dropdown menu  
		if (stickerType.equalsIgnoreCase("folder")) {
			xpathOfDrodownMenu = "//button[@*='Move to']";
		} else if (stickerType.equalsIgnoreCase("label")) {
			xpathOfDrodownMenu = "//button[@*='Label as']";
		}
		WebDriver.findElement(By.xpath(xpathOfDrodownMenu)).click();

		Thread.sleep(2000);

		String stickerName = "";
		for (int iter_l=1; iter_l<=LABEL_QTY; iter_l++) {

			System.out.println("currentEmailToMultiLabelAssignmentArray[" + (iter_l-1) + "]=" + currentEmailToMultiLabelAssignmentArray[iter_l-1]);

			// if label is assigned, then click on it to select it 
			if (currentEmailToMultiLabelAssignmentArray[iter_l-1] == 1) {

				stickerName = LABEL_PATTERN + iter_l;
				System.out.println("=> let's click on stickerName=" + stickerName + "\n");

				// wait until sticker name is clickable into the dropdown folder menu 
				waitUntilElementToBeClickable( "(//*[text()='" + stickerName + "'])[2]" );
				actionsMoveToWebElementClickBuildPerform( "(//*[text()='" + stickerName + "'])[2]", 0 );

			}

		}

		// in case of 'label sticker' then 'Apply' must be clicked to assign selected label(s) 
		if (stickerType.equalsIgnoreCase("label")) {
			findElementsByXpathGetOneByIndexAndClick( "(//button[text()='Apply'])", 0 );
		}
		Thread.sleep(1000);
		String textRoleAlert = waitUntilElementToBeClickableAndGetText( "(//*[@role='alert'])[1]" );
		Assert.assertEquals( "Labels Saved", textRoleAlert );


	}

	public static void deleteAllStickers() throws InterruptedException {

		waitUntilElementToBeClickableAndClick( "//*[text()='Folders / Labels']" );

		// arriving on 'Folders and labels' page 
		waitUntilElementToBeClickable( "//h2[contains(text(), 'Folders and labels')]" );
		Thread.sleep(1000);

		System.out.println("findElementsByXpathGetOneByIndexAndGetText(\"//*[@*='folderlist']/*/*\")=" + findElementsByXpathGetOneByIndexAndGetText("//*[@*='folderlist']/*/*", 4) );
		// if text 'No labels' is not present, then delete all one by one,
		if ( findElementsByXpathGetOneByIndexAndGetText( "//*[@*='folderlist']/*/*", 4).contains("No labels") == false ) {

			waitUntilElementToBeClickable( "//button[@*='dropdown:open']" );
			int foldersAndLabelsQty = findElementsByXpathAndGetSize("//button[@*='dropdown:open']");

			System.out.println("foldersAndLabelsQty=" + foldersAndLabelsQty);
			String stickerCurrentName = "";
			String stickerType="";
			for (int a=1; a<=foldersAndLabelsQty; a++) {

				// get current folder or label name 
				stickerCurrentName = findElementsByXpathGetOneByIndexAndGetText( "//*[contains(@*,'folders/labels')]", 0 ).split("\\s+")[0];

				// click on the actions dropdown menu 
				findElementsByXpathGetOneByIndexAndClick( "//button[@*='dropdown:open']", 0 );
				Thread.sleep(1000);

				// click on the Delete option to select it 
				waitUntilElementToBeClickable( "//button[text()='Delete'][1]" );
				findElementsByXpathGetOneByIndexAndClick( "//button[text()='Delete']", 0);
				Thread.sleep(1000);

				// old xpath : "(//*[@id='modalTitle'])[1]"
				if ( stickerCurrentName.toLowerCase().contains("folder") == true ) {
					stickerType = "folder";
				}
				if ( stickerCurrentName.toLowerCase().contains("label") == true ) {
					stickerType = "label";
				}
				String textTitle = waitUntilElementToBeClickableAndGetText( "(//*[text()='Delete " + stickerType + "'])[1]" );
				Assert.assertEquals( "Delete " + stickerType, textTitle );

				// click on Confirm button
				waitUntilElementToBeClickable( "//button[text()='Confirm'][1]" );
				findElementsByXpathGetOneByIndexAndClick( "//button[text()='Confirm']", 0 );
				//Thread.sleep(1000);

				// old xpath : "(//*[@role='alert'])[1]"
				String textRoleAlert = waitUntilElementToBeClickableAndGetText( "(//*[text()='" + stickerCurrentName + " removed'])[1]" );
				System.out.println( "textRoleAlert=" + textRoleAlert );
				System.out.println( "stickerCurrentName=" + stickerCurrentName );
				Assert.assertEquals( stickerCurrentName + " removed", textRoleAlert );

			}

		} else {
			// otherwise it's useless (nothing to do as there as no folders and no labels !) 
			System.out.println("There are no folders and no labels to delete !");
		}

	}

	public static void createAllStickersForAGivenType(String stickerType, int stickerQty, String stickerNamePattern) throws InterruptedException { 

		waitUntilElementToBeClickableAndClick( "//a/*/*[contains(text(), 'Folders')]" );

		int colorChoiceQty = 0;
		int chosenColour = 0;
		String stickerNameToType = "";
		String lastStickerCreated = "";

		// foreach folder
		for (int s=1; s<=stickerQty; s++) {

			stickerNameToType = stickerNamePattern + s;
			System.out.println( "stickerNameToType=" + stickerNameToType );

			waitUntilElementToBeClickable( "//button[text()='Add " + stickerType + "']" );
			System.out.println("//button[text()='Add " + stickerType + "']");
			actionsMoveToWebElementClickBuildPerform( "//button[text()='Add " + stickerType + "']" );

			waitUntilElementToBeClickable( "//input[@type='text']" );
			findElementsByXpathAndGetOneByIndex( "//input[@type='text']" , 0 ).sendKeys(stickerNameToType);

			colorChoiceQty = findElementsByXpathAndGetSize( "//input[contains(@*, 'Color')]" );
			chosenColour = generateRandomInt( 1, colorChoiceQty );
			System.out.println("chosen color : " + chosenColour);
			// click on the random chosen color 
			findElementsByXpathAndGetOneByIndex( "//input[contains(@*, 'Color')]", chosenColour - 1 ).click();
			findElementsByXpathAndGetOneByIndex( "//button[text()='Submit']", 0).click();
			Thread.sleep(2000);

			String notificationAlert = findElementsByXpathGetOneByIndexAndGetText( "//*[@role='alert']", 0 );
			String textRoleAlert = "";
			if ( notificationAlert.contains( " created" ) ) {

				// old xpath : "(//*[@role='alert'])[1]"
				textRoleAlert = waitUntilElementToBeClickableAndGetText( "(//*[text()='" + stickerNameToType + " created'])[1]" );
				System.out.println("textRoleAlert=" + textRoleAlert);
				System.out.println("stickerNameToType=" + stickerNameToType);
				Assert.assertEquals( stickerNameToType + " created", textRoleAlert );
				Thread.sleep(1000);

				// old xpath : "(//*[contains(@*,'folders/labels')])[" + s + "]"
				lastStickerCreated = waitUntilElementToBeClickableAndGetText( "(//*[text()='" + stickerNameToType + "'])[1]" );
				//findElementsByXpathGetOneByIndexAndGetText( "//*[text()='Label_1']", 0 ).split("\\s+")[0];
				System.out.println("lastStickerCreated=" + lastStickerCreated);
				System.out.println("stickerNameToType=" + stickerNameToType);
				Assert.assertEquals( stickerNameToType, lastStickerCreated );

			} else {

				textRoleAlert = waitUntilElementToBeClickableAndGetText( "(//*[contains(text(), ' limit reached')])[1]" );
				System.out.println("[ INFO ] " + stickerType + " limit reached !");
				Assert.assertEquals( notificationAlert.contains( " limit reached" ) , true );

			}

		}

	}

}

