package Amazon.PageObjects;

import java.util.HashMap;
import java.util.Iterator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import Amazon.ElementFactory.SearchResultElements;
import genericLibrary.Log;
import genericLibrary.WaitHelper;

public class SearchResults extends SearchResultElements{

	WebDriver driver = null;

	/**
	 * DefaultPage constructor.
	 * @param driver
	 */
	public SearchResults(WebDriver driver)
	{
		this.driver = driver;
		PageFactory.initElements(driver, this);	
		WaitHelper.SoftWaitUntilElementDisplayed(searchResults.get(0), driver);
	}
	
	/**
	 * This function is used to get the first search results details like title, paperback price and kindle price if available.
	 * @return
	 */
	public HashMap<String, String> GetFirstSearchResultDetails()
	{
		HashMap<String, String> firstSearchResult = new HashMap<>();
		firstSearchResult.put("Title", searchResults.get(0).findElement(By.cssSelector(searchResultTitleCss)).getText());
		firstSearchResult.put("Paperback price", searchResults.get(0).findElement(By.xpath(searchResultPaperbackPriceXpath)).getText().replaceAll("\n", "."));
		try
		{
			WebElement kindlePrice = searchResults.get(0).findElement(By.xpath(searchResultKindlePriceXpath));
			firstSearchResult.put("Kindle price", kindlePrice.getText().replaceAll("\n", "."));
		}catch(Exception ex) {}
		return firstSearchResult;
	}
	
	/**
	 * This function is used to print the search results.
	 * @param result
	 */
	public void PrintSearchResult(HashMap<String, String> result)
	{
		Iterator<String> keys = result.keySet().iterator();
		while(keys.hasNext())
		{
			String key = keys.next(); 
			Log.message( key +": "+result.get(key));
		}
	}
}
