
+++
date = "2017-08-06T18:54:24+02:00"
draft = false
share = true
title = "Real estate ads scraping with Scala and Akka #1 - Proof of concept"
slug = "real-estate-prices"
tags = ['scala', 'data', 'mysql', 'docker']
banner = ""
aliases = ['/rt-prices/']
+++

## Introduction

Disclaimer: Not an expert in scala world - any feedback is appreciated.

A question came to mind - can I crawl through a real estate website, gather some data and infer something from that data. This is also becoming relevant and interesting to me.

What experience I want to get doing this side project:

- Data scraping (common problems, ...)
- Concurrency while analyzing, gathering data (Akka Aktors)
- Data analysis, statistical inference (Knowing what to query and what results mean)
- Data visualization (how to display data in a meaningful way)
- DevOps experience (long running app: deploy, updates, backups, ...)

## Planning phase

How I imagine this little spider would work:

- Spin up scala service
- Start Supervisor with instructions to crawl specific category of real estate (example: apartment/flat rents)
- This Supervisor spawns some spiders for different RT (real estate) websites
- Spider visits website, gets raw data, mines it, passes it to store
- Store saves it into database

![](/images/2017/07/rt-prices-plan.png)

Surely we could easily expand this to support multiple websites, but for the sake of simplicity will stick to one.

Talking about technology stack.

- Scala, Akka - a language I admire
~~- InfluxDB - Interesting database for monitoring real time data - want to try out~~
- MySQL - battle tested DB, our scraper will not "scale" that much to start encountering lock problems
- ~~Docker - I want application to run smoothly and automated. I have some prior experience with it - might just use it~~ Can't use it due that fact that 32bit platforms are not supported by docker engine. You might ask, but why 32bit? Its just that I got this old laptop laying around which is begging to be resurrected.


#### Legal corner

Before scraping websites I advise to read their ToS on their policy of scraping. Unless you are doing it for commercial purposes - owners will usually be okay with it. Be sure to be polite to the website (not hit it with a lot of requests). Web is an open space - unless you "accidentally DOS" or steal content you will be fine.

Website to "mine" on - [aruodas.lt](aruodas.lt) - popular real estate website in Lithuania. Surely I am not planning to share mined raw data with anyone, sell it or keep for a long time. After contacting their administration they were nice enough to give me a permit (on my IP address) to scrape their ads. ~~Thanks a lot!~~ They did ban my IP - breaking their word.

## Choice of database

Initial plan was to try out influxdb, grafana. While I certainly did that - I encountered some tooling problems, which I though will be easier to work around. Especially with influxdb. Tools are lacking to view data, CLI tools are not that good to view data or interact with it. Sure it works well with data monitoring of real time data, but it is not something I am looking for. Chronograf at this moment also doesn't seem to fit my use case. I bet for monitoring CPU it would be perfect, but for "long term" storage data - not worth the trouble. That being said - let's grab what we know will work - MySQL or this expirment will never get finished

## Getting stuff done

Let's start talking about classifier


Here you can find a 1000 meters view of app's architecture. You have r estate categories, real estate sites.

Categories classify different types of ads and sites distinguish between different sites we are scraping. Each category might have different implementation + each site for sure will have different implementation for how scrapers need to behave.

![](/images/2017/08/rt-categories-sites.png)

Also we have ad itself and its properties. As we are scraping only one website now - we will base it on it and extend as we go. Might happen that we need different ones for different categories, but no reason to overengineer at this moment.


RTDetails is just some sugar to have types for different ad properties. Not sure if this is a good pattern at this moment - not going to go into detail. (pun not intended)

```scala
package rt

trait RTCategory {
}

trait RTSite {
  def categoryId(category: RTCategory): String
}

case class RTFlatsRent() extends RTCategory

case class RTFlatsSell() extends RTCategory

case class RTHousesSell() extends RTCategory

case class RTAruodas() extends RTSite {

  override def categoryId(category: RTCategory): String = category match {
    case RTFlatsRent() => "4"
    case RTFlatsSell() => "1"
    case RTHousesSell() => "2"
  }

}


case class RTItem(
                        itemId: Option[String],
                        url: Option[String],
                        price: Option[Double],
                        pricePerMeter: Option[Double],
                        area: Option[Double],
                        rooms: Option[Int],
                        floor: Option[Int],
                        numberOfFloors: Option[Int],
                        buildYear: Option[Int],
                        houseType: Option[String],
                        heatingSystem: Option[String],
                        equipment: Option[String],
                        shortDescription: Option[String],
                        comment: Option[String],
                        created: Option[String],
                        edited: Option[String],
                        interested: Option[String]
                      )

case class RTDetailsArea(value: Double) {
}

case class RTDetailsNumberOfRooms(value: Int)

case class RTDetailsFloor(value: Int)

case class RTDetailsNumberOfFloors(value: Int)

case class RTDetailsBuildYear(value: Int)

case class RTDetailsHouseType(value: String)

case class RTDetailsHeatingSystem(value: String)

case class RTDetailsEquipment(value: String)

case class RTDetailsShortDescription(value: String)
```

