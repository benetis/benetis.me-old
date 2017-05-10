
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

## Planning

Quick mockup on how I imagine this will look like. Probably best starting place for any application which can be componetized.

![](/images/2017/05/points-mock.png)
