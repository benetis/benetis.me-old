
---
date : "2016-12-04"
draft : false
title : "Let's code: Learning Haskell #1"
slug : "lets-code-learning-haskell-1"
tags : ['Learning', "Let's code"]
banner : ""
aliases : ['/lets-code-learning-haskell-1/']
menu : "main"
menu:
    main:
        parent: 'Functional programming and math'
---

#### Introduction

Last week has been very immutable for me. I was looking into redux, Elm, React - just the ideas and trying to implement something similar for Angular.

For few years I have had a keen interest into functional programming and considered myself a good in the field. You know - using higher order functions, understand why `null` is evil - so code produced by me was "fairly" functional. Avoiding state, writing functions as pure as possible. During the last year I upgraded my level to good novice by learning concepts of partials functions, functors. What is the next step? Tackle the real beast - learn the *haskell*. Baby steps.

#### Exercises

Let's start with Project Euler and go problem by problem googling haskell syntax and semantics on our way.


**Problem #1**

> If we list all the natural numbers below 10 that are multiples of 3 or 5, we get 3, 5, 6 and 9. The sum of these multiples is 23.
Find the sum of all the multiples of 3 or 5 below 1000.

Let's go step by step.

* take first 1000 of natural numbers  `[1..999]`
* filter the list to only have multiples of `3` or `5`

```haskell
disibleByThreeOrFive n =
    if n `mod` 3 == 0 || n `mod` 5 == 0 then
        True
    else
        False
```

```haskell
filter disibleByThreeOrFive [1..999]
```
which returns a list of all numbers we need to sum

* fold and sum them
```haskell
foldl (\res prev -> res + prev) 0 (filter disibleByThreeOrFive [1..999])
```
which returns `233168`. It is accepted by euler website - but the code we wrote could be improved.

First let's get replace `foldl` with `sum`

```haskell
sum (filter disibleByThreeOrFive [1..999])
```

Okay - those brackets are still there - good for us there is `$` operator which is exactly for that - getting rid of them. Anything that comes after dollar sign will take precedence.

```haskell
sum $ filter disibleByThreeOrFive [1..999]
```

Making it a one liner by extracting to lambda function

```haskell
sum $
      filter
       (\x -> x `mod` 3 == 0
       || x `mod` 5 == 0)
       [1..999]
```

#### Summary

Remembered a little bit of haskell syntax, tackled the new editor plugin change and looking forward to doing more exercises.

### Feedback

If you have any suggestions - I am eagerly waiting for feedback. [https://benetis.me/posts/contact-me/](/posts/contact-me/)
