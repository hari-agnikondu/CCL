package com.incomm.cclp.dao;

import java.math.BigDecimal;
import java.util.List;

import com.incomm.cclp.domain.Location;



/**
 * @author Lavanya
 *
 */
public interface LocationDAO {
	
			public List<Location> getAllLocations();
			
			public Location getLocationById(Long locationId);

			public void createLocation(Location location);
			
			public void updateLocation(Location location);
			
			public void deleteLocation(Location location);

			public List<Location> getLocationByMerchantName(String merchantName);
			
			public BigDecimal isMappedToInventory(Long locationId);

}
