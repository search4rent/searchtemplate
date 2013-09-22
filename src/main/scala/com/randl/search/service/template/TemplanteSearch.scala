package com.randl.search.service.template

import org.elasticsearch.index.query.QueryBuilders
import java.util.UUID
import com.randl.core.servicelib.elasticsearch.ESClient
import org.elasticsearch.index.query.QueryStringQueryBuilder.Operator
import com.randl.search.service.template.reponse.{Template, TemplateList}
import scala.Some
import scala.collection.JavaConversions._

/**
 * Created with IntelliJ IDEA.
 * User: cgonzalez
 * Date: 9/22/13
 * Time: 1:44 PM
 * To change this template use File | Settings | File Templates.
 */
trait TemplanteSearch extends ESClient {

  val CATEGORIES = "categories"

  def normalizeQueryString(str: String) = {
    escape(str).trim()
  }

  def escape(str: String) = {
    if (str == null)
      str

    var escapedStr = str
    for (seq <- ESCAPED_SEQUENCES) {
      escapedStr = escapedStr.replace(seq._1, seq._2)
    }
    escapedStr
  }

  def queryName(searchStr: String) = QueryBuilders
    .queryString(normalizeQueryString(searchStr))
    .field(SRC_NAME)
    .analyzer("name_analyzer_search")
    .defaultOperator(Operator.OR)

  def queryBool(searchStr: String) = QueryBuilders
    .boolQuery()
    .must(queryName(searchStr))


  def templateById(id: String) = {

    val search = client
      .prepareSearch("template")
      .setTypes("template")
      .setQuery(QueryBuilders.fieldQuery("id", id)).execute().actionGet()
    search.getHits.getHits.toList match {
      case head :: tail =>
        Template(
          id = UUID.fromString(head.id()),
          name = head.getSource.get(SRC_NAME).asInstanceOf[String],
          categories = head.getSource.get(CATEGORIES).asInstanceOf[java.util.List[String]].toList
        )
      case _ => null
    }
  }


  def templateSearch(searchStr: String): TemplateList = {

    val search1 = client
      .prepareSearch(index)
      .setTypes(esType)
      .setQuery(queryBool(searchStr))
    println(search1)
    val search = search1.execute().actionGet();
    val hits = search.getHits();
    TemplateList(hits.getTotalHits(), hits.getHits().toList.map
      (
        hit => {
          Template(
            id = UUID.fromString(hit.getId),
            name = hit.getSource.get(SRC_NAME).asInstanceOf[String],
            categories = hit.getSource.get(CATEGORIES).asInstanceOf[java.util.List[String]].toList
          )
        }
      ).toList
    )
  }
}