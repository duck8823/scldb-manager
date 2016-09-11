package com.duck8823.scldbc

import org.skife.jdbi.v2.Handle

/**
	* Created by maeda on 9/8/2016.
	*/
class Executable(db:Handle, sql:String) {

	def execute(): Unit = {
		db.execute(sql)
	}

	def getSQL: String = {
		sql
	}
}
