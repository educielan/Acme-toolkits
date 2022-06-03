package acme.testing.inventor.chimpum;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class InventorChimpumUpdateTest extends TestHarness {
	@ParameterizedTest
	@CsvFileSource(resources = "/inventor/chimpum/update-positive.csv", encoding ="utf-8", numLinesToSkip = 1)
	@Order(10)
	public void positiveTest(final int recordIndex, final String title, final String description,
		 final String startDate,final String endDate, final String budget,final String link) {
		
		super.signIn("inventor1", "inventor1");
		
		super.clickOnMenu("Inventor", "List my chimpums");
		super.checkListingExists();
		super.clickOnListingRecord(recordIndex);
		
		super.checkFormExists();
		
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("description", description);
		super.fillInputBoxIn("startDate", startDate);
		super.fillInputBoxIn("endDate", endDate);
		super.fillInputBoxIn("budget", budget);
		super.fillInputBoxIn("link", link);
		
		super.clickOnSubmit("Update");
		super.checkNotErrorsExist();
		
		super.clickOnMenu("Inventor","List my chimpums");
		super.checkListingExists();

		super.signOut();
				
	}
	
	@ParameterizedTest
	@CsvFileSource(resources = "/inventor/chimpum/update-negative.csv", encoding ="utf-8", numLinesToSkip = 1)
	@Order(10)
	public void negativeTest(final int recordIndex, final String title, final String description,
		 final String startDate,final String endDate, final String budget,final String link) {

		super.signIn("inventor1", "inventor1");
		
		super.clickOnMenu("Inventor", "List my chimpums");
		super.checkListingExists();
		super.clickOnListingRecord(recordIndex);
		
		super.checkFormExists();
		
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("budget", budget);
		super.fillInputBoxIn("description", description);
		super.fillInputBoxIn("startDate", startDate);
		super.fillInputBoxIn("endDate", endDate);
		super.fillInputBoxIn("link", link);
		
		super.clickOnSubmit("Update");
		super.checkErrorsExist();
	
		super.signOut();
			
	}
	
	@Test
	@Order(30)
	public void hackingUpdateTest() {
		super.checkNotLinkExists("Account");
		super.navigate("/inventor/chimpum/update");
		super.checkPanicExists();
		
		super.signIn("administrator", "administrator");
		super.navigate("/inventor/chimpum/update");
		super.checkPanicExists();
		super.signOut();
	}
}
