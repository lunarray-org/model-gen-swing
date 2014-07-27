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
package org.lunarray.model.generation.swing.render.factories.impl.property;

import javax.swing.JLabel;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lunarray.model.descriptor.builder.annotation.presentation.builder.PresQualBuilder;
import org.lunarray.model.descriptor.builder.annotation.simple.SimpleBuilder;
import org.lunarray.model.descriptor.converter.ConverterTool;
import org.lunarray.model.descriptor.converter.def.DefaultConverterTool;
import org.lunarray.model.descriptor.dictionary.enumeration.EnumDictionary;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.entity.EntityDescriptor;
import org.lunarray.model.descriptor.model.property.PropertyDescriptor;
import org.lunarray.model.descriptor.resource.simpleresource.SimpleClazzResource;
import org.lunarray.model.descriptor.validator.beanvalidation.BeanValidationValidator;
import org.lunarray.model.generation.swing.model.Sample01;
import org.lunarray.model.generation.swing.model.Sample02;
import org.lunarray.model.generation.swing.model.SampleEnum;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.Descriptor;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.MutateBuffer;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.ValueChangeListener;
import org.lunarray.model.generation.swing.render.factories.form.impl.property.PropertyDescriptorImpl;

/**
 * Tests the abstract parameter descriptor.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public class PropertyDescriptorImplTest {
	/** An entity. */
	private Sample01 entity;
	/** The presentation model. */
	private Model<Object> presentationModel;
	/** The simple model. */
	private Model<Object> simpleModel;
	/** A mock listener. */
	@SuppressWarnings("rawtypes")
	private ValueChangeListener valueChangeListener;

	/** Sets up the test. */
	@Before
	public void setup() throws Exception {
		this.valueChangeListener = EasyMock.createMock(ValueChangeListener.class);
		@SuppressWarnings("unchecked")
		final SimpleClazzResource<Object> resource = new SimpleClazzResource<Object>(Sample01.class, Sample02.class, SampleEnum.class);
		final BeanValidationValidator validator = new BeanValidationValidator();
		final EnumDictionary dictionary = new EnumDictionary(null);
		final ConverterTool converter = new DefaultConverterTool();
		this.presentationModel = PresQualBuilder.createBuilder().resources(resource).extensions(validator, dictionary, converter).build();
		this.simpleModel = SimpleBuilder.createBuilder().resources(resource).extensions(validator, dictionary, converter).build();
		this.entity = new Sample01();
		EasyMock.reset(this.valueChangeListener);
	}

	/**
	 * Test getting the label.
	 * 
	 * @see Descriptor#getLabel()
	 */
	@Test
	public void testGetLabelPresentation() {
		Assert.assertEquals("Sample01.testValue4", this.getPresentationIntegerDescriptor().getLabel());
	}

	/**
	 * Test getting the label.
	 * 
	 * @see Descriptor#getLabel()
	 */
	@Test
	public void testGetLabelSimple() {
		Assert.assertEquals("testValue4", this.getSimpleIntegerDescriptor().getLabel());
	}

	/**
	 * Test getting the name.
	 * 
	 * @see Descriptor#getRelatedName()
	 */
	@Test
	public void testGetMappedNamePresentation() {
		Assert.assertEquals("Sample02", this.getPresentationMappedDescriptor().getRelatedName());
	}

	/**
	 * Test getting the name.
	 * 
	 * @see Descriptor#getRelatedName()
	 */
	@Test
	public void testGetMappedNameSimple() {
		Assert.assertEquals("Sample02", this.getSimpleMappedDescriptor().getRelatedName());
	}

	/**
	 * Test getting the name.
	 * 
	 * @see Descriptor#getRelatedName()
	 */
	@Test
	public void testGetMappedNameUnmappedPresentation() {
		Assert.assertNull(this.getPresentationIntegerDescriptor().getRelatedName());
	}

	/**
	 * Test getting the name.
	 * 
	 * @see Descriptor#getRelatedName()
	 */
	@Test
	public void testGetMappedNameUnmappedSimple() {
		Assert.assertNull(this.getSimpleIntegerDescriptor().getRelatedName());
	}

	/**
	 * Test getting the name.
	 * 
	 * @see Descriptor#getName()
	 */
	@Test
	public void testGetNamePresentation() {
		Assert.assertEquals("testValue4", this.getPresentationIntegerDescriptor().getName());
	}

	/**
	 * Test getting the name.
	 * 
	 * @see Descriptor#getName()
	 */
	@Test
	public void testGetNameSimple() {
		Assert.assertEquals("testValue4", this.getSimpleIntegerDescriptor().getName());
	}

	/**
	 * Test setting a value.
	 * 
	 * @see MutateBuffer#setStringValue(JLabel, String)
	 */
	@Test
	public void testSetInvalidValueStringPresentation() {
		final JLabel message = new JLabel();
		this.getPresentationIntegerDescriptor().getBufferMutator().setStringValue(message, "test");
		Assert.assertEquals("Could not convert.", message.getText());
	}

	/**
	 * Test setting a value.
	 * 
	 * @see MutateBuffer#setStringValue(JLabel, String)
	 */
	@Test
	public void testSetInvalidValueStringSimple() {
		final JLabel message = new JLabel();
		this.getSimpleIntegerDescriptor().getBufferMutator().setStringValue(message, "test");
		Assert.assertEquals("Could not convert.", message.getText());
	}

	/**
	 * Test setting a value.
	 * 
	 * @see MutateBuffer#setCoerceValue(JLabel, Object)
	 */
	@Test
	public void testSetValueEntityCoerceCopyPresentation() {
		this.getPresentationIntegerDescriptor().getBufferMutator().setCoerceValue(new JLabel(), Integer.valueOf(100));
	}

	/**
	 * Test setting a value.
	 * 
	 * @see MutateBuffer#setCoerceValue(JLabel, Object)
	 */
	@Test
	public void testSetValueEntityCoerceCopySimple() {
		this.getSimpleIntegerDescriptor().getBufferMutator().setCoerceValue(new JLabel(), Integer.valueOf(100));
	}

	/**
	 * Test setting a value.
	 * 
	 * @see MutateBuffer#setCoerceValue(JLabel, Object)
	 */
	@Test
	public void testSetValueEntityCoercePresentation() {
		this.getPresentationIntegerDescriptor().getBufferMutator().setCoerceValue(new JLabel(), Long.valueOf(100L));
	}

	/**
	 * Test setting a value.
	 * 
	 * @see MutateBuffer#setCoerceValue(JLabel, Object)
	 */
	@Test
	public void testSetValueEntityCoerceSimple() {
		this.getSimpleIntegerDescriptor().getBufferMutator().setCoerceValue(new JLabel(), Long.valueOf(100L));
	}

	/**
	 * Test setting a value.
	 * 
	 * @see MutateBuffer#setValue(JLabel, Object)
	 */
	@Test
	public void testSetValueEntityNullPresentation() {
		this.getPresentationIntegerDescriptor().getBufferMutator().setValue(new JLabel(), null);
	}

	/**
	 * Test setting a value.
	 * 
	 * @see MutateBuffer#setValue(JLabel, Object)
	 */
	@Test
	public void testSetValueEntityNullSimple() {
		this.getSimpleIntegerDescriptor().getBufferMutator().setValue(new JLabel(), null);
	}

	/**
	 * Test setting a value.
	 * 
	 * @see MutateBuffer#setValue(JLabel, Object)
	 */
	@Test
	public void testSetValueEntityPresentation() {
		this.getPresentationIntegerDescriptor().getBufferMutator().setValue(new JLabel(), Integer.valueOf(100));
	}

	/**
	 * Test setting a value.
	 * 
	 * @see MutateBuffer#setValue(JLabel, Object)
	 */
	@Test
	public void testSetValueEntitySimple() {
		this.getSimpleIntegerDescriptor().getBufferMutator().setValue(new JLabel(), Integer.valueOf(100));
	}

	/**
	 * Test setting a value.
	 * 
	 * @see MutateBuffer#setStringValue(JLabel, String)
	 */
	@Test
	public void testSetValueStringCopyPresentation() {
		this.getPresentationStringDescriptor().getBufferMutator().setStringValue(new JLabel(), "100");
	}

	/**
	 * Test setting a value.
	 * 
	 * @see MutateBuffer#setStringValue(JLabel, String)
	 */
	@Test
	public void testSetValueStringCopySimple() {
		this.getSimpleStringDescriptor().getBufferMutator().setStringValue(new JLabel(), "100");
	}

	/**
	 * Test setting a value.
	 * 
	 * @see MutateBuffer#setStringValue(JLabel, String)
	 */
	@Test
	public void testSetValueStringGetCoercePresentation() {
		final Descriptor<Integer> descriptor = this.getPresentationIntegerDescriptor();
		descriptor.getBufferMutator().setStringValue(new JLabel(), "100");
		Assert.assertEquals(Long.valueOf(100), descriptor.getBufferAccessor().getCoerceValue(new JLabel(), Long.class));
	}

	/**
	 * Test setting a value.
	 * 
	 * @see MutateBuffer#setStringValue(JLabel, String)
	 */
	@Test
	public void testSetValueStringGetCoerceSimple() {
		final Descriptor<Integer> descriptor = this.getSimpleIntegerDescriptor();
		descriptor.getBufferMutator().setStringValue(new JLabel(), "100");
		Assert.assertEquals(Long.valueOf(100), descriptor.getBufferAccessor().getCoerceValue(new JLabel(), Long.class));
	}

	/**
	 * Test setting a value.
	 * 
	 * @see MutateBuffer#setStringValue(JLabel, String)
	 */
	@Test
	public void testSetValueStringGetStringPresentation() {
		final Descriptor<Integer> descriptor = this.getPresentationIntegerDescriptor();
		descriptor.getBufferMutator().setStringValue(new JLabel(), "100");
		Assert.assertEquals("100", descriptor.getBufferAccessor().getStringValue(new JLabel()));
	}

	/**
	 * Test setting a value.
	 * 
	 * @see MutateBuffer#setStringValue(JLabel, String)
	 */
	@Test
	public void testSetValueStringGetStringSimple() {
		final Descriptor<Integer> descriptor = this.getSimpleIntegerDescriptor();
		descriptor.getBufferMutator().setStringValue(new JLabel(), "100");
		Assert.assertEquals("100", descriptor.getBufferAccessor().getStringValue(new JLabel()));
	}

	/**
	 * Test setting a value.
	 * 
	 * @see MutateBuffer#setStringValue(JLabel, String)
	 */
	@Test
	public void testSetValueStringGetValuePresentation() {
		final Descriptor<Integer> descriptor = this.getPresentationIntegerDescriptor();
		descriptor.getBufferMutator().setStringValue(new JLabel(), "100");
		Assert.assertEquals(Integer.valueOf(100), descriptor.getBufferAccessor().getValue(new JLabel()));
	}

	/**
	 * Test setting a value.
	 * 
	 * @see MutateBuffer#setStringValue(JLabel, String)
	 */
	@Test
	public void testSetValueStringGetValueSimple() {
		final Descriptor<Integer> descriptor = this.getSimpleIntegerDescriptor();
		descriptor.getBufferMutator().setStringValue(new JLabel(), "100");
		Assert.assertEquals(Integer.valueOf(100), descriptor.getBufferAccessor().getValue(new JLabel()));
	}

	/**
	 * Test setting a value, firing listener.
	 * 
	 * @see MutateBuffer#setStringValue(JLabel, String)
	 * @see Descriptor#addListener(ValueChangeListener)
	 * @see Descriptor#apply()
	 * @see ValueChangeListener
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testSetValueStringListenerApplyPresentation() {
		final Descriptor<Integer> descriptor = this.getPresentationIntegerDescriptor();
		descriptor.addListener(this.valueChangeListener);
		this.valueChangeListener.valueChanged(EasyMock.notNull());
		EasyMock.expectLastCall();
		EasyMock.replay(this.valueChangeListener);
		descriptor.getBufferMutator().setStringValue(new JLabel(), "100");
		descriptor.apply();
		EasyMock.verify(this.valueChangeListener);
	}

	/**
	 * Test setting a value, firing listener.
	 * 
	 * @see MutateBuffer#setStringValue(JLabel, String)
	 * @see Descriptor#addListener(ValueChangeListener)
	 * @see Descriptor#apply()
	 * @see ValueChangeListener
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testSetValueStringListenerApplySimple() {
		final Descriptor<Integer> descriptor = this.getSimpleIntegerDescriptor();
		descriptor.addListener(this.valueChangeListener);
		this.valueChangeListener.valueChanged(EasyMock.notNull());
		EasyMock.expectLastCall();
		EasyMock.replay(this.valueChangeListener);
		descriptor.getBufferMutator().setStringValue(new JLabel(), "100");
		descriptor.apply();
		EasyMock.verify(this.valueChangeListener);
	}

	/**
	 * Test setting a value, firing listener.
	 * 
	 * @see MutateBuffer#setStringValue(JLabel, String)
	 * @see Descriptor#addListener(ValueChangeListener)
	 * @see ValueChangeListener
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testSetValueStringListenerNoApplyPresentation() {
		final Descriptor<Integer> descriptor = this.getPresentationIntegerDescriptor();
		descriptor.addListener(this.valueChangeListener);
		EasyMock.replay(this.valueChangeListener);
		descriptor.getBufferMutator().setStringValue(new JLabel(), "100");
		EasyMock.verify(this.valueChangeListener);
	}

	/**
	 * Test setting a value, firing listener.
	 * 
	 * @see MutateBuffer#setStringValue(JLabel, String)
	 * @see Descriptor#addListener(ValueChangeListener)
	 * @see ValueChangeListener
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testSetValueStringListenerNoApplySimple() {
		final Descriptor<Integer> descriptor = this.getSimpleIntegerDescriptor();
		descriptor.addListener(this.valueChangeListener);
		EasyMock.replay(this.valueChangeListener);
		descriptor.getBufferMutator().setStringValue(new JLabel(), "100");
		EasyMock.verify(this.valueChangeListener);
	}

	/**
	 * Test setting a value, firing listener.
	 * 
	 * @see MutateBuffer#setStringValue(JLabel, String)
	 * @see Descriptor#addListener(ValueChangeListener)
	 * @see ValueChangeListener
	 * @see Descriptor#revert()
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testSetValueStringListenerRevertPresentation() {
		final Descriptor<Integer> descriptor = this.getPresentationIntegerDescriptor();
		descriptor.addListener(this.valueChangeListener);
		this.valueChangeListener.valueChanged(Integer.valueOf(5));
		EasyMock.expectLastCall();
		EasyMock.replay(this.valueChangeListener);
		descriptor.getBufferMutator().setStringValue(new JLabel(), "100");
		descriptor.revert();
		EasyMock.verify(this.valueChangeListener);
	}

	/**
	 * Test setting a value, firing listener.
	 * 
	 * @see MutateBuffer#setStringValue(JLabel, String)
	 * @see Descriptor#addListener(ValueChangeListener)
	 * @see ValueChangeListener
	 * @see Descriptor#revert()
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testSetValueStringListenerRevertSimple() {
		final Descriptor<Integer> descriptor = this.getSimpleIntegerDescriptor();
		descriptor.addListener(this.valueChangeListener);
		this.valueChangeListener.valueChanged(Integer.valueOf(5));
		EasyMock.expectLastCall();
		EasyMock.replay(this.valueChangeListener);
		descriptor.getBufferMutator().setStringValue(new JLabel(), "100");
		descriptor.revert();
		EasyMock.verify(this.valueChangeListener);
	}

	/** Get a presentation descriptor. */
	protected Descriptor<Integer> getPresentationIntegerDescriptor() {
		final EntityDescriptor<Sample01> entity = this.presentationModel.getEntity(Sample01.class);
		final PropertyDescriptor<Integer, Sample01> property = entity.getProperty("testValue4", Integer.class);
		final PropertyDescriptorImpl<Integer, Sample01> descriptor = new PropertyDescriptorImpl<Integer, Sample01>(property,
				this.simpleModel);
		descriptor.setEntity(this.entity);
		return descriptor;
	}

	/** Get a presentation descriptor. */
	protected Descriptor<Sample02> getPresentationMappedDescriptor() {
		final EntityDescriptor<Sample01> entity = this.presentationModel.getEntity(Sample01.class);
		final PropertyDescriptor<Sample02, Sample01> property = entity.getProperty("inlineValue2", Sample02.class);
		final PropertyDescriptorImpl<Sample02, Sample01> descriptor = new PropertyDescriptorImpl<Sample02, Sample01>(property,
				this.simpleModel);
		descriptor.setEntity(this.entity);
		return descriptor;
	}

	/** Get a presentation descriptor. */
	protected Descriptor<String> getPresentationStringDescriptor() {
		final EntityDescriptor<Sample01> entity = this.presentationModel.getEntity(Sample01.class);
		final PropertyDescriptor<String, Sample01> property = entity.getProperty("testValue", String.class);
		final PropertyDescriptorImpl<String, Sample01> descriptor = new PropertyDescriptorImpl<String, Sample01>(property, this.simpleModel);
		descriptor.setEntity(this.entity);
		return descriptor;
	}

	/** Get a simple descriptor. */
	protected Descriptor<Integer> getSimpleIntegerDescriptor() {
		final EntityDescriptor<Sample01> entity = this.simpleModel.getEntity(Sample01.class);
		final PropertyDescriptor<Integer, Sample01> property = entity.getProperty("testValue4", Integer.class);
		final PropertyDescriptorImpl<Integer, Sample01> descriptor = new PropertyDescriptorImpl<Integer, Sample01>(property,
				this.simpleModel);
		descriptor.setEntity(this.entity);
		return descriptor;
	}

	/** Get a simple descriptor. */
	protected Descriptor<Sample02> getSimpleMappedDescriptor() {
		final EntityDescriptor<Sample01> entity = this.simpleModel.getEntity(Sample01.class);
		final PropertyDescriptor<Sample02, Sample01> property = entity.getProperty("inlineValue2", Sample02.class);
		final PropertyDescriptorImpl<Sample02, Sample01> descriptor = new PropertyDescriptorImpl<Sample02, Sample01>(property,
				this.simpleModel);
		descriptor.setEntity(this.entity);
		return descriptor;
	}

	/** Get a simple descriptor. */
	protected Descriptor<String> getSimpleStringDescriptor() {
		final EntityDescriptor<Sample01> entity = this.simpleModel.getEntity(Sample01.class);
		final PropertyDescriptor<String, Sample01> property = entity.getProperty("testValue", String.class);
		final PropertyDescriptorImpl<String, Sample01> descriptor = new PropertyDescriptorImpl<String, Sample01>(property, this.simpleModel);
		descriptor.setEntity(this.entity);
		return descriptor;
	}
}
