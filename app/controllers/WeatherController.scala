package controllers

import javax.inject.{Inject, Singleton}

import play.api.mvc._

/**
  * Created by Tomasz Kopczynski.
  */
@Singleton
class WeatherController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def weather(city: String) = Action { implicit request: Request[AnyContent] =>
    Ok("weather in " + city)
  }
}
