package com.incomm.cclp.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppVersionController {

	@Value("${application.name:ccl-spil}")
	private String applicationName;

	@Value("${build.version:unknown}")
	private String buildVersion;

	@Value("${build.timestamp:unknown}")
	private String buildTimestamp;

	@Value("${SVN_VERSION:unknown}")
	private String scmVersion;

	@GetMapping("version")
	public String getAppVersion() {
		return applicationName + "-" + buildVersion + "\n" + scmVersion + "-" + buildTimestamp;
	}
}
