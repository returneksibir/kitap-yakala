package kitapyakala.spiders

import yakala.spiders.Spider
import yakala.logging.Logger
import org.jsoup.nodes.Document


class IlknoktaSpider(logger : Logger) extends Spider {
  private val STORE_ID            = 6
  private val BOOK_PRICE_PATTERN  = """.*ilknokta : (\S+) TL.*""".r
  private val BOOK_PRICE_PATTERN2 = """.*Fiyatı : (\S+) TL.*""".r
  private val BOOK_ISBN_PATTERN   = """.*ISBN: (\S+).*""".r

  def domainName()          : String              = "ilknokta.com"
  def startURL()            : String              = "http://www.ilknokta.com/"
  def productPagePattern()  : util.matching.Regex = """.*/kitap/(\d+)/.*""".r
  def followRulePattern()   : util.matching.Regex = """(.*)""".r
  
  def processItem(doc : Document) : Map[String, String] = {
    val title    = doc.title();
    logger.info("------- " + title + " -------")

    val docText  = doc.text() 

    var price = ""
    docText match {
      case BOOK_PRICE_PATTERN(price_)   => price = price_
      case BOOK_PRICE_PATTERN2(price_)  => price = price_
      case _ => throw new Exception("Price information is not in TL")
    }

    var isbn = ""
    docText match {
      case BOOK_ISBN_PATTERN(isbn_)   => isbn = isbn_
      case _ => throw new Exception("Improper ISBN information")
    }

    logger.info("Price = " + price + "; ISBN = " + isbn)
    Map("price" ->  price, "isbn"  ->  isbn, "storeID" ->  STORE_ID.toString())
  }
}

