package com.duck8823.scldbc

import com.duck8823.scldbc.Operator.{EQUAL, LIKE, NOT_EQUAL}
import org.scalatest.{FlatSpec, Matchers}

/**
	* Created by maeda on 9/11/2016.
	*/
class WhereSpec extends FlatSpec with Matchers {

	"Where" should "be Where" in {
		val where = new Where()
		where.getClass should be (classOf[Where])
	}

	"toString" should "correct" in {
		var where:Where = new Where()
		where = new Where(null, 1, EQUAL)
		a [IllegalStateException] should be thrownBy {
			where.toString
		}

		where = new Where("id", null, EQUAL)
		a [IllegalStateException] should be thrownBy {
			where.toString
		}

		where = new Where(null, null, EQUAL)
		a [IllegalStateException] should be thrownBy {
			where.toString
		}

		where = new Where(null, Array[Byte](), EQUAL)
		a [IllegalStateException] should be thrownBy {
			where.toString
		}

		new Where(null, null, null).toString should be ("")
		new Where("name", "name", LIKE).toString should be ("WHERE name LIKE '%name%'")
	}

	"operator" should "correct" in {
		EQUAL.value should be ("=")
		NOT_EQUAL.value should be ("<>")
		LIKE.value should be ("LIKE")
	}
}
