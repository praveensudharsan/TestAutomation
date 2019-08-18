package Amazon.Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import Amazon.PageObjects.*;
import genericLibrary.WaitHelper;

public class DefaultPage {
	
	WebDriver driver = null;
	
	@FindBy(how = How.CSS, using="div[role='alertdialog'] .glow-toaster-footer input[data-action-type='DISMISS']")
	protected WebElement dismissToasterMsg;
	
	public SearchPanel searchPanel;
	public SearchResults searchResults;
	
	/**
	 * DefaultPage constructor.
	 * @param driver
	 */
	public DefaultPage(WebDriver driver)
	{
		this.driver = driver;
		PageFactory.initElements(driver, this);		
		searchPanel = new SearchPanel(driver);
		DismissToasterMessage();
	}
	
	/**
	 * This function is used to dismiss the todler message.
	 */
	public void DismissToasterMessage()
	{
		if(WaitHelper.SoftWaitUntilElementDisplayed(dismissToasterMsg, driver))
			dismissToasterMsg.click();
	}
}
