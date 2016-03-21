package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.i18n.MessagesApi
import play.api.i18n.I18nSupport
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import services.Github._
import play.api.libs.json.{ Json, Reads }
import services.Github

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() (val messagesApi: MessagesApi, val github: services.Github) extends Controller with I18nSupport {

  def index(q: Option[String] = None) = Action.async { implicit request =>
    Logger.info(s"q =>  $q")
    q.fold(Future.successful(Ok(views.html.index()))) { q =>
      Logger.info(s"q = $q")
      github.searchRepositories(q).map { x =>
        Logger.info(Json.prettyPrint(Json.parse(x)))
        repositoriesReads.reads(Json.parse(x)).fold(_ => ServiceUnavailable("XXX"), { data =>
          Logger.info(s"data => $data")
          Ok(views.html.index(data))
        })
      }
    }
  }

  def show(user: String, name: String) = Action.async { implicit request =>
    //(github.repositoryCommits(user, name) map Json.parse map Json.prettyPrint ).map(s => Logger.info(s))
    val futureData = for {
      repositoryResult <- github.repository(user, name) map Json.parse map repositoryInfoReads.reads
      contributorsResult <- github.repositoryContributors(user, name) map Json.parse map Reads.list(userReads).reads
      commitsResult <- github.repositoryCommits(user, name) map Json.parse map repositoryCommitsReads.reads
    } yield {
        for {
        	repository <- repositoryResult
        	contributors <- contributorsResult
        	repoCommits <- commitsResult
        } yield (repository.copy(contributors = contributors), repoCommits)
    }
    futureData.map { _.fold ( { invalid =>
    Logger.info(s"$invalid")
      ServiceUnavailable("Bad data")
    }
    , { case (repo, commits) =>
      Ok(views.html.repo(repo, commits))
    })
    }.recover({ case _ =>
      ServiceUnavailable("Github down")
    })
  }
}
