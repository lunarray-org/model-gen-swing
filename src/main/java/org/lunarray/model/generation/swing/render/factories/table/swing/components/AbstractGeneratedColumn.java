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
package org.lunarray.model.generation.swing.render.factories.table.swing.components;

import org.apache.commons.lang.Validate;
import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.entity.EntityDescriptor;
import org.lunarray.model.descriptor.model.entity.KeyedEntityDescriptor;
import org.lunarray.model.descriptor.model.property.PropertyDescriptor;
import org.lunarray.model.descriptor.model.relation.RelationDescriptor;
import org.lunarray.model.descriptor.presentation.PresentationEntityDescriptor;
import org.lunarray.model.generation.swing.render.factories.table.GeneratedColumn;

/**
 * A column generator.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The property type.
 * @param <E>
 *            The entity type.
 * @param <R>
 *            The render type.
 */
public abstract class AbstractGeneratedColumn<P, E, R>
		implements GeneratedColumn<E, R> {
	/** The property strategy. */
	private OutputPropertyStrategy<P, E> outputPropertyStrategy;

	/**
	 * Default constructor.
	 * 
	 * @param outputPropertyStrategy
	 *            The output strategy. May not be null.
	 */
	public AbstractGeneratedColumn(final OutputPropertyStrategy<P, E> outputPropertyStrategy) {
		Validate.notNull(outputPropertyStrategy, "Output strategy may not be null.");
		this.outputPropertyStrategy = outputPropertyStrategy;
	}

	/** {@inheritDoc} */
	@Override
	public final String getLabel() {
		return this.outputPropertyStrategy.getPropertyLabel();
	}

	/**
	 * Gets the value for the outputPropertyStrategy field.
	 * 
	 * @return The value for the outputPropertyStrategy field.
	 */
	public final OutputPropertyStrategy<P, E> getOutputPropertyStrategy() {
		return this.outputPropertyStrategy;
	}

	/**
	 * Sets a new value for the outputPropertyStrategy field.
	 * 
	 * @param outputPropertyStrategy
	 *            The new value for the outputPropertyStrategy field.
	 */
	public final void setOutputPropertyStrategy(final OutputPropertyStrategy<P, E> outputPropertyStrategy) {
		this.outputPropertyStrategy = outputPropertyStrategy;
	}

	/**
	 * Resolves what property of the relation entity should be displayed, if
	 * any.
	 * 
	 * @return The property name, or null.
	 */
	protected final PropertyDescriptor<?, P> resolveDisplayProperty() {
		final RelationDescriptor relationDescrptor = this.outputPropertyStrategy.getProperty().adapt(RelationDescriptor.class);
		PropertyDescriptor<?, P> result = null;
		if (!CheckUtil.isNull(relationDescrptor)) {
			result = this.resolveRelationDescriptor(this.outputPropertyStrategy.getModel(), relationDescrptor);
		}
		return result;
	}

	/**
	 * Resolve for a relation descriptor.
	 * 
	 * @param model
	 *            The model. May not be null.
	 * @param relationDescrptor
	 *            The descriptor. May not be null.
	 * @return The display property of a relation descriptor.
	 */
	protected final PropertyDescriptor<?, P> resolveRelationDescriptor(final Model<?> model, final RelationDescriptor relationDescrptor) {
		Validate.notNull(relationDescrptor, "Relation descriptor may not be null.");
		Validate.notNull(model, "Model may not be null.");
		PropertyDescriptor<?, P> result = null;
		@SuppressWarnings("unchecked")
		final EntityDescriptor<P> entityDescriptor = (EntityDescriptor<P>) model.getEntity(relationDescrptor.getRelatedName());
		Validate.notNull(entityDescriptor,
				String.format("Relation name '%s' does not refer to a known entity.", relationDescrptor.getRelatedName()));
		@SuppressWarnings("unchecked")
		final KeyedEntityDescriptor<P, ?> keyedDescriptor = entityDescriptor.adapt(KeyedEntityDescriptor.class);
		@SuppressWarnings("unchecked")
		final PresentationEntityDescriptor<P> presentationDescriptor = entityDescriptor.adapt(PresentationEntityDescriptor.class);
		if (!CheckUtil.isNull(presentationDescriptor)) {
			final PropertyDescriptor<?, P> nameDescriptor = presentationDescriptor.getNameProperty();
			if (!CheckUtil.isNull(nameDescriptor)) {
				result = nameDescriptor;
			}
		}
		if (CheckUtil.isNull(result) && !CheckUtil.isNull(keyedDescriptor)) {
			result = keyedDescriptor.getKeyProperty();
		}
		return result;
	}
}
