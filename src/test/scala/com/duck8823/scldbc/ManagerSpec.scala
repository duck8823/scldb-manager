package com.duck8823.scldbc

import com.duck8823.Scldbc
import com.duck8823.scldbc.Driver.SQLite3
import com.duck8823.scldbc.Operator.EQUAL
import org.scalatest.{FlatSpec, Matchers}
import org.skife.jdbi.v2.Handle
import org.skife.jdbi.v2.exceptions.NoResultsException

import scala.collection.JavaConverters._
import scala.collection.mutable

case class Test(id:Int, name:String)
/**
	* Created by maeda on 9/6/2016.
	*/
class ManagerSpec extends FlatSpec with Matchers {
	val manager = Scldbc.connect(SQLite3, "test.db")

	"manager" should "be able to create a table" in {
		manager.drop[Test].execute()
		manager.create[Test].execute()

		val field = manager.getClass.getDeclaredField("db")
		field.setAccessible(true)
		val db = field.get(manager).asInstanceOf[Handle]

		val actual = mutable.Map[String, String]()
		db.select("PRAGMA TABLE_INFO(Test)").asScala.foreach(row => {
			actual.put(row.get("name").toString, row.get("type").toString)
		})
		val expect = Map("id" -> "INTEGER", "name" -> "TEXT")

		assertResult(expect)(actual)
	}

	it should "be able to drop a table" in {
		manager.drop[Test].execute()
		manager.create[Test].execute()

		manager.drop[Test].execute()

		val field = manager.getClass.getDeclaredField("db")
		field.setAccessible(true)
		val db = field.get(manager).asInstanceOf[Handle]

		a [NoResultsException] should be thrownBy {
			db.select("PRAGMA TABLE_INFO(Test)")
		}
	}

	it should "be able to insert entities" in {
		manager.drop[Test].execute()
		manager.create[Test].execute()

		manager.insert(Test(1, "name_1")).execute()
		manager.insert(Test(2, "name_2")).execute()

		val field = manager.getClass.getDeclaredField("db")
		field.setAccessible(true)
		val db = field.get(manager).asInstanceOf[Handle]

		val actual = db.select("SELECT id, name FROM Test").asScala.map(row => {
			Map("id" -> row.get("id"), "name" -> row.get("name"))
		}).toList
		val expect = List(Map("id" -> 1, "name" -> "name_1"), Map("id" -> 2, "name" -> "name_2"))
		actual should be (expect)
	}

	it should "be able to delete a entity" in {
		manager.drop[Test].execute()
		manager.create[Test].execute()

		manager.insert(Test(1, "name_1")).execute()
		manager.insert(Test(2, "name_2")).execute()

		manager.from[Test].where(new Where("id", 1, EQUAL)).delete().execute()
		val actual = manager.from[Test].singleResult()
		val expect = Test(2, "name_2")

		actual should be (expect)
	}
}
