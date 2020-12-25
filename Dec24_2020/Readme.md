Powering Amazon Redshift Analytics with Apache Spark and Amazon Machine Learning (Data Enginner Flow Using AWS)
---------------------------------------------------------
1. Sources
       Weather Data ---> Save into Hive
       Flight Data ---> Save into Redshift
2. Sinks ---> RedShift
3. EMR For Spark process
4. Analaytics using AWS sagemaker 


Reference Link :
----------------------------------------------------- 
1. https://aws.amazon.com/blogs/big-data/powering-amazon-redshift-analytics-with-apache-spark-and-amazon-machine-learning/

AWS EMR vs EC2 vs Spark vs Glue vs SageMaker vs Redshift
----------------------------------------------------------------

## EMR
----------------------------------------------------------------------------------------------
Amazon EMR is a managed cluster platform (using AWS EC2 instances) that simplifies running big data frameworks, such as Apache Hadoop and Apache Spark, on AWS to process and analyze vast amounts of data. By using these frameworks and related open-source projects, such as Apache Hive and Apache Pig, you can process data for analytics purposes and business intelligence workloads. Additionally, you can use Amazon EMR to transform and move large amounts of data into and out of other AWS data stores and databases, such as Amazon Simple Storage Service (Amazon S3) and Amazon DynamoDB.

EMR is when you need to process massive amounts of data and heavily rely on Spark, Hadoop, and MapReduce (EMR = Elastic MapReduce). Essentially, if your data is in large enough volume to make use of the efficiencies of Spark, Hadoop, Hive, HDFS, HBase and Pig stack then go with EMR.

EMR segregates slave nodes into two subtypes – Core Nodes and Task nodes. The core node acts as both the data node and the worker node, whereas, the task node only act as worker node. Apart from scalability, this segregation allows the users following key advantages:

No loss of HDFS data – You can remove (Scale-In) task nodes without losing HDFS data since these nodes do not act as DataNodes.
Lower Costs – Using spot instances for the task nodes cuts the costs by a factor of 10.
Additionally, AWS CloudWatch can be used to monitor and scale the cluster based on various pre-defined rules – Memory Utilization, Free Containers Remaining etc.

You can dynamically orchestrate a new cluster on-demand within a very short span of time. After successful completion of the jobs, this cluster can be terminated in turn, improving the utilization and reducing the costs drastically.

You can access data on S3 from EMR directly or through Hive Tables. EMR is highly tuned for working with data on S3 through AWS-proprietary binaries.

Pros: Cheap, Auto-Scaling Cluster, monitoring with CloudWatch, trivial to work with data in S3

Cons: Do you really need it for the project you are working on, usually requires massive data to reap its benefits, no console, EMR cluster cannot be shut down and can only be terminated as per the design.

EC2
--------------------------------------------------------------------------
Virtual compute machines (instances) you can spin up for processing.

Spark
---------------------------------------------------------------------------
Lightning fast library for big data. If Hadoop/MapReduce is the original 1 ton gorilla, then Spark is the lean, fast cheetah. Apache Spark achieves high performance for both batch and streaming data, using a state-of-the-art DAG scheduler, a query optimizer, and a physical execution engine. Write applications quickly in Java, Scala, Python, R, and SQL. Spark powers a stack of libraries including SQL and DataFrames, MLlib for machine learning, GraphX, and Spark Streaming. You can combine these libraries seamlessly in the same application.

Glue
---------------------------------------------------------------------------
AWS Glue is a fully-managed, pay-as-you-go, extract, transform, and load (ETL) service that automates the time-consuming steps of data preparation for analytics. AWS Glue automatically discovers and profiles your data via the Glue Data Catalog, recommends and generates ETL code to transform your source data into target schemas, and runs the ETL jobs on a fully managed, scale-out Apache Spark environment to load your data into its destination. It also allows you to setup, orchestrate, and monitor complex data flows.

Yes, EMR does work out to be cheaper than Glue, and this is because Glue is meant to be serverless and fully managed by AWS, so the user doesn’t have to worry about the infrastructure running behind the scenes, but EMR requires a whole lot of configuration to set up. So it’s a trade off between user friendliness and cost, and for more technical users EMR can be the better option.

Pros: Ease of use, serverless – AWS manages the server config for you, crawler can scan your data and infer schema / create Athena tables for you

Cons: Bit more expensive than EMR, less configurable, more limitations than EMR.

Event-driven ETL pipelines diagram
Example glue process with Lambda triggers and event driven pipelines.

RedShift
---------------------------------------------------------------------------
Amazon Redshift is a petabyte-scale data warehouse that is accessed via SQL. Data must be loaded into Redshift before being queried, which often requires some for of transformation (“ETL”).

So which one to choose?

If you want to use SQL and you have structured data (eg CSV files), then Redshift is the simplest solution.
If you want to process unstructured data (eg in strange formats rather than structured CSV files), Amazon EMR can provide a Hadoop system that is very capable.
Sometimes people use both — use Hadoop to transform data, then use Redshift for querying the data.
If Amazon Redshift can fit your needs, then use it rather than Hadoop. Redshift is simpler to use because it presents itself as a standard SQL database that you can get going in a few minutes. All the cluster stuff is behind-the-scenes and you don’t have to know much to use it.

If you need more flexible capabilities and you don’t mind getting low-level and technical, then Hadoop on Amazon EMR will offer you more capabilities.

SageMaker
Amazon SageMaker is a fully-managed platform that enables developers and data scientists to quickly and easily build, train, and deploy machine learning models at any scale. Amazon SageMaker removes all the barriers that typically slow down developers who want to use machine learning.

Cons: Expensive!

Sources:

https://stackoverflow.com/questions/52437599/pros-and-cons-of-amazon-sagemaker-vs-amazon-emr-for-deploying-tensorflow-based
https://stackoverflow.com/questions/37627274/what-is-the-difference-between-aws-elastic-mapreduce-and-aws-redshift
https://aws.amazon.com/glue/faqs/
Post date

