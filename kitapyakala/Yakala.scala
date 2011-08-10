package kitapyakala

import kitapyakala.pipelines._
import kitapyakala.spiders._
import yakala.crawler._
import yakala.logging._

object Yakala {
  def main(args : Array[String]) {

    val logger = new ConsoleLogger()
    logger.setLogLevel(Logger.LOG_INFO)

    val pipeline = new GoogleAppEngineBookDB(logger)

    var spiders = List(
      new PandoraSpider(logger),
      new IdefixSpider(logger),
      new ImgeSpider(logger) )

    val crawler = new Crawler(logger, pipeline)
    crawler.start

    args.foreach{ domainName =>
      spiders.foreach{ spider =>
        if (spider.domainName == domainName)
        {
          spider.start
          crawler ! (spider, spider.startURL)
        }
      }
    }
  }
}
