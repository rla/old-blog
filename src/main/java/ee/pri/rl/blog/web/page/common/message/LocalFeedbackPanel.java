/**
 * Copyright (C) 2009 Raivo Laanemets <rl@starline.ee>
 *
 * This file is part of rl-blog.
 *
 * Foobar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with rl-blog.  If not, see <http://www.gnu.org/licenses/>.
 */
package ee.pri.rl.blog.web.page.common.message;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

/**
 * Panel for form messages. Taken from Wicket-tricks draft (trick L).
 * Not modified code but used here without LocalFeedbackContainer.
 * 
 * @author Jonathan Locke
 * @author Raivo Laanemets
 */
public class LocalFeedbackPanel extends FeedbackPanel {

	private static final long serialVersionUID = -2321986159300632847L;

	public LocalFeedbackPanel(String id, final MarkupContainer container) {
		super(id, new IFeedbackMessageFilter() {
			private static final long serialVersionUID = -1883211501894198761L;

			public boolean accept(FeedbackMessage message) {
				return container.contains(message.getReporter(), true);
			}
		});
	}
}