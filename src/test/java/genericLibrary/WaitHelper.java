package genericLibrary;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaitHelper {

	/**
	 * This function is used to wait till the element, if the element is not displayed it will throw the exception.
	 * @param element
	 * @param driver
	 */
	public static void WaitUntilElementDisplayed(WebElement element, WebDriver driver)
	{
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	/**
	 * This function is used to wait till the element and returns whether the element is displayed or not.
	 * @param element
	 * @param driver
	 * @return
	 */
	public static boolean SoftWaitUntilElementDisplayed(WebElement element, WebDriver driver)
	{
		try
		{
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.visibilityOf(element));
			return element.isDisplayed();
		}
		catch(Exception ex)
		{
			return false;
		}

	}

}
