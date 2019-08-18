package genericLibrary;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.Augmenter;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.SkipException;

public class Log {

	private static String screenShotFolderPath;
	private static HashMap<Integer, String> tests = new HashMap<Integer, String>();

	/**
	 * Static block clears the screenshot folder if any in the output during every suite execution and also sets up the print console flag for the run
	 */
	static {

		PropertyConfigurator.configure("./src/test/resources/log4j.properties");
		File screenShotFolder = new File(Reporter.getCurrentTestResult().getTestContext().getOutputDirectory());
		screenShotFolderPath = screenShotFolder.getParent() + File.separator + "ScreenShot" + File.separator;
		screenShotFolder = new File(screenShotFolderPath);

		if (!screenShotFolder.exists()) {
			screenShotFolder.mkdir();
		}

		File[] screenShots = screenShotFolder.listFiles();

		// delete files if the folder has any
		if (screenShots != null && screenShots.length > 0) {
			for (File screenShot : screenShots) {
				screenShot.delete();
			}
		}

	} // static block
	/**
	 * cleanScreenShotFolder : Cleans screenshot folder of previous executions
	 * @param context
	 * @return None
	 * @throws Exception 
	 */
	public static void cleanScreenShotFolder(ITestContext context) throws Exception {

		try {

			File screenShotFolder = new File(Reporter.getCurrentTestResult().getTestContext().getOutputDirectory());
			screenShotFolder = new File(screenShotFolder.getParent() + File.separator + "ScreenShot");

			if (!screenShotFolder.exists()) {
				screenShotFolder.mkdir();
				return;
			}

			File[] screenShots = screenShotFolder.listFiles();

			for (File screenShot : screenShots)
				screenShot.delete();

			screenShotFolder = new File(screenShotFolder.getParent() + File.separator + "Pass_ScreenShot");

			if (!screenShotFolder.exists()) {
				screenShotFolder.mkdir();
				return;
			}

			screenShots = screenShotFolder.listFiles();

			for (File screenShot : screenShots)
				screenShot.delete();
		} //End try

		catch (Exception e) {
			throw new Exception ("Exception in Log.cleanScreenShotFolder;: "+e.getMessage(), e);
		} //End catch

	} //End cleanScreenShotFolder

	/**
	 * deleteDownloadedFilesFolder : Deletes download file folder
	 * @param context
	 * @return None
	 * @throws Exception 
	 */
	public static void deleteDownloadedFilesFolder(ITestContext context) throws Exception {

		try {

			File downloadedFilesFolder = new File(Reporter.getCurrentTestResult().getTestContext().getOutputDirectory());
			downloadedFilesFolder = new File(downloadedFilesFolder.getParent() + File.separator + "DownloadedFiles");

			if (!downloadedFilesFolder.exists()) {
				downloadedFilesFolder.mkdir();
				return;
			}

			File[] downloadedFiles = downloadedFilesFolder.listFiles();

			for (File downloadedFile : downloadedFiles)
				downloadedFile.delete();
		} //End try
		catch (Exception e) {
			throw new Exception("Exception at Log.deleteDownloadedFilesFolder: "+e.getMessage(), e);
		} //End catch

	} //End deleteDownloadedFilesFolder

	/**
	 * message print the test case custom message in the log (level=info)
	 * 
	 * @param description
	 *            test case
	 */
	public static void message(String description) {
		Reporter.log(description);
		lsLog4j().log(callerClass(), Level.INFO, description, null);
		ExtentReporter.info(description);		
	}



	/**
	 * message : Prints the messages
	 * @param  description - Message to be printed
	 * @param  driver - Web Driver
	 * @return None
	 * @throws Exception 
	 */
	public static void message(String description, WebDriver driver) throws Exception {

		try{
			String inputFile = "";

			inputFile = takeScreenShot(driver);
			Reporter.log(description + "&emsp;" + getScreenShotHyperLink(inputFile));

			lsLog4j().log(callerClass(), Level.INFO, description, null);

			ExtentReporter.info(description, "./ScreenShot/"+ inputFile);			
		}
		catch (Exception e) {
			throw new Exception ("Exception at Log.message : "+e.getMessage(), e);
		} //End catch
	}

	/**
	 * pass : Prints the pass description messages
	 * @param  msg - Message to be printed
	 * @return None
	 * @throws Exception 
	 */
	public static void pass(String msg) throws Exception {

		try {


			Reporter.log(msg);
			lsLog4j().log(callerClass(), Level.INFO, msg, null);
			ExtentReporter.pass(msg);

		} //End try

		catch (Exception e) {
			throw new Exception ("Exception at Log.pass : "+e.getMessage(), e);
		} //End catch
	} //End pass


