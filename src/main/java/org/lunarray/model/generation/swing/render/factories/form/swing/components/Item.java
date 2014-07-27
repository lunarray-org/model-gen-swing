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
package org.lunarray.model.generation.swing.render.factories.form.swing.components;

/**
 * Describes an item for a list.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The property type.
 */
public final class Item<P> {

	/** The entity. */
	private P entity;

	/** The label. */
	private String label;

	/**
	 * Default constructor.
	 */
	public Item() {
		// Default constructor.
	}

	/**
	 * Gets the value for the entity field.
	 * 
	 * @return The value for the entity field.
	 */
	public P getEntity() {
		return this.entity;
	}

	/**
	 * Gets the value for the label field.
	 * 
	 * @return The value for the label field.
	 */
	public String getLabel() {
		return this.label;
	}

	/**
	 * Sets a new value for the entity field.
	 * 
	 * @param entity
	 *            The new value for the entity field.
	 */
	public void setEntity(final P entity) {
		this.entity = entity;
	}

	/**
	 * Sets a new value for the label field.
	 * 
	 * @param label
	 *            The new value for the label field.
	 */
	public void setLabel(final String label) {
		this.label = label;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return this.getLabel();
	}
}
