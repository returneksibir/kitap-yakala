package kitapyakala.spiders

import yakala.spiders.Spider
import yakala.logging.Logger
import org.jsoup.nodes.Document


abstract class BaseBookSpider(logger : Logger) extends Spider {

  def pricePath()    : String
  def pricePattern() : scala.util.matching.Regex
  def isbnPath()     : String
  def isbnPattern()  : scala.util.matching.Regex
  def storeID()      : Int
  
  def processItem(doc : Document) : Map[String, String] = {
    val title    = doc.title();
    logger.info("------- " + title + " -------")
    try {
      var price = ""
      var isbn  = ""
      try {
        val bookPrice = doc.select(pricePath).first().text().trim()
        logger.debug("Price = " + bookPrice)
        val PRICE_PATTERN = pricePattern
        val PRICE_PATTERN(price_) = bookPrice
        price = price_
      } catch {
        case e : MatchError => throw new Exception("Improper price information")
      }
      try {
        val bookIsbn = doc.select(isbnPath).first().text().trim()
        logger.debug("ISBN = " + bookIsbn)
        val ISBN_PATTERN = isbnPattern
        val ISBN_PATTERN(isbn_) = bookIsbn
        isbn = isbn_
      } catch {
        case e : MatchError => throw new Exception("Improper ISBN information")
      }
      Map("price" ->  price, "isbn"  ->  isbn, "storeID" ->  storeID.toString())
    } catch {
      case e : NullPointerException => throw new Exception("Düzgün biçimli kitap bilgisi bulunamadı.")
    }
  }
}

