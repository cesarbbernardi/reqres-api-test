package reqres;
/*
	This package implements some API tests for the Req|Res Demo API.
	This file implements tests for the User class. 
*/

import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import java.io.IOException;
import static org.hamcrest.CoreMatchers.is;

public class User {
	String uri = "https://reqres.in/api/";
	String contentType = "application/json";
	String userId;


	@Test (priority=1, enabled=true)
		public void getAllUsers() {
			/* GETs all registered preset users from the database.
			Works for pages IDs 1 and 2, set for random IDs within that range */
		
			Integer randomizedId = Functions.randomId(2); // it'll randomize between 0 and 1, then we increment it.

			given()
				.contentType(contentType)
				.log().all()
			.when()
				.get(uri+"users?page="+randomizedId)
			.then()
				.statusCode(200)
				.log().all()
			;
		}

	@Test (priority=2, enabled=true)
		public void postUser() throws IOException { // POSTs a new User
			String jsonBody = Functions.readJSON("db/users/user1.json");
			
			userId =
			given()
				.contentType(contentType)
				.log().all()
				.body(jsonBody)
			.when()
				.post(uri+"users")
			.then()
				.statusCode(201)
				.log().all()
				.extract().response().path("id")
			;
		}

	@Test (priority=3, enabled=true)	
			public void getUser_success() {
			/* GETs an User from the database. Always fails for the users I've posted, but Works for some predefined users (IDs 1-12).
			Set for random IDs within that range */
				Integer randomizedId = Functions.randomId(12); // it'll randomize between 0 and 11, then we increment it.
				given()
					.contentType(contentType)
					.log().all()
				.when()
					.get(uri+"users/"+randomizedId)
				.then()
					.statusCode(200)
					.log().all()
				;
			}
	
		@Test (priority=4, enabled=true)
		public void getUser_fail() {
		/* Fails for any ID bigger than 12, so it's simply a matter of adding 12 to any ID randomized.
		Also verifies the response body */
			Integer randomizedId = (Functions.randomId(12) + 12);
		
			String responseBody =
			given()
				.contentType(contentType)
				.log().all()
			.when()
				.get(uri+"users/"+randomizedId)
			.then()
				.statusCode(404)
				.log().all()
				.extract().body().asString()
			;
			Assert.assertEquals(responseBody, "{}"); // This response *is not* an empty string. The content is two empty brackets.
		}

	@Test (priority=5, enabled=true)
		public void putUser() throws IOException {
			/* PUTs new data for the User posted.
			Since the GET method only reliably works for IDs 1-12, we need to check the body of the User posted.*/
			String jsonBody = Functions.readJSON("db/users/user1updt.json");

			given()
				.contentType(contentType)
				.log().all()
				.body(jsonBody)
			.when()
				.put(uri+"update/"+userId)
			.then()
				.statusCode(200)
				.log().all()
				.body("job", is("unemployed"))
			;
		}

	@Test (priority=6, enabled=true)
		public void deleteUser() {
			/* DELETEs the User posted (this one works for custom ids). Since GETting custom IDs doesn't work for this API
			(which is more reliable), validation of the expected response for this request (empty body) was added */
			String responseString =
			given()
				.contentType(contentType)
				.log().all()
			.when()
				.delete(uri+"delete/"+userId)
			.then()
				.statusCode(204)
				.log().all()
				.extract().response().body().asString() // extracts an empty string
			;
			Assert.assertEquals(responseString.isEmpty(),true); // confirms that the string is empty
		}

	@Test (priority=7, enabled=true)
		public void getDelayedResponse() {
			/* Gets a delayed response based on a parameter "?delay", valued in random seconds (randomized here between 3-8)
			Returns the first page of preset users*/
			Integer randomDelay = (Functions.randomId(6) + 2);
			// this +2 sets the lower limit as 3 and the upper limit as 8, without having to modify the randomId function used before.

			given()
				.contentType(contentType)
				.log().all()
			.when()
				.get(uri+"users?delay="+randomDelay)
			.then()
				.statusCode(200)
				.log().all()
			;
		}

}
