package org.lab.estate.repository;

import java.util.Collection;

import org.lab.estate.client.EstateSvcApi;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * An interface for a repository that can store Estate objects and allow them to
 * be searched by description.
 * 
 * @author fsc
 * 
 */
@EnableScan
@RepositoryRestResource(path = EstateSvcApi.ESTATE_SVC_PATH)
public interface EstateRepository extends CrudRepository<Estate, String> {

	// Find all estates with a matching title (e.g., Estate.name)
	public Collection<Estate> findByType(
			@Param(EstateSvcApi.ESTATE_TYPE_SEARCH_PATH) EstateType type);

	// Find all esates that have a price higher than specified price
	public Collection<Estate> findByPurchasepriceLessThan(
	// The @Param annotation tells tells Spring Data Rest which HTTP request
	// parameter it should use to fill in the "duration" variable used to
	// search for Estates
			@Param(EstateSvcApi.ESTATE_PURCHASEPRICE_SEARCH_PATH) long purchaseprice);
}
