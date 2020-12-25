What is AWS Redshift?
-------------------------------------------
A cloud data warehouse that extends seamlessly to the data lake (S3)
Not a standalone data warehouse, but integrates seamlessly with data lake (S3)
Fully managed, ACID, ANSI SQL compliant
Architecture: massive parallel + share nothing + columnar data store
Performance: new RA3 instances gets up to 3x the performance of any cloud data warehouse (according to the AWS documentation)
Capacity: can query data from GB to EB
Scaling: automatic scaling; compute and storage capabilities can scale independently
RA3 instance: latest instance type, up to 64TB per node, 48 vCPU per node
Redshift Architecture
Image for post
# Source: 
  https://docs.aws.amazon.com/redshift/latest/dg/c_high_level_system_architecture.html

Leader node
-------------------------------------------------------
SQL endpoint. Connects to SQL client / BI tools by JDBC/ODBC
Stores metadata
Query compilation
Query optimisation: use machine learning to generate efficient query plans — routing short queries to SQA (Short Query Acceleration) queue
Coordinate parallel SQL processing: e.g. concurrency scaling
Pricing: not charging for the leader node for any cluster with two or more nodes

Compute node
--------------------------------------------------------------
Local, columnar storage
Executes queries in parallel by slices
Load, backup, restore
Pricing: based on compute node instance type and number of nodes

