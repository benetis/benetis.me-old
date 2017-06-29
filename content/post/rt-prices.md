
+++
date = "2017-06-29T18:54:24+02:00"
draft = true
share = true
title = "Real estate ads mining with Scala, Akka and InfluxDB #1 - Proof of concept"
slug = "real-estate-prices"
tags = ['scala', 'influxdb', 'data-science', 'devops', 'docker']
banner = ""
aliases = ['/rt-prices/']
+++

## Introduction

A question came to mind - can I crawl through a real estate website, gather some data and infer something from that data. This is also becoming relevant to me as 23 year old.

What experience I want to get doing this side project:

- Data mining (common problems, ...)
- Concurrency while data mining (Akka Aktors)
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
- InfluxDB - I lack experience with noSQL databases, especially "exotic" ones and this one looks interesting to me due some previous experience building IoT weather station (for bachelor thesis)
- Docker - I want application to run smoothly and automated. I have some prior experience with it - might just use it


#### Legal corner

Before scraping websites I advise to read their ToS on their policy of scraping. Unless you are doing it for commercial purposes - owners will usually be okay with it. Be sure to be polite to the website (not hit it with a lot of requests). Web is an open space - unless you "accidentally DOS" or steal content you will be fine.

Website to "mine" on - [aruodas.lt](aruodas.lt) - popular real estate website in Lithuania. Surely I am not planning to share mined raw data with anyone, sell it or keep for a long time. After contacting their administration they were nice enough to give me a permit (on my IP address) to scrape their ads. Thanks a lot!
