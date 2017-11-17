
---
date : "2016-12-18"
draft : false
title : "A quick view at CSS Variables (Custom properties)"
slug : "css-variables-custom-properties-a-short-glimpse-2"
tags : ['Frontend']
banner : ""
aliases : ['/css-variables-custom-properties-a-short-glimpse-2/']
menu:
    main:
        parent: 'frontend'
---

##### Introduction

For quite some time we had variables in CSS with the help of SASS/LESS. Variables are useful, but they come with a drawback - you cannot use them in runtime. Well that time has ended - CSS variables are landing in new browsers.

![can i use table for css variables](/images/2017/02/Screen_Shot_2016_12_18_at_21_02_47.png)
[screen taken - 2016-12-20, you can view up to date table here [here](http://caniuse.com/#feat=css-variables)]


Caniuse table shows that major browsers aside Edge already support


##### Syntax

```css
:root {
  --header-color: #06c;
}

#foo h1 {
  color: var(--header-color);
}
```
example taken from [[0]](https://developers.google.com/web/updates/2016/02/css-variables-why-should-you-care)

Variables have two dashes `--my-var` before them and you can choose a name for them of your liking. To use variables you will need to use `var` keyword as in `var(--my-var)`

Well you are probably thinking - damn that is one ugly syntax! Where is my `$foo?` Well CSSWG wanted to leave this notation for some cool feature in futurre. More about it here [[3]](http://www.xanthir.com/blog/b4KT0)

##### Behavior

You probably noticed `:root` selector in the syntax example. It assigns our custom named property `--header-color` to root element with value of `#06c`. Our defined property will be inherited to the rest of document elements

##### Example

Taken from [Javascript30 challenge](https://benetis.me/vanilla-javascript-challenge/) challenge - CSS Custom properties with sliders example.

Below you can see how moving a slider sets CSS property and CSS automatically reacts

Setting CSS property with Javascript
```javascript
document.documentElement.style.setProperty(`--header-color`, `red`);

```

![](/images/2017/02/3o6ZsSmLsROdCXMtws.gif)

Working code example - [github](https://github.com/benetis/Javascript30_Challenge/blob/master/03%20-%20CSS%20Variables)

##### More use cases

###### 1

Separating strings in internationalisation

```css
:root:lang(en) {--external-link: "external link";}
:root:lang(de) {--external-link: "externer Link";}

a[href^="http"]::after {content: " (" var(--external-link) ")"}
```
taken from [2]

###### 2

`calc` function can also be tied neatly

```css
--one: calc(var(--two) + 20px);
```


##### Summarry

Custom properties are quite cool - although we cannot throw SASS/Less in favour of them because lack of support of other features - mixins, nesting. Browser support is also still behind. All in all, still an interesting thing to try out. I bet in year or two we will be using them daily

### Feedback

If you have any suggestions - I am eagerly waiting for feedback. [https://benetis.me/posts/contact-me/](/posts/contact-me/)

### Sources

- [0] - https://developers.google.com/web/updates/2016/02/css-variables-why-should-you-care
- [1] - http://caniuse.com
- [2] - https://www.w3.org/TR/css-variables/
- [3] - http://www.xanthir.com/blog/b4KT0
