[ ![Download](https://api.bintray.com/packages/ovotech/maven/ciris-credstash/images/download.svg) ](https://bintray.com/ovotech/maven/ciris-credstash/_latestVersion)
# ciris-credstash
Ciris Integration for loading secrets from [Credstash](https://github.com/fugue/credstash). 

Setup
-----
To use ciris-credstash, add the following artifact to your dependencies: 

```
libraryDependencies += "com.ovoenergy" %% "ciris-credstash" %% VERSION

```

Dependencies
------------

This integration depends on aws-java-sdk-core.


Example
-------
```tut:silent
import ciris._
import ciris.credstash._

case class TestConfig(credstashSecret: String)

object Example {
  val res = loadConfig(
    credstash[String]()("uat.twilio.auth_token")
  ) { (secret: String) =>
    TestConfig(secret)
  }
}
```

The AWS credentials and region can be configured by passing an AwsCredentialsProvider and/or an AwsRegionProvider.
```tut:silent
import ciris._
import ciris.credstash._
import com.amazonaws.regions.DefaultAwsRegionProviderChain
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain

case class TestConfig(credstashSecret: String)

object Example {
  val res = loadConfig(
    credstash[String](awsCredentialProvider = new DefaultAWSCredentialsProviderChain,
                      awsRegionProvider = new DefaultAwsRegionProviderChain)("uat.twilio.auth_token")
  ) { (secret: String) =>
    TestConfig(secret)
  }
}

```