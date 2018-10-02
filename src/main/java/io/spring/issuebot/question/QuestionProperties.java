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

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * {@link EnableConfigurationProperties Configuration properties} for configuring the
 * monitoring of old questions.
 *
 * @author Spencer Gibb
 */
@ConfigurationProperties(prefix = "issuebot.question")
final class QuestionProperties {

	/**
	 * Name of the label that marks an issue as a question.
	 */
	private String label;

	/**
	 * The text of the comment that is added when an issue is clsed as feedback has not
	 * been provided.
	 */
	private String closeComment;

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getCloseComment() {
		return this.closeComment;
	}

	public void setCloseComment(String closeComment) {
		this.closeComment = closeComment;
	}

}
