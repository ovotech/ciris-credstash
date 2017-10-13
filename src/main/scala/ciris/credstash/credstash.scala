package ciris

import scala.collection.mutable.ListBuffer
import scala.sys.process._

package object credstash {

  val keyType = ConfigKeyType[String]("Credstash key")

  def credstash[Value: ConfigReader](region: String)(key: String): ConfigValue[Value] =
    ConfigValue(key)(credstashSource(region), ConfigReader[Value])

  def credstashSource(region: String): ConfigSource[String] = ConfigSource(keyType){ key =>
    val outputBuffer = ListBuffer[String]()
    val exitValue = (s"credstash get -r $region $key" run ProcessLogger(outputBuffer append _)).exitValue

    val result = outputBuffer mkString "\n" trim()
    exitValue match{
      case 0 => Right(result)
      case _ => Left(ConfigError(result))
    }
  }
}