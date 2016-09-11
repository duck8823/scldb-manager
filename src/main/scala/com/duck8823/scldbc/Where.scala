package com.duck8823.scldbc

/**
	* Created by maeda on 9/7/2016.
	*/
class Where(column:String, private var value:Any, operator:Operator) {
	def this() = {
		this(null, null, null)
	}

	override def toString: String = {
		if(column == null && value == null && operator == null) {
			return ""
		} else if((column != null && value == null) || (column == null && value != null) || (column == null && value == null && operator != null)) {
			throw new IllegalStateException("error: {column=%s, value=%s, operator=%s}" format(column, value, operator))
		}
		if(!Array(classOf[Integer], classOf[String], classOf[Boolean]).contains(value.getClass)) {
			throw new IllegalStateException("次の型は対応していません. %s (%s)" format(column, value.getClass.getName))
		}
		if(operator == Operator.LIKE) {
			value = "%" + value + "%"
		}
		"WHERE %s %s '%s'" format(column, operator.value, value)
	}
}
object Operator {
	case object EQUAL extends Operator("=")
	case object NOT_EQUAL extends Operator("<>")
	case object LIKE extends Operator("LIKE")
}
sealed abstract class Operator(val value:String)