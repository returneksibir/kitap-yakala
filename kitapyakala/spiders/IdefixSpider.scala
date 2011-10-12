package kitapyakala.spiders

import yakala.logging.Logger


class IdefixSpider(logger : Logger) extends BaseBookSpider(logger) {

  def domainName()          : String                    = "idefix.com"
  def startURL()            : String                    = "http://www.idefix.com/kitap/"
  def productPagePattern()  : util.matching.Regex       = """.*/kitap/(.*)/tanim\.asp\?sid=.*""".r
  def pricePath()           : String                    = "b.pricerange"
  def pricePattern()        : scala.util.matching.Regex = """.{2}(\S+) TL.*""".r
  def isbnPath()            : String                    = "div#tanitimbox.disableSelection"
  def isbnPattern()         : scala.util.matching.Regex = """.*(?:ISBN : )(\S+).*""".r
  def storeID()             : Int                       = 2
  
}

