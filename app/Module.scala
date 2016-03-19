import com.google.inject.AbstractModule
import services.Github
import play.api.{Configuration, Environment}

class Module (environment: Environment, configuration: Configuration) extends AbstractModule {

  override def configure() = {
    val githubToken = configuration.getString("github.token").getOrElse("")
    bind(classOf[services.Github]).toInstance(new services.Github(githubToken))
  }

}