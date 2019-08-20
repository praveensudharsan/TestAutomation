package API.Tests;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.testng.Reporter;
import org.testng.annotations.*;
import org.testng.xml.XmlTest;

import genericLibrary.Log;
import genericLibrary.RestAssuredAPI;
import io.restassured.response.Response;

public class APITests {

	protected static String baseURL = null;
	protected static String accessToken = null;
	protected static String contentType = null;
	protected static String userID = null;
	protected static Map<String, String> body = new HashMap<>();
	protected static String newUserName = null;

	@BeforeClass
	public void init()
	{
		XmlTest xml = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest();
		baseURL = xml.getParameter("baseURL");
		accessToken = xml.getParameter("accessToken");
		contentType = xml.getParameter("contentType");

		newUserName = "john"+new Date().getTime();

		body.put("email", newUserName+"@gmail.com");
		body.put("first_name", newUserName);
		body.put("last_name", "cena");
		body.put("gender", "male");

	}	

	@AfterMethod
	public void afterTest() throws Exception
	{
		RestAssuredAPI.closeConnection();
		Log.endTestCase();
	}

	/**
	 * This function is used to assert the body used to create the user with the response of the request.
	 * @param body
	 * @param response
	 * @return
	 */
	public String assertExpectedBodyWithReponse(Map<String, String> body, Response response)
	{
		String result = "";

		Iterator<String> it = body.keySet().iterator();

		while(it.hasNext())
		{
			String key = it.next();
			if(!response.asString().contains("\""+key+"\":\""+body.get(key)))
				result += "\""+key+"\":\""+body.get(key)+"\" is not present in body;";
		}

		return result;
	}

	/**
	 * Create new user using post request
	 * @throws Exception
	 */
	@Test(description="Create new user using post request")
	public void CreateUserUsingPostRequest() throws Exception
	{
		try {
			// Instantiate the extent test.
			String description = Reporter.getCurrentTestResult().getMethod().getDescription();
			String className = Reporter.getCurrentTestResult().getClass().getSimpleName().toString().trim();
			String methodName = Reporter.getCurrentTestResult().getMethod().getMethodName();
			Log.testCaseInfo(description, className + " - " +  methodName);

			// Post URL.
			String postURL = "/public-api/users";

			// Make post request and get the response.
			Response response = RestAssuredAPI.POST(baseURL, accessToken, contentType, body, postURL);

			// Assert that post request is success.
			if(!response.asString().contains("\"success\":true"))
				Log.fail("Test case failed.New user '"+newUserName+"' is not created successfully using post request."
						+ "Additional info. : "+ response.asString());

			// Assert that expected details present in the response body.
			String result = assertExpectedBodyWithReponse(body, response);

			// Verify that user is created successfully using post request.
			if(result.equalsIgnoreCase(""))
			{
				userID = response.getHeader("Location").replaceAll(baseURL, "").replaceAll(postURL, "");
				Log.pass("Test case passed. New user '"+newUserName+"' created successfully using post request and user id is "+userID+"."
						+ "<br>Additional info. : Response - "+ response.asString());
			}
			else
				Log.fail("Test case failed.New user '"+newUserName+"' is not created successfully using post request."
						+ "<br>Additional info. : Result - "+result+"\nResponse - "+ response.asString());
		}
		catch(Exception ex)
		{
			Log.exception(ex);
		}
	} // End CreateUserUsingPostRequest

