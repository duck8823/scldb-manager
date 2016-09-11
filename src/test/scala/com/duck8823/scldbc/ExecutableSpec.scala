package com.duck8823.scldbc

import com.duck8823.Scldbc
import com.duck8823.scldbc.Driver.SQLite3
import org.scalatest.{FlatSpec, Matchers}

/**
	* Created by maeda on 9/11/2016.
	*/
class ExecutableSpec extends FlatSpec with Matchers {

	"Executable" should "throw Exception." in {
		val manager = Scldbc.connect(SQLite3, "test.db")

		case class Fail(id:Int, fail:Array[Byte])

		a [IllegalStateException] should be thrownBy {
			manager.create[Fail].execute()
		}

		case class Success(id:Int, name:String)

		manager.drop[Success].execute()
		manager.create[Success].execute()

		a [Exception] should be thrownBy {
			manager.create[Success].execute()
		}
	}

	"getSQL" should "correct" in {
		val manager = Scldbc.connect(SQLite3, "test.db")

		case class Hoge(id:Int, name:String)

		val actual = manager.create[Hoge].getSQL
		val expect = "CREATE TABLE Hoge (id INTEGER, name TEXT)"
		assertResult(expect)(actual)
	}
}
