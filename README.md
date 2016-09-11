# ScldbcManager
[![Build Status](https://travis-ci.org/duck8823/scldbc-manager.svg?branch=master)](https://travis-ci.org/duck8823/scldbc-manager)
[![Coverage Status](http://coveralls.io/repos/github/duck8823/scldbc-manager/badge.svg?branch=master)](https://coveralls.io/github/duck8823/scldbc-manager?branch=master)
[![MIT License](http://img.shields.io/badge/license-MIT-blue.svg?style=flat)](LICENSE)  

case classでデータベースを操作する

## INSTALL

## SYNOPSIS
```scala
import com.duck8823.Scldbc.connect
import com.duck8823.scldbc.Driver._
import com.duck8823.scldbc.Operator._
import com.duck8823.scldbc.Where

// case class の定義
case class Hoge(id:Int, name:String, flg:Boolean)

object Main {
	def main(args: Array[String]) {
		// データベースへの接続
		val manager = connect(Postgres, "//localhost:5432/test", "postgres", "")
		// テーブルの作成
		manager.create[Hoge].execute()
		// データの挿入
		manager.insert(Hoge(1, "name_1", true)).execute()
		manager.insert(Hoge(2, "name_2", false)).execute()
		// データの取得（リスト）
		val rows = manager.from[Hoge].list()
		rows.foreach(println)
		manager.from[Hoge].where(new Where("name", "name", LIKE)).list()
		// データの取得（一意）
		val row = manager.from[Hoge].where(new Where("id", 1, EQUAL)).singleResult()
		println(row)
		// データの削除
		manager.from[Hoge].where(new Where("id", 1, EQUAL)).delete().execute()
		// テーブルの削除
		manager.drop[Hoge].execute()
		// SQLの取得
		val createSQL = manager.create[Hoge].getSQL
		val insertSQL = manager.insert(Hoge(1, "name_1", true)).getSQL
		val deleteSQL = manager.from[Hoge].where(new Where("id", 1 ,EQUAL)).delete().getSQL
		val dropSQL = manager.drop[Hoge].getSQL
	}
}
```

## License
MIT License