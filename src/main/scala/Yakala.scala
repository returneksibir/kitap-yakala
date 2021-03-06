package kitapyakala

import kitapyakala.pipelines._
import kitapyakala.spiders._
import yakala.crawler._
import yakala.logging._
import yakala.utils._
import java.util.Timer;
import java.util.TimerTask;

object Yakala {

  def createVisitedLinkSet() : SetTrait = {
//  new DefaultLinkSet()
    val falsePositiveProbability = 0.1
    val expectedSize = 500000
    new BloomFilterLinkSet(falsePositiveProbability, expectedSize)
  }

  def main(args : Array[String]) {

    val logger = new ConsoleLogger()
    logger.setLogLevel(Logger.LOG_INFO)

    val pipelines = List(
      new DummyBookDB(logger),
      new MongoDBBookDB("Kitapsever", logger) )

    pipelines.foreach {
      pipeline => pipeline.start
    }

    var spiders = List(
      new PandoraSpider(logger),
      new IdefixSpider(logger),
      new KitapyurduSpider(logger),
      new IlknoktaSpider(logger),
      new ImgeSpider(logger) )

    val visitedLinkSet = createVisitedLinkSet()

    val crawler = new Crawler(logger, pipelines, visitedLinkSet)
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
    
    val task = new TimerTask(){            
      def run(){
        val out = new java.io.FileWriter("Stats.txt")

        out write "\n\n--- Crawler ---"
        crawler printStats out.write

        out write "\n\n--- Pipeline ---"
        pipelines.foreach {
          pipeline => pipeline printStats out.write
        }

        out write "\n\n--- Spiders ---"
        spiders.foreach{ spider =>
          spider printStats out.write
        }

        out write "\n"
        out close
      }
    };
     
    val timer = new Timer();
    timer.schedule(task, 0, 5000);
  }
}
