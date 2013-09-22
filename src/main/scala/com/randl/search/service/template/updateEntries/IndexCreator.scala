package com.randl.search.service.template.updateEntries

import scala.io.Source
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest
import com.randl.core.servicelib.elasticsearch.{ElasticSearchFactory, ESClient}
 import com.randl.search.service.template._

/**
 * Created with IntelliJ IDEA.
 * User: cgonzalez
 * Date: 7/16/13
 * Time: 11:21 AM
 * To change this template use File | Settings | File Templates.
 */
object IndexCreator extends App with ESClient{
  ElasticSearchFactory.init()
  //Read settings and mapping from file
  val settings = Source.fromURL(this.getClass.getResource("setting.json")).getLines().mkString
  val mapping = Source.fromURL(this.getClass.getResource("mapping.json")).getLines().mkString

  //if exits create
  if (client.admin().indices().exists(new IndicesExistsRequest(index)).actionGet().isExists) {
    client.admin().indices().prepareDelete(index).execute().actionGet()
    client.admin().cluster().prepareHealth(index).setWaitForYellowStatus().execute().actionGet()
  }

  //create index
  client.admin().indices().prepareCreate(index).setSettings(settings).execute().actionGet()
  try {
    //wait until the index is available
    client.admin().cluster().prepareHealth(index).setWaitForYellowStatus().execute().actionGet()
    //set the settings (analyzer...)

    //  Thread.sleep(5000)
    //client.admin().indices().prepareUpdateSettings().setSettings(settings).execute().actionGet()
    //set the mapping
    client.admin().indices().preparePutMapping(index).setType(esType).setSource(mapping).execute().actionGet().ensuring(true)
    //we wait here until the mapping is set on the primary sharp
    client.admin().cluster().prepareHealth(index).setWaitForYellowStatus().execute().actionGet()
  } finally {
    client.close()
  }


}
