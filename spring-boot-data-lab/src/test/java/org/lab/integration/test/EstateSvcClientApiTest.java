package org.lab.integration.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.UUID;

import org.junit.Test;
import org.lab.estate.TestData;
import org.lab.estate.client.EstateSvcApi;
import org.lab.estate.client.SecuredRestBuilder;
import org.lab.estate.client.SecuredRestException;
import org.lab.estate.repository.Estate;

import retrofit.RestAdapter.LogLevel;
import retrofit.RetrofitError;
import retrofit.client.ApacheClient;

import com.google.gson.JsonObject;

/**
 * 
 * This integration test sends a POST request to the EstateServlet to add a new
 * Estate and then sends a second GET request to check that the Estate showed up
 * in the list of Estates. Actual network communication using HTTP is performed
 * with this test.
 * 
 * The test requires that the EstateSvc be running first (see the directions in
 * the README.md file for how to launch the Application).
 * 
 * To run this test, right-click on it in Eclipse and select
 * "Run As"->"JUnit Test"
 * 
 * Pay attention to how this test that actually uses HTTP and the test that just
 * directly makes method calls on a EstateSvc object are essentially identical.
 * All that changes is the setup of the EstateService variable. Yes, this could
 * be refactored to eliminate code duplication...but the goal was to show how
 * much Retrofit simplifies interaction with our service!
 * 
 * @author jules
 * 
 */
public class EstateSvcClientApiTest {

	private final String USERNAME = "admin";
	private final String PASSWORD = "123456";
	private final String CLIENT_ID = "mobile";
	private final String READ_ONLY_CLIENT_ID = "mobileReader";

	private final String TEST_URL = "https://localhost:8443";

	private EstateSvcApi estateService = new SecuredRestBuilder()
			.setLoginEndpoint(TEST_URL + EstateSvcApi.TOKEN_PATH)
			.setUsername(USERNAME)
			.setPassword(PASSWORD)
			.setClientId(CLIENT_ID)
			.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			.setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL).build()
			.create(EstateSvcApi.class);

	private EstateSvcApi readOnlyEstateService = new SecuredRestBuilder()
			.setLoginEndpoint(TEST_URL + EstateSvcApi.TOKEN_PATH)
			.setUsername(USERNAME)
			.setPassword(PASSWORD)
			.setClientId(READ_ONLY_CLIENT_ID)
			.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			.setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL).build()
			.create(EstateSvcApi.class);

	private EstateSvcApi invalidClientEstateService = new SecuredRestBuilder()
			.setLoginEndpoint(TEST_URL + EstateSvcApi.TOKEN_PATH)
			.setUsername(UUID.randomUUID().toString())
			.setPassword(UUID.randomUUID().toString())
			.setClientId(UUID.randomUUID().toString())
			.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			.setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL).build()
			.create(EstateSvcApi.class);

	private Estate estate = TestData.randomEstate();

	
	/**
	 * This test creates a Estate, adds the Estate to the EstateSvc, and then
	 * checks that the Estate is included in the list when getEstateList() is
	 * called.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testEstateAddAndList() throws Exception {
		// Add the Estate
		estateService.addEstate(estate);

		// We should get back the Estate that we added above
		Collection<Estate> estates = estateService.getEstateList();
		assertTrue(estates.contains(estate));
	}

	/**
	 * This test ensures that clients with invalid credentials cannot get access
	 * to estates.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAccessDeniedWithIncorrectCredentials() throws Exception {

		try {
			// Add the estate
			invalidClientEstateService.addEstate(estate);

			fail("The server should have prevented the client from adding a estate"
					+ " because it presented invalid client/user credentials");
		} catch (RetrofitError e) {
			assert (e.getCause() instanceof SecuredRestException);
		}
	}

	/**
	 * This test ensures that read-only clients can access the estate list but
	 * not add new estates.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testReadOnlyClientAccess() throws Exception {

		Collection<Estate> estates = readOnlyEstateService.getEstateList();
		assertNotNull(estates);

		try {
			// Add the estate
			readOnlyEstateService.addEstate(estate);

			fail("The server should have prevented the client from adding a estate"
					+ " because it is using a read-only client ID");
		} catch (RetrofitError e) {
			JsonObject body = (JsonObject) e.getBodyAs(JsonObject.class);
			assertEquals("insufficient_scope", body.get("error").getAsString());
		}
	}

	/**
	 * Test that multiple estates were inserted properly
	 * 
	 * @throws Exception
	 */
	@Test
	public void testEstateAddMultipleAndList() throws Exception {
		// Add the Estate

		Estate e1 = TestData.randomEstate();
		Estate e2 = TestData.randomEstate();
		Estate e3 = TestData.randomEstate();

		estateService.addEstate(e1);
		estateService.addEstate(e2);
		estateService.addEstate(e3);

		// We should get back the Estate that we added above
		Collection<Estate> estates = estateService.getEstateList();

		assertTrue(estates.contains(e1));
		assertTrue(estates.contains(e2));
		assertTrue(estates.contains(e3));
	}

}
