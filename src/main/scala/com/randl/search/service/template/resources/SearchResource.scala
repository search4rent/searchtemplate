package com.randl.search.service.template.resources

import javax.ws.rs._
import scala.Array
import javax.ws.rs.core.MediaType._
import javax.ws.rs.core.{Response, HttpHeaders, Context}
import java.util.Locale
import com.randl.search.service.template.TemplanteSearch

/**
 * Created with IntelliJ IDEA.
 * User: cgonzalez
 * Date: 9/22/13
 * Time: 2:46 PM
 * To change this template use File | Settings | File Templates.
 */
@Path("/search")
@Produces(Array(APPLICATION_JSON))
@Consumes(Array(APPLICATION_JSON))
class SearchResource extends TemplanteSearch {


  @GET
  @Path("-/item/{id}")
  def getItem(
               @PathParam("id") id: String,
               @Context headers: HttpHeaders) = {
    val locale = if (headers.getLanguage() == null) Locale.US else headers.getLanguage()
    val result = templateSearch(id)
    Response.ok(result).build()
  }

  @GET
  @Path("-/suggest/{input}")
  def getItems(
                @PathParam("input") input: String,
                @Context headers: HttpHeaders
                ) = {
    val locale = if (headers.getLanguage() == null) Locale.US else headers.getLanguage()
    val result = templateSearch(input)
    Response.ok(result).build()
  }
}