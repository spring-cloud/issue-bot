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

package io.spring.issuebot.github;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Details of a GitHub pull request.
 *
 * @author Andy Wilkinson
 */
public class PullRequest {

	private final String url;

	/**
	 * Creates a new {@code PullRequest} that has the given {@code url} in the GitHub API.
	 * @param url the url
	 */
	@JsonCreator
	public PullRequest(@JsonProperty("url") String url) {
		this.url = url;
	}

	public String getUrl() {
		return this.url;
	}

}
