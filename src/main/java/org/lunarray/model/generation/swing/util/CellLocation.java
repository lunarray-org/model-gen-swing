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
package org.lunarray.model.generation.swing.util;

import java.io.Serializable;

import org.apache.commons.lang.Validate;
import org.lunarray.common.check.CheckUtil;

/**
 * A cell location.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class CellLocation
		implements Serializable {

	/** Serial id. */
	private static final long serialVersionUID = 7142909581323891300L;
	/** The column, must be positive. */
	private int column;
	/** The row, must be positive. */
	private int row;

	/**
	 * Default constructor.
	 */
	public CellLocation() {
		// Default constructor.
	}

	/**
	 * Default constructor.
	 * 
	 * @param builder
	 *            The builder.
	 */
	protected CellLocation(final CellLocationBuilder builder) {
		this.row = builder.getRow();
		this.column = builder.getColumn();
		Validate.isTrue(CheckUtil.checkPositive(this.row), "Row must be positive.");
		Validate.isTrue(CheckUtil.checkPositive(this.column), "Column must be positive.");
	}

	/**
	 * Gets a builder.
	 * 
	 * @return The builder.
	 */
	public static CellLocationBuilder createBuilder() {
		return new CellLocationBuilder();
	}

	/**
	 * Gets the value for the column field.
	 * 
	 * @return The value for the column field.
	 */
	public int getColumn() {
		return this.column;
	}

	/**
	 * Gets the value for the row field.
	 * 
	 * @return The value for the row field.
	 */
	public int getRow() {
		return this.row;
	}

	/**
	 * Sets a new value for the column field.
	 * 
	 * @param column
	 *            The new value for the column field.
	 */
	public void setColumn(final int column) {
		this.column = column;
	}

	/**
	 * Sets a new value for the row field.
	 * 
	 * @param row
	 *            The new value for the row field.
	 */
	public void setRow(final int row) {
		this.row = row;
	}

	/**
	 * A builder.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	public static final class CellLocationBuilder {
		/** The column. */
		private transient int columnBuilder;
		/** The row. */
		private transient int rowBuilder;

		/**
		 * Default constructor.
		 */
		protected CellLocationBuilder() {
			// Default constructor.
		}

		/**
		 * Build the cell location.
		 * 
		 * @return The cell location.
		 */
		public CellLocation build() {
			return new CellLocation(this);
		}

		/**
		 * Sets a new value for the column field.
		 * 
		 * @param column
		 *            The new value for the column field.
		 * @return The builder.
		 */
		public CellLocationBuilder column(final int column) {
			this.columnBuilder = column;
			return this;
		}

		/**
		 * Sets a new value for the row field.
		 * 
		 * @param row
		 *            The new value for the row field.
		 * @return The builder.
		 */
		public CellLocationBuilder row(final int row) {
			this.rowBuilder = row;
			return this;
		}

		/**
		 * Gets the value for the column field.
		 * 
		 * @return The value for the column field.
		 */
		protected int getColumn() {
			return this.columnBuilder;
		}

		/**
		 * Gets the value for the row field.
		 * 
		 * @return The value for the row field.
		 */
		protected int getRow() {
			return this.rowBuilder;
		}
	}
}
