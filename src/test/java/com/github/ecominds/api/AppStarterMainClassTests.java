/**
 * @author	: Rajiv Kumar
 * @project	: boot-rest-api
 * @since	: 0.0.1
 * @date	: 07-Jan-2023
 * @version : 0.0.2
 */

/**
 * In <code>0.0.2</code> version, @SpringBootTest is updated to @DataJpaTest
 * @UpdatedDate : 07-Jan-2023
 */

package com.github.ecominds.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AppStarterMainClassTests {

	@Test
	void contextLoads() {
	}
}