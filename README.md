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

This integration requires Credstash to be installed on the host machine, details of how to do this can be found in the Credstash [Readme](https://github.com/fugue/credstash). 


Example
-------
```scala
import ciris._
import ciris.credstash._

case class TestConfig(credstashSecret: String)

object Example {
  val res = loadConfig(
    credstash[String]("eu-west-1")("uat.twilio.auth_token")
  ) { (secret: String) =>
    TestConfig(secret)
  }
}
```
