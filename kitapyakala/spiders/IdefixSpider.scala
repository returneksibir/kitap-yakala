package kitapyakala.spiders

import yakala.logging.Logger


class IdefixSpider(logger : Logger) extends BaseBookSpider(logger) {

  def domainName()          : String                    = "idefix.com"
  def startURL()            : String                    = "http://www.idefix.com/kitap/"
  def productPagePattern()  : util.matching.Regex       = """.*/kitap/(.*)/tanim\.asp\?sid=.*""".r
  def followRulePattern()   : util.matching.Regex       = """(.*/kitap/.*)""".r
  def pricePath()           : String                    = "table#fiyattbl"
  def pricePattern()        : util.matching.Regex       = """.*idefix Fiyatı : (\S+) TL .*""".r
  def isbnPath()            : String                    = "div.tTextPad"
  def isbnPattern()         : util.matching.Regex       = """.*ISBN : (\S+).*""".r
  def storeID()             : Int                       = 2
  
}

