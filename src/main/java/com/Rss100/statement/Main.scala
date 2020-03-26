package com.Rss100.statement

import java.sql.ResultSet

import com.Rss100.util.JDBCUtils

import scala.util.matching.Regex

/**
 * Created by wststart on 2020/3/25
 */
object Main {
  def main(args: Array[String]): Unit = {
    val set: ResultSet = JDBCUtils.getQuery(
      """
        |
        |SHOW FULL FIELDS FROM test.ads_info
        |""".stripMargin)
    var create_head: String =
      s"""
         |create external table if not exists ${args(0)}.${args(1)} (
         |""".stripMargin
    var create_tail = ""
    args(2) match {
      case "1" => create_tail =
        s"""
           |partitioned by(${args(3)} string)
           |row format delimited fields terminated by ','
           |location '/hivetable/${args(1)}';
           |""".stripMargin
      case "2" => create_tail =
        s"""
           |row format delimited fields terminated by ','
           |location '/hivetable/${args(1)}';
           |""".stripMargin
      case _ => throw new IllegalArgumentException("参数异常")

    }
    while (set.next()) {
      var columnType: String = set.getString("Type")
      val field: String = set.getString("Field")
      val comment: String = set.getString("Comment")
      if (columnType.matches("bigint.*")) {
        columnType = "bigint"
      } else if (columnType.matches("int.*") || columnType.matches("tinyint.*")
        || columnType.matches("smallint.*") || columnType.matches("mediumint.*") || columnType.matches("integer.*")) {
        columnType = "int"
      } else if (columnType.matches("double.*") || columnType.matches("float.*")) {
        columnType = "double"
      } else if (columnType.matches("decimal.*")) {
        columnType = "decimal(18,2)"
      } else {
        columnType = "string"
      }
      create_head += field + " " + columnType + " comment '" + comment + "' ,\n"
    }
    val str: String = create_head.substring(0, create_head.length - 2) + "\n)" + create_tail
    println(str)
  }

}
