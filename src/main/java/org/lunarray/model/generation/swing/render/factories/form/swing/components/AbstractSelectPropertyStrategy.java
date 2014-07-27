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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;

import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.descriptor.accessor.exceptions.ValueAccessException;
import org.lunarray.model.descriptor.dictionary.Dictionary;
import org.lunarray.model.descriptor.dictionary.exceptions.DictionaryException;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.entity.EntityDescriptor;
import org.lunarray.model.descriptor.model.entity.KeyedEntityDescriptor;
import org.lunarray.model.descriptor.model.property.PropertyDescriptor;
import org.lunarray.model.descriptor.presentation.PresentationEntityDescriptor;
import org.lunarray.model.generation.swing.render.RenderContext;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.Descriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract select field.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The property type.
 * @param <J>
 *            The component type.
 * @param <L>
 *            The lookup type.
 */
public abstract class AbstractSelectPropertyStrategy<P, L, J extends JComponent>
		extends AbstractFormPropertyRenderStrategy<P, J> {

	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSelectPropertyStrategy.class);

	/**
	 * Constructs the strategy.
	 * 
	 * @param descriptor
	 *            The property descriptor.
	 * @param context
	 *            The render context.
	 */
	public AbstractSelectPropertyStrategy(final Descriptor<P> descriptor, final RenderContext<?> context) {
		super(descriptor, context);
	}

	/** {@inheritDoc} */
	@Override
	protected final J createComponent() {
		return this.createSelectComponent();
	}

	/**
	 * Create the select component.
	 * 
	 * @return The component.
	 */
	protected abstract J createSelectComponent();

	/**
	 * Gets the items.
	 * 
	 * @return The items.
	 */
	protected final List<Item<L>> getItems() {
		final List<Item<L>> result = new LinkedList<Item<L>>();
		@SuppressWarnings("unchecked")
		final PropertyDescriptor<Object, L> displayProperty = (PropertyDescriptor<Object, L>) this.resolveDisplayProperty();
		final Collection<L> entities = this.lookupEntities();
		if (!CheckUtil.isNull(entities)) {
			for (final L entity : entities) {
				result.add(this.processEntity(displayProperty, entity));
			}
		}
		return result;
	}

	/**
	 * Lookup all entities for this property.
	 * 
	 * @return The entities.
	 */
	protected final Collection<L> lookupEntities() {
		final Model<?> model = this.getModel();
		final Dictionary dictionary = model.getExtension(Dictionary.class);
		Collection<L> result = null;
		if (this.getDescriptor().isRelation() && !CheckUtil.isNull(dictionary)) {
			@SuppressWarnings("unchecked")
			final EntityDescriptor<L> entityDescriptor = (EntityDescriptor<L>) model.getEntity(this.getDescriptor().getRelatedName());
			try {
				result = dictionary.lookup(entityDescriptor);
			} catch (final DictionaryException e) {
				AbstractSelectPropertyStrategy.LOGGER.warn("Could not look up in dictionary '{}'.", this.getDescriptor().getRelatedName(),
						e);
			}
		}
		return result;
	}

	/**
	 * Process an entity.
	 * 
	 * @param displayProperty
	 *            The display property.
	 * @param entity
	 *            The entity.
	 * @return The item.
	 */
	protected final Item<L> processEntity(final PropertyDescriptor<Object, L> displayProperty, final L entity) {
		final Item<L> item = new Item<L>();
		item.setEntity(entity);
		if (CheckUtil.isNull(item)) {
			AbstractSelectPropertyStrategy.LOGGER.info("Added existing null item.");
		}
		if (!CheckUtil.isNull(displayProperty)) {
			displayProperty.getName();
			Object displayValue = null;
			try {
				displayValue = displayProperty.getValue(entity);
			} catch (final ValueAccessException e) {
				AbstractSelectPropertyStrategy.LOGGER.warn("Could not access value.", e);
			}
			if (!CheckUtil.isNull(displayValue)) {
				item.setLabel(displayValue.toString());
			}
		}
		return item;
	}

	/**
	 * Process as presentation entity.
	 * 
	 * @param entityDescriptor
	 *            The entity descriptor.
	 * @return The result.
	 */
	protected final PropertyDescriptor<?, P> processPresentation(final EntityDescriptor<P> entityDescriptor) {
		PropertyDescriptor<?, P> result = null;
		@SuppressWarnings("unchecked")
		final PresentationEntityDescriptor<P> presentationDescriptor = entityDescriptor.adapt(PresentationEntityDescriptor.class);
		if (!CheckUtil.isNull(presentationDescriptor)) {
			final PropertyDescriptor<?, P> nameDescriptor = presentationDescriptor.getNameProperty();
			if (!CheckUtil.isNull(nameDescriptor)) {
				result = nameDescriptor;
			}
		}
		return result;
	}

	/**
	 * Resolves what property of the related entity should be displayed, if any.
	 * 
	 * @return The property name, or null.
	 */
	protected final PropertyDescriptor<?, P> resolveDisplayProperty() {
		final Model<?> model = this.getModel();
		PropertyDescriptor<?, P> result = null;
		if (this.getDescriptor().isRelation()) {
			@SuppressWarnings("unchecked")
			final EntityDescriptor<P> entityDescriptor = (EntityDescriptor<P>) model.getEntity(this.getDescriptor().getRelatedName());
			@SuppressWarnings("unchecked")
			final KeyedEntityDescriptor<P, ?> keyedDescriptor = entityDescriptor.adapt(KeyedEntityDescriptor.class);
			result = this.processPresentation(entityDescriptor);
			if (CheckUtil.isNull(result) && !CheckUtil.isNull(keyedDescriptor)) {
				result = keyedDescriptor.getKeyProperty();
			}
		}
		return result;
	}
}
