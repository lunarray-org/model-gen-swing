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

import java.util.Locale;

import org.apache.commons.lang.Validate;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.generation.swing.components.FormComponent;
import org.lunarray.model.generation.swing.components.impl.FormComponentImpl.MessagePosition;

/**
 * The builder.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <S>
 *            The model super type.
 * @param <E>
 *            The entity type.
 */
public final class FormComponentBuilder<S, E extends S> {
	/** The entity. */
	private transient E entityBuilder;
	/** The entity key. */
	private transient String entityKeyBuilder;
	/** The locale. */
	private transient Locale localeBuilder;
	/** The message position. */
	private transient MessagePosition messagePositionBuilder;
	/** The model. */
	private transient Model<S> modelBuilder;

	/**
	 * Default constructor.
	 */
	private FormComponentBuilder() {
		this.localeBuilder = Locale.getDefault();
		this.messagePositionBuilder = MessagePosition.RIGHT;
	}

	/**
	 * Creates the builder.
	 * 
	 * @return The builder.
	 * @param <S>
	 *            The super type.
	 * @param <E>
	 *            The entity.
	 */
	public static <S, E extends S> FormComponentBuilder<S, E> createBuilder() {
		return new FormComponentBuilder<S, E>();
	}

	/**
	 * Sets a new value for the messagePosition field.
	 * 
	 * @return The builder.
	 */
	public FormComponentBuilder<S, E> bottomMessages() {
		this.messagePositionBuilder = MessagePosition.BOTTOM;
		return this;
	}

	/**
	 * Builds the component.
	 * 
	 * @return The component.
	 */
	public FormComponent<E> build() {
		Validate.notNull(this.modelBuilder, "Model may not be null.");
		Validate.notNull(this.entityKeyBuilder, "Entity key may not be null.");
		Validate.notNull(this.entityBuilder, "Entity may not be null.");
		Validate.notNull(this.messagePositionBuilder, "Message position has to be set.");
		return new FormComponentImpl<S, E>(this.modelBuilder, this.entityKeyBuilder, this.entityBuilder, this.localeBuilder,
				this.messagePositionBuilder);
	}

	/**
	 * Sets a new value for the entity field.
	 * 
	 * @param entity
	 *            The new value for the entity field.
	 * @return The builder.
	 */
	public FormComponentBuilder<S, E> entity(final E entity) {
		this.entityBuilder = entity;
		return this;
	}

	/**
	 * Sets a new value for the entityKey field.
	 * 
	 * @param entityKey
	 *            The new value for the entityKey field.
	 * @return The builder.
	 */
	public FormComponentBuilder<S, E> entityKey(final String entityKey) {
		this.entityKeyBuilder = entityKey;
		return this;
	}

	/**
	 * Sets a new value for the messagePosition field.
	 * 
	 * @return The builder.
	 */
	public FormComponentBuilder<S, E> leftMessages() {
		this.messagePositionBuilder = MessagePosition.LEFT;
		return this;
	}

	/**
	 * Sets a new value for the locale field.
	 * 
	 * @param locale
	 *            The new value for the locale field.
	 * @return The builder.
	 */
	public FormComponentBuilder<S, E> locale(final Locale locale) {
		this.localeBuilder = locale;
		return this;
	}

	/**
	 * Sets a new value for the messagePosition field.
	 * 
	 * @param messagePosition
	 *            The new value for the messagePosition field.
	 * @return The builder.
	 */
	public FormComponentBuilder<S, E> messagePosition(final MessagePosition messagePosition) {
		this.messagePositionBuilder = messagePosition;
		return this;
	}

	/**
	 * Sets a new value for the model field.
	 * 
	 * @param model
	 *            The new value for the model field.
	 * @return The builder.
	 */
	public FormComponentBuilder<S, E> model(final Model<S> model) {
		this.modelBuilder = model;
		return this;
	}

	/**
	 * Sets a new value for the messagePosition field.
	 * 
	 * @return The builder.
	 */
	public FormComponentBuilder<S, E> rightMessages() {
		this.messagePositionBuilder = MessagePosition.RIGHT;
		return this;
	}

	/**
	 * Sets a new value for the messagePosition field.
	 * 
	 * @return The builder.
	 */
	public FormComponentBuilder<S, E> topMessages() {
		this.messagePositionBuilder = MessagePosition.TOP;
		return this;
	}
}
