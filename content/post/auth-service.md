+++
date = "2017-05-05T19:40:24+02:00"
draft = true
share = true
title = "Let's code: Authentication in Angular #2 - Auth service"
slug = "angular-authentication-service"
tags = ['Angular', 'Frontend', 'Side-project', "Let's code", "ngrx"]
banner = ""
aliases = ['/angular-authentication-service/']
+++

### Introduction

This is part two of **Authentication in Angular** series. This one is about building authentication service to handle OAuth for us. You can find first post here: [https://benetis.me/post/angular-authentication/](/post/angular-authentication/)

We will be using redux with our angular project to help us handle side effects. [https://github.com/ngrx/store](https://github.com/ngrx/store)

### Aims

- After user clicks login - we need to call OAuth endpoint to get `access` and `refresh` tokens which we will store in local storage
- After user clicks logout - we need to clean local storage and remove tokens + reset redux state.
- Create `AuthGuard` to protect routes. Basically we want to redirect unauthenticated user to our login form. Which we will do by checking local storage - if it has specific key defined where we saved our tokens
- If user access token is expired - we want to use refresh token to get new access token. User shouldn't notice he was logged out.
- We want to store tokens in our redux store so they are easily accessible and can be added as headers to our api requests
