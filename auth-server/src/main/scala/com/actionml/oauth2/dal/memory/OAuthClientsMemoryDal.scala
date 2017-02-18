package com.actionml.oauth2.dal.memory

import org.joda.time.DateTime
import ru.pavlenov.oauth2.dal.{AccountsDal, OAuthClientsDal}
import ru.pavlenov.oauth2.entities.{Account, OAuthAuthorizationCode, OAuthClient}
import scaldi.Injector
import scaldi.akka.AkkaInjectable

import scala.collection.mutable
import scala.concurrent.Future
import scalaoauth2.provider.OAuthGrantType

/**
  * ⓭ + 32
  * Какой сам? by Pavlenov Semen 22.01.17.
  */
class OAuthClientsMemoryDal(implicit inj: Injector) extends OAuthClientsDal with AkkaInjectable{

  val accountsDal: AccountsDal = inject[AccountsDal]

  private val storage: mutable.HashMap[String, OAuthClient] = mutable.HashMap(
    "test_client_id" -> OAuthClient(
      id = "test_client_id",
      ownerId = "account_id",
      grantType = OAuthGrantType.CLIENT_CREDENTIALS,
      clientSecret = "test_client_secret",
      redirectUri = None,
      createdAt = DateTime.now()
    )
  )

  override def validate(
    clientId: String,
    clientSecret: String,
    grantType: String
  ): Future[Boolean] = Future.successful {
    storage.get(clientId)
      .filter(_.clientSecret == clientSecret)
      .filter(_.grantType == grantType || grantType == OAuthGrantType.REFRESH_TOKEN)
      .exists(_ => true)
  }

  override def findByClientId(clientId: String): Future[Option[OAuthClient]] = Future.successful {
    storage.get(clientId)
  }

  override def findClientCredentials(
    clientId: String,
    clientSecret: String
  ): Future[Option[Account]] = {
    storage.get(clientId)
      .filter(_.clientSecret == clientSecret)
      .map(_.ownerId)
      .map(accountsDal.findByAccountId) match {
        case Some(eventualMaybeAccount) => eventualMaybeAccount
        case _ => Future.successful(None)
      }
  }
}
