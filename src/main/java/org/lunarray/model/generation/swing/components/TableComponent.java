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

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.lunarray.model.generation.swing.render.factories.table.GeneratedColumn;

/**
 * The table component.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <E>
 *            The entity type.
 */
public interface TableComponent<E> {

	/**
	 * Add a column.
	 * 
	 * @param column
	 *            The column to add.
	 * @param <R>
	 *            The render type.
	 */
	<R> void addColumn(GeneratedColumn<E, R> column);

	/**
	 * Gets the component.
	 * 
	 * @return The component.
	 */
	JComponent getComponent();

	/**
	 * Gets the value for the table field.
	 * 
	 * @return The value for the table field.
	 */
	JPanel getTable();

	/**
	 * Gets the value for the tableModel field.
	 * 
	 * @return The value for the tableModel field.
	 */
	DataTableModel<E> getTableModel();
}
