
+++
date = "2017-06-29T18:54:24+02:00"
draft = true
share = true
title = "Real estate ads scraping with Scala and Akka #1 - Proof of concept"
slug = "real-estate-prices"
tags = ['scala', 'data', 'devops', 'docker']
banner = ""
aliases = ['/rt-prices/']
+++

## Introduction

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
- Docker - I want application to run smoothly and automated. I have some prior experience with it - might just use it


#### Legal corner

Before scraping websites I advise to read their ToS on their policy of scraping. Unless you are doing it for commercial purposes - owners will usually be okay with it. Be sure to be polite to the website (not hit it with a lot of requests). Web is an open space - unless you "accidentally DOS" or steal content you will be fine.

Website to "mine" on - [aruodas.lt](aruodas.lt) - popular real estate website in Lithuania. Surely I am not planning to share mined raw data with anyone, sell it or keep for a long time. After contacting their administration they were nice enough to give me a permit (on my IP address) to scrape their ads. Thanks a lot!

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
