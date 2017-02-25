package com.example

import scalikejdbc._

case class TagId(value: Int) extends AnyVal

case class Tag(
  id: TagId,
  postId: PostId,
  name: String
)

object Tag extends SQLSyntaxSupport[Tag] {
  override val columns = autoColumns[Tag]()

  import Post.postIdBinders

  implicit val tagIdBinders: Binders[TagId] = Binders.int.xmap(TagId.apply, _.value)

  val t = this.syntax("t")

  def apply(rs: WrappedResultSet): Tag = autoConstruct(rs, t.resultName)
  def opt(rs: WrappedResultSet): Option[Tag] = rs.intOpt(t.resultName.id).map(_ => apply(rs))
}
