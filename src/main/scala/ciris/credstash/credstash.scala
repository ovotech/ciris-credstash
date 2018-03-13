package ciris

import java.util

import ciris.api._
import com.amazonaws.auth.{AWSCredentialsProvider, DefaultAWSCredentialsProviderChain}
import com.amazonaws.regions.{AwsRegionProvider, DefaultAwsRegionProviderChain}
import com.jessecoyle.{CredStashBouncyCastleCrypto, JCredStash}

import scala.util.{Failure, Success, Try}


package object credstash {

  private[credstash] class ConstantAwsRegionProvider(region: String) extends AwsRegionProvider {
    override def getRegion: String = region
  }

  private val emptyContext = new util.HashMap[String, String]()

  val CredstashKeyType: ConfigKeyType[String] =
    ConfigKeyType("credstash key")

  def credstashSource(awsCredentialProvider: AWSCredentialsProvider = new DefaultAWSCredentialsProviderChain,
                      awsRegionProvider: AwsRegionProvider = new DefaultAwsRegionProviderChain): ConfigSource[Id, String, String] =
    ConfigSource(CredstashKeyType) { key =>

      val credstashClient = new JCredStash("credential-store", awsCredentialProvider, awsRegionProvider, new CredStashBouncyCastleCrypto())

      Try(credstashClient.getSecret(key, emptyContext)) match {
        case Success(value) => Right(value)
        case Failure(e) => Left(ConfigError(e.getMessage))
      }
    }

  def credstash[Value](region: String)(key: String)(
      implicit decoder: ConfigDecoder[String, Value]
  ): ConfigEntry[Id, String, String, Value] = {
    credstashSource(awsRegionProvider = new ConstantAwsRegionProvider(region))
      .read(key)
      .decodeValue[Value]
  }

  def credstashF[F[_]: Sync, Value](region: String)(key: String)(
      implicit decoder: ConfigDecoder[String, Value]
  ): ConfigEntry[F, String, String, Value] = {
    credstashSource(awsRegionProvider = new ConstantAwsRegionProvider(region))
      .suspendF[F]
      .read(key)
      .decodeValue[Value]
  }

  def credstash[Value](awsCredentialProvider: AWSCredentialsProvider = new DefaultAWSCredentialsProviderChain,
                       awsRegionProvider: AwsRegionProvider = new DefaultAwsRegionProviderChain)(key: String)(
    implicit decoder: ConfigDecoder[String, Value]
  ): ConfigEntry[Id, String, String, Value] = {
    credstashSource(awsCredentialProvider, awsRegionProvider)
      .read(key)
      .decodeValue[Value]
  }

  def credstashF[F[_]: Sync, Value](awsCredentialProvider: AWSCredentialsProvider = new DefaultAWSCredentialsProviderChain,
                                    awsRegionProvider: AwsRegionProvider = new DefaultAwsRegionProviderChain)(key: String)(
    implicit decoder: ConfigDecoder[String, Value]
  ): ConfigEntry[F, String, String, Value] = {
    credstashSource(awsCredentialProvider, awsRegionProvider)
      .suspendF[F]
      .read(key)
      .decodeValue[Value]
  }

}
