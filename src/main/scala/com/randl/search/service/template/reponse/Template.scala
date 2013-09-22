package com.randl.search.service.template.reponse

import java.util.UUID

/**
 * Created with IntelliJ IDEA.
 * User: cgonzalez
 * Date: 9/22/13
 * Time: 1:53 PM
 * To change this template use File | Settings | File Templates.
 */
case class Template(
                     id: UUID,
                     name: String,
                     categories: List[String]
                     )

case class TemplateList (totalHits: Long, templates: List[Template])