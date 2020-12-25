/**
  * Example to demonstrate combining tables in Hive and Amazon Redshift for data enrichment.
  */

import org.apache.spark.sql._
import com.amazonaws.auth._
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.AWSSessionCredentials
import com.amazonaws.auth.InstanceProfileCredentialsProvider
import com.amazonaws.services.redshift.AmazonRedshiftClient
import _root_.com.amazon.redshift.jdbc41.Driver
import org.apache.spark.sql.SaveMode
import org.apache.spark.sql.SQLContext



// Instance Profile for authentication to AWS resources
val provider = new InstanceProfileCredentialsProvider();
val credentials: AWSSessionCredentials = provider.getCredentials.asInstanceOf[AWSSessionCredentials];
val token = credentials.getSessionToken;
val awsAccessKey = credentials.getAWSAccessKeyId;
val awsSecretKey = credentials.getAWSSecretKey

val sqlContext = new SQLContext(sc)
import sqlContext.implicits._;

// Read weather table from hive
val rawWeatherDF = sqlContext.table("weather")

// Retrieve the header
val header = rawWeatherDF.first()

// Remove the header from the dataframe
val noHeaderWeatherDF = rawWeatherDF.filter(row => row != header)

// UDF to convert the air temperature from celsius to fahrenheit
val toFahrenheit = udf {(c: Double) => c * 9 / 5 + 32}

// Apply the UDF to maximum and minimum air temperature
val weatherDF = noHeaderWeatherDF.withColumn("new_tmin", toFahrenheit(noHeaderWeatherDF("tmin")))
                                 .withColumn("new_tmax", toFahrenheit(noHeaderWeatherDF("tmax")))
                                 .drop("tmax")
                                 .drop("tmin")
                                 .withColumnRenamed("new_tmax","tmax")
                                 .withColumnRenamed("new_tmin","tmin")

// Provide the jdbc url for Amazon Redshift
val jdbcURL = "jdbc:redshift://<redshift-cluster-name>:<port/<database-name>?user=<dbuser>&password=<dbpassword>"

// Create and declare an S3 bucket where the temporary files are written
val s3TempDir = "s3://<S3TempBucket>/"


// Query against the ord_flights table in Amazon Redshift
val flightsQuery = """
                    select ORD_DELAY_ID, DAY_OF_MONTH, DAY_OF_WEEK, FL_DATE, f_days_from_holiday(year, month, day_of_month) as DAYS_TO_HOLIDAY, UNIQUE_CARRIER, FL_NUM, substring(DEP_TIME, 1, 2) as DEP_HOUR, cast(DEP_DEL15 as smallint),
                    cast(AIR_TIME as integer), cast(FLIGHTS as smallint), cast(DISTANCE as smallint)
                    from flights where origin='ORD' and cancelled = 0
                   """

// Create a Dataframe to hold the results of the above query
val flightsDF = sqlContext.read.format("com.databricks.spark.redshift")
                                          .option("url", jdbcURL)
                                          .option("tempdir", s3TempDir)
                                          .option("query", flightsQuery)
                                          .option("temporary_aws_access_key_id", awsAccessKey)
                                          .option("temporary_aws_secret_access_key", awsSecretKey)
                                          .option("temporary_aws_session_token", token).load()
										  

// Join the two dataframes
val joinedDF = flightsDF.join(weatherDF, flightsDF("fl_date") === weatherDF("dt"))


joinedDF.write
  .format("com.databricks.spark.redshift")
  .option("temporary_aws_access_key_id", awsAccessKey)
  .option("temporary_aws_secret_access_key", awsSecretKey)
  .option("temporary_aws_session_token", token)
  .option("url", jdbcURL)
  .option("dbtable", "ord_flights")
  .option("aws_iam_role", "arn:aws:iam::<AWS-account>:role/<redshift-role>")
  .option("tempdir", s3TempDir)
  .mode("error")
  .save()
