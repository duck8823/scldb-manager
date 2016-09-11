package com.duck8823.scldbc

/**
	* Created by maeda on 9/11/2016.
	*/
sealed abstract class Driver(val name:String, val clazz:Class[_])
object Driver {
	case object SQLite3  extends Driver("sqlite", classOf[org.sqlite.JDBC])
	case object Postgres extends Driver("postgresql", classOf[org.postgresql.ds.PGSimpleDataSource])
}