package reqres;
/*
	This package implements some API tests for the Req|Res Demo API.
	This file implements tests for the Register class.
*/

import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import java.io.IOException;
import static org.hamcrest.CoreMatchers.is;

public class Login {
	String uri = "https://reqres.in/api/";
	String contentType = "application/json";

	@Test (priority=1, enabled=true)
	public void postLogin_success() throws IOException { 
		/* Receives a token after logging in with an email and password.
		Unfortunately, for this API, this only works for some preset e-mails */
		String jsonBody = Functions.readJSON("db/login/login1.json");

		given()
			.contentType(contentType)
			.log().all()
			.body(jsonBody)
		.when()
			.post(uri+"login")
		.then()
			.statusCode(200)
			.log().all()
			.body("token", is("QpwL5tke4Pnpja7X4")) // token is not random for this API, it is always the same value
		;
	}

	@Test (priority=2, enabled=true)
	public void postRegister_fail1() throws IOException { 
		/* Receives an error message when trying to login an email without password.
		Unfortunately, for this API, this only works for some preset e-mails */
		String jsonBody = Functions.readJSON("db/login/login2.json");

		given()
			.contentType(contentType)
			.log().all()
			.body(jsonBody)
		.when()
			.post(uri+"login")
		.then()
			.statusCode(400)
			.log().all()
			.body("error", is("Missing password"))
		;
	}

	@Test (priority=3, enabled=true)
	public void postRegister_fail2() throws IOException { 
		// Receives an error message when trying to login a password without e-mail.
		String jsonBody = Functions.readJSON("db/login/login3.json");

		given()
			.contentType(contentType)
			.log().all()
			.body(jsonBody)
		.when()
			.post(uri+"login")
		.then()
			.statusCode(400)
			.log().all()
			.body("error", is("Missing email or username"))
		;
	}

	@Test (priority=4, enabled=true)
	public void postRegister_fail3() { 
		// Receives an error message when trying to login without email or password.
		given()
			.contentType(contentType)
			.log().all()
			//.body("")
		.when()
			.post(uri+"login")
		.then()
			.statusCode(400)
			.log().all()
			.body("error", is("Missing email or username"))
		;
	}

}
