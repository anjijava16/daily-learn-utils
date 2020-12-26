The solution has six main categories.
--------------------------------------------------------------
1. Data generators – Clickstream and order data is generated with the help of an AWS Lambda function. 
	The function is triggered by a scheduled Amazon CloudWatch Events event every minute and generates random clicks for ingestion into a Kinesis data stream. 
	Similarly, another function triggered by a CloudWatch event generates random orders for ingestion into a second data stream. In a production environment,
	this data comes from clickstream generators and a centralized order management system.
2. Data ingestion – Kinesis data streams ingest clickstream and order data as they are generated.
3. Data sessionization – Data sessionization helps group related data. For clickstream data, we can group clicks on an ad by different users or time periods.
	For order data, we can group orders by different ads. We use Amazon Kinesis Data Analytics for SQL to analyze streaming data in real time with standard SQL. 
	Sessionized clickstream and order data is ingested into another in-application stream.
4. Data processing and storage – The sessionization stream from Kinesis Data Analytics for SQL is ingested into an
    Amazon Kinesis Data Firehose delivery stream, which delivers the data to a pre-configured S3 bucket.
5. Data Catalog – You use AWS Glue to crawl the clickstream and orders data in their respective S3 buckets, as well as build metadata definitions and tables in Athena. 
   AWS Glue crawlers run every hour to update table definitions, and Athena views are built to compute the ad-to-order conversion.
6. Data visualization – You use QuickSight to generate visualizations.

Reference Links:
------------------------------------------------------------
1. URL : https://github.com/anjijava16/aws_kinesis_stream-utils

