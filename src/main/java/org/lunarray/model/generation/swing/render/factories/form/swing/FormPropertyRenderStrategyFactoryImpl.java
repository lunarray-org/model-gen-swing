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
package org.lunarray.model.generation.swing.render.factories.form.swing;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.lunarray.common.event.Bus;
import org.lunarray.model.descriptor.model.operation.OperationDescriptor;
import org.lunarray.model.descriptor.model.operation.parameters.CollectionParameterDescriptor;
import org.lunarray.model.descriptor.model.operation.parameters.ParameterDescriptor;
import org.lunarray.model.descriptor.model.operation.result.CollectionResultDescriptor;
import org.lunarray.model.descriptor.model.operation.result.ResultDescriptor;
import org.lunarray.model.descriptor.model.property.CollectionPropertyDescriptor;
import org.lunarray.model.descriptor.model.property.PropertyDescriptor;
import org.lunarray.model.descriptor.presentation.RenderType;
import org.lunarray.model.descriptor.util.OperationInvocationBuilder;
import org.lunarray.model.generation.swing.components.FormComponent;
import org.lunarray.model.generation.swing.render.RenderContext;
import org.lunarray.model.generation.swing.render.factories.form.FormPropertyRenderStrategy;
import org.lunarray.model.generation.swing.render.factories.form.impl.parameter.CollectionParameterDescriptorImpl;
import org.lunarray.model.generation.swing.render.factories.form.impl.parameter.ParameterDescriptorImpl;
import org.lunarray.model.generation.swing.render.factories.form.impl.property.CollectionPropertyDescriptorImpl;
import org.lunarray.model.generation.swing.render.factories.form.impl.property.PropertyDescriptorImpl;
import org.lunarray.model.generation.swing.render.factories.form.impl.result.ResultValueDescriptorImpl;
import org.lunarray.model.generation.swing.render.factories.form.swing.components.CheckboxPropertyStrategy;
import org.lunarray.model.generation.swing.render.factories.form.swing.components.MenuSelectPropertyStrategy;
import org.lunarray.model.generation.swing.render.factories.form.swing.components.MultipleListSelectPropertyStrategy;
import org.lunarray.model.generation.swing.render.factories.form.swing.components.OperationOutputStrategy;
import org.lunarray.model.generation.swing.render.factories.form.swing.components.RadioSelectPropertyStrategy;
import org.lunarray.model.generation.swing.render.factories.form.swing.components.SingleListSelectPropertyStrategy;
import org.lunarray.model.generation.swing.render.factories.form.swing.components.TextAreaPropertyStrategy;
import org.lunarray.model.generation.swing.render.factories.form.swing.components.TextFieldPropertyStrategy;
import org.lunarray.model.generation.swing.render.factories.form.swing.components.TextOutputPropertyStrategy;
import org.lunarray.model.generation.util.RenderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The form render strategy factory.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <E>
 *            The entity type.
 */
