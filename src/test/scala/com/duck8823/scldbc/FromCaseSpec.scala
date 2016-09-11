package com.duck8823.scldbc

import com.duck8823.Scldbc
import com.duck8823.scldbc.Driver.SQLite3
import com.duck8823.scldbc.Operator.EQUAL
import org.scalatest.{FlatSpec, Matchers}


case class TestFromCase(id:Int, name:String)
/**
	* Created by maeda on 9/11/2016.
	*/
class FromCaseSpec extends FlatSpec with Matchers {
	val manager = Scldbc.connect(SQLite3, "test.db")

	"list result" should "correct" in {
		manager.drop[TestFromCase].execute()
		manager.create[TestFromCase].execute()

		manager.insert(TestFromCase(1, "name_1")).execute()
		manager.insert(TestFromCase(2, "name_2")).execute()

		val actual = manager.from[TestFromCase].list()
		val expect = List(TestFromCase(1, "name_1"), TestFromCase(2, "name_2"))

		assertResult(expect)(actual)

		case class NotExist(id:Int)
		a [Exception] should be thrownBy {
			manager.from[NotExist].list()
		}
	}

	"singleResult" should "correct" in {
		manager.drop[TestFromCase].execute()
		manager.create[TestFromCase].execute()

		manager.insert(TestFromCase(1, "name_1")).execute()
		manager.insert(TestFromCase(2, "name_2")).execute()

		val actual = manager.from[TestFromCase].where(new Where("id", 1, EQUAL)).singleResult()
		val expect = TestFromCase(1, "name_1")
		assertResult(expect)(actual)

		a [IllegalStateException] should be thrownBy {
			manager.from[TestFromCase].where(new Where("id", Array[Byte](), EQUAL)).singleResult()
		}

		a [IllegalStateException] should be thrownBy {
			manager.from[TestFromCase].singleResult()
		}
	}

	"delete result" should "correct" in {
		manager.drop[TestFromCase].execute()
		manager.create[TestFromCase].execute()

		manager.insert(TestFromCase(1, "name_1")).execute()
		manager.insert(TestFromCase(2, "name_2")).execute()

		manager.from[TestFromCase].where(new Where("id", 1, EQUAL)).delete().execute()
		val actual = manager.from[TestFromCase].singleResult()
		val expect = TestFromCase(2, "name_2")

		assertResult(expect)(actual)
	}
}
