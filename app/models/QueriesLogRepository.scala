package models

import java.sql.Timestamp
import javax.inject.{Inject, Singleton}

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.H2Profile

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Tomasz Kopczynski.
  */
@Singleton
class QueriesLogRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[H2Profile]

  import dbConfig._
  import profile.api._

  private class QueriesLogTable(tag: Tag) extends Table[QueriesLog](tag, "QUERIES_LOG") {

    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

    def queryTime = column[Timestamp]("QUERY_TIME")

    def city = column[String]("CITY")

    def * = (id, queryTime, city) <> ((QueriesLog.apply _).tupled, QueriesLog.unapply)
  }

  private val queriesLog = TableQuery[QueriesLogTable]

  def create(city: String) = db.run {
    queriesLog.map(c => (c.queryTime, c.city)) += (new Timestamp(System.currentTimeMillis()), city)
  }

  def mostRecentQueries(count: Int): Future[Seq[QueriesLog]] = db.run {
    queriesLog.sortBy(_.queryTime.desc).result
  }

}
