package com.duck8823

import com.duck8823.scldbc.Driver._
import com.duck8823.scldbc.Manager
import org.scalatest.{FlatSpec, Matchers}


/**
	* Created by maeda on 9/7/2016.
	*/
class ScldbcSpec extends FlatSpec with Matchers {

	"A Scldbc" should "create a Manager" in {
		val manager = Scldbc.connect(SQLite3, "./test.db")
		manager.getClass should be (classOf[Manager])
	}

}

