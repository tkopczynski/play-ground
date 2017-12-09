package models

import java.sql.Timestamp

/**
  * Created by Tomasz Kopczynski.
  */
case class QueriesLog(id: Long, queryTime: Timestamp, city: String)