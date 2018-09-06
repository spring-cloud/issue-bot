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

import java.util.Map;

import io.spring.issuebot.MonitoringProperties.Repository;
import io.spring.issuebot.github.Issue;

/**
 * A {@code MultiRepositoryIssueListener} that performs repository-based routing to
 * specific {@link IssueListener IssueListeners}.
 *
 * @author Andy Wilkinson
 */
public class RoutingMultiRepositoryIssueListener implements MultiRepositoryIssueListener {

	private final Map<Repository, IssueListener> delegates;

	public RoutingMultiRepositoryIssueListener(Map<Repository, IssueListener> delegates) {
		this.delegates = delegates;
	}

	@Override
	public void onOpenIssue(Repository repository, Issue issue) {
		IssueListener listener = this.delegates.get(repository);
		if (listener != null) {
			listener.onOpenIssue(issue);
		}
	}

	@Override
	public void onIssueClosure(Repository repository, Issue issue) {
		IssueListener listener = this.delegates.get(repository);
		if (listener != null) {
			listener.onIssueClosure(issue);
		}
	}

}
