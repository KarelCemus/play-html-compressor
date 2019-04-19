/**
 * Play HTML Compressor
 *
 * LICENSE
 *
 * This source file is subject to the new BSD license that is bundled
 * with this package in the file LICENSE.md.
 * It is also available through the world-wide-web at this URL:
 * https://github.com/mohiva/play-html-compressor/blob/master/LICENSE.md
 */
package com.mohiva.play.xmlcompressor.fixtures

import play.api.OptionalDevContext
import play.api.http._
import play.api.mvc._
import play.api.routing.Router
import play.core.WebCommands

import controllers.AssetsMetadata
import javax.inject.Inject

/**
 * Request handler which defines the routes for the tests.
 */
class RequestHandler @Inject() (
  webCommands: WebCommands,
  optDevContext: OptionalDevContext,
  router: Router,
  errorHandler: HttpErrorHandler,
  configuration: HttpConfiguration,
  filters: HttpFilters,
  components: ControllerComponents,
  meta: AssetsMetadata
)
  extends DefaultHttpRequestHandler(webCommands, optDevContext.devContext, router, errorHandler, configuration, filters.filters) {

  /**
   * Specify custom routes for this test.
   *
   * @param request The HTTP request header.
   * @return An action to handle this request.
   */
  override def routeRequest(request: RequestHeader): Option[Handler] = {
    lazy val controller = new TestController(components, meta)
    (request.method, request.path) match {
      case ("GET", "/action") => Some(controller.action)
      case ("GET", "/asyncAction") => Some(controller.asyncAction)
      case ("GET", "/nonXML") => Some(controller.nonXML)
      case ("GET", "/static") => Some(controller.staticAsset)
      case ("GET", "/chunked") => Some(controller.chunked)
      case ("GET", "/gzipped") => Some(controller.gzipped)
      case _ => None
    }
  }
}
