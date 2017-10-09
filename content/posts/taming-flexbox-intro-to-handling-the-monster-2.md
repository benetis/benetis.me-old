---
date : "2017-02-21T21:43:46+02:00"
draft : false
title : "Becoming the one with flexbox: Intro to handling the monster"
slug : "taming-flexbox-intro-to-handling-the-monster-2"
tags : []
banner : ""
aliases : ['/taming-flexbox-intro-to-handling-the-monster-2/']
menu:
    main:
        parent: 'Web, Frontend and Client side'
---

### Learn more about it. But why?

Flexbox is becoming one of core concepts in CSS. Might as well get to know more about it. It's always a good investment to know basics well.
We will keep this as short as possible. No unneeded content, pictures and etc. Reading whole post should give you basics of it.

### What is flexbox?

Also known as "Flexible box" is layout mode in CSS. Main purpose of it is to be predictable when coding responsive layouts [1].  Solves some of the major problems we have been complaining as web developers [2].

### Flexbox concepts

MDN has wrote this perfectly. Just read it slowly.

> The defining aspect of the flex layout is the ability to alter its items' width and/or height to best fit in the available space on any display device. A flex container expands items to fill available free space, or shrinks them to prevent overflow.

>The flexbox layout algorithm is direction-agnostic as opposed to the block layout, which is vertically-biased, or the inline layout, which is horizontally-biased.

### Getting into flexbox

#### Container properties

Flexbox container has two main properties - main axis and cross axis

![flexbox](/images/2017/02/Screenshot_18_02_2017__20_49.png)

Both of them go across the whole container.

#### `justify-content`

For moving items along main axis there is property called `justify-content`

- Put items at start of main axis
    `justify-content: flex-start;`

- Put items at end of main axis
    `justify-content: flex-end;`

- Put items at center of main axis
    `justify-content: center;`

- Distribute evenly, but first and last items are at beginning and end
    `justify-content: space-between;`


Great - you are in a lot of power now. Just knowing these properties.

#### Ever heard of `flex-direction`?

Remember how we talked that there are two axis? Main and cross? So with `flex-direction` you can change which axis is main. As a bonus - you can also reverse that axis.

### `align-items`

As you probably guessed or already know - this property is for playing with cross axis.

Same as before:

- Put items at start of main axis
    `align-items: flex-start;`

- Put items at end of main axis
    `align-items: flex-end;`

- Put items at center of main axis
    `align-items: center;`

Additional ones:

- Stretch. Stretches the items to fit
    `align-items: stretch`

- Aligns to items baselines
    `align-items: baseline`

### `order`

Want some element appear before another one? `order: number` property is reporting for duty. You can specify it on flex-item. As a default - all flex-items start with `0`

### `align-self`

If you are looking how to align one element on cross axis? Just use `align-self`. Works with same values as `align-items`, just applied to one item. Overrides `align-items` behavior

### Real world case

#### The notorious sticky footer

Ahh, the pain of developing it in CSS. You don't its height and you want it to be responsive. And it is such a common case - its crazy.

- Negative margins (will never be perfect)
- JS layout resize (UI presentation using JS sucks)

Flexbox makes this thing possible without hacks. If you like to see how - read here [https://philipwalton.github.io/solved-by-flexbox/demos/sticky-footer/](https://philipwalton.github.io/solved-by-flexbox/demos/sticky-footer/)

More cases - you can find here - [https://philipwalton.github.io/solved-by-flexbox/demos](https://philipwalton.github.io/solved-by-flexbox/demos)

### When to avoid flexbox

- If you can use CSS grids - avoid using flexbox for page layouts. Flexbox depends on its content which means using it for grids can be unpredictable [3] + performance
- Don't use it everywhere. There are other CSS properties that might fit better.

### Browser support

Supported in major browsers + two versions back. Unless you specifically have to support old ones - no reason why you should hold back from using it.

Up to date link - [caniuse.com](http://caniuse.com/#feat=flexbox)

![Browser support table](/images/2017/02/Screen-Shot-2017-02-15-at-07.55.58.png)

### Learn more

A cool tutorial/game which introduces and lets you practice flexbox as you go - [flexboxdefense.com](http://www.flexboxdefense.com)

### Feedback

If you have any suggestions - I am eagerly waiting for feedback. [https://benetis.me/post/contact-me/](/post/contact-me/)

## Sources
[1] - [https://developer.mozilla.org/en-US/docs/Web/CSS/CSS_Flexible_Box_Layout/Using_CSS_flexible_boxes](https://developer.mozilla.org/en-US/docs/Web/CSS/CSS_Flexible_Box_Layout/Using_CSS_flexible_boxes)

[2] - https://philipwalton.github.io/solved-by-flexbox/

[3] - https://jakearchibald.com/2014/dont-use-flexbox-for-page-layout/
