package kitapyakala

import kitapyakala.pipelines._
import kitapyakala.spiders._
import yakala.crawler._
import yakala.logging._

object Yakala {
  def main(args : Array[String]) {

    val url = args(0)
    val logger = new ConsoleLogger()
    logger.setLogLevel(Logger.LOG_DEBUG)

    val spiders = List( new PandoraSpider(logger), new ImgeSpider(logger) )
    val pipeline = new GoogleAppEngineBookDB(logger)
    val crawler = new Crawler(logger)

    args.foreach(crawler ! _)
  }
}
