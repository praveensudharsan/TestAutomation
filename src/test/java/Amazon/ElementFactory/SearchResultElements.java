package Amazon.ElementFactory;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.LoadableComponent;

public class SearchResultElements extends LoadableComponent<SearchResultElements>{

	@FindBy(how = How.CSS, using = "div[data-cel-widget*='search_result']")
	protected List<WebElement> searchResults;
	
	protected static String searchResultTitleCss = ".a-link-normal span[class*='a-text-normal']";
	protected static String searchResultPaperbackPriceXpath = "//a[contains(text(),'Paperback')]//..//..//span[@class='a-price' and @data-a-color='base']//span[@aria-hidden='true']";
	protected static String searchResultKindlePriceXpath = "//a[contains(text(),'Kindle')]//..//..//span[@class='a-price' and @data-a-color='base']//span[@aria-hidden='true']";
	
	
	@Override
	protected void load() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void isLoaded() throws Error {
		// TODO Auto-generated method stub
		
	}

}
