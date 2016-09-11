package com.duck8823.scldbc


import java.lang.reflect.Field

import com.zaxxer.hikari.HikariDataSource
import org.skife.jdbi.v2.DBI

import scala.collection.mutable.ListBuffer
import scala.reflect.ClassTag


/**
	* Created by maeda on 9/6/2016.
	*/
class Manager(driver:Driver, datasource:String, user:String, password: String) {
	private val ds = new HikariDataSource()
	ds.setJdbcUrl("jdbc:%s:%s".format(driver.name, datasource))
	ds.setDriverClassName(driver.clazz.getName)
	if (user != null) ds.setUsername(user)
	if (password != null) ds.setPassword(password)
	private val db = new DBI(ds).open()

	def from[T](implicit tag: ClassTag[T]): FromCase[T] = {
		new FromCase[T](db)
	}

	def drop[T](implicit tag: ClassTag[T]): Executable = {
		new Executable(db, "DROP TABLE IF EXISTS %s" format Manager.parseClassName(tag.runtimeClass))
	}

	def create[T](implicit tag: ClassTag[T]): Executable = {
		val columns = ListBuffer[String]()
		Manager.findFields(tag.runtimeClass).foreach(f => {
			val type_ =
				f.getType match {
					case x if x == classOf[Int]     => "INTEGER"
					case x if x == classOf[String]  => "TEXT"
					case x if x == classOf[Boolean] => "BOOLEAN"
					case _ => throw new IllegalStateException("次の型は利用できません:" + f.getType.getName)
				}
			columns += "%s %s" format(f.getName, type_)
		})
		new Executable(db, "CREATE TABLE %s (%s)" format(Manager.parseClassName(tag.runtimeClass), columns.mkString(", ")))
	}

	def insert(data:Object): Executable = {
		new Executable(db, "INSERT INTO %s %s" format(Manager.parseClassName(data.getClass), Manager.createSentence(data)))
	}
}
object Manager {
	def parseClassName(clazz: Class[_]): String = {
		clazz.getSimpleName.replaceFirst("\\$\\d+?$", "")
	}

	def findFields(clazz: Class[_]): List[Field] = {
		clazz.getDeclaredFields.filterNot(_.getName.matches("^\\$.*$")).toList
	}

	def createSentence(data: Object): String = {
		val columns = ListBuffer[String]()
		val values  = ListBuffer[String]()
		data.getClass.getDeclaredFields.filterNot(_.getName.matches("^\\$.*$")).foreach(f => {
			f.setAccessible(true)
			columns += f.getName
			values  += "'%s'" format f.get(data).toString
		})
		"(%s) VALUES (%s)" format(columns.mkString(", "), values.mkString(", "))
	}
}

