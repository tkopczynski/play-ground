package controllers

import javax.inject.{Inject, Singleton}

import play.api.cache.AsyncCacheApi
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import play.api.mvc._
import play.api.{Configuration, Logger}

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Tomasz Kopczynski.
  */
@Singleton
class WeatherController @Inject()(cc: ControllerComponents, ws: WSClient, config: Configuration, cache: AsyncCacheApi) extends AbstractController(cc) {

  def locateEndpoint: String = config.get[String]("api.weather.locate")

  def locationWeatherEndpoint: String = config.get[String]("api.weather.locationWeather")

  def logger: Logger = Logger(this.getClass.getSimpleName)

  def weather(city: String): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val locationWoeid = cache.getOrElseUpdate[String](s"location.$city") {
      logger.info("querying for location woeid")
      ws.url(locateEndpoint).addQueryStringParameters("query" -> city).get()
        .map {
          response => response.json \ 0 \ "woeid"
        }
        .map(_.as[Int])
        .map(String.valueOf)
    }

    locationWoeid.flatMap { woeid =>
      ws.url(s"$locationWeatherEndpoint/$woeid").get()
    }
      .map {
        response => response.json \ "consolidated_weather" \ 0 \ "the_temp"
      }
      .map(_.as[Double])
      .map(temperature => Ok(Json.obj("current_temperature" -> temperature)))
      .recover {
        case e: Exception => {
          logger.error("Error when calling Weather API", e)
          ServiceUnavailable(Json.obj("error" -> e.getMessage))
        }
      }
  }
}
