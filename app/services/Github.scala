package services

import net.caoticode.buhtig.Buhtig
import net.caoticode.buhtig.Converters._

import play.api.libs.json._ // JSON library
import play.api.libs.json.Reads._ // Custom validation helpers
import play.api.libs.functional.syntax._ // Combinator syntax

class Github(val token: String) {
  val buhtig = new Buhtig(token)
  val client = buhtig.asyncClient

  def searchRepositories(q: String) = (client.search.repositories ? ("q" -> q )).get[String]

  def close = client.close
}

object Github {
  case class Repository(val fullName: String, val description: Option[String])

  val repositoryReads: Reads[Repository] = ((JsPath \ "full_name").read[String] and (JsPath \ "description").readNullable[String])(Repository.apply _)

  val repositoriesReads: Reads[List[Repository]] = (JsPath \ "items").read[List[Repository]](Reads.list(repositoryReads))
}