### Next up - Scraper and Supervisor
Two more things to talk about: scraper and supervisor

Scraper is pretty straightforward. Receive a message from Actor and start scraping it. Once we get what we need - pass message to store it into database. Nothing more at this moment. More complex composition/abstraction will need to land here later to fit in more sites and specific categories.

One thing I don't like about this "MVP" implementation is how parsedItemDetails are parsed with collectFirst by types. This makes my IDE slow due type checking. Some better data structure can be fit here - not to waste too much time here - let's move on.

p.s scraping done with the help of "scalascraper"

```java
package rt

import akka.actor.{Actor, ActorRef}
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Element
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.element

import scala.util.Try

class Scraper(supervisor: ActorRef) extends Actor {

  override def receive: Receive = {
    case ScrapDetails(url, rtSite, rtCategory) =>
      println("=== start scraping details ===")
      println(s"url = $url")
      sender() ! StoreDetails(scrapDetails(url, rtSite, rtCategory), rtSite, rtCategory)
  }

  def scrapDetails(url: String,
                   rTSite: RTSite,
                   rTCategory: RTCategory): RTItem = {
    val browser = new JsoupBrowser(
      "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")

    val doc = browser.get(url)

    println("=== [DETAILS] FLATS ===")
    val id = url.substring(url.length - 10, url.length - 1)

    val priceEleOpt = doc >?> element(".obj-price")

    priceEleOpt match { // if page exists
      case Some(priceEle) =>
        val (price, pricePerMeter) = parsePrice(priceEle)

        val comment = doc >> element(".obj-comment") >?> text

        val detailsTerms = doc >> elementList(".obj-details dt")
        val detailsItem = doc >> elementList(".obj-details dd")

        val itemDetails = detailsTerms.zip(detailsItem)

        lazy val parsedItemDetails: Seq[Any] = this.splitDetails(itemDetails)

        val stats = doc >> elementList(".obj-stats dl dd")

        val created = stats(1).text
        val edited = stats(2).text
        val interested = stats(3).text


        RTItem(
          Some(id)
          , Some(url)
          , price
          , pricePerMeter
          , parsedItemDetails.collectFirst { case d: RTDetailsArea => d.value }
          , parsedItemDetails.collectFirst { case d: RTDetailsNumberOfRooms => d.value }
          , parsedItemDetails.collectFirst { case d: RTDetailsFloor => d.value }
          , parsedItemDetails.collectFirst { case d: RTDetailsNumberOfFloors => d.value }
          , parsedItemDetails.collectFirst { case d: RTDetailsBuildYear => d.value }
          , parsedItemDetails.collectFirst { case d: RTDetailsHouseType => d.value }
          , parsedItemDetails.collectFirst { case d: RTDetailsHeatingSystem => d.value }
          , parsedItemDetails.collectFirst { case d: RTDetailsEquipment => d.value }
          , parsedItemDetails.collectFirst { case d: RTDetailsShortDescription => d.value }
          , comment
          , Some(created)
          , Some(edited)
          , Some(interested)
        )
      case None => RTItem(Some(id), None, None, None, None, None, None, None, None, None, None, None, None, None, None, None, None)
    }


  }

  private def parsePrice(price: => Element)
  : (Option[Double], Option[Double]) = {

    def extractPricePerMeter(priceWithoutAdvert: Option[String]): Option[Double] = {

      val capturePricePerMeterWithWhiteSpace = "\\([0-9 ]+".r

      val matched = capturePricePerMeterWithWhiteSpace
        .findFirstIn(priceWithoutAdvert.getOrElse("-1"))

      matched
        .map(_.replace(" ", ""))
        .map(_.replace("(", ""))
        .map(_.toDouble)

    }

    def extractPriceWithoutCurrency(priceWithoutAdvert: Option[String]): Option[Double] = {
      val capturePriceWithoutCurrencyWithWhiteSpace = "^\\s?[0-9 ]+".r

      val matched = capturePriceWithoutCurrencyWithWhiteSpace
        .findFirstIn(priceWithoutAdvert.getOrElse("-1"))

      matched
        .map(_.replace(" ", ""))
        .map(_.toDouble)
    }

    def extractPriceWithoutAdvert: Option[String] = {
      val priceAdvert: Option[String] = price >?> element(".price-change") match {
        case Some(ele: Element) => ele >?> text
        case None => None
      }

      val rawPriceOpt = price >?> text

      rawPriceOpt match {
        case Some(rawPrice: String) => priceAdvert match {
          case Some(advert: String) => Some(rawPrice.replace(advert, ""))
          case None => Some(rawPrice)
        }
        case None => None
      }
    }

    val priceWithoutAdvert = extractPriceWithoutAdvert

    val pricePerMeter = extractPricePerMeter(priceWithoutAdvert)

    val priceWithoutCurrency = extractPriceWithoutCurrency(priceWithoutAdvert)

    (
      priceWithoutCurrency,
      pricePerMeter
    )
  }

  private def splitDetails(details: Seq[(Element, Element)]): Seq[Any]

  = {
    lazy val _details = details.map({
      case (term, item) => term.text match {
        case "Area (m²):" => convertAreaToDouble(item.text)
        case "Number of rooms :" => RTDetailsNumberOfRooms(item.text.toInt)
        case "Floor:" => RTDetailsFloor(item.text.toInt)
        case "No. of floors:" => RTDetailsNumberOfRooms(item.text.toInt)
        case "Build year:" => RTDetailsBuildYear(item.text.toInt)
        case "House Type:" => RTDetailsHouseType(item.text)
        case "Heating system:" => RTDetailsHeatingSystem(item.text)
        case "Equipment:" => RTDetailsEquipment(item.text)
        case "Description:" => RTDetailsShortDescription(item.text)
        case _ => 1.0d
      }
    })

    def convertAreaToDouble(area: String): Option[RTDetailsArea] = {
      Some({
        val areaStr = area
          .dropRight(3) // drop m^2
          .replace(",", ".") //to dots notation

        if (areaStr.isEmpty)
          RTDetailsArea(0.0d)
        else
          RTDetailsArea(areaStr.toDouble)
      })
    }

    _details
  }

}

```

Last piece of our proof concept - Supervisor.

Here you can inspect "communication" channels for our actors. Implementation is pretty straightforward without any edge case handling. Moreover, you have to handle ids yourself which kinda sucks, but we can improve on that later.

You can also inspect 2 second delay on our scraper - this is not to cause any performance problems for the website.

```scala
package rt

import akka.actor.{Actor, ActorRef, ActorSystem, _}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

case class StartDetails(rtSite: RTSite,
                        rTCategory: RTCategory)

case class StoreDetails(details: RTItem,
                        rtSite: RTSite,
                        rTCategory: RTCategory)

case class EndDetails()

case class ScrapDetails(url: String,
                        rtSite: RTSite,
                        rTCategory: RTCategory)

class Supervisor(system: ActorSystem) extends Actor {

  var host2Actor = Map.empty[String, ActorRef]

  val scrapers = system.actorOf(Props(new Scraper(self)))

  override def receive: Receive = {
    case StartDetails(rtSite: RTSite, rTCategory: RTCategory) => startDetails(rtSite, rTCategory)
    case StoreDetails(details: RTItem, rtSite: RTSite, rtCategory: RTCategory) =>
      storeDetails(details, rtSite, rtCategory)
    case EndDetails => ???
  }

  private def storeDetails(details: RTItem,
                           rtSite: RTSite,
                           rtCategory: RTCategory) = {
    Store.writeDetails(details, rtSite, rtCategory)
  }

  private def startDetails(site: RTSite,
                           category: RTCategory) = {

    var id = 2284843
    val last = 2271641
    system.scheduler.schedule(2 seconds, 2 seconds)({
      scrapers ! ScrapDetails(
        s"https://en.aruodas.lt/1-$id/",
        site,
        category
      )
      id = id + 1
    })

  }


}

```

### "Deployment" step

Next - preparing for "deployment". We will be putting into a an old laptop behind the tv, in private home network. We need to generate a JAR file with all dependencies. For that - `sbt-assembly` comes to help. Was a bit tough to set it up - but seems to work well.

Configure mysql users.

More on how to - [https://github.com/sbt/sbt-assembly](https://github.com/sbt/sbt-assembly)

Copy everything to the server with `scp` and run `java -jar rt-pricer-assembly.jar`

You can find code here - [https://github.com/benetis/rt-pricer](https://github.com/benetis/rt-pricer)

That's it!

### Results

A gif how new rows appear in database after code with this specific revision.

![](/images/2017/08/rt-pricer-rows-adding.gif)

[Code revision](https://github.com/benetis/rt-pricer/tree/7b1f783ea94aca5bcaa65e5dc8cb97ff2320e0de)


### Feedback

If you have any suggestions - I am eagerly waiting for feedback. [https://benetis.me/post/contact-me/](/post/contact-me/)
