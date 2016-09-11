package com.duck8823.scldbc

import com.duck8823.Scldbc
import com.duck8823.scldbc.Driver.SQLite3
import com.duck8823.scldbc.Operator.EQUAL
import org.scalatest.{FlatSpec, Matchers}


/**
	* Created by maeda on 9/11/2016.
	*/
class FromCaseSpec extends FlatSpec with Matchers {
	val manager = Scldbc.connect(SQLite3, "test.db")

	"list result" should "correct" in {
		manager.drop[Test].execute()
		manager.create[Test].execute()

		manager.insert(Test(1, "name_1")).execute()
		manager.insert(Test(2, "name_2")).execute()

		val actual = manager.from[Test].list()
		val expect = List(Test(1, "name_1"), Test(2, "name_2"))

		assertResult(expect)(actual)

		case class NotExist(id:Int)
		a [Exception] should be thrownBy {
			manager.from[NotExist].list()
		}
	}

	"singleResult" should "correct" in {
		manager.drop[Test].execute()
		manager.create[Test].execute()

		manager.insert(Test(1, "name_1")).execute()
		manager.insert(Test(2, "name_2")).execute()

		val actual = manager.from[Test].where(new Where("id", 1, EQUAL)).singleResult()
		val expect = Test(1, "name_1")
		assertResult(expect)(actual)

		a [IllegalStateException] should be thrownBy {
			manager.from[Test].where(new Where("id", Array[Byte](), EQUAL)).singleResult()
		}

		a [IllegalStateException] should be thrownBy {
			manager.from[Test].singleResult()
		}
	}

	"delete result" should "correct" in {
		manager.drop[Test].execute()
		manager.create[Test].execute()

		manager.insert(Test(1, "name_1")).execute()
		manager.insert(Test(2, "name_2")).execute()

		manager.from[Test].where(new Where("id", 1, EQUAL)).delete().execute()
		val actual = manager.from[Test].singleResult()
		val expect = Test(2, "name_2")

		assertResult(expect)(actual)
	}
}
