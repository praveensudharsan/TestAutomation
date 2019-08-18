package Amazon.ElementFactory;

public class Captions {

	public enum SearchCategories { //Search Categories

		AllDepartments			("All Departments"),//("search-alias=aps"),
		ArtsAndCrafts			("Arts & Crafts"),//("search-alias=arts-crafts-intl-ship"),
		Automotive				("Automotive"),//("search-alias=automotive-intl-ship"),
		Baby					("Baby"),//("search-alias=baby-products-intl-ship"),
		BeautyAndPersonalCare	("Beauty & Personal Care"),//("search-alias=beauty-intl-ship"),
		Books					("Books"),//("search-alias=stripbooks-intl-ship"),
		Computers				("Computers"),//("search-alias=computers-intl-ship"),
		DigitalMusic			("Digital Music"),//("search-alias=digital-music"),
		Electronics				("Electronics"),//("search-alias=electronics-intl-ship"),
		KindleStore				("Kindle Store"),//("search-alias=digital-text"),
		PrimeVideo				("Prime Video"),//("search-alias=instant-video"),
		WomensFashion			("Women's Fashion"),//("search-alias=fashion-womens-intl-ship"),
		MensFashion				("Men's Fashion"),//("search-alias=fashion-mens-intl-ship"),
		GirlsFashion			("Girls' Fashion"),//("search-alias=fashion-girls-intl-ship"),
		BoysFashion				("Boys' Fashion"),//("search-alias=fashion-boys-intl-ship"),
		Deals					("Deals"),//("search-alias=deals-intl-ship"),
		HealthAndHousehold		("Health & Household"),//("search-alias=hpc-intl-ship"),
		HomeAndKitchen			("Home & Kitchen"),//("search-alias=kitchen-intl-ship"),
		IndustrialAndScientific	("Industrial & Scientific"),//("search-alias=industrial-intl-ship"),
		Luggage					("Luggage"),//("search-alias=luggage-intl-ship"),
		MoviesAndTV				("Movies & TV"),//("search-alias=movies-tv-intl-ship"),
		MusicCDsAndVinyl		("Music, CDs & Vinyl"),//("search-alias=music-intl-ship"),
		PetSupplies				("Pet Supplies"),//("search-alias=pets-intl-ship"),
		Software				("Software"),//("search-alias=software-intl-ship"),
		SportsAndOutdoors		("Sport & Outdoors"),//("search-alias=sporting-intl-ship"),
		ToolsAndHomeImprovement	("Tools & Home Improvement"),//("search-alias=tools-intl-ship"),
		ToysAndGames			("Toys & Games"),//("search-alias=toys-and-games-intl-ship"),
		VideoGames				("Video Games");//("search-alias=videogames-intl-ship");

		public String Value;

		SearchCategories(String caption) {
			this.Value = caption;
		}

	}//end enum SearchCategories
}
