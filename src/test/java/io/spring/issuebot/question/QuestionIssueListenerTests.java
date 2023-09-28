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
import java.util.Arrays;

import io.spring.issuebot.IssueListener;
import io.spring.issuebot.github.Event;
import io.spring.issuebot.github.GitHubOperations;
import io.spring.issuebot.github.Issue;
import io.spring.issuebot.github.Label;
import io.spring.issuebot.github.PullRequest;
import io.spring.issuebot.github.StandardPage;
import org.junit.jupiter.api.Test;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Tests for {@link QuestionIssueListener}.
 *
 * @author Spencer Gibb
 */
public class QuestionIssueListenerTests {

	private final GitHubOperations gitHub = mock(GitHubOperations.class);

	private final IssueListener issueListener = mock(IssueListener.class);

	private final IssueListener listener = new QuestionIssueListener(this.gitHub, "question", "Closing old question",
			Arrays.asList(this.issueListener));

	@Test
	public void nonQuestionsAreIgnored() {
		Issue issue = new Issue(null, null, null, null, null, Arrays.asList(new Label("foo")), null,
				new PullRequest("url"));
		this.listener.onOpenIssue(issue);
		verifyNoMoreInteractions(this.gitHub, this.issueListener);
	}

	@Test
	public void questionsLessThanSixMonthsAreIgnored() {
		Issue issue = new Issue(null, null, null, null, null, Arrays.asList(new Label("question")), null, null);
		OffsetDateTime requestTime = OffsetDateTime.now();
		given(this.gitHub.getEvents(issue)).willReturn(new StandardPage<>(
				Arrays.asList(new Event("labeled", requestTime, new Label("required"))), () -> null));
		this.listener.onOpenIssue(issue);
		verify(this.gitHub).getEvents(issue);
		verifyNoMoreInteractions(this.gitHub);
	}

	@Test
	public void questionsOlderThanSixMonthsAreClosed() {
		Issue issue = new Issue("http://github/old/issue", null, null, null, null, Arrays.asList(new Label("question")),
				null, null);
		OffsetDateTime requestTime = OffsetDateTime.now().minusMonths(7);
		given(this.gitHub.getEvents(issue)).willReturn(new StandardPage<>(
				Arrays.asList(new Event("labeled", requestTime, new Label("question"))), () -> null));
		this.listener.onOpenIssue(issue);
		verify(this.gitHub).addComment(issue, "Closing old question");
		verify(this.gitHub).close(issue);
		verify(this.issueListener).onIssueClosure(issue);
	}

}
