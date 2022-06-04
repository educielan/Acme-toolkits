package acme.testing.inventor.chimpum;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class InventorChimpumDeleteTest extends TestHarness{
	
	
	
	@ParameterizedTest
	@CsvFileSource(resources = "/inventor/chimpum/delete.csv", encoding ="utf-8", numLinesToSkip = 1)
	@Order(10)
	public void positiveTest(final int recordIndex, final String title) {
		
		super.signIn("inventor1", "inventor1");
		
		super.clickOnMenu("Inventor", "List my chimpums");
		super.checkListingExists();
		
		super.sortListing(0, "asc");
		
		super.checkColumnHasValue(recordIndex, 1, title);
		super.clickOnListingRecord(recordIndex);
		

		super.clickOnSubmit("Delete");
		
		super.clickOnMenu("Inventor", "List my chimpums");
		super.checkNotErrorsExist();

		super.signOut();
		
	
	}
	
	@Test
	@Order(30)
	public void hackingDeleteTest() {
		super.checkNotLinkExists("Account");
		super.navigate("/inventor/chimpum/delete");
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.navigate("/inventor/chimpum/delete");
		super.checkPanicExists();
		super.signOut();

		super.signIn("patron1", "patron1");
		super.navigate("/inventor/chimpum/delete");
		super.checkPanicExists();
		super.signOut();

	}

}
