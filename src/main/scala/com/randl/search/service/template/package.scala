package com.randl.search.service

/**
 * Created with IntelliJ IDEA.
 * User: cgonzalez
 * Date: 9/22/13
 * Time: 2:18 PM
 * To change this template use File | Settings | File Templates.
 */
package object template {
  val CATEGORIES = "categories"
  val SRC_NAME = "name"
  val SPECIAL_SEQUENCES = """+ - && || ! ( ) { } [ ] ^ " ~ * ? :""".split(' ')
  val ESCAPED_SEQUENCES = Map(SPECIAL_SEQUENCES.map(seq => (seq, seq.map("\\" + _).foldLeft("")(_ + _))): _*)
  val index = "template"
  val esType = "template"
}