	/**
	 * pass : Prints the pass description messages
	 * @param  msg - Message to be printed
	 * @return None
	 * @throws Exception 
	 */
	public static void pass(String msg, WebDriver driver) throws Exception {

		try {

			String inputFile = "";

			try {
				File screenShotFolder = new File(Reporter.getCurrentTestResult().getTestContext().getOutputDirectory());
				String strBasePath = screenShotFolder.getParent() + File.separator + "ScreenShot" + File.separator;
				inputFile = Reporter.getCurrentTestResult().getName() + "_" + (new Date()).getTime() + ".png";
				driver.switchTo().defaultContent();
				WebDriver augmented = new Augmenter().augment(driver);
				File screenshot = ((TakesScreenshot) augmented).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(screenshot, new File(strBasePath + inputFile));
			}
			catch (IOException | WebDriverException e1) {
				try {
					message("Screen shot capture failed.");
				} catch (Exception e) {
					e.printStackTrace();
				}
				e1.printStackTrace();
			}

			{
				String screenShotLink = "<a href=\"." + File.separator + "ScreenShot" + File.separator + inputFile + "\" target=\"_blank\" >[ScreenShot]</a>";
				Reporter.log("<br><font color=\"green\"><strong>" + msg + "</strong></font></br><p>" + screenShotLink + "</p>");
			}

			lsLog4j().log(callerClass(), Level.INFO, msg, null);


			ExtentReporter.pass(msg, "./ScreenShot/"+ inputFile);

		} //End try

		catch (Exception e) {
			throw new Exception ("Exception at Log.pass: "+e.getMessage(), e);
		} //End catch

	} //End pass


	/**
	 * fail print test case status as Fail with custom message (level=error)
	 * 
	 * @param description
	 *            custom message in the test case
	 */
	public static void fail(String description) {

		Reporter.log(description);
		lsLog4j().log(callerClass(), Level.ERROR, description, null);
		ExtentReporter.fail(description);
		Assert.fail(description);
	}
	/**
	 * fail : Prints the fail description messages
	 * @param  msg - Message to be printed
	 * @param  driver - Web driver
	 * @return None
	 * @throws Exception 
	 */
	public static void fail(String msg, WebDriver driver) throws Exception {

		String inputFile = "";

		try {
			File screenShotFolder = new File(Reporter.getCurrentTestResult().getTestContext().getOutputDirectory());
			String strBasePath = screenShotFolder.getParent() + File.separator + "ScreenShot" + File.separator;
			inputFile = Reporter.getCurrentTestResult().getName() + "_" + (new Date()).getTime() + ".png";
			driver.switchTo().defaultContent();
			WebDriver augmented = new Augmenter().augment(driver);
			File screenshot = ((TakesScreenshot) augmented).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(screenshot, new File(strBasePath + inputFile));

		}
		catch (IOException | WebDriverException e1) {
			try {
				message("Screen shot capture failed.");
			} catch (Exception e) {
				e.printStackTrace();
			}
			e1.printStackTrace();
		}

		lsLog4j().log(callerClass(), Level.FATAL, msg, null);

		ExtentReporter.fail(msg, "./ScreenShot/"+ inputFile);


		{
			String screenShotLink = "<a href=\"." + File.separator + "ScreenShot" + File.separator + inputFile + "\" target=\"_blank\" >[ScreenShot]</a>";
			Reporter.log(msg+"</br><p>" + screenShotLink + "</p>");
		}

		Assert.fail(msg);
	} //End fail


	public static void exception(Exception e) throws Exception{

		String eMessage = e.getMessage();
		if (eMessage != null && eMessage.contains("\n")) {
			eMessage = eMessage.substring(0, eMessage.indexOf("\n"));
		}

		if (e instanceof SkipException) {

			Reporter.log(eMessage);
			ExtentReporter.skip(eMessage);
			ExtentReporter.logStackTrace(e);
		}
		else {

			Reporter.log(e.getMessage());
			ExtentReporter.error(eMessage);
			ExtentReporter.logStackTrace(e);
		}
		throw e;

	}


