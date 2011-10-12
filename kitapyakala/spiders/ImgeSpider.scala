package kitapyakala.spiders

import yakala.logging.Logger


class ImgeSpider(logger : Logger) extends BaseBookSpider(logger) {

  def productPagePattern()  : util.matching.Regex       = """.*/product_info\.php\?products_id=(.*)""".r
  def followRulePattern()   : util.matching.Regex       = """(.*)""".r
  def domainName()          : String                    = "imge.com.tr"
  def startURL()            : String                    = "http://www.imge.com.tr"
  def pricePath()           : String                    = "td.price"
  def pricePattern()        : scala.util.matching.Regex = """(?:.*Sitemizde \(KDV Dahil\): )?(\d+,\d{2})TL.*""".r
  def isbnPath()            : String                    = "table#ana_alan"
  def isbnPattern()         : scala.util.matching.Regex = """.* ISBN: (\S+) .*""".r
  def storeID()             : Int                       = 1 
  
}

