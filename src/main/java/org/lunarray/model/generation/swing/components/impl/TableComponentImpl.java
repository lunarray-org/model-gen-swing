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

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.generation.swing.components.DataTableModel;
import org.lunarray.model.generation.swing.components.TableComponent;
import org.lunarray.model.generation.swing.render.RenderContext;
import org.lunarray.model.generation.swing.render.factories.table.GeneratedColumn;
import org.lunarray.model.generation.swing.render.factories.table.swing.TablePropertyRenderStrategyFactoryImpl;
import org.lunarray.model.generation.util.Composer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Vaadin table.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <S>
 *            The super type.
 * @param <E>
 *            The entity type.
 */
public final class TableComponentImpl<S, E extends S>
		extends AbstractComponent<S, E>
		implements TableComponent<E> {

	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(TableComponent.class);
	/** Serial id. */
	private static final long serialVersionUID = 7168781367616477749L;
	/** The entities. */
	private List<E> entities;
	/** The table. */
	private JPanel table;
	/** The model. */
	private DataTableModel<E> tableModel;

	/**
	 * Constructs the form component.
	 * 
	 * @param model
	 *            The model.
	 * @param entityKey
	 *            The entity key.
	 * @param locale
	 *            The locale.
	 */
	public TableComponentImpl(final Model<S> model, final String entityKey, final Locale locale) {
		this(model, entityKey, null, locale);
	}

	/**
	 * Constructs the form component.
	 * 
	 * @param model
	 *            The model. May not be null.
	 * @param entityKey
	 *            The entity key. May not be null.
	 * @param entities
	 *            The entities.
	 * @param locale
	 *            The locale.
	 */
	protected TableComponentImpl(final Model<S> model, final String entityKey, final Collection<E> entities, final Locale locale) {
		super(model, entityKey, locale);
		if (CheckUtil.isNull(entities)) {
			this.entities = new ArrayList<E>();
		} else {
			this.entities = new ArrayList<E>(entities);
		}
		TableComponentImpl.LOGGER.debug("Showing entities: {}", entities);
		this.init();
	}

	/** {@inheritDoc} */
	@Override
	public <R> void addColumn(final GeneratedColumn<E, R> column) {
		this.tableModel.addColumn(column);
	}

	/** {@inheritDoc} */
	@Override
	public JComponent getComponent() {
		return this;
	}

	/**
	 * Gets the value for the entities field.
	 * 
	 * @return The value for the entities field.
	 */
	public List<E> getEntities() {
		return this.entities;
	}

	/** {@inheritDoc} */
	@Override
	public JPanel getTable() {
		return this.table;
	}

	/** {@inheritDoc} */
	@Override
	public DataTableModel<E> getTableModel() {
		return this.tableModel;
	}

	/**
	 * Sets a new value for the entities field.
	 * 
	 * @param entities
	 *            The new value for the entities field.
	 */
	public void setEntities(final List<E> entities) {
		this.entities = entities;
		TableComponentImpl.LOGGER.debug("Setting entities: {}", entities);
		this.tableModel.updatedEntities();
	}

	/**
	 * Sets a new value for the table field.
	 * 
	 * @param table
	 *            The new value for the table field.
	 */
	public void setTable(final JPanel table) {
		this.table = table;
	}

	/**
	 * Sets a new value for the tableModel field.
	 * 
	 * @param tableModel
	 *            The new value for the tableModel field.
	 */
	public void setTableModel(final DataTableModel<E> tableModel) {
		this.tableModel = tableModel;
	}

	/** Initializes the table. */
	private void init() {
		final Composer<RenderContext<E>, S, E> composer = new Composer<RenderContext<E>, S, E>();
		composer.setContext(new RenderContext<E>(this.getModel()));
		composer.setPropertyRenderStrategyFactory(new TablePropertyRenderStrategyFactoryImpl<E>(this));
		composer.setVariableResolver(new ComponentVariableResolver());
		this.table = new JPanel(new BorderLayout());
		final JTable content = new JTable();
		final JLabel label = new JLabel(composer.getLabel());
		label.setLabelFor(content);
		this.table.add(label, BorderLayout.NORTH);
		this.table.add(new JScrollPane(content), BorderLayout.CENTER);
		this.tableModel = new DataTableModel<E>(this.getEntities());
		composer.compose(false);
		content.setModel(this.tableModel);
		this.add(content);
	}
}
