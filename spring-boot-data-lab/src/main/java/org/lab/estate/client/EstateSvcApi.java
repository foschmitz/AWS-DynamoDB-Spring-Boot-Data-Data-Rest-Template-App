package org.lab.estate.client;

import java.util.Collection;

import org.lab.estate.repository.Estate;
import org.lab.estate.repository.EstateType;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * This interface defines an API for a EstateService. The interface is used to
 * provide a contract for client/server interactions. The interface is annotated
 * with Retrofit annotations so that clients can automatically convert the
 * interface into a client object. See EstateSvcClientApiTest for an example of
 * how Retrofit is used to turn this interface into a client.
 * 
 * @author fs
 * 
 */
public interface EstateSvcApi {

	public static final String PASSWORD_PARAMETER = "password";

	public static final String USERNAME_PARAMETER = "username";

	public static final String TYPE_PARAMETER = "type";

	public static final String PURCHASEPRICE_PARAMETER = "purchaseprice";

	public static final String TOKEN_PATH = "/oauth/token";

	// The path where we expect the EstateSvc to live
	public static final String ESTATE_SVC_PATH = "/estate";

	// The path to search estates by title
	public static final String ESTATE_TYPE_SEARCH_PATH = ESTATE_SVC_PATH
			+ "/findByType";

	public static final String ESTATE_PURCHASEPRICE_SEARCH_PATH = ESTATE_SVC_PATH
			+ "/search/findByPurchasepriceLessThan";

	@GET(ESTATE_SVC_PATH)
	public Collection<Estate> getEstateList();

	@POST(ESTATE_SVC_PATH)
	public Void addEstate(@Body Estate e);

	@GET(ESTATE_SVC_PATH)
	public Collection<Estate> findByType(@Query(TYPE_PARAMETER) EstateType type);

	@GET(ESTATE_PURCHASEPRICE_SEARCH_PATH)
	public Collection<Estate> findByPurchasepriceLessThan(
			@Query(PURCHASEPRICE_PARAMETER) Long purchasePrice);

}
