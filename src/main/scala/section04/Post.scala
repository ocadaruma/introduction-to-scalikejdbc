package section04

import org.joda.time.DateTime
import scalikejdbc._

case class Post(
  id: Int,
  body: String,
  postedAt: DateTime
)

object Post extends SQLSyntaxSupport[Post] {
  // define alias name of post table
  val p = this.syntax("p")

  def apply(rs: WrappedResultSet): Post = autoConstruct(rs, p.resultName)
}
