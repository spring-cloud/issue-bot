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

import io.spring.issuebot.github.Issue;
import io.spring.issuebot.github.Milestone;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link MilestoneAppliedTriageFilter}.
 *
 * @author Andy Wilkinson
 *
 */
public class MilestoneAppliedTriageFilterTests {

	private TriageFilter filter = new MilestoneAppliedTriageFilter();

	@Test
	public void issueWithMilestoneApplied() {
		assertThat(this.filter.triaged(new Issue(null, null, null, null, null, null, new Milestone("test"), null)))
			.isTrue();
	}

	@Test
	public void issueWithNoMilestoneApplied() {
		assertThat(this.filter.triaged(new Issue(null, null, null, null, null, null, null, null))).isFalse();
	}

}
