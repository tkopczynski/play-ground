package controllers

import javax.inject.{Inject, Singleton}

import play.api.Configuration
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Tomasz Kopczynski.
  */
@Singleton
class WeatherController @Inject()(cc: ControllerComponents, ws: WSClient, config: Configuration) extends AbstractController(cc) {

  def locateEndpoint: String = config.get[String]("api.weather.locate")
  def locationWeatherEndpoint: String = config.get[String]("api.weather.locationWeather")

  def weather(city: String): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    ws.url(locateEndpoint).addQueryStringParameters("query" -> city)
      .get()
      .map {
        response => response.json \ 0 \ "woeid"
      }
      .map(jsLookupResult => jsLookupResult.as[Int])
      .map(woeid => String.valueOf(woeid))
      .flatMap { woeid =>
        ws.url(s"$locationWeatherEndpoint/$woeid").get()
      }
      .map {
        response => response.json \ "consolidated_weather" \ 0 \ "the_temp"
      }
      .map(jsLookupResult => jsLookupResult.as[Double])
      .map(temperature => Ok(Json.obj("current_temperature" -> temperature)))
    }
}
