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
package org.lunarray.model.generation.swing.render.factories.impl.returntype;

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
import org.lunarray.model.descriptor.model.operation.OperationDescriptor;
import org.lunarray.model.descriptor.model.operation.result.ResultDescriptor;
import org.lunarray.model.descriptor.resource.simpleresource.SimpleClazzResource;
import org.lunarray.model.descriptor.validator.beanvalidation.BeanValidationValidator;
import org.lunarray.model.generation.swing.model.Sample01;
import org.lunarray.model.generation.swing.model.Sample02;
import org.lunarray.model.generation.swing.model.SampleEnum;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.Descriptor;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.MutateBuffer;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.ValueChangeListener;
import org.lunarray.model.generation.swing.render.factories.form.impl.result.ResultValueDescriptorImpl;

/**
 * Tests the abstract parameter descriptor.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public class ReturnValueDescriptorImplTest {
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
		EasyMock.reset(this.valueChangeListener);
	}

	/**
	 * Test getting the label.
	 * 
	 * @see Descriptor#getLabel()
	 */
	@Test
	public void testGetLabelPresentation() {
		Assert.assertEquals("", this.getPresentationIntegerDescriptor().getLabel());
	}

	/**
	 * Test getting the label.
	 * 
	 * @see Descriptor#getLabel()
	 */
	@Test
	public void testGetLabelSimple() {
		Assert.assertEquals("", this.getSimpleIntegerDescriptor().getLabel());
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
		Assert.assertEquals("echoMethodInt", this.getPresentationIntegerDescriptor().getName());
	}

	/**
	 * Test getting the name.
	 * 
	 * @see Descriptor#getName()
	 */
	@Test
	public void testGetNameSimple() {
		Assert.assertEquals("echoMethodInt", this.getSimpleIntegerDescriptor().getName());
	}

	/**
	 * Test setting a value.
	 * 
	 * @see MutateBuffer#setStringValue(JLabel, String)
	 */
	@Test
	public void testSetInvalidValueStringPresentation() {
		this.getPresentationIntegerDescriptor().getBufferMutator().setStringValue(new JLabel(), "test");
	}

	/**
	 * Test setting a value.
	 * 
	 * @see MutateBuffer#setStringValue(JLabel, String)
	 */
	@Test
	public void testSetInvalidValueStringSimple() {
		this.getSimpleIntegerDescriptor().getBufferMutator().setStringValue(new JLabel(), "test");
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

	/** Get a presentation descriptor. */
	protected Descriptor<Integer> getPresentationIntegerDescriptor() {
		final EntityDescriptor<Sample01> entity = this.presentationModel.getEntity(Sample01.class);
		final OperationDescriptor<Sample01> operation = entity.getOperation("echoMethodInt");
		final ResultDescriptor<Integer> resultDescriptor = operation.getResultDescriptor(Integer.class);
		return new ResultValueDescriptorImpl<Integer>(resultDescriptor, operation, this.simpleModel);
	}

	/** Get a presentation descriptor. */
	protected Descriptor<Sample02> getPresentationMappedDescriptor() {
		final EntityDescriptor<Sample01> entity = this.presentationModel.getEntity(Sample01.class);
		final OperationDescriptor<Sample01> operation = entity.getOperation("echoMethodMapped");
		final ResultDescriptor<Sample02> resultDescriptor = operation.getResultDescriptor(Sample02.class);
		return new ResultValueDescriptorImpl<Sample02>(resultDescriptor, operation, this.simpleModel);
	}

	/** Get a presentation descriptor. */
	protected Descriptor<String> getPresentationStringDescriptor() {
		final EntityDescriptor<Sample01> entity = this.presentationModel.getEntity(Sample01.class);
		final OperationDescriptor<Sample01> operation = entity.getOperation("echoMethod");
		final ResultDescriptor<String> resultDescriptor = operation.getResultDescriptor(String.class);
		return new ResultValueDescriptorImpl<String>(resultDescriptor, operation, this.simpleModel);
	}

	/** Get a simple descriptor. */
	protected Descriptor<Integer> getSimpleIntegerDescriptor() {
		final EntityDescriptor<Sample01> entity = this.simpleModel.getEntity(Sample01.class);
		final OperationDescriptor<Sample01> operation = entity.getOperation("echoMethodInt");
		final ResultDescriptor<Integer> resultDescriptor = operation.getResultDescriptor(Integer.class);
		return new ResultValueDescriptorImpl<Integer>(resultDescriptor, operation, this.simpleModel);
	}

	/** Get a simple descriptor. */
	protected Descriptor<Sample02> getSimpleMappedDescriptor() {
		final EntityDescriptor<Sample01> entity = this.simpleModel.getEntity(Sample01.class);
		final OperationDescriptor<Sample01> operation = entity.getOperation("echoMethodMapped");
		final ResultDescriptor<Sample02> resultDescriptor = operation.getResultDescriptor(Sample02.class);
		return new ResultValueDescriptorImpl<Sample02>(resultDescriptor, operation, this.simpleModel);
	}

	/** Get a simple descriptor. */
	protected Descriptor<String> getSimpleStringDescriptor() {
		final EntityDescriptor<Sample01> entity = this.simpleModel.getEntity(Sample01.class);
		final OperationDescriptor<Sample01> operation = entity.getOperation("echoMethod");
		final ResultDescriptor<String> resultDescriptor = operation.getResultDescriptor(String.class);
		return new ResultValueDescriptorImpl<String>(resultDescriptor, operation, this.simpleModel);
	}
}
