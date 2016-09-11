package com.duck8823.scldbc


import org.skife.jdbi.v2.Handle

import scala.collection.JavaConverters._
import scala.reflect.ClassTag

/**
	* Created by maeda on 9/7/2016.
	*/
class FromCase[T](db:Handle)(implicit tag: ClassTag[T]) {
	var where:Where = new Where()

	def where(where:Where): this.type = {
		this.where = where
		this
	}

	def list(): List[T] = {
		val fields = Manager.findFields(tag.runtimeClass)
		val result = db.select("SELECT %s FROM %s %s" format(fields.map(_.getName).mkString(", "), Manager.parseClassName(tag.runtimeClass), where)).asScala.map(entry => {
			val constructor = tag.runtimeClass.getDeclaredConstructor(tag.runtimeClass.getDeclaredFields.map(_.getType): _*)
			val values = fields.map(f => entry.get(f.getName))
			constructor.newInstance(values: _*).asInstanceOf[T]
		})
		result.toList
	}

	def singleResult(): T = {
		val result = list()
		if (result.length > 1) {
			throw new IllegalStateException("結果が一意でありません.")
		}
		result.head
	}

	def delete(): Executable = {
		new Executable(db, "DELETE FROM %s %s" format(Manager.parseClassName(tag.runtimeClass), where))
	}
}
