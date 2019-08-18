package genericLibrary;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.xml.XmlTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

/**
 * 
 * ExtentReports Generator (Works with @Listeners(EmailReport.class))
 *
 */
public class ExtentReporter {

	private static ExtentReports extentReport = null;
	private static HashMap<String, ExtentTest> parentTests = new HashMap<String, ExtentTest>();
	private static HashMap<Integer, ExtentTest> tests = new HashMap<Integer, ExtentTest>();	

	/**
	 * To form a unique test name in the format	
	 * "PackageName.ClassName#MethodName"
	 * 
	 * @param iTestResult
	 * @return String - test name
	 */
	private static String getTestName(ITestResult iTestResult) {
		try{
			String testClassName = iTestResult.getTestClass().getName().replace("Amazon.Tests.", "").replace(".", ":");
			String testMethodName = iTestResult.getMethod().getMethodName().toString().trim();
			return testClassName +" - "+ testMethodName;
		}
		catch(Exception e){
			return ((iTestResult.getTestClass().getName().trim()+" - "+iTestResult.getMethod().getMethodName()).trim());
		}
	}

	/**
	 * Returns an ExtentReports instance if already exists. Creates new and
	 * returns otherwise.
	 * 
	 * @param iTestResult
	 * @return {@link ExtentReports} - Extent report instance
	 */
	private static synchronized ExtentReports getReportInstance(ITestResult iTestResult) {

		if (extentReport == null) {

			String reportFilePath = new File(iTestResult.getTestContext().getOutputDirectory()).getParent() + File.separator + "AutomationExtentReport.html";

			// Create the fresh report file.
			// -----------------------------
			try {
				File report = new File(reportFilePath);

				if(report.exists())
					report.delete();

				report.createNewFile();
			} 
			catch (IOException e1) {}

			XmlTest xmlParameters = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest();
			String reportName = "Test Automation Report - " + xmlParameters.getParameter("driverType").toUpperCase();
			String hostname = "Unknown";

			try
			{
				InetAddress addr;
				addr = InetAddress.getLocalHost();
				hostname = addr.getHostName();
			}
			catch (UnknownHostException ex)
			{
				System.out.println("Hostname can not be resolved");
			}

			ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(reportFilePath);
			htmlReporter.config().setDocumentTitle(reportName);
			htmlReporter.config().setReportName(reportName);
			htmlReporter.config().setTheme(Theme.STANDARD);
			List<Status> statusHierarchy = Arrays.asList(
					Status.FATAL,
					Status.FAIL,
					Status.ERROR,
					Status.SKIP,
					Status.PASS,
					Status.WARNING,
					Status.DEBUG,
					Status.INFO
					);

			extentReport = new ExtentReports();
			extentReport.setSystemInfo("Host Name", hostname);
			extentReport.setSystemInfo("OS", System.getProperty("os.name"));
			extentReport.setSystemInfo("User Name", System.getProperty("user.name"));
			extentReport.setSystemInfo("Java Version", System.getProperty("java.version"));
			extentReport.config().statusConfigurator().setStatusHierarchy(statusHierarchy);

			extentReport.attachReporter(htmlReporter);

		}
		return extentReport;
	}

	/**
	 * To start and return a new extent test instance with given test case
	 * description. Returns the test instance if the test has already been
	 * started
	 * 
	 * @param description
	 *            - test case description
	 * @return {@link ExtentTest} - ExtentTest Instance
	 */
	private static ExtentTest startTest(String description) {
		ExtentTest parentTest = null;
		ExtentTest test = null;
		ITestNGMethod method = Reporter.getCurrentTestResult().getMethod();
		ITestResult iTestResult = Reporter.getCurrentTestResult();
		String testName = iTestResult != null ? getTestName(iTestResult) : Thread.currentThread().getName();
		Integer hashCode = iTestResult != null ? iTestResult.hashCode() : Thread.currentThread().hashCode();
		XmlTest xmlParameters = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest();
		String driverType = xmlParameters.getParameter("driverType");
		String author = "GoGo";		
		int level = 1;

		if(method.isTest())
			if(parentTests.containsKey(testName+":"+driverType))
			{
				parentTest = parentTests.get(testName+":"+driverType);
				level = parentTest.getModel().getNodeContext().getAll().size() + 1;

				if (tests.containsKey(hashCode)) {
					test = tests.get(hashCode);
					return test;
				} else {
					test = parentTest.createNode("Test data : " + level).assignCategory(testName.split("-")[0].trim()+" - "+driverType.toUpperCase());
					tests.put(hashCode, test);
				}

				test = parentTest.createNode("Test data : " + level).assignCategory(testName.split("-")[0].trim()+" - "+driverType.toUpperCase());
				tests.put(hashCode, test);				
			}
			else
			{
				parentTest = getReportInstance(iTestResult).createTest(testName, description).assignAuthor(author);
				parentTests.put(testName+":"+driverType, parentTest);
				test = parentTest.createNode("Test data : " + level).assignCategory(testName.split("-")[0].trim()+" - "+driverType.toUpperCase());
				tests.put(hashCode, test);
			}

		return test;
	}

	/**
	 * Returns the test instance if the test has already been started. Else
	 * creates a new test with empty description
	 * 
	 * @return {@link ExtentTest} - ExtentTest Instance
	 */
	private static ExtentTest getTest() {
		return startTest("");
	}

	/**
	 * To start a test with given test case info
	 * 
	 * @param testCaseInfo
	 */
	public static void testCaseInfo(String testCaseInfo) {
		startTest("<strong><font size = \"4\" color = \"#000080\">" + testCaseInfo + "</font></strong>");
	}

	/**
	 * To add the machine info in the test case
	 * 
	 * @param testCaseInfo
	 */
	public static void testMachineInfo(String testMachineInfo) {
		if(getTest() != null)
			getTest().log(Status.INFO, "<font size = \"2\" color = \"#2E4053\">" + testMachineInfo + "</font>");
	}

	/**
	 * To log the given message to the reporter at INFO level
	 * 
	 * @param message
	 */
	public static void info(String message) {
		if (getTest() != null)
			getTest().info(message);//log(Status.INFO, message);
	}

	/**
	 * To log the given message to the reporter at INFO level
	 * 
	 * @param message
	 */
	public static void warning(String message) {
		if (getTest() != null)
			getTest().warning(message);//log(Status.WARNING, message);
	}

	/**
	 * To log the given message to the reporter at INFO level with Screenshot
	 * 
	 * @param message: Description
	 * @param screenshotPath: ScreenshotPath
	 * @throws IOException 
	 */
	public static void info(String message, String screenshotPath) throws IOException {
		if (getTest() != null)
			getTest().info(message, MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath, "Test").build());//log(Status.INFO, message + getTest().addScreenCaptureFromPath(screenshotPath, "Test"));
	}

	/**
	 * To log the given message to the reporter at DEBUG level
	 * 
	 * @param event
	 */
	public static void debug(String event) {
		if (getTest() != null)
			getTest().debug(event);//log(Status.FATAL, event);
	}

	/**
	 * To log the given message to the reporter at PASS level
	 * 
	 * @param passMessage
	 */
	public static void pass(String passMessage) {
		if (getTest() != null)
			getTest().pass("<font color=\"green\">" + passMessage + "</font>");
	}

	/**
	 * To log the given message to the reporter at PASS level
	 * 
	 * @param passMessage
	 * @throws IOException 
	 */
	public static void pass(String passMessage, String screenshotPath) throws IOException {
		if (getTest() != null)
			getTest().pass("<font color=\"green\">" + passMessage + "</font>", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());//log(Status.PASS, "<font color=\"green\">" + passMessage + "</font>" + getTest().addScreenCaptureFromPath(screenshotPath));
	}

	/**
	 * To log the given message to the reporter at FAIL level
	 * 
	 * @param failMessage
	 */
	public static void fail(String failMessage) {
		if (getTest() != null)
			getTest().fail("<font color=\"red\">" + failMessage + "</font>");//log(Status.FAIL, "<font color=\"red\">" + failMessage + "</font>");
	}

	/**
	 * To log the given message to the reporter at Error level
	 * 
	 * @param errorMessage
	 */
	public static void error(String errorMessage) {
		if (getTest() != null)
			getTest().error("<font color=\"red\">" + errorMessage + "</font>");
	}

	/**
	 * To log the given message to the reporter at FAIL level
	 * 
	 * @param failMessage
	 * @throws IOException 
	 */
	public static void fail(String failMessage, String screenshotPath) throws IOException {
		if (getTest() != null)
			getTest().fail("<font color=\"red\">" + failMessage + "</font>", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());//log(Status.FAIL, "<font color=\"red\">" + failMessage + "</font>" + getTest().addScreenCaptureFromPath(screenshotPath));
	}

	/**
	 * To log the given message to the reporter at Error level
	 * 
	 * @param errorMessage
	 * @throws IOException 
	 */
	public static void error(String errorMessage, String screenshotPath) throws IOException {
		if (getTest() != null)
		{
			try
			{
				if(errorMessage.contains("<"))
					errorMessage.replaceAll("<", "{");
				else if( errorMessage.contains(">"))
					errorMessage.replaceAll(">", "}");
			}catch(Exception e) {}

			getTest().error("<font color=\"red\">" + errorMessage + "</font>", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
		}
	}

	/**
	 * To log the given message to the reporter at SKIP level
	 * 
	 * @param message
	 */
	public static void skip(String message) {
		if (getTest() != null)
			getTest().skip("<font color=\"orange\">" + message + "</font>");//log(Status.SKIP, "<font color=\"orange\">" + message + "</font>");
	}

	/**
	 * To log the given message to the reporter at SKIP level
	 * 
	 * @param message
	 * @throws IOException 
	 */
	public static void skip(String message, String screenshotPath) throws IOException {
		if (getTest() != null)
			getTest().skip("<font color=\"orange\">" + message + "</font>", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());//log(Status.SKIP, "<font color=\"orange\">" + message + "</font>" + getTest().addScreenCaptureFromPath(screenshotPath));
	}

	/**
	 * To print the stack trace of the given error/exception
	 * 
	 * @param t
	 */
	public static void logStackTrace(Throwable t) {
		if (t instanceof SkipException) {
			if (getTest() != null)
				getTest().skip(t);
		} else {
			if (getTest() != null)
				getTest().fail(t);
		}
	}	

	/**
	 * To end an extent test instance
	 */
	public static void endTest() {
		getReportInstance(Reporter.getCurrentTestResult()).flush();
	}
}