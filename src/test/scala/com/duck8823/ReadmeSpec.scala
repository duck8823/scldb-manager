package com.duck8823

import com.duck8823.scldbc.Driver._
import com.duck8823.scldbc.Operator.{EQUAL, LIKE}
import com.duck8823.scldbc.Where
import org.scalatest.{FlatSpec, Matchers}

// case class の定義
case class Hoge(id:Int, name:String, flg:Boolean)
/**
	* Created by maeda on 9/11/2016.
	*/
class ReadmeSpec extends FlatSpec with Matchers {

	"readme" should "correct" in {
		val manager = Scldbc.connect(Postgres, "//localhost:5432/test", "postgres", "")
		manager.drop[Hoge].execute()

		manager.create[Hoge].execute()

		manager.insert(Hoge(1, "name_1", true)).execute()
		manager.insert(Hoge(2, "name_2", false)).execute()

		val rows = manager.from[Hoge].list()
		rows.foreach(println)
		manager.from[Hoge].where(new Where("name", "name", LIKE)).list()

		val row = manager.from[Hoge].where(new Where("id", 1, EQUAL)).singleResult()
		println(row)

		manager.from[Hoge].where(new Where("id", 1, EQUAL)).delete().execute()
		manager.drop[Hoge].execute()

		val createSQL = manager.create[Hoge].getSQL
		val insertSQL = manager.insert(Hoge(1, "name_1", true)).getSQL
		val deleteSQL = manager.from[Hoge].where(new Where("id", 1 ,EQUAL)).delete().getSQL
		val dropSQL = manager.drop[Hoge].getSQL
	}
}
