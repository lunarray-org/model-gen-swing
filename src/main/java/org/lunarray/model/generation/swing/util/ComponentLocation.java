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

import org.apache.commons.lang.Validate;
import org.lunarray.model.generation.swing.util.CellLocation.CellLocationBuilder;

/**
 * A component location.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class ComponentLocation {

	/** The component. */
	private CellLocation component;
	/** The message. */
	private CellLocation message;

	/**
	 * Default constructor.
	 */
	public ComponentLocation() {
		// Default constructor.
	}

	/**
	 * Default constructor.
	 * 
	 * @param builder
	 *            The builder.
	 */
	protected ComponentLocation(final ComponentLocationBuilder builder) {
		this.message = builder.createMessage();
		this.component = builder.createComponent();
		Validate.notNull(this.message, "Message location may not be null.");
		Validate.notNull(this.component, "Component location may not be null.");
	}

	/**
	 * Creates the builder.
	 * 
	 * @return The builder.
	 */
	public static ComponentLocationBuilder createBuilder() {
		return new ComponentLocationBuilder();
	}

	/**
	 * Gets the value for the component field.
	 * 
	 * @return The value for the component field.
	 */
	public CellLocation getComponent() {
		return this.component;
	}

	/**
	 * Gets the value for the message field.
	 * 
	 * @return The value for the message field.
	 */
	public CellLocation getMessage() {
		return this.message;
	}

	/**
	 * Sets a new value for the component field.
	 * 
	 * @param component
	 *            The new value for the component field.
	 */
	public void setComponent(final CellLocation component) {
		this.component = component;
	}

	/**
	 * Sets a new value for the message field.
	 * 
	 * @param message
	 *            The new value for the message field.
	 */
	public void setMessage(final CellLocation message) {
		this.message = message;
	}

	/**
	 * The builder.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	public static final class ComponentLocationBuilder {

		/** The component. */
		private final transient CellLocationBuilder componentBuilder;
		/** The message. */
		private final transient CellLocationBuilder messageBuilder;

		/**
		 * Default constructor.
		 */
		protected ComponentLocationBuilder() {
			this.componentBuilder = CellLocation.createBuilder();
			this.messageBuilder = CellLocation.createBuilder();
		}

		/**
		 * Build.
		 * 
		 * @return The component location.
		 */
		public ComponentLocation build() {
			return new ComponentLocation(this);
		}

		/**
		 * Gets the value for the component field.
		 * 
		 * @return The value for the component field.
		 */
		public CellLocationBuilder component() {
			return this.componentBuilder;
		}

		/**
		 * Gets the value for the message field.
		 * 
		 * @return The value for the message field.
		 */
		public CellLocationBuilder message() {
			return this.messageBuilder;
		}

		/**
		 * Gets the value for the component field.
		 * 
		 * @return The value for the component field.
		 */
		protected CellLocation createComponent() {
			return this.componentBuilder.build();
		}

		/**
		 * Gets the value for the message field.
		 * 
		 * @return The value for the message field.
		 */
		protected CellLocation createMessage() {
			return this.messageBuilder.build();
		}
	}
}
