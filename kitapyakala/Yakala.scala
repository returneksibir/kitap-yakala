package kitapyakala

import kitapyakala.pipelines._
import kitapyakala.spiders._
import yakala.crawler._
import yakala.logging._
import java.util.Timer;
import java.util.TimerTask;

object Yakala {
  def main(args : Array[String]) {

    val logger = new ConsoleLogger()
    logger.setLogLevel(Logger.LOG_INFO)

    val pipeline = new GoogleAppEngineBookDB(logger)
    pipeline.start

    var spiders = List(
      new PandoraSpider(logger),
      new IdefixSpider(logger),
      new KitapyurduSpider(logger),
      new IlknoktaSpider(logger),
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
    
    val task = new TimerTask(){            
      def run(){
        val out = new java.io.FileWriter("Stats.txt")
        out write "\n\n--- Crawler ---"
        crawler printStats out.write
        out write "\n\n--- Pipeline ---"
        pipeline printStats out.write
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
