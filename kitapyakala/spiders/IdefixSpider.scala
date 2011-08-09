package kitapyakala.spiders

import yakala.spiders.Spider
import yakala.logging.Logger
import org.jsoup.nodes.Document


class IdefixSpider(logger : Logger) extends Spider {
  private val DOMAIN_NAME         = "idefix.com"
  private val START_URL           = "http://www.idefix.com/kitap/"
  private val BOOK_PRICE_PATH     = "b.pricerange"
  private val BOOK_ISBN_PATH      = "div#tanitimbox.disableSelection"
  private val STORE_ID            = 2
  private val BOOK_PAGE_PATTERN   = "http://www.idefix.com/kitap/"
  private val BOOK_PRICE_PATTERN  = """.{2}(\S+) TL.*""".r
  private val BOOK_ISBN_PATTERN   = """.* ISBN : (\S+) .*""".r

  def isProductPage(pageUrl : String) : Boolean = { pageUrl.startsWith(BOOK_PAGE_PATTERN) }
  def domainName()  : String = DOMAIN_NAME
  def startURL()    : String = START_URL
  
  def processItem(doc : Document) : Map[String, String] = {
    val title    = doc.title();
    logger.info("------- " + title + " -------")
    try {
      val bookPrice = doc.select(BOOK_PRICE_PATH).first().text().trim().replace(",", ".")
      var bookIsbn  = doc.select(BOOK_ISBN_PATH).first().text().trim().replace("-", "")
      var BOOK_ISBN_PATTERN(isbn)   = bookIsbn
      val BOOK_PRICE_PATTERN(price) = bookPrice
      val len = isbn.length()
      isbn = if (len < 10) isbn else isbn.substring(len-10, len-1)
      Map("price" ->  price, "isbn"  ->  isbn, "storeID" ->  STORE_ID.toString())
    } catch {
      case e : NullPointerException => throw new Exception("Düzgün biçimli kitap bilgisi bulunamadı.")
      case e : MatchError           => throw new Exception("Price information is not in TL")
    }
  }
}

