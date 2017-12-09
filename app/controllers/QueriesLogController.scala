package controllers

import javax.inject.{Inject, Singleton}

import models.{QueriesLog, QueriesLogRepository}
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Tomasz Kopczynski.
  */
@Singleton
class QueriesLogController @Inject() (cc: ControllerComponents, queriesLogRepository: QueriesLogRepository) extends AbstractController(cc) {

  implicit val queriesWrites = Json.writes[QueriesLog]

  def mostRecentLogQueries(count: String): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    queriesLogRepository.mostRecentQueries(count.toInt)
      .map(queries => Json.toJson(queries))
      .map(json => Ok(json))
  }
}
