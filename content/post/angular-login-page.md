+++
date = "2017-04-16T10:54:24+02:00"
draft = true
share = true
title = "Let's code: Authentication in Angular #1 - creating login form"
slug = "angular-authentication"
tags = ['Angular', 'Frontend', 'Side-project', "Let's code"]
banner = ""
aliases = ['/angular-authentication/']
+++

### Introduction

Login form - a gate though which user logs into application. If it is working well - you don't notice it - if it is bad - it will always annoy you. Of course it is not only UX/UI problem - there are security implications on server side also. But we will keep these concerns beyond this post scope.

We will have few blog posts on authenticating Angular application. This one will focus on login form. Before diving into login form we will outline what we in a whole for authentication

The project to which we will add login form is a side project of mine. (anvilium.com)

### Plan for authentication

What we need for authentication to work:

I will highlight the points which will be covered in this post.

UX:

- **Our login page will consist of two tabs: login and registration**
- **For user to register we will only ask for email and password**
- If user is not authenticated - redirect him to login page
- **It should be easy to switch between login/register tabs**
- After registering - user needs to click on activation link in email

Technical:

- If user has expired token - try refreshing it (OAuth)
- If user has no token or refresh token failed - redirect him to login page (OAuth)
- We need to limit login attempts (server only)
- **Password strength - at least 8 symbols**
- User should activate new account in 24 hours before it expires

There is a great post on logins written by Jeff Atwood. We will use some ideas from that post in our login form. [https://blog.codinghorror.com/the-god-login/](https://blog.codinghorror.com/the-god-login/)

### Login form plan

Quick sketch before I dive into actual work. (Using draw.io)

![](/images/2017/04/anvilium-login.jpg)

Sorry for poor sketch, but ignoring that - this is how login screen should look. We will add some material look 'n feel to it.

Some considerations - we will need to display login form instead of whole application - it means route for login will need to be at app level. Also for us it means we can stick LoginForm into separate module.
