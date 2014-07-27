/* 
 * Model Tools.
 * Copyright (C) 2013 Pal Hargitai (pal@lunarray.org)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lunarray.model.generation.swing.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.apache.commons.lang.Validate;

/**
 * Utility for messages.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public enum MessageUtil {

	/** The instance. */
	INSTANCE;

	/** The message bundle. */
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(MessageUtil.BUNDLE_NAME);
	/** The bundle name. */
	private static final String BUNDLE_NAME = "org.lunarray.model.generation.swing.Messages";

	/**
	 * Formats a message.
	 * 
	 * @param key
	 *            The message key. May not be null.
	 * @param parameters
	 *            The message parameters.
	 * @return The formatted message.
	 */
	public static String getMessage(final String key, final Object... parameters) {
		Validate.notNull(key, "Key may not be null.");
		return MessageFormat.format(MessageUtil.BUNDLE.getString(key), parameters);
	}
}
