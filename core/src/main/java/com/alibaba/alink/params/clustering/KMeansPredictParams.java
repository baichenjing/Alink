package com.alibaba.alink.params.clustering;

import org.apache.flink.ml.api.misc.param.WithParams;

import com.alibaba.alink.params.mapper.RichModelMapperParams;
import com.alibaba.alink.params.shared.HasNumThreads;
/**
 * Params for KMeansPredictor.
 */
public interface KMeansPredictParams<T> extends WithParams <T>,
	RichModelMapperParams <T>, HasNumThreads <T>, HasPredictionDistanceCol<T> {
}
