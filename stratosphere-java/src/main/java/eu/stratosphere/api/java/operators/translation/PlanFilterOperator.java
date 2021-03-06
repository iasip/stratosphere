/***********************************************************************************************************************
 *
 * Copyright (C) 2010-2013 by the Stratosphere project (http://stratosphere.eu)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 **********************************************************************************************************************/
package eu.stratosphere.api.java.operators.translation;

import eu.stratosphere.api.common.functions.GenericFlatMap;
import eu.stratosphere.api.common.operators.base.FlatMapOperatorBase;
import eu.stratosphere.api.java.functions.FilterFunction;
import eu.stratosphere.api.java.typeutils.TypeInformation;
import eu.stratosphere.util.Collector;
import eu.stratosphere.util.Reference;

/**
 *
 */
public class PlanFilterOperator<T> extends FlatMapOperatorBase<GenericFlatMap<Reference<T>, Reference<T>>>
	implements UnaryJavaPlanNode<T, T>
{
	private final TypeInformation<T> type;
	
	
	public PlanFilterOperator(FilterFunction<T> udf, String name, TypeInformation<T> type) {
		super(new ReferenceWrappingFilter<T>(udf), name);
		this.type = type;
	}
	
	@Override
	public TypeInformation<T> getReturnType() {
		return this.type;
	}

	@Override
	public TypeInformation<T> getInputType() {
		return this.type;
	}
	
	
	// --------------------------------------------------------------------------------------------
	
	public static final class ReferenceWrappingFilter<T> extends WrappingFunction<FilterFunction<T>>
		implements GenericFlatMap<Reference<T>, Reference<T>>
	{

		private static final long serialVersionUID = 1L;
		
		private ReferenceWrappingFilter(FilterFunction<T> wrapped) {
			super(wrapped);
		}

		@Override
		public final void flatMap(Reference<T> value, Collector<Reference<T>> out) throws Exception {
			if (this.wrappedFunction.filter(value.ref)) {
				out.collect(value);
			}
		}
	}
}
