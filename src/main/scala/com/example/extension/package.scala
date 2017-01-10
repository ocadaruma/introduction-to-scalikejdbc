package com.example

import scalikejdbc._

package object extension {
  implicit class SQLSyntaxExtension(val self: SQLSyntax) extends AnyVal {
    def over(window: SQLSyntax): SQLSyntax = self.append(
      sqls"over${sqls.roundBracket(window)}"
    )

    def as(columnAlias: SQLSyntax): SQLSyntax = self.append(
      sqls"as $columnAlias"
    )
  }

  object sqlsEx {
    val rank: SQLSyntax = sqls"rank()"
  }
}
