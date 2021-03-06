## Description
Transform a document to a sparse vector based on the statistics provided by DocCountVectorizerTrainBatchOp.
 It supports several types: IDF/TF/TF-IDF/One-Hot/WordCount.
 It processes streaming data.

## Parameters
| Name | Description | Type | Required？ | Default Value |
| --- | --- | --- | --- | --- |
| numThreads | Thread number of operator. | Integer |  | 1 |
| selectedCol | Name of the selected column used for processing | String | ✓ |  |
| outputCol | Name of the output column | String |  | null |
| reservedCols | Names of the columns to be retained in the output table | String[] |  | null |

## Script Example
#### Code
```
import numpy as np
import pandas as pd
data = np.array([
    [0, u'二手旧书:医学电磁成像'],
    [1, u'二手美国文学选读（ 下册 ）李宜燮南开大学出版社 9787310003969'],
    [2, u'二手正版图解象棋入门/谢恩思主编/华龄出版社'],
    [3, u'二手中国糖尿病文献索引'],
    [4, u'二手郁达夫文集（ 国内版 ）全十二册馆藏书']])
df = pd.DataFrame({"id": data[:, 0], "text": data[:, 1]})
inOp1 = BatchOperator.fromDataframe(df, schemaStr='id int, text string')
inOp2 = StreamOperator.fromDataframe(df, schemaStr='id int, text string')

segment = SegmentBatchOp().setSelectedCol("text").linkFrom(inOp1)
train = DocCountVectorizerTrainBatchOp().setSelectedCol("text").linkFrom(segment)
predictBatch = DocCountVectorizerPredictBatchOp().setSelectedCol("text").linkFrom(train, segment)
[model,predict] = collectToDataframes(train, predictBatch)
print(model)
print(predict)

segment = SegmentStreamOp().setSelectedCol("text").linkFrom(inOp2)
predictStream = DocCountVectorizerPredictStreamOp(train).setSelectedCol("text").linkFrom(segment)
predictStream.print(refreshInterval=-1)
StreamOperator.execute()
```

#### Results
##### Model
```
rowID  model_id                                         model_info
0          0     {"minTF":"1.0","featureType":"\"WORD_COUNT\""}
1    1048576                        {"f0":"二手","f1":0.0,"f2":0}
2    2097152          {"f0":"/","f1":1.0986122886681098,"f2":1}
3    3145728        {"f0":"出版社","f1":0.6931471805599453,"f2":2}
4    4194304          {"f0":"（","f1":0.6931471805599453,"f2":3}
5    5242880          {"f0":"）","f1":0.6931471805599453,"f2":4}
6    6291456  {"f0":"9787310003969","f1":1.0986122886681098,...
7    7340032          {"f0":":","f1":1.0986122886681098,"f2":6}
8    8388608         {"f0":"下册","f1":1.0986122886681098,"f2":7}
9    9437184         {"f0":"中国","f1":1.0986122886681098,"f2":8}
10  10485760         {"f0":"主编","f1":1.0986122886681098,"f2":9}
11  11534336         {"f0":"书","f1":1.0986122886681098,"f2":10}
12  12582912        {"f0":"入门","f1":1.0986122886681098,"f2":11}
13  13631488         {"f0":"全","f1":1.0986122886681098,"f2":12}
14  14680064        {"f0":"医学","f1":1.0986122886681098,"f2":13}
15  15728640       {"f0":"十二册","f1":1.0986122886681098,"f2":14}
16  16777216        {"f0":"华龄","f1":1.0986122886681098,"f2":15}
17  17825792      {"f0":"南开大学","f1":1.0986122886681098,"f2":16}
18  18874368        {"f0":"国内","f1":1.0986122886681098,"f2":17}
19  19922944        {"f0":"图解","f1":1.0986122886681098,"f2":18}
20  20971520         {"f0":"思","f1":1.0986122886681098,"f2":19}
21  22020096        {"f0":"成像","f1":1.0986122886681098,"f2":20}
22  23068672        {"f0":"文学","f1":1.0986122886681098,"f2":21}
23  24117248        {"f0":"文献","f1":1.0986122886681098,"f2":22}
24  25165824        {"f0":"文集","f1":1.0986122886681098,"f2":23}
25  26214400        {"f0":"旧书","f1":1.0986122886681098,"f2":24}
26  27262976       {"f0":"李宜燮","f1":1.0986122886681098,"f2":25}
27  28311552        {"f0":"正版","f1":1.0986122886681098,"f2":26}
28  29360128         {"f0":"版","f1":1.0986122886681098,"f2":27}
29  30408704        {"f0":"电磁","f1":1.0986122886681098,"f2":28}
30  31457280       {"f0":"糖尿病","f1":1.0986122886681098,"f2":29}
31  32505856        {"f0":"索引","f1":1.0986122886681098,"f2":30}
32  33554432        {"f0":"美国","f1":1.0986122886681098,"f2":31}
33  34603008        {"f0":"谢恩","f1":1.0986122886681098,"f2":32}
34  35651584        {"f0":"象棋","f1":1.0986122886681098,"f2":33}
35  36700160        {"f0":"选读","f1":1.0986122886681098,"f2":34}
36  37748736       {"f0":"郁达夫","f1":1.0986122886681098,"f2":35}
37  38797312        {"f0":"馆藏","f1":1.0986122886681098,"f2":36}
```


##### Output Data
```
rowID   id                                               text
0   0        $37$0:1.0 6:1.0 13:1.0 20:1.0 24:1.0 28:1.0
1   1  $37$0:1.0 2:1.0 3:1.0 4:1.0 5:1.0 7:1.0 16:1.0...
2   2  $37$0:1.0 1:2.0 2:1.0 9:1.0 11:1.0 15:1.0 18:1...
3   3               $37$0:1.0 8:1.0 22:1.0 29:1.0 30:1.0
4   4  $37$0:1.0 3:1.0 4:1.0 10:1.0 12:1.0 14:1.0 17:...
```

