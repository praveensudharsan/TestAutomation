package Amazon.Tests;

import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.testng.Reporter;
import org.testng.annotations.*;
import org.testng.xml.XmlTest;

import Amazon.ElementFactory.Captions;
import Amazon.PageObjects.SearchResults;
import Amazon.Pages.DefaultPage;
import genericLibrary.Log;
import genericLibrary.WebDriverUtils;

public class AmazonTests {

	static String appUrl = null;
	String driverType = null;
	WebDriver driver = null;

	@BeforeClass
	public void beforeClass()
	{
		XmlTest test = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest();
		appUrl = test.getParameter("appUrl");
		driverType = test.getParameter("driverType");
	}

	@AfterMethod
	public void afterTest()
	{
		try {

			if(driver != null)
				driver.quit();

			Log.endTestCase();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test(description="Search for books")
	public void BookSearch() throws Exception
	{
		try
		{
			String description = Reporter.getCurrentTestResult().getMethod().getDescription();
			String className = Reporter.getCurrentTestResult().getClass().getSimpleName().toString().trim();
			String methodName = Reporter.getCurrentTestResult().getMethod().getMethodName();
			Log.testCaseInfo(description + "[" + driverType.toUpperCase() + "]", className + " - " +  methodName);

			// Launch the driver.
			driver = WebDriverUtils.getDriver();

			Log.message("1. Launched the driver");

			// Launch the application url.
			driver.get(appUrl);

			Log.message("2. Launched the application URL");

			// Instantiate the default page.
			DefaultPage page = new DefaultPage(driver);	

			// Perform the search.
			SearchResults searchResult = page.searchPanel.SearchByCategory("data", Captions.SearchCategories.Books.Value);

			Log.message("3. Performed the search.");

			// Get the first search result details.
			HashMap<String, String> result = searchResult.GetFirstSearchResultDetails();

			// Check that result is not empty.
			if(result.isEmpty())
				Log.fail("Test case failed. No Details Found.");

			Log.message("4. Object details are follows:");

			// Print the obtained search result information.
			searchResult.PrintSearchResult(result);

			Log.pass("Test case passed. Book details are found.", driver);

		}
		catch(Exception ex)
		{
			Log.exception(ex, driver);
		}
	}

}
