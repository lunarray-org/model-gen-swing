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
package org.lunarray.model.generation.swing;

import java.util.Collection;

import org.lunarray.model.descriptor.dictionary.composite.EntityDictionary;
import org.lunarray.model.generation.swing.model.Sample02;

public class Sample02Dictionary
		implements EntityDictionary<Sample02, String> {

	@Override
	public String getEntityName() {
		return "Sample02";
	}

	@Override
	public Collection<Sample02> lookup() {
		return Sample02.SAMPLES;
	}

	@Override
	public Sample02 lookup(final String identifier) {
		for (final Sample02 s : Sample02.SAMPLES) {
			if (s.getId().equals(identifier)) {
				return s;
			}
		}
		return null;
	}
}
