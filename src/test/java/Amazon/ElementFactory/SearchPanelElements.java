package Amazon.ElementFactory;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.LoadableComponent;

public class SearchPanelElements extends LoadableComponent<SearchPanelElements> {

	@FindBy(how = How.CSS, using=".nav-searchbar #nav-search-dropdown-card .nav-search-scope")
	protected WebElement searchCategoryDropDown;
	
	@FindBy(how = How.CSS, using=".nav-searchbar #nav-search-dropdown-card .nav-search-scope #searchDropdownBox")
	protected WebElement searchCategory;
	
	@FindBy(how = How.CSS, using="#twotabsearchtextbox")
	protected WebElement searchTextBox;
	
	@FindBy(how = How.CSS, using="input[type='submit']")
	protected WebElement searchButton;
	
	@Override
	protected void load() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void isLoaded() throws Error {
		// TODO Auto-generated method stub
		
	}

}
