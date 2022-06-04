package acme.testing.inventor.chimpum;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class InventorChimpumShowTest extends TestHarness {
	
	@ParameterizedTest
	@CsvFileSource(resources = "/inventor/chimpum/show.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(10)
	public void positiveTest(final int recordIndex, final String code, final String creationMoment,final String title, 
		 final String description, final String startDate,final String endDate,
		 final String budget, final String link) {
		
		super.signIn("inventor1", "inventor1");
	
		super.clickOnMenu("Inventor", "List my chimpums");
		super.sortListing(0, "asc");
		
		super.checkColumnHasValue(recordIndex, 0, code);
		super.checkColumnHasValue(recordIndex, 1, title);
		
		super.clickOnListingRecord(recordIndex);
		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("creationMoment",creationMoment);
		super.checkInputBoxHasValue("budget", budget);
		super.checkInputBoxHasValue("description",description);
		super.checkInputBoxHasValue("startDate",startDate);
		super.checkInputBoxHasValue("endDate", endDate);
		super.checkInputBoxHasValue("link",link);
		
		super.signOut();
	}

}