public final class FormPropertyRenderStrategyFactoryImpl<E>
		implements RenderFactory<RenderContext<E>, E> {

	/** Validation message. */
	private static final String CONTEXT_NULL = "Context may not be null.";
	/** Validation message. */
	private static final String DESCRIPTOR_NULL = "Descriptor may not be null.";
	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(FormPropertyRenderStrategyFactoryImpl.class);
	/** The builders. */
	private Map<OperationDescriptor<E>, OperationInvocationBuilder<E>> builders;
	/** The factories. */
	private EnumMap<RenderType, FormPropertyRenderStrategy.CollectionDescriptorFactory> collectionFactories;
	/** The factories. */
	private FormPropertyRenderStrategy.CollectionDescriptorFactory defaultCollectionFactories;
	/** The factories. */
	private FormPropertyRenderStrategy.DescriptorFactory defaultDescriptorFactories;
	/** The factories. */
	private EnumMap<RenderType, FormPropertyRenderStrategy.DescriptorFactory> descriptorFactories;
	/** The event bus. */
	private Bus eventBus;
	/** The form. */
	private FormComponent<E> form;

	/**
	 * The default constructor.
	 * 
	 * @param form
	 *            The form
	 */
	public FormPropertyRenderStrategyFactoryImpl(final FormComponent<E> form) {
		this.form = form;
		this.descriptorFactories = new EnumMap<RenderType, FormPropertyRenderStrategy.DescriptorFactory>(RenderType.class);
		this.descriptorFactories.put(RenderType.CHECKBOX, new CheckboxPropertyStrategy.StrategyFactory());
		this.descriptorFactories.put(RenderType.DATE_PICKER, new TextFieldPropertyStrategy.StrategyFactory());
		this.descriptorFactories.put(RenderType.DATE_TIME_PICKER, new TextFieldPropertyStrategy.StrategyFactory());
		this.descriptorFactories.put(RenderType.DROPDOWN, new SingleListSelectPropertyStrategy.StrategyFactory());
		this.descriptorFactories.put(RenderType.PICKLIST, new MenuSelectPropertyStrategy.StrategyFactory());
		this.descriptorFactories.put(RenderType.RADIO, new RadioSelectPropertyStrategy.StrategyFactory());
		this.descriptorFactories.put(RenderType.RICH_TEXT, new TextAreaPropertyStrategy.StrategyFactory());
		this.descriptorFactories.put(RenderType.SHUTTLE, new SingleListSelectPropertyStrategy.StrategyFactory());
		this.descriptorFactories.put(RenderType.TEXT, new TextAreaPropertyStrategy.StrategyFactory());

		this.descriptorFactories.put(RenderType.TEXT_AREA, new TextFieldPropertyStrategy.StrategyFactory());
		this.descriptorFactories.put(RenderType.TIME_PICKER, new TextOutputPropertyStrategy.StrategyFactory());
		this.collectionFactories = new EnumMap<RenderType, FormPropertyRenderStrategy.CollectionDescriptorFactory>(RenderType.class);
		this.defaultCollectionFactories = new MultipleListSelectPropertyStrategy.StrategyFactory();
		this.defaultDescriptorFactories = new TextOutputPropertyStrategy.StrategyFactory();
		this.builders = new HashMap<OperationDescriptor<E>, OperationInvocationBuilder<E>>();
		this.eventBus = new Bus();
	}

	/** {@inheritDoc} */
	@Override
	public void beginOperation(final RenderContext<E> context, final OperationDescriptor<E> descriptor) {
		FormPropertyRenderStrategyFactoryImpl.LOGGER.debug("Rendering begin operation {}", descriptor);
		Validate.notNull(context, FormPropertyRenderStrategyFactoryImpl.CONTEXT_NULL);
		Validate.notNull(descriptor, FormPropertyRenderStrategyFactoryImpl.DESCRIPTOR_NULL);
		final OperationInvocationBuilder<E> builder = new OperationInvocationBuilder<E>(descriptor);
		builder.target(this.form.getEntity());
		this.builders.put(descriptor, builder);
		this.form.processBeginStrategy(new OperationOutputStrategy<E>(descriptor, this.builders.get(descriptor), this.eventBus));
	}

	/** {@inheritDoc} */
	@Override
	public void endOperation(final RenderContext<E> context, final OperationDescriptor<E> descriptor) {
		FormPropertyRenderStrategyFactoryImpl.LOGGER.debug("Rendering end operation {}", descriptor);
		Validate.notNull(context, FormPropertyRenderStrategyFactoryImpl.CONTEXT_NULL);
		Validate.notNull(descriptor, FormPropertyRenderStrategyFactoryImpl.DESCRIPTOR_NULL);
		this.form.processEndStrategy(new OperationOutputStrategy<E>(descriptor, this.builders.get(descriptor), this.eventBus));
	}

	/**
	 * Gets the value for the builders field.
	 * 
	 * @return The value for the builders field.
	 */
	public Map<OperationDescriptor<E>, OperationInvocationBuilder<E>> getBuilders() {
		return this.builders;
	}

	/**
	 * Gets the value for the collectionFactories field.
	 * 
	 * @return The value for the collectionFactories field.
	 */
	public EnumMap<RenderType, FormPropertyRenderStrategy.CollectionDescriptorFactory> getCollectionFactories() {
		return this.collectionFactories;
	}

	/**
	 * Gets the value for the defaultCollectionFactories field.
	 * 
	 * @return The value for the defaultCollectionFactories field.
	 */
	public FormPropertyRenderStrategy.CollectionDescriptorFactory getDefaultCollectionFactories() {
		return this.defaultCollectionFactories;
	}

	/**
	 * Gets the value for the defaultDescriptorFactories field.
	 * 
	 * @return The value for the defaultDescriptorFactories field.
	 */
	public FormPropertyRenderStrategy.DescriptorFactory getDefaultDescriptorFactories() {
		return this.defaultDescriptorFactories;
	}

	/**
	 * Gets the value for the descriptorFactories field.
	 * 
	 * @return The value for the descriptorFactories field.
	 */
	public EnumMap<RenderType, FormPropertyRenderStrategy.DescriptorFactory> getDescriptorFactories() {
		return this.descriptorFactories;
	}

	/**
	 * Gets the value for the eventBus field.
	 * 
	 * @return The value for the eventBus field.
	 */
	public Bus getEventBus() {
		return this.eventBus;
	}

	/**
	 * Gets the value for the form field.
	 * 
	 * @return The value for the form field.
	 */
	public FormComponent<E> getForm() {
		return this.form;
	}

	/** {@inheritDoc} */
	@Override
	public <D, P extends Collection<D>> void renderCollectionParameter(final RenderContext<E> context,
			final OperationDescriptor<E> operation, final CollectionParameterDescriptor<D, P> descriptor, final RenderType renderType) {
		FormPropertyRenderStrategyFactoryImpl.LOGGER.debug("Rendering for render type {} collection parameter {}", renderType, descriptor);
		Validate.notNull(context, FormPropertyRenderStrategyFactoryImpl.CONTEXT_NULL);
		Validate.notNull(descriptor, FormPropertyRenderStrategyFactoryImpl.DESCRIPTOR_NULL);
		final CollectionParameterDescriptorImpl<D, P, E> collDescriptor = new CollectionParameterDescriptorImpl<D, P, E>(descriptor,
				this.builders.get(operation), context.getModel());
		FormPropertyRenderStrategy<P> strategy;
		if (this.collectionFactories.containsKey(renderType)) {
			strategy = this.collectionFactories.get(renderType).createStrategy(context, collDescriptor);
		} else {
			strategy = this.defaultCollectionFactories.createStrategy(context, collDescriptor);
		}
		this.form.processStrategy(strategy);
	}

	/** {@inheritDoc} */
	@Override
	public <D, P extends Collection<D>> void renderCollectionProperty(final RenderContext<E> context,
			final CollectionPropertyDescriptor<D, P, E> descriptor, final RenderType renderType) {
		FormPropertyRenderStrategyFactoryImpl.LOGGER.debug("Rendering for render type {} collection property {}", renderType, descriptor);
		Validate.notNull(context, FormPropertyRenderStrategyFactoryImpl.CONTEXT_NULL);
		Validate.notNull(descriptor, FormPropertyRenderStrategyFactoryImpl.DESCRIPTOR_NULL);
		final CollectionPropertyDescriptorImpl<D, P, E> collDescriptor = new CollectionPropertyDescriptorImpl<D, P, E>(descriptor,
				context.getModel());
		collDescriptor.setEntity(this.form.getEntity());
		FormPropertyRenderStrategy<P> strategy;
		if (this.collectionFactories.containsKey(renderType)) {
			strategy = this.collectionFactories.get(renderType).createStrategy(context, collDescriptor);
		} else {
			strategy = this.defaultCollectionFactories.createStrategy(context, collDescriptor);
		}
		this.form.processStrategy(strategy);
	}

	/** {@inheritDoc} */
	@Override
	public <D, R extends Collection<D>> void renderCollectionResultType(final RenderContext<E> context,
			final OperationDescriptor<E> operation, final CollectionResultDescriptor<D, R> resultDescriptor, final RenderType renderType) {
		FormPropertyRenderStrategyFactoryImpl.LOGGER.debug("Rendering for render type {} collection result {}", renderType,
				resultDescriptor);
		Validate.notNull(context, FormPropertyRenderStrategyFactoryImpl.CONTEXT_NULL);
		Validate.notNull(resultDescriptor, FormPropertyRenderStrategyFactoryImpl.DESCRIPTOR_NULL);
		final ResultValueDescriptorImpl<R> descriptor = new ResultValueDescriptorImpl<R>(resultDescriptor, operation, context.getModel());
		this.eventBus.addListener(descriptor, this.builders.get(descriptor));
		FormPropertyRenderStrategy<R> strategy;
		if (this.descriptorFactories.containsKey(renderType)) {
			strategy = this.descriptorFactories.get(renderType).createStrategy(context, descriptor);
		} else {
			strategy = this.defaultDescriptorFactories.createStrategy(context, descriptor);
		}
		this.form.processStrategy(strategy);
	}

	/** {@inheritDoc} */
	@Override
	public <P> void renderParameter(final RenderContext<E> context, final ParameterDescriptor<P> descriptor,
			final OperationDescriptor<E> operation, final RenderType renderType) {
		FormPropertyRenderStrategyFactoryImpl.LOGGER.debug("Rendering for render type {} parameter {}", renderType, descriptor);
		Validate.notNull(context, FormPropertyRenderStrategyFactoryImpl.CONTEXT_NULL);
		Validate.notNull(descriptor, FormPropertyRenderStrategyFactoryImpl.DESCRIPTOR_NULL);
		final ParameterDescriptorImpl<P, E> collDescriptor = new ParameterDescriptorImpl<P, E>(descriptor, this.builders.get(operation),
				context.getModel());
		FormPropertyRenderStrategy<P> strategy;
		if (this.descriptorFactories.containsKey(renderType)) {
			strategy = this.descriptorFactories.get(renderType).createStrategy(context, collDescriptor);
		} else {
			strategy = this.defaultDescriptorFactories.createStrategy(context, collDescriptor);
		}
		this.form.processStrategy(strategy);
	}

	/** {@inheritDoc} */
	@Override
	public <P> void renderProperty(final RenderContext<E> context, final PropertyDescriptor<P, E> propertyDescriptor,
			final RenderType renderType) {
		FormPropertyRenderStrategyFactoryImpl.LOGGER.debug("Rendering for render type {} property {}", renderType, propertyDescriptor);
		Validate.notNull(context, FormPropertyRenderStrategyFactoryImpl.CONTEXT_NULL);
		Validate.notNull(propertyDescriptor, FormPropertyRenderStrategyFactoryImpl.DESCRIPTOR_NULL);
		final PropertyDescriptorImpl<P, E> descriptor = new PropertyDescriptorImpl<P, E>(propertyDescriptor, context.getModel());
		descriptor.setEntity(this.form.getEntity());
		FormPropertyRenderStrategy<P> strategy;
		if (this.descriptorFactories.containsKey(renderType)) {
			strategy = this.descriptorFactories.get(renderType).createStrategy(context, descriptor);
		} else {
			strategy = this.defaultDescriptorFactories.createStrategy(context, descriptor);
		}
		this.form.processStrategy(strategy);
	}

	/** {@inheritDoc} */
	@Override
	public <R> void renderResultType(final RenderContext<E> context, final OperationDescriptor<E> operation,
			final ResultDescriptor<R> resultDescriptor, final RenderType renderType) {
		FormPropertyRenderStrategyFactoryImpl.LOGGER.debug("Rendering for render type {} result {}", renderType, resultDescriptor);
		Validate.notNull(context, FormPropertyRenderStrategyFactoryImpl.CONTEXT_NULL);
		Validate.notNull(resultDescriptor, FormPropertyRenderStrategyFactoryImpl.DESCRIPTOR_NULL);
		final ResultValueDescriptorImpl<R> descriptor = new ResultValueDescriptorImpl<R>(resultDescriptor, operation, context.getModel());
		this.eventBus.addListener(descriptor, this.builders.get(operation));
		FormPropertyRenderStrategy<R> strategy;
		if (this.descriptorFactories.containsKey(renderType)) {
			strategy = this.descriptorFactories.get(renderType).createStrategy(context, descriptor);
		} else {
			strategy = this.defaultDescriptorFactories.createStrategy(context, descriptor);
		}
		this.form.processStrategy(strategy);
	}

	/**
	 * Sets a new value for the builders field.
	 * 
	 * @param builders
	 *            The new value for the builders field.
	 */
	public void setBuilders(final Map<OperationDescriptor<E>, OperationInvocationBuilder<E>> builders) {
		this.builders = builders;
	}

	/**
	 * Sets a new value for the collectionFactories field.
	 * 
	 * @param collectionFactories
	 *            The new value for the collectionFactories field.
	 */
	public void setCollectionFactories(final EnumMap<RenderType, FormPropertyRenderStrategy.CollectionDescriptorFactory> collectionFactories) {
		this.collectionFactories = collectionFactories;
	}

	/**
	 * Sets a new value for the defaultCollectionFactories field.
	 * 
	 * @param defaultCollectionFactories
	 *            The new value for the defaultCollectionFactories field.
	 */
	public void setDefaultCollectionFactories(final FormPropertyRenderStrategy.CollectionDescriptorFactory defaultCollectionFactories) {
		this.defaultCollectionFactories = defaultCollectionFactories;
	}

	/**
	 * Sets a new value for the defaultDescriptorFactories field.
	 * 
	 * @param defaultDescriptorFactories
	 *            The new value for the defaultDescriptorFactories field.
	 */
	public void setDefaultDescriptorFactories(final FormPropertyRenderStrategy.DescriptorFactory defaultDescriptorFactories) {
		this.defaultDescriptorFactories = defaultDescriptorFactories;
	}

	/**
	 * Sets a new value for the descriptorFactories field.
	 * 
	 * @param descriptorFactories
	 *            The new value for the descriptorFactories field.
	 */
	public void setDescriptorFactories(final EnumMap<RenderType, FormPropertyRenderStrategy.DescriptorFactory> descriptorFactories) {
		this.descriptorFactories = descriptorFactories;
	}

	/**
	 * Sets a new value for the eventBus field.
	 * 
	 * @param eventBus
	 *            The new value for the eventBus field.
	 */
	public void setEventBus(final Bus eventBus) {
		this.eventBus = eventBus;
	}

	/**
	 * Sets a new value for the form field.
	 * 
	 * @param form
	 *            The new value for the form field.
	 */
	public void setForm(final FormComponent<E> form) {
		this.form = form;
	}
}
