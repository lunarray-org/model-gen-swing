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
package org.lunarray.model.generation.swing.render.factories.table.swing;

import java.util.Collection;
import java.util.EnumMap;

import org.apache.commons.lang.Validate;
import org.lunarray.model.descriptor.model.operation.OperationDescriptor;
import org.lunarray.model.descriptor.model.operation.parameters.CollectionParameterDescriptor;
import org.lunarray.model.descriptor.model.operation.parameters.ParameterDescriptor;
import org.lunarray.model.descriptor.model.operation.result.CollectionResultDescriptor;
import org.lunarray.model.descriptor.model.operation.result.ResultDescriptor;
import org.lunarray.model.descriptor.model.property.CollectionPropertyDescriptor;
import org.lunarray.model.descriptor.model.property.PropertyDescriptor;
import org.lunarray.model.descriptor.presentation.RenderType;
import org.lunarray.model.generation.swing.components.TableComponent;
import org.lunarray.model.generation.swing.render.RenderContext;
import org.lunarray.model.generation.swing.render.factories.table.TablePropertyRenderStrategy;
import org.lunarray.model.generation.swing.render.factories.table.swing.components.CheckboxOutputPropertyStrategy;
import org.lunarray.model.generation.swing.render.factories.table.swing.components.TextOutputPropertyStrategy;
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
public final class TablePropertyRenderStrategyFactoryImpl<E>
		implements RenderFactory<RenderContext<E>, E> {

	/** Validation message. */
	private static final String CONTEXT_NULL = "Context may not be null.";
	/** Validation message. */
	private static final String DESCRIPTOR_NULL = "Descriptor may not be null.";
	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(TablePropertyRenderStrategyFactoryImpl.class);
	/** The default factory. */
	private TablePropertyRenderStrategy.Factory defaultFactory;
	/** The factories. */
	private EnumMap<RenderType, TablePropertyRenderStrategy.Factory> factoryMap;
	/** The table. */
	private TableComponent<E> table;

	/**
	 * The default constructor.
	 * 
	 * @param table
	 *            The table. May not be null.
	 */
	public TablePropertyRenderStrategyFactoryImpl(final TableComponent<E> table) {
		Validate.notNull(table, "Table may not be null.");
		this.table = table;
		this.defaultFactory = new TextOutputPropertyStrategy.StrategyFactory();
		this.factoryMap = new EnumMap<RenderType, TablePropertyRenderStrategy.Factory>(RenderType.class);
		this.factoryMap.put(RenderType.CHECKBOX, new CheckboxOutputPropertyStrategy.StrategyFactory());
	}

	/** {@inheritDoc} */
	@Override
	public void beginOperation(final RenderContext<E> context, final OperationDescriptor<E> descriptor) {
		// Ignored
	}

	/** {@inheritDoc} */
	@Override
	public void endOperation(final RenderContext<E> context, final OperationDescriptor<E> operation) {
		// Ignored.
	}

	/**
	 * Gets the value for the defaultFactory field.
	 * 
	 * @return The value for the defaultFactory field.
	 */
	public TablePropertyRenderStrategy.Factory getDefaultFactory() {
		return this.defaultFactory;
	}

	/**
	 * Gets the value for the factoryMap field.
	 * 
	 * @return The value for the factoryMap field.
	 */
	public EnumMap<RenderType, TablePropertyRenderStrategy.Factory> getFactoryMap() {
		return this.factoryMap;
	}

	/**
	 * Gets the value for the table field.
	 * 
	 * @return The value for the table field.
	 */
	public TableComponent<E> getTable() {
		return this.table;
	}

	/** {@inheritDoc} */
	@Override
	public <D, P extends Collection<D>> void renderCollectionParameter(final RenderContext<E> context,
			final OperationDescriptor<E> operation, final CollectionParameterDescriptor<D, P> descriptor, final RenderType renderType) {
		// Ignored.
	}

	/** {@inheritDoc} */
	@Override
	public <D, P extends Collection<D>> void renderCollectionProperty(final RenderContext<E> context,
			final CollectionPropertyDescriptor<D, P, E> descriptor, final RenderType renderType) {
		TablePropertyRenderStrategyFactoryImpl.LOGGER.debug("Rendering for render type {} collection property: {}", renderType, descriptor);
		Validate.notNull(context, TablePropertyRenderStrategyFactoryImpl.CONTEXT_NULL);
		Validate.notNull(descriptor, TablePropertyRenderStrategyFactoryImpl.DESCRIPTOR_NULL);
		TablePropertyRenderStrategy<P, E> strategy;
		if (this.factoryMap.containsKey(renderType)) {
			strategy = this.factoryMap.get(renderType).createStrategy(context, descriptor);
		} else {
			strategy = this.defaultFactory.createStrategy(context, descriptor);
		}
		this.table.addColumn(strategy.getGenerator());
	}

	/** {@inheritDoc} */
	@Override
	public <D, R extends Collection<D>> void renderCollectionResultType(final RenderContext<E> context,
			final OperationDescriptor<E> operation, final CollectionResultDescriptor<D, R> resultDescriptor, final RenderType renderType) {
		// Ignored.
	}

	/** {@inheritDoc} */
	@Override
	public <P> void renderParameter(final RenderContext<E> context, final ParameterDescriptor<P> descriptor,
			final OperationDescriptor<E> operation, final RenderType renderType) {
		// Ignored.
	}

	/** {@inheritDoc} */
	@Override
	public <P> void renderProperty(final RenderContext<E> context, final PropertyDescriptor<P, E> descriptor, final RenderType renderType) {
		TablePropertyRenderStrategyFactoryImpl.LOGGER.debug("Rendering for render type {} property: {}", renderType, descriptor);
		Validate.notNull(context, TablePropertyRenderStrategyFactoryImpl.CONTEXT_NULL);
		Validate.notNull(descriptor, TablePropertyRenderStrategyFactoryImpl.DESCRIPTOR_NULL);
		TablePropertyRenderStrategy<P, E> strategy;
		if (this.factoryMap.containsKey(renderType)) {
			strategy = this.factoryMap.get(renderType).createStrategy(context, descriptor);
		} else {
			strategy = this.defaultFactory.createStrategy(context, descriptor);
		}
		this.table.addColumn(strategy.getGenerator());
	}

	/** {@inheritDoc} */
	@Override
	public <R> void renderResultType(final RenderContext<E> context, final OperationDescriptor<E> operation,
			final ResultDescriptor<R> resultDescriptor, final RenderType renderType) {
		// Ignored.
	}

	/**
	 * Sets a new value for the defaultFactory field.
	 * 
	 * @param defaultFactory
	 *            The new value for the defaultFactory field.
	 */
	public void setDefaultFactory(final TablePropertyRenderStrategy.Factory defaultFactory) {
		this.defaultFactory = defaultFactory;
	}

	/**
	 * Sets a new value for the factoryMap field.
	 * 
	 * @param factoryMap
	 *            The new value for the factoryMap field.
	 */
	public void setFactoryMap(final EnumMap<RenderType, TablePropertyRenderStrategy.Factory> factoryMap) {
		this.factoryMap = factoryMap;
	}

	/**
	 * Sets a new value for the table field.
	 * 
	 * @param table
	 *            The new value for the table field.
	 */
	public void setTable(final TableComponent<E> table) {
		this.table = table;
	}
}
