
+++
date = "2017-05-10T23:00:24+02:00"
draft = false
share = true
title = "Let's code: Advanced CRUD in Angular with unit tests"
slug = "angular-advanced-crud"
tags = ['Angular', 'Frontend', 'Side-project', "Let's code"]
banner = ""
aliases = ['/angular-advanced-crud/']
+++

## Introduction

What does word advanced mean? It's just that the CRUD application we are going to implement will have some additional complexity you might expect:

- import data
- save/load chunks data
- lazy data processing
- pagination
- tests

## Requirements & Task itself

Aside of the stuff we mentioned above - we obviously will be implementing CRUD operations. Our model on which are going to operate is going to be points (coordinate points `{x, y}`).

- Input points by hand
- Import points from file
- Clear all points
- Points should be paginated, pagination size can change
- How many squares can be formed from these squares (lazy processing)
- Remove loaded points
- Save chunk of points with specific name (if name exists - overwrite)
- Load chunk of points from saved list
- If something happens (duplicate points ignored...) user needs to be informed
- Delete list of points

We will be using Angular 4.1 with CLI 1.0.1

## Planning

Quick mockup on how I imagine this will look like. Probably best starting place for any application which can be componetized.

![](/images/2017/05/points-mock.png)

Clearly we will need a good grid/table component. I have implemented tables so many times in Angular that this time I am just gonna skip it. There is even a post on Angular table by me - [[https://benetis.me/post/angular-table/](/post/angular-table/)

Tabs component to switch between points and squares. (Since squares need pagination) OR just put squares table below. This is a very possible solution since it let's to look at points and have squares "counting" at same time.

Action menu for actions to perform on selected rows of table

Validations for inputs to add to the table. We have limits

Research into square finding algorithm - but I guess we can leave that for later.

Import file and upload to table can be plugged anywhere

After retro - I have updated mockup with another possible solution:

![](/images/2017/05/updated-mockup.png)