	/**
	 * exception : Prints the exception description messages
	 * @param  e - Exception details
	 * @param  driver - Web Driver
	 * @param  extentTest - Extent Test
	 * @return None
	 * @throws Exception 
	 */
	public static void exception(Exception e, WebDriver driver) throws Exception {

		if (driver != null) {

			String inputFile = "";

			try
			{
				File screenShotFolder = new File(Reporter.getCurrentTestResult().getTestContext().getOutputDirectory());
				String strBasePath = screenShotFolder.getParent() + File.separator + "ScreenShot" + File.separator;
				inputFile = Reporter.getCurrentTestResult().getName() + "_" + (new Date()).getTime() + ".png";
				driver.switchTo().defaultContent();
				WebDriver augmented = new Augmenter().augment(driver);
				File screenshot = ((TakesScreenshot) augmented).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(screenshot, new File(strBasePath + inputFile));

			}
			catch (IOException | WebDriverException e1) {
				try {
					message("Screen shot capture failed.");
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				e1.printStackTrace();
			}
			String eMessage = (e.getMessage() != null) ? e.getMessage() : "".replaceAll("<", "{").replaceAll(">", "}").trim();

			if (eMessage != null && eMessage.contains("\n")) {
				eMessage = eMessage.substring(0, eMessage.indexOf("\n"));
			}

			lsLog4j().log(callerClass(), Level.FATAL, eMessage, e);

			if (e instanceof SkipException) {

				Reporter.log(eMessage + "-" + getScreenShotHyperLink(inputFile));

				ExtentReporter.skip(eMessage, "./ScreenShot/"+ inputFile);
				ExtentReporter.logStackTrace(e);
			}
			else {
				Reporter.log(eMessage+"-"+getScreenShotHyperLink(inputFile));
				try{
					ExtentReporter.error(eMessage, "./ScreenShot/"+ inputFile);
				}
				catch(Exception e0){}
				ExtentReporter.logStackTrace(e);
			}

			throw e;
		}
		else {
			Log.exception(e);
		}
	}

	/**
	 * testCaseInfo : Prints the test case information
	 * @param  description - test case description
	 * @return 
	 * @return None
	 * @throws Exception 
	 */
	public static void testCaseInfo(String description, String testName) {

		int hashCode = Reporter.getCurrentTestResult().hashCode();

		if(!tests.containsKey(hashCode))//Checks if the info is already logged for the test
		{
			tests.put(hashCode, testName);
			lsLog4j().setLevel(Level.ALL);
			lsLog4j().info("");
			lsLog4j().log(callerClass(), Level.INFO, "****             " + description + "             *****", null);
			lsLog4j().info("");
			Reporter.log(description);
			ExtentReporter.testCaseInfo(description);
		}

	}

	/**
	 * getScreenShotHyperLink will convert the log status to hyper link
	 * 
	 * @param inputFile
	 *            converts log status to hyper link
	 * 
	 * @return String take sheet shot link path
	 */
	public static String getScreenShotHyperLink(String inputFile) {
		String screenShotLink = "";
		screenShotLink = "<a href=\"." + File.separator + "ScreenShot" + File.separator + inputFile + "\" target=\"_blank\" >[ScreenShot]</a>";
		return screenShotLink;
	}	

	/**
	 * endTestCase : Ends test case
	 * @param  extentTest
	 * @return None
	 * @throws Exception 
	 */
	public static void endTestCase() throws Exception {
		try {
			lsLog4j().info("****             " + "-E---N---D-" + "             *****");
			ExtentReporter.endTest();
		}
		catch (Exception e) {
			throw new Exception("Exception at endTestCase : " + e);
		}

	} //endTestCase

	/**
	 * lsLog4j returns name of the logger from the current thread
	 * 
	 * @return get current thread name
	 */
	public static Logger lsLog4j() {
		return Logger.getLogger(Thread.currentThread().getName());
	}

	/**
	 * callerClass method used to retrieve the Class Name
	 * 
	 * @return String -current test class name
	 */
	public static String callerClass() {
		return Thread.currentThread().getStackTrace()[2].getClassName();
	}

	/**
	 * takeScreenShot will take the screenshot by sending driver as parameter in the log and puts in the screenshot folder
	 * 
	 * @param driver
	 *            to take screenshot
	 * @return String take sheet shot path
	 */
	public static String takeScreenShot(WebDriver driver) {
		String inputFile = "";
		inputFile = Reporter.getCurrentTestResult().getName() + "_" + (new Date()).getTime() + ".png";
		ScreenshotManager.takeScreenshot(driver, screenShotFolderPath + inputFile);
		return inputFile;
	}
}
