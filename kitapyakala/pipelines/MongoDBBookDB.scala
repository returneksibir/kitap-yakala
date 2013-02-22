package kitapyakala.pipelines

import java.util.ArrayList

import yakala.pipelines.ItemPipeline
import yakala.logging.Logger
import com.mongodb.BasicDBObject
import com.mongodb.DB
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.mongodb.MongoClient


/**
 * @author cem
 *
 * A pipeline class which stores book entries in MongoDB
 */
class MongoDBBookDB(dbName : String, logger : Logger) extends ItemPipeline(logger) {

  private var mongoClient : MongoClient = new MongoClient();
  private val db : DB = mongoClient.getDB(dbName);
  private val coll : DBCollection = db.getCollection("Books");

  def processItem(item : Map[String, String]) {
    addBook(item("isbn"), item("price"), item("url"), item("storeID"))
  }

  def addBook(isbn : String, price : String, url : String, storeID : String) : Unit = {
    val book = getBook(isbn, storeID)

    if (book != null) {
      updateBook(book, price, url)
    } else {
      insertBook(isbn, price, url, storeID)
    }
  }

  private def getBook(isbn : String, storeID : String): DBObject = {
    val query : BasicDBObject = new BasicDBObject("isbn", isbn).
                              append("storeID", storeID)
    coll.findOne(query)
  }

  private def updateBook(book: com.mongodb.DBObject, price : String, url : String): Unit = {
    book.put("url", url)
    book.put("price", price)
    coll.save(book)
  }

  private def insertBook(isbn : String, price : String, url : String, storeID : String): Unit = {
    val book : BasicDBObject = new BasicDBObject("isbn", isbn).
                                append("price", price).
                                append("url", url).
                                append("storeID", storeID);

    coll.insert(book);
  }

  def getNumberOfBooks() : Long = {
    coll.count()
  }

  def clear() : Unit = {
    coll.drop()
  }
}
