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

    args.foreach{ domainName =>
      println(domainName)
      spiders = spiders.filter{ _.domainName == domainName }
    }

    val crawler = new Crawler(logger, pipeline)
    crawler.start

    spiders.foreach{ spider =>
      spider.start
      crawler ! (spider, spider.startURL)}
  }
}
