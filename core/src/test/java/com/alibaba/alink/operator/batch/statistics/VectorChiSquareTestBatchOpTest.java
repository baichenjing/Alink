package com.alibaba.alink.operator.batch.statistics;

import org.apache.flink.types.Row;

import com.alibaba.alink.operator.batch.source.MemSourceBatchOp;
import com.alibaba.alink.operator.common.statistics.ChiSquareTestResult;
import com.alibaba.alink.operator.common.statistics.ChiSquareTestResults;
import com.alibaba.alink.testutil.AlinkTestBase;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.function.Consumer;

public class VectorChiSquareTestBatchOpTest extends AlinkTestBase {

	@Test
	public void test() {

		Row[] testArray = new Row[] {
			Row.of(7, "0.0  0.0  18.0  1.0", 1.0),
			Row.of(8, "0.0  1.0  12.0  0.0", 0.0),
			Row.of(9, "1.0  0.0  15.0  0.1", 0.0),
		};

		String[] colNames = new String[] {"id", "features", "clicked"};

		MemSourceBatchOp source = new MemSourceBatchOp(Arrays.asList(testArray), colNames);

		VectorChiSquareTestBatchOp test = new VectorChiSquareTestBatchOp()
			.setSelectedCol("features")
			.setLabelCol("clicked");

		test.linkFrom(source);

		test.lazyCollectChiSquareTest(new Consumer <ChiSquareTestResults>() {
			@Override
			public void accept(ChiSquareTestResults summary) {
				Assert.assertEquals(summary.results[0].getP(), 0.3864762307712323, 10e-4);
			}
		});

		test.lazyPrintChiSquareTest();

		ChiSquareTestResult[] result = test.collectChiSquareTest().results;

		Assert.assertEquals(result[0].getP(), 0.3864762307712323, 10e-4);
		Assert.assertEquals(result[0].getDf(), 1.0, 10e-4);
		Assert.assertEquals(result[1].getP(), 0.3864762307712323, 10e-4);
		Assert.assertEquals(result[2].getP(), 0.22313016014843035, 10e-4);
		Assert.assertEquals(result[3].getP(), 0.22313016014843035, 10e-4);

	}
}