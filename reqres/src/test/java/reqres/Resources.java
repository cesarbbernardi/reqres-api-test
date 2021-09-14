package reqres;
/*
	This package implements some API tests for the Req|Res Demo API.
	This file implements tests for the "Unknown" class (described in the page as "List <Resource>", but stored in a /unknown/ folder)
*/

import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;

public class Resources {
	String uri = "https://reqres.in/api/unknown/";
	String contentType = "application/json";


	@Test (priority=1, enabled=true)
		public void getAllResources() {	// GETs all registered pre-posted resources from the database.
			given()
				.contentType(contentType)
				.log().all()
			.when()
				.get(uri)
			.then()
				.statusCode(200)
				.log().all()
			;
		}

	@Test (priority=2, enabled=true)	
			public void getResource_success() {
			/* GETs a Resource from the database. Works for some predefined resources (IDs 1-12).
			Searches for random IDs within that range */
				Integer randomizedId = Functions.randomId(12); // it'll randomize between 0 and 11, then increment it.

				given()
					.contentType(contentType)
					.log().all()
				.when()
					.get(uri+randomizedId)
				.then()
					.statusCode(200)
					.log().all()
				;
			}
	
		@Test (priority=3, enabled=true)
		public void getResource_fail() {
			/* Fails for any ID bigger than 12, so it's simply a matter of adding 12 to any ID randomized.
			Also verifies the response body */

			Integer randomizedId = (Functions.randomId(12) + 12);
			
			String responseBody =
			given()
				.contentType(contentType)
				.log().all()
			.when()
				.get(uri+randomizedId)
			.then()
				.statusCode(404)
				.log().all()
				.extract().body().asString()
			;
			Assert.assertEquals(responseBody,"{}"); // This response *is not* an empty string. The content is two empty brackets.
		}


}
