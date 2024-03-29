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

package io.spring.issuebot.feedback;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.spring.issuebot.GitHubProperties;
import io.spring.issuebot.IssueListener;
import io.spring.issuebot.MonitoringProperties;
import io.spring.issuebot.MonitoringProperties.Repository;
import io.spring.issuebot.MultiRepositoryIssueListener;
import io.spring.issuebot.RoutingMultiRepositoryIssueListener;
import io.spring.issuebot.github.GitHubOperations;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Central configuration for the beans involved in managing issues that are waiting for
 * feedback.
 *
 * @author Andy Wilkinson
 */
@Configuration
@EnableConfigurationProperties(FeedbackProperties.class)
class FeedbackConfiguration {

	@Bean
	MultiRepositoryIssueListener feedbackIssueListener(MonitoringProperties monitoringProperties,
			GitHubOperations gitHub, GitHubProperties githubProperties, FeedbackProperties feedbackProperties) {
		Map<Repository, IssueListener> delegates = monitoringProperties.getRepositories()
			.stream()
			.collect(Collectors.toMap(Function.identity(),
					(repository) -> createListener(repository, gitHub, githubProperties, feedbackProperties)));
		return new RoutingMultiRepositoryIssueListener(delegates);
	}

	private FeedbackIssueListener createListener(Repository repository, GitHubOperations gitHub,
			GitHubProperties githubProperties, FeedbackProperties feedbackProperties) {
		return new FeedbackIssueListener(gitHub, feedbackProperties.getRequiredLabel(), repository.getCollaborators(),
				githubProperties.getCredentials().getUsername(),
				new StandardFeedbackListener(gitHub, feedbackProperties.getProvidedLabel(),
						feedbackProperties.getRequiredLabel(), feedbackProperties.getReminderLabel(),
						feedbackProperties.getReminderComment(), feedbackProperties.getCloseComment(),
						Collections.emptyList()));
	}

}
