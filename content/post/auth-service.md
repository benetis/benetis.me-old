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

Our setup - angular-cli 1.0 + Angular4 (Angular 4.1)

### Aims

- After user clicks login - we need to call OAuth endpoint to get `access` and `refresh` tokens which we will store in local storage
- After user clicks logout - we need to clean local storage and remove tokens + reset redux state.
- Create `AuthGuard` to protect routes. Basically we want to redirect unauthenticated user to our login form. Which we will do by checking local storage - if it has specific key defined where we saved our tokens
- If user access token is expired - we want to use refresh token to get new access token. User shouldn't notice he was logged out.
- We want to store tokens in our redux store so they are easily accessible and can be added as headers to our api requests

### Login

Ah, the login. Grabbing the access token with your username and password.

#### Security concerns
We are going to save that token in local storage. Although for security purposes it should end up in cookies with `httpOnly` and `secure` flags. It is all because of XSS. If javascript can access token - attacker can do that also. Read more here - [https://auth0.com/blog/cookies-vs-tokens-definitive-guide/](https://auth0.com/blog/cookies-vs-tokens-definitive-guide/)

#### Ngrx

Creating folder named `classes` under `app/` to hold `auth.reducer, auth.effects and auth.actions`. I keep reducers and actions close to the module they belong too, although effects need to be imported in root module.

We will need two actions for login. One will be dispatched when user clicks login and another after we get response from server.

```typescript
LOGIN: type('[Auth] Login'),
LOGIN_COMPLETE: type('[Auth] Login complete'),
```

Just a basic skeleton for now, no business logic. We will come back in a sec.

#### Login events

Upon clicking login we will dispatch event and show response to the user. If error - we will display error message from response. (`either invalid username/password` or `too many attempts`)

So from last blog we have this `login-form.component`. Let's update!

I like to start from models. First let's create interface of "LoginUser" and call it exactly that.

> Do not try to generalize User interface here. It will be hard to manage optional parameters. Just create few. Don't be afraid to have `LoginUser`, `RegisterUser` and `ProfileUser` interfaces.

```typescript
export interface LoginUser {
  email: string,
  password: string
}
```

Next add variable to hold our login form variable state (info we will submit later)

```typescript
public user: LoginUser = {email: '', password: ''}
```

`onSubmit` function which is called when user clicks `Login`. As said previously - it dispatches event to login which we will handle later.

```typescript
public onSubmit() {
  this.store.dispatch(new auth.LoginAction({...this.user}))
}
```

### Auth client

We have `api-client` which handle all api requests to backend and we could add auth routes to it also. However, this is a great opportunity to split them into separate client (Separation of concerns!). All those auth routes fall onto one route.
