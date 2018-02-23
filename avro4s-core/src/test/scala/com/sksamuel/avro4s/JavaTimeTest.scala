package com.sksamuel.avro4s

import java.io.File
import java.time.{Instant, LocalDate, LocalDateTime, ZoneOffset}

import org.scalatest.{Matchers, WordSpec}

case class Time(instant: Instant, localDateTime: LocalDateTime, localDate: LocalDate)

class JavaTimeTest extends WordSpec with Matchers {
  "java.time classes" should {
    "serialize and deserialize" in {
      val date = LocalDate.of(2018, 2, 23)
      val dateTime = date.atTime(1, 30)
      val instant = dateTime.toInstant(ZoneOffset.UTC)
      val timeA = Time(instant, dateTime, date)

      val dateB = LocalDate.of(2013, 4, 13)
      val dateTimeB = dateB.atTime(19, 14)
      val instantB = dateTimeB.toInstant(ZoneOffset.UTC)
      val timeB = Time(instantB, dateTimeB, dateB)

      val file = new File("time.avro")
      val os = AvroOutputStream.data[Time](file)
      os.write(Seq(timeA, timeB))
      os.flush()
      os.close()

      val is = AvroInputStream.data[Time](file)
      val times = is.iterator.toList
      is.close()
      times shouldEqual List(timeA, timeB)
      file.delete()
    }
  }
}
