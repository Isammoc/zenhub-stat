package services

import net.caoticode.buhtig.Buhtig
import net.caoticode.buhtig.Converters._
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._ // Combinator syntax
import com.ning.http.client.Response

class Github(val token: String) {
  val buhtig = new Buhtig(token)
  val client = buhtig.asyncClient

  def searchRepositories(q: String) = (client.search.repositories ? ("q" -> q )).get[String]
  def repository(user: String, name: String) = client.repos(user, name).get[String]
  def repositoryContributors(user: String, name: String) = client.repos(user, name).contributors.get[String]
  def close = client.close
}

object Github {
  case class RepositoryInfo(val owner: User, val name: String, val description: Option[String], val contributors: List[User])
  case class Repository(val ownerLogin: String, val name: String, val description: Option[String])
  case class User(val name: String, val avatar: String)

  val userReads: Reads[User] = ( (JsPath \ "login").read[String] and (JsPath \ "avatar_url").read[String])(User.apply _)

  val repositoryReads: Reads[Repository] = ((JsPath \ "owner" \ "login").read[String] and (JsPath \ "name").read[String] and (JsPath \ "description").readNullable[String])(Repository.apply _)

  val repositoriesReads: Reads[List[Repository]] = (JsPath \ "items").read[List[Repository]](Reads.list(repositoryReads))

  val repositoryInfoReads: Reads[RepositoryInfo] = (
      (JsPath \ "owner").read[User](userReads)
      and (JsPath \ "name").read[String]
      and (JsPath \ "description").readNullable[String]
      )((owner, name, desc) => RepositoryInfo(owner, name, desc, Nil))
}

