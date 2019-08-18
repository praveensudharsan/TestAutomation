package Amazon.PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.testng.Reporter;
import org.testng.xml.XmlTest;

import Amazon.ElementFactory.SearchPanelElements;
import genericLibrary.WaitHelper;

public class SearchPanel extends SearchPanelElements{

	WebDriver driver = null;

	public SearchResults searchResults;

	/**
	 * DefaultPage constructor.
	 * @param driver
	 */
	public SearchPanel(WebDriver driver)
	{
		this.driver = driver;
		PageFactory.initElements(driver, this);		
	}

	/**
	 * This function is used to select the search category.
	 * @param category
	 * @throws Exception 
	 */
	public void SelectCategory(String category) throws Exception
	{
		// Open the drop down list.
		searchCategoryDropDown.click();

		XmlTest test = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest();
		String driverType = test.getParameter("driverType");

		// Check if driverType is firefox.
		// Note: In Firefox, Drop down selection is not working as other browser thorugh selenium.
		// So handled that using sendkeys.
		if(driverType.equalsIgnoreCase("firefox"))
		{
			Select sel = new Select(searchCategory);
			int size = sel.getOptions().size();
			int i = 0;

			// Check until expected item is selected with exit criteria of max category.
			while(i < size)
			{
				// Enter the first letter of the category.
				searchCategory.sendKeys(Character.toString(category.charAt(0)));

				// Check if selected item is expected category and break if its expected.
				if(new Select(searchCategory).getAllSelectedOptions().get(0).getText().equalsIgnoreCase(category))
					break;
				i++;
			}

			// Check whether the item is selected or not.
			if(i >= size)
				throw new Exception("Category '" + category + "' not found to select in the search category drop down list.");
		}
		else
		{
			WebElement option = searchCategory.findElement(By.xpath("//option[text()='"+category+"']"));
			WaitHelper.WaitUntilElementDisplayed(option, driver);
			option.click();
		}
	}

	/**
	 * This function is used to perform the search using the search keyword with specific category
	 * @param searchWord
	 * @param category
	 * @return
	 * @throws Exception
	 */
	public SearchResults SearchByCategory(String searchWord, String category) throws Exception
	{
		// Select the category.
		SelectCategory(category);

		// Enter the keyword after clearing the search field.
		searchTextBox.click();
		searchTextBox.clear();
		searchTextBox.sendKeys(searchWord);

		// Click the search button.
		ClickSearchButton();

		return new SearchResults(driver);
	}

	/**
	 * This function is used to click the search button.
	 */
	public void ClickSearchButton(){
		searchButton.click();
	}
}
