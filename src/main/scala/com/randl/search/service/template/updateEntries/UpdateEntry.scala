package com.randl.search.service.template.updateEntries

import java.util.UUID
import com.codahale.jerkson.Json
import com.randl.core.servicelib.elasticsearch.{ElasticSearchFactory, ESClient}
import com.randl.search.service.template.reponse.Template
import com.randl.search.service.template._

object UpdateEntry extends App with ESClient {

  ElasticSearchFactory.init()

  client
  //create the bulkRequest where we are going to set the entries
  val bulkRequest = client.prepareBulk()
  (1 to 10).map(entry => {
    //create a writeRequest for each entry
    val writeRequest = client.prepareIndex().setIndex(index).setType(esType)
    val title = "playstation" + (entry % 3)
    val item = Template(
      id = UUID.randomUUID(),
      name = title,
      categories = List[String]()

    )
    bulkRequest.add(writeRequest.setId(item.id.toString).setSource(Json.generate(item)))
  })
  bulkRequest.execute().actionGet()

}
