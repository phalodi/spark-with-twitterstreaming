import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.spark.streaming._
import org.apache.spark.streaming.twitter._
import org.apache.spark.streaming.StreamingContext._

object Tutorial extends App{
  val conf=new SparkConf().setAppName("myStream").setMaster("local[2]")
  val sc=new SparkContext(conf)
  val ssc = new StreamingContext(sc, Seconds(2))
  val client=new twitterclient()
  val tweetauth=client.start()
  val inputDstream=TwitterUtils.createStream(ssc, Option(tweetauth.getAuthorization))


  val statuses= inputDstream.map { x => x.getText }
  val lines=statuses.flatMap { x => x.split("\n") }
  val words=statuses.flatMap { x => x.split(" ") }
  val hastag=words.filter { x => x.contains("#")}
  hastag.saveAsTextFiles("tweets")
  ssc.start()             
  ssc.awaitTermination()

}
