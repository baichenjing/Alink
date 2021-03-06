package com.alibaba.alink.operator.stream.dataproc;

import org.apache.flink.ml.api.misc.param.Params;

import com.alibaba.alink.operator.batch.BatchOperator;
import com.alibaba.alink.operator.common.dataproc.IndexToStringModelMapper;
import com.alibaba.alink.operator.stream.utils.ModelMapStreamOp;
import com.alibaba.alink.params.dataproc.IndexToStringPredictParams;

/**
 * Map index to string.
 */
public final class IndexToStringPredictStreamOp
	extends ModelMapStreamOp <IndexToStringPredictStreamOp>
	implements IndexToStringPredictParams <IndexToStringPredictStreamOp> {

	private static final long serialVersionUID = -1554788528740494195L;

	public IndexToStringPredictStreamOp(BatchOperator model) {
		this(model, new Params());
	}

	public IndexToStringPredictStreamOp(BatchOperator model, Params params) {
		super(model, IndexToStringModelMapper::new, params);
	}
}
