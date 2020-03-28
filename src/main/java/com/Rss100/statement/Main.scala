package com.Rss100.statement

import java.sql.ResultSet

import com.Rss100.util.{JDBCUtils, Utils}

import scala.util.matching.Regex

/**
 * Created by wststart on 2020/3/25
 */
object Main {
  def get_table_info(mysqlDatabase: String, mysqlTable: String, hiveDatabase: String, hiveTable: String, ifPartition: String, partitions: String*) = {
    val set: ResultSet = JDBCUtils.getQuery(
      s"""
         |
         |SHOW FULL FIELDS FROM $mysqlDatabase.$mysqlTable
         |""".stripMargin)
    var create_head: String =
      s"""
         |create external table if not exists $hiveDatabase.$hiveTable (
         |""".stripMargin
    var create_tail = ""
    val partition: String = Utils.getPartition(partitions: _*)
    ifPartition match {
      case "1" => create_tail =
        s"""
           |partitioned by($partition string)
           |row format delimited fields terminated by ','
           |location '/hivetable/$hiveDatabase/$hiveTable';
           |""".stripMargin
      case "2" => create_tail =
        s"""
           |row format delimited fields terminated by ','
           |location '/hivetable/$hiveDatabase/$hiveTable';
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
    create_head.substring(0, create_head.length - 2) + "\n)" + create_tail
  }


  def main(args: Array[String]): Unit = {

  }
}
