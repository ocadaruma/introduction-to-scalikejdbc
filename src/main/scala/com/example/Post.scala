package com.example

import org.joda.time.DateTime
import scalikejdbc._

case class PostId(value: Int) extends AnyVal

case class Post(
  id: PostId,
  body: String,
  postedAt: DateTime
)

case class PostWithTags(
  post: Post,
  tags: Seq[Tag]
)

case class PostWithRank(
  post: Post,
  latestPostRank: Int
)

object Post extends SQLSyntaxSupport[Post] {
  override val columns = autoColumns[Post]()

  implicit val postIdBinders: Binders[PostId] = Binders.int.xmap(PostId.apply, _.value)

  val p = this.syntax("p")

  def apply(rs: WrappedResultSet): Post = autoConstruct(rs, p.resultName)
  def apply(rs: WrappedResultSet, rn: ResultName[Post]): Post = autoConstruct(rs, rn)

  def allWithTags(ids: Seq[PostId])(implicit session: DBSession): Seq[PostWithTags] = {
    import Tag.t

    withSQL {
      selectFrom(Post as p)
        .leftJoin(Tag as t)
        .on(p.id, t.postId)
        .where.in(p.id, ids)
    }
      .one(Post(_))
      .toMany(Tag.opt)
      .map(PostWithTags)
      .list
      .apply()
  }

  def allWithTags(ids: Seq[PostId], limit: Int, offset: Int)(implicit session: DBSession): Seq[PostWithTags] = {
    import Tag.t

    val subp = SubQuery.syntax("subp").include(p)

    withSQL {
      select(subp.result.*, t.result.*).from(
        selectFrom(Post as p)
          .where.in(p.id, ids)
          .limit(limit)
          .offset(offset)
          .as(subp)
      )
        .leftJoin(Tag as t)
        .on(subp(p).id, t.postId)
    }
      .one(Post(_, subp(p).resultName))
      .toMany(Tag.opt)
      .map(PostWithTags(_, _))
      .list
      .apply()
  }

  def allWithRank()(implicit session: DBSession): Seq[PostWithRank] = {
    import extension._, sqlsEx.rank, sqls.orderBy

    val rankAlias = sqls"rnk"

    withSQL {
      select(
        p.result.*,
        rank.over(orderBy(p.postedAt.desc)).as(rankAlias)
      ).from(Post as p)
    }.map { rs =>
      PostWithRank(
        Post(rs),
        rs.int(rankAlias)
      )
    }.list.apply()
  }
}