	/**
	 * Validate new user details using get request
	 * @throws Exception
	 */
	@Test(description="Validate new user details using get request", dependsOnMethods="CreateUserUsingPostRequest")
	public void ValidateNewUserUsingGetRequest() throws Exception
	{
		try {
			// Instantiate the extent test.
			String description = Reporter.getCurrentTestResult().getMethod().getDescription();
			String className = Reporter.getCurrentTestResult().getClass().getSimpleName().toString().trim();
			String methodName = Reporter.getCurrentTestResult().getMethod().getMethodName();
			Log.testCaseInfo(description, className + " - " +  methodName);

			// Get request URL.
			String getURL = "/public-api/users"+userID;

			// Make get request with newly created user id and get the response.
			Response response = RestAssuredAPI.GET(baseURL, accessToken, contentType, getURL);

			// Assert that get request is success.
			if(!response.asString().contains("\"success\":true"))
				Log.fail("Test case failed. New user '"+newUserName+"' is not created successfully."
						+ "<br>Additional info. : Response of GET request - "+ response.asString());

			// Assert that expected body is present in the reponse.
			String result = assertExpectedBodyWithReponse(body, response);

			// Verify that new user details are correct and verified using get request.
			if(result.equalsIgnoreCase(""))
				Log.pass("Test case passed. New user '"+newUserName+"' is created successfully and validated using get request."
						+ "<br>Additional info. : Response of GET request - "+ response.asString());
			else
				Log.fail("Test case failed.New user '"+newUserName+"' is not created successfully."
						+ "<br>Additional info. : Result - "+result+"\nResponse - "+ response.asString());
		}
		catch(Exception ex)
		{
			Log.exception(ex);
		} 
	} // End ValidateNewUserUsingGetRequest


	/**
	 * Check that creating new user with same details is not possible using post request
	 * @throws Exception
	 */
	@Test(description="Check that creating new user with same details is not possible using post request", 
			dependsOnMethods="CreateUserUsingPostRequest")
	public void DuplicateUserCreationFailsUsingPostRequest() throws Exception
	{
		try {
			// Instantiate the extent test.
			String description = Reporter.getCurrentTestResult().getMethod().getDescription();
			String className = Reporter.getCurrentTestResult().getClass().getSimpleName().toString().trim();
			String methodName = Reporter.getCurrentTestResult().getMethod().getMethodName();
			Log.testCaseInfo(description, className + " - " +  methodName);

			// Post URL.
			String postURL = "/public-api/users";

			// Make post request and get the response.
			Response response = RestAssuredAPI.POST(baseURL, accessToken, contentType, body, postURL);

			// Assert that post request is success.
			if(!response.asString().contains("\"success\":false"))
				Log.fail("Test case failed. Duplicate user '"+newUserName+"' is created with same details using post request."
						+ "Additional info. : "+ response.asString());

			// Verify that duplicate user is not created with same details using post request.
			if(response.asString().contains("Email \\\""+newUserName+"@gmail.com\\\" has already been taken"))
				Log.pass("Test case passed. Duplicate user is not created when trying to create the user with same email using post request."
						+ "<br>Additional info. : Response - "+response.asString());
			else
				Log.fail("Test case failed. Expected message 'Email \\\""+newUserName+"@gmail.com\\\" has already been taken' is not displayed when trying to create the duplicate user with same email using post request."
						+ "<br>Additional info. : Response - "+response.asString());

		}
		catch(Exception ex)
		{
			Log.exception(ex);
		}
	} // End CreateUserUsingPostRequest

	@Test(description="Check that request fails when invalid token is used")
	public void POSTRequestFailsWithInvalidToken() throws Exception
	{
		try {
			// Instantiate the extent test.
			String description = Reporter.getCurrentTestResult().getMethod().getDescription();
			String className = Reporter.getCurrentTestResult().getClass().getSimpleName().toString().trim();
			String methodName = Reporter.getCurrentTestResult().getMethod().getMethodName();
			Log.testCaseInfo(description, className + " - " +  methodName);

			// Post URL.
			String postURL = "/public-api/users";

			// Make post request and get the response.
			Response response = RestAssuredAPI.POST(baseURL, accessToken+"123", contentType, body, postURL);

			// Assert that post request is success.
			if(!response.asString().contains("\"success\":false"))
				Log.fail("Test case failed. Post request is not failed when invalid authentication token is used."
						+ "Additional info. : "+ response.asString());

			// Verify that duplicate user is not created with same details using post request.
			if(response.asString().contains("Your request was made with invalid credentials"))
				Log.pass("Test case passed. POST request is failed when using invalid authentication token."
						+ "<br>Additional info. : Response - "+response.asString());
			else
				Log.fail("Test case failed. Post request is not failed when invalid authentication token is used."
						+ "Additional info. : "+ response.asString());


		}
		catch(Exception ex)
		{
			Log.exception(ex);
		}
	}
}
