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
package org.lunarray.model.generation.swing.components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.lunarray.model.generation.swing.render.factories.table.GeneratedColumn;

/**
 * The data table model.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <E>
 *            The enity type.
 */
public final class DataTableModel<E>
		implements TableModel, Serializable {

	/** The serial id. */
	private static final long serialVersionUID = -193903937377182047L;
	/** The columns. */
	private List<GeneratedColumn<E, ?>> columns;
	/** The entities. */
	private List<E> entities;
	/** The listeners. */
	private List<TableModelListener> listeners;

	/**
	 * Default constructor.
	 * 
	 * @param entities
	 *            The entities.
	 */
	public DataTableModel(final List<E> entities) {
		this.entities = entities;
		this.listeners = new LinkedList<TableModelListener>();
		this.columns = new ArrayList<GeneratedColumn<E, ?>>();
	}

	/**
	 * Add a column.
	 * 
	 * @param column
	 *            The column to add.
	 */
	public void addColumn(final GeneratedColumn<E, ?> column) {
		this.columns.add(column);
	}

	/** {@inheritDoc} */
	@Override
	public void addTableModelListener(final TableModelListener listener) {
		this.listeners.add(listener);
	}

	/** {@inheritDoc} */
	@Override
	public Class<?> getColumnClass(final int columnIndex) {
		return this.columns.get(columnIndex).getRenderType();
	}

	/** {@inheritDoc} */
	@Override
	public int getColumnCount() {
		return this.columns.size();
	}

	/** {@inheritDoc} */
	@Override
	public String getColumnName(final int columnIndex) {
		return this.columns.get(columnIndex).getLabel();
	}

	/**
	 * Gets the value for the columns field.
	 * 
	 * @return The value for the columns field.
	 */
	public List<GeneratedColumn<E, ?>> getColumns() {
		return this.columns;
	}

	/**
	 * Gets the value for the entities field.
	 * 
	 * @return The value for the entities field.
	 */
	public List<E> getEntities() {
		return this.entities;
	}

	/**
	 * Gets the value for the listeners field.
	 * 
	 * @return The value for the listeners field.
	 */
	public List<TableModelListener> getListeners() {
		return this.listeners;
	}

	/** {@inheritDoc} */
	@Override
	public int getRowCount() {
		return this.entities.size();
	}

	/** {@inheritDoc} */
	@Override
	public Object getValueAt(final int rowIndex, final int columnIndex) {
		return this.columns.get(columnIndex).getValue(this.entities.get(rowIndex));
	}

	/** {@inheritDoc} */
	@Override
	public boolean isCellEditable(final int rowIndex, final int columnIndex) {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public void removeTableModelListener(final TableModelListener listener) {
		this.listeners.remove(listener);
	}

	/**
	 * Sets a new value for the columns field.
	 * 
	 * @param columns
	 *            The new value for the columns field.
	 */
	public void setColumns(final List<GeneratedColumn<E, ?>> columns) {
		this.columns = columns;
	}

	/**
	 * Sets a new value for the entities field.
	 * 
	 * @param entities
	 *            The new value for the entities field.
	 */
	public void setEntities(final List<E> entities) {
		this.entities = entities;
	}

	/**
	 * Sets a new value for the listeners field.
	 * 
	 * @param listeners
	 *            The new value for the listeners field.
	 */
	public void setListeners(final List<TableModelListener> listeners) {
		this.listeners = listeners;
	}

	/** {@inheritDoc} */
	@Override
	public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {
		// We don't support this.
	}

	/**
	 * Propagate entity update events.
	 */
	public void updatedEntities() {
		final TableModelEvent event = new TableModelEvent(this, 0, this.entities.size(), TableModelEvent.ALL_COLUMNS);
		for (final TableModelListener listener : this.listeners) {
			listener.tableChanged(event);
		}
	}
}
