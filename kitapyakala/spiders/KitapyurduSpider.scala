package kitapyakala.spiders

import yakala.spiders.Spider
import yakala.logging.Logger
import org.jsoup.nodes.Document


class KitapyurduSpider(logger : Logger) extends Spider {
  private val DOMAIN_NAME         = "kitapyurdu.com"
  private val START_URL           = "http://www.kitapyurdu.com/kitap/default.asp?id=585237"
  private val STORE_ID            = 3
  private val BOOK_PAGE_PATTERN   = """.*/kitap/default\.asp\?id=(.*)""".r
  private val BOOK_PRICE_PATTERN  = """.* Kitapyurdu Fiyatı: (\S+).{2}TL\. .*""".r
  private val BOOK_ISBN_PATTERN   = """.* ISBN:(\S+) .*""".r

  def productPagePattern()  : util.matching.Regex = BOOK_PAGE_PATTERN
  def domainName()  : String = DOMAIN_NAME
  def startURL()    : String = START_URL
  
  def processItem(doc : Document) : Map[String, String] = {
    val title    = doc.title();
    logger.info("------- " + title + " -------")
    val docText  = doc.text() 
    try {
      var BOOK_ISBN_PATTERN(isbn)   = docText
      var BOOK_PRICE_PATTERN(price) = docText
      price  = price.replace(",", ".")
      val len = isbn.length()
      isbn = if (len < 10) isbn else isbn.substring(len-10, len-1)
      Map("price" ->  price, "isbn"  ->  isbn, "storeID" ->  STORE_ID.toString())
    } catch {
      case e : NullPointerException => throw new Exception("Düzgün biçimli kitap bilgisi bulunamadı.")
      case e : MatchError           => throw new Exception("Price information is not in TL")
    }
  }
}

