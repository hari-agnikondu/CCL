/**
 * 
 */
package com.incomm.cclpvms.profiles;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Load the Incomm UAT A profile.
 * 
 * @author abutani
 *
 */
@Configuration
@Profile("uata")
public class UatAProfile {


	@Bean(name="uata")
	public static PropertyPlaceholderConfigurer properties() {

		PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
		Resource[] resources = new ClassPathResource[] { 
				new ClassPathResource("application-uata.properties") };
		ppc.setLocations(resources);
		ppc.setIgnoreUnresolvablePlaceholders(true);
		
		return ppc;
	}
}
