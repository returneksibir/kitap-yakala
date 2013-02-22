package kitapyakala.spiders

import yakala.logging.Logger


class PandoraSpider(logger : Logger) extends BaseBookSpider(logger) {

  def productPagePattern()  : util.matching.Regex       = """.*/urun/(.*)/.*""".r
  def followRulePattern()   : util.matching.Regex       = """(.*)""".r
  def domainName()          : String                    = "pandora.com.tr"
  def startURL()            : String                    = "http://www.pandora.com.tr"
  def pricePath()           : String                    = "div.etiket"
  def pricePattern()        : scala.util.matching.Regex = """.* fiyatı: (\S+) TL""".r
  def isbnPath()            : String                    = "span#CphOrta_LabelIsbn"
  def isbnPattern()         : scala.util.matching.Regex = """Isbn: (.*)""".r
  def storeID()             : Int                       = 4
  
}

