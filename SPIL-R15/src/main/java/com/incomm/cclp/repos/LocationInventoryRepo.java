package com.incomm.cclp.repos;

import java.math.BigInteger;
import java.util.Map;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.domain.LocationInventory;

@Repository
public interface LocationInventoryRepo extends CrudRepository<LocationInventory, String> {
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = QueryConstants.UPDATE_LOCATION_INVENTORY, nativeQuery = true)
	public int updateInventory(@Param("productId") BigInteger productId, @Param("merchantId") String merchantId,
			@Param("locationId") String locationId);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = QueryConstants.SUBTRACT_LOCATION_INVENTORY, nativeQuery = true)
	public int removeFromInventory(@Param("productId") BigInteger productId, @Param("merchantId") String merchantId,
			@Param("locationId") String locationId);

	@Query(value = QueryConstants.GET_LOCATION_MERCHANT_ID, nativeQuery = true)
	public Map<String, String> getLocationMerchanntID(@Param("cardNumHash") String cardNumHash);
}
