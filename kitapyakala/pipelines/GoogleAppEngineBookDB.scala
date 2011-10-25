package kitapyakala.pipelines

import yakala.pipelines.ItemPipeline
import yakala.logging.Logger
import java.net.URLEncoder
import org.jsoup.Jsoup

class GoogleAppEngineBookDB(logger : Logger) extends ItemPipeline {

  val BOOK_SERVICE_ADDRESS = "http://rimbiskitapsever.appspot.com/book"
  //val BOOK_SERVICE_ADDRESS = "http://localhost:8080/book"

  var numberOfProcessedItems : Long = 0;
  var numberOfExceptions : Long = 0;
  
  def processItem(item : Map[String, String]) {
    item.foreach{case (key, value) => logger.info(key + "\t:" + value)}

    val price = item("price").replace(",", ".")
    var isbn  = item("isbn").replace("-", "")
    val len = isbn.length()
    isbn = if (len < 10) isbn else isbn.substring(len-10, len-1)

    val urlParameters = "isbn=" + isbn + "&price=" + price + "&link=" + URLEncoder.encode(item("url"), "UTF-8") + "&store=" + item("storeID");
    val url = BOOK_SERVICE_ADDRESS + "?" + urlParameters
    logger.debug("Connecting to " + url)
    try {
      Jsoup.connect(url).execute()
      numberOfProcessedItems += 1
    } catch {
      case e  =>
        numberOfExceptions += 1
        logger.debug("Exception :" + e.getMessage())
    }
  }
  
  def printStats(print : String => Unit) {
    print("\n# messages in mailbox = " + this.mailboxSize)
    print("\n# processed items = " + numberOfProcessedItems)
    print("\n# exceptions = " + numberOfExceptions)
  }
}

