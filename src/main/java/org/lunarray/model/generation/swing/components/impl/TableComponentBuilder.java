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
package org.lunarray.model.generation.swing.components.impl;

import java.util.Collection;
import java.util.Locale;

import org.apache.commons.lang.Validate;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.generation.swing.components.TableComponent;

/**
 * The builder.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <S>
 *            The model super type.
 * @param <E>
 *            The entity type.
 */
public final class TableComponentBuilder<S, E extends S> {
	/** The entity. */
	private transient Collection<E> entitiesBuilder;
	/** The entity key. */
	private transient String entityKeyBuilder;
	/** The locale. */
	private transient Locale localeBuilder;
	/** The model. */
	private transient Model<S> modelBuilder;

	/**
	 * Default constructor.
	 */
	private TableComponentBuilder() {
		this.localeBuilder = Locale.getDefault();
	}

	/**
	 * a Creates the builder.
	 * 
	 * @return The builder.
	 * @param <S>
	 *            The model super type.
	 * @param <E>
	 *            The entity type.
	 */
	public static <S, E extends S> TableComponentBuilder<S, E> createBuilder() {
		return new TableComponentBuilder<S, E>();
	}

	/**
	 * Builds the component.
	 * 
	 * @return The component.
	 */
	public TableComponent<E> build() {
		Validate.notNull(this.modelBuilder, "Model may not be null.");
		Validate.notNull(this.entityKeyBuilder, "Entity key may not be null.");
		return new TableComponentImpl<S, E>(this.modelBuilder, this.entityKeyBuilder, this.entitiesBuilder, this.localeBuilder);
	}

	/**
	 * Sets a new value for the entities field.
	 * 
	 * @param entities
	 *            The new value for the entities field.
	 * @return The builder.
	 */
	public TableComponentBuilder<S, E> entity(final Collection<E> entities) {
		this.entitiesBuilder = entities;
		return this;
	}

	/**
	 * Sets a new value for the entityKey field.
	 * 
	 * @param entityKey
	 *            The new value for the entityKey field.
	 * @return The builder.
	 */
	public TableComponentBuilder<S, E> entityKey(final String entityKey) {
		this.entityKeyBuilder = entityKey;
		return this;
	}

	/**
	 * Sets a new value for the locale field.
	 * 
	 * @param locale
	 *            The new value for the locale field.
	 * @return The builder.
	 */
	public TableComponentBuilder<S, E> locale(final Locale locale) {
		this.localeBuilder = locale;
		return this;
	}

	/**
	 * Sets a new value for the model field.
	 * 
	 * @param model
	 *            The new value for the model field.
	 * @return The builder.
	 */
	public TableComponentBuilder<S, E> model(final Model<S> model) {
		this.modelBuilder = model;
		return this;
	}
}
