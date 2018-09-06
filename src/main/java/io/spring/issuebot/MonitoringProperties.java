/*
 * Copyright 2015-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.issuebot;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties for configuring repository monitoring.
 *
 * @author Andy Wilkinson
 */
@ConfigurationProperties(prefix = "issuebot.monitoring")
public class MonitoringProperties {

	private List<Repository> repositories;

	public List<Repository> getRepositories() {
		return this.repositories;
	}

	public void setRepositories(List<Repository> repositories) {
		this.repositories = repositories;
	}

	/**
	 * A repository that is monitored.
	 */
	public static class Repository {

		/**
		 * The name of the organization that owns the repository.
		 */
		private String organization;

		/**
		 * The name of the repository.
		 */
		private String name;

		/**
		 * The names of the repository's collaborators.
		 */
		private List<String> collaborators;

		public String getOrganization() {
			return this.organization;
		}

		public void setOrganization(String organization) {
			this.organization = organization;
		}

		public String getName() {
			return this.name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<String> getCollaborators() {
			return this.collaborators;
		}

		public void setCollaborators(List<String> collaborators) {
			this.collaborators = collaborators;
		}

	}

}
