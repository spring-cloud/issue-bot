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

package io.spring.issuebot.question;

import java.time.OffsetDateTime;
import java.util.List;

import io.spring.issuebot.IssueListener;
import io.spring.issuebot.github.Event;
import io.spring.issuebot.github.GitHubOperations;
import io.spring.issuebot.github.Issue;
import io.spring.issuebot.github.Label;
import io.spring.issuebot.github.Page;

/**
 * An {@link IssueListener} that closes questions that are old.
 *
 * @author Spencer Gibb
 */
final class QuestionIssueListener implements IssueListener {

	private final GitHubOperations gitHub;

	private final String labelName;

	private final String closeComment;

	private final List<IssueListener> issueListeners;

	QuestionIssueListener(GitHubOperations gitHub, String labelName, String closeComment,
			List<IssueListener> issueListeners) {
		this.gitHub = gitHub;
		this.labelName = labelName;
		this.closeComment = closeComment;
		this.issueListeners = issueListeners;
	}

	@Override
	public void onOpenIssue(Issue issue) {
		if (labelledAsQuestion(issue)) {
			OffsetDateTime questionSince = getQuestionSince(issue);
			if (questionSince != null) {
				questionSince(issue, questionSince);
			}
		}
	}

	private void questionSince(Issue issue, OffsetDateTime questionSince) {
		OffsetDateTime now = OffsetDateTime.now();
		if (questionSince.plusMonths(6).isBefore(now)) {
			close(issue);
		}
	}

	private void close(Issue issue) {
		this.gitHub.addComment(issue, this.closeComment);
		this.gitHub.close(issue);
		this.issueListeners.forEach((listener) -> listener.onIssueClosure(issue));
	}

	private boolean labelledAsQuestion(Issue issue) {
		for (Label label : issue.getLabels()) {
			if (this.labelName.equals(label.getName())) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("Duplicates")
	private OffsetDateTime getQuestionSince(Issue issue) {
		OffsetDateTime createdAt = null;
		Page<Event> page = this.gitHub.getEvents(issue);
		while (page != null) {
			for (Event event : page.getContent()) {
				if (Event.Type.LABELED.equals(event.getType()) && this.labelName.equals(event.getLabel().getName())) {
					createdAt = event.getCreationTime();
				}
			}
			page = page.next();
		}
		return createdAt;
	}

}
