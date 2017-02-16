
+++
date = "2017-02-15"
draft = true
title = "Becoming the one with flexbox: Intro to handling the monster"
slug = "taming-flexbox-intro-to-handling-the-monster-2"
tags = []
banner = ""
aliases = ['/taming-flexbox-intro-to-handling-the-monster-2/']
+++

### What is flexbox?

Also known as "Flexible box" is layout mode in CSS. Main purpose of it is to be predictable when coding responsive layouts [1].  Solves some of the major problems we have been complaining as web developers [2].

### Flexbox concepts

MDN has wrote this perfectly. Just read it slowly.

> The defining aspect of the flex layout is the ability to alter its items' width and/or height to best fit in the available space on any display device. A flex container expands items to fill available free space, or shrinks them to prevent overflow.

>The flexbox layout algorithm is direction-agnostic as opposed to the block layout, which is vertically-biased, or the inline layout, which is horizontally-biased.

### Real world case

The notorious sticky footer. Ahh, the pain of developing it in CSS. You don't its height and you want it to be responsive. And it is such a common case - its crazy.

- Negative margins (will never be perfect)
- JS layout resize (UI presentation using JS sucks)



### When to avoid flexbox

- For page layouts. Flexbox depends on its content which means using it for grids can be unpredictable [3]

### Browser support

Supported in major browsers + two versions back. Unless you specifically have to support old ones - no reason why you should hold back from using it.

Up to date link - [caniuse.com](http://caniuse.com/#feat=flexbox)

![Browser support table](/images/2017/02/Screen-Shot-2017-02-15-at-07.55.58.png)


## Sources
[1] - [https://developer.mozilla.org/en-US/docs/Web/CSS/CSS_Flexible_Box_Layout/Using_CSS_flexible_boxes](https://developer.mozilla.org/en-US/docs/Web/CSS/CSS_Flexible_Box_Layout/Using_CSS_flexible_boxes)

[2] - https://philipwalton.github.io/solved-by-flexbox/

[3] - https://jakearchibald.com/2014/dont-use-flexbox-for-page-layout/
