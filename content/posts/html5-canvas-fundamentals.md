
---
date : "2017-01-15"
draft : false
title : "HTML5 Canvas. Fundamentals"
slug : "html5-canvas-fundamentals"
tags : ['Learning', 'Frontend']
banner : ""
aliases : ['/html5-canvas-fundamentals/']
menu:
    main:
        parent: 'frontend'
---

#### Introduction

Today I met HTML5 Canvas. Not in real world - but during one the exercises from [Javascript30](https://benetis.me/vanilla-javascript-challenge/). What are those "canvas"?

#### Basics

Canvas is a HTML element used to draw graphics on fly via Javascript - [0].
Main idea is that you have this rectangle box on the page without any content and you use javascript to draw stuff on it. You do drawing by interacting with context object not the actual canvas object. Example below:
```javascript
const canvas = document.querySelector('#myCanvas');
const ctx = canvas.getContext('2d');

ctx.moveTo(50,50);
ctx.lineTo(150,100);
ctx.stroke();
```
Outputs a simple line. You can check and experiment [here](http://www.w3schools.com/html/tryit.asp?filename=tryhtml5_canvas_tut_path)

##### Our task from Javascript30

Create this simple drawing tool where you press mouse keys and it draws different color trail.

![](/images/2017/02/giphy.gif)

First task - we want to take our canvas element and set its width and height to window size.

```html
<canvas id="draw" width="800" height="800"></canvas>
```
```javascript
  const canvas = document.querySelector('#draw')
  const ctx = canvas.getContext('2d')


  canvas.width = window.innerWidth
  canvas.height = window.innerHeight

```

Next we want to catch mouse event needed to paint our beautiful rainbow. The thing is - we only want to paint when mouse is clicked.

```javascript
  let drawing = false;

  function draw(e) {
      if(drawing) {
          console.log(e);
      }
  }

  canvas.addEventListener('mousemove', draw);
  canvas.addEventListener('mousedown', e => drawing = true)
  canvas.addEventListener('mouseout', e => drawing = false)
  canvas.addEventListener('mouseup', e => drawing = false)
```

We add `mousemove` listener to call draw function on each mouse movement, but we check if `drawing` variables is true. Variable is to to true when `mousedown` or `mouseout` event happens. In draw function we 'bounce' calls if `drawing` is set to false

Moving on. Drawing the actual line. We need the point FROM and point TO for line to appear. Let's add a new object `drawFrom` which will hold our point with x and y coordinates where we want our line to being

```javascript
    let drawFrom = {x: 0, y: 0}
    function draw(e) {
        if (drawing) {
            ctx.beginPath()
            ctx.moveTo(drawFrom.x, drawFrom.y)
            ctx.lineTo(e.offsetX, e.offsetY)
            ctx.stroke() //drawing wont happen until we call stroke
            drawFrom.x = e.offsetX //we update our point
            drawFrom.y = e.offsetY
        }
    }
```

and of course when we click down we need to update our point as well

```javascript
    canvas.addEventListener('mousedown', e => {
        drawing = true
        drawFrom.x = e.offsetX
        drawFrom.y = e.offsetY
    })
```

Result:

![](/images/2017/02/giphy--1-.gif)

Next step - hue rainbows.

After setting canvas size let's include basic styles for our stroke.

```javascript
    ctx.lineWidth = 50
    ctx.lineJoin = 'round' //makes our point round
    ctx.lineCap = 'round'
```

For hue rainbows - we can use and abuse HSL. More on HSL [here](https://css-tricks.com/yay-for-hsla/)

Adding this line after `ctx.beginPath()` will result in drawing gummy bear worms.
```javascript   
    ctx.strokeStyle = `hsl(${e.offsetX % 360}, 100%, 50%)`
```

![](/images/2017/02/giphy--2-.gif)

If you want - you can adjust width and count hue differently like it is shown in challenge, but IMHO this is enough for understanding the very very basics of HTML5 canvas.

Code is in github - [here](https://github.com/benetis/JavaScript30/blob/608928a47a6b1c6f54a9fa99118f5a3f6f605bdf/08%20-%20Fun%20with%20HTML5%20Canvas/index-START.html)

[0] - http://www.w3schools.com/html/html5_canvas.asp

### Feedback

If you have any suggestions - I am eagerly waiting for feedback. [https://benetis.me/posts/contact-me/](/posts/contact-me/)
