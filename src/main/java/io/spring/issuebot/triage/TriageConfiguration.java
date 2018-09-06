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

package io.spring.issuebot.triage;

import java.util.Arrays;
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
 * Central configuration for the beans involved in identifying issues that require triage.
 *
 * @author Andy Wilkinson
 */
@Configuration
@EnableConfigurationProperties(TriageProperties.class)
class TriageConfiguration {

	@Bean
	MultiRepositoryIssueListener triageIssueListener(GitHubOperations gitHubOperations,
			TriageProperties triageProperties, MonitoringProperties monitoringProperties,
			GitHubProperties gitHubProperties) {
		Map<Repository, IssueListener> delegates = monitoringProperties.getRepositories()
				.stream()
				.collect(Collectors.toMap(Function.identity(),
						(repository) -> createListener(repository, gitHubOperations,
								triageProperties)));
		return new RoutingMultiRepositoryIssueListener(delegates);
	}

	private TriageIssueListener createListener(Repository repository,
			GitHubOperations gitHubOperations, TriageProperties triageProperties) {
		return new TriageIssueListener(
				Arrays.asList(
						new OpenedByCollaboratorTriageFilter(
								repository.getCollaborators()),
						new LabelledTriageFilter(), new MilestoneAppliedTriageFilter()),
				new LabelApplyingTriageListener(gitHubOperations,
						triageProperties.getLabel()));
	}

}
