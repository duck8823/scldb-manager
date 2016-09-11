package com.duck8823

import com.duck8823.scldbc.{Driver, Manager}


/**
	* Created by maeda on 9/6/2016.
	*/
object Scldbc {
	def connect(driver:Driver, datasource:String, user:String=null, password:String=null): Manager = {
		new Manager(driver, datasource, user, password)
	}
}
