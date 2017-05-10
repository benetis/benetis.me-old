+++
date = "2017-05-10T21:40:24+02:00"
draft = false
share = true
title = "Let's code: Authentication in Angular #2 - Auth service"
slug = "angular-authentication-oauth"
tags = ['Angular', 'Frontend', 'Side-project', "Let's code", "ngrx"]
banner = ""
aliases = ['/angular-authentication-oauth/']
+++

### Introduction

This is part two of **Authentication in Angular** series. This one is about building authentication part to handle OAuth calls for us. You can find first post here: [https://benetis.me/post/angular-authentication/](/post/angular-authentication/)

We will be using redux with our angular project to help us handle side effects. [https://github.com/ngrx/store](https://github.com/ngrx/store)

Our setup - angular-cli 1.0 + Angular4 (Angular 4.1)

### Aims

- After user clicks login - we need to call OAuth endpoint to get `access` and `refresh` tokens which we will store in local storage
- Show errors for user
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
  password: string,
  grant_type: 'password', //We set type to password since its not going to change
  client_id: 1 //Same with client_id - it is not going to change. Prevent mistakes at compile time :^)
}
```

Next add variable to hold our login form variable state (info we will submit later)

```typescript
public user: LoginUser = {email: '', password: '', client_id: 1, grant_type: 'password'}
```

`onSubmit` function which is called when user clicks `Login`. As said previously - it dispatches event to login which we will handle later.

```typescript
public onSubmit() {
  this.store.dispatch(new auth.LoginAction({...this.user}))
}
```

### Auth client part

We have `api-client` which handle all api requests to backend and we could add auth routes to it also. Instead - we will create new service just for auth. Reason being - OAuth2 which we are implementing has different responses from our usual api responses (following spec) and we want to isolate them

p.s similar example of what api-client is: [https://github.com/ESNLithuania/boarded/blob/master/src/app/services/request.service.ts](https://github.com/ESNLithuania/boarded/blob/master/src/app/services/request.service.ts)

Basically a service where we wrap our requests to manage them easier.

`auth-client.ts`

```typescript
public oauth(): { login: (LoginUser) => Observable<Response> } {
  return {
    login: (loginUser) => {
      return this
        .post(`oauth/access_token`, loginUser)
    }
  }
}

Also it's important to note that since responses are different - we need to handle errors differently. We grab error description from json response and just leave it for effect to handle.

Handle error function taken from - [https://angular.io/docs/ts/latest/guide/server-communication.html#!#error-handling](https://angular.io/docs/ts/latest/guide/server-communication.html#!#error-handling)

```typescript
private post(url: string
  , objToPost: any): Observable<Response> {

  const headers = new Headers({'Content-Type': 'application/json'});
  const options = new RequestOptions({headers: headers});

  return this.http
    .post(this.url + url, objToPost, options)
    .map(this.extractData)
    .catch(this.handleError)
}

private extractData(res: Response) {
  const body = res.json();
  return body || {};
}

private handleError(error: Response | any) {
  const errMsg = error.json().error_description
  return Observable.throw(errMsg);
}

```

And our effect for doing login looks like this:
```typescript

constructor(private actions$: Actions
   , private authClient: AuthClientService) {
 }

 @Effect()
 loginUser$: Observable<Action> = this.actions$
   .ofType(auth.ActionTypes.LOGIN)
   .map(toPayload)
   .switchMap((payload: LoginUser) => {
     return this
       .authClient
       .oauth()
       .login(payload)
       .map(res => new auth.LoginCompleteAction())
       .catch(err => of(new auth.LoginCompleteAction(err)))
   });

```

We should get access token now after doing request in login form.

### Handling login response

Things to do with response:

- Error -> Show error in login form
- Indicate that request is happening
- Successful -> Put token in LocalStorage & Redirect

#### Error handling

For errors we can create another action: `LoginCompleteErrorAction` to pass error response to reducer and subscribe to error messages for login form.

```typescript
export class LoginCompleteErrorAction implements Action {
  type = ActionTypes.LOGIN_COMPLETE_WITH_ERROR;

  constructor(public payload: string) {
  }
}
```

In our `auth.reducer` we will add few variables in state.

```typescript
import * as login from './auth.actions';

export interface State {
  loginErrMsg: string;
  loginResponseAwaiting: boolean;
  loginSuccessful: boolean;
}

export const initialState: State = {
  loginErrMsg: '',
  loginResponseAwaiting: false,
  loginSuccessful: false
};

export function reducer(state = initialState,
                        action: login.Actions): State {

  switch (action.type) {
    case login.ActionTypes.LOGIN: {
      return {
        ...state,
        loginResponseAwaiting: true
      }
    }
    case login.ActionTypes.LOGIN_COMPLETE_WITH_ERROR: {
      return {
        ...state,
        loginErrMsg: <string>action.payload,
        loginResponseAwaiting: false
      };
    }
    case login.ActionTypes.LOGIN_COMPLETE: {
      return {
        ...state,
        loginResponseAwaiting: false,
        loginSuccessful: true,
        loginErrMsg: ''
      };
    }
    default: {
      return state;
    }
  }
}

export const getLoginErrMsg = (state: State) => state.loginErrMsg
export const getLoginResponseAwaiting = (state: State) => state.loginResponseAwaiting
export const getLoginSuccessful = (state: State) => state.loginSuccessful
```

`login-form.component.html`
```html
<form *ngIf="!(loginResponseAwaiting$ | async)"
      fxLayout="column"
      #loginForm="ngForm"
      (ngSubmit)="onSubmit()"
>
    <anv-alert [active]="errMsg.length > 0">{{errMsg}}</anv-alert>
    <md-input-container fxFlex="100">
        <input mdInput
               name="email"
               type="email"
               [(ngModel)]="user.username"
               required
               validEmail
               placeholder="Email">
        <md-error>Email is invalid</md-error>
    </md-input-container>
    <md-input-container fxFlex="100">
        <input mdInput
               name="password"
               type="password"
               [(ngModel)]="user.password"
               required
               placeholder="Password">
        <md-error>Needs to be at least 8 characters</md-error>
    </md-input-container>
    <div fxFlex="33"
         fxFlexAlign="end">
        <button md-raised-button
                type="submit"
                [disabled]="!loginForm.valid"
        >Login
        </button>
    </div>
</form>
<div *ngIf="loginResponseAwaiting$ | async"
     fxLayoutAlign="center center">
    <md-spinner mode="indeterminate"></md-spinner>
</div>
```

`login-form.component`
```typescript
@Component({
  selector: 'anv-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.scss']
})
export class LoginFormComponent implements OnInit, OnDestroy {

  public user: LoginUser = {username: '', password: '', client_id: 1, grant_type: 'password'}

  public errMsg$: Observable<string>;
  public errMsg: string = '';
  public loginResponseAwaiting$: Observable<boolean>;
  public loginSuccessful$: Observable<boolean>;

  private sub: any;

  constructor(private store: Store<fromRoot.State>) {
    this.loginResponseAwaiting$ = store.select(fromRoot.getLoginResponseAwaiting)
    this.errMsg$ = store.select(fromRoot.getLoginErrMsg)

    this.sub = this.errMsg$.subscribe(_ => this.errMsg)

    this.loginSuccessful$ = store.select(fromRoot.getLoginSuccessful)
  }

  ngOnInit() {
  }

  public onSubmit() {
    this.store.dispatch(new auth.LoginAction({...this.user}))
  }

  ngOnDestroy() {
    this.sub.unsubscribe()
  }
}

```

Response handled. If error - shows it above login form. We can adjust that to our needs in effect or reducer. We also indicate request is happening by showing `<md-spinner>`

#### Saving token

Starting with action to set access token. After we set access token we will need to update our state to have newest token + put in local storage so it can be grabbed later. State update will happen in reducer, as for LocalStorage update - it is a side effect so we will put it in effects.

```typescript
export interface AuthInfo {
  access_token: string,
  expires_in: number
}

private readonly tokenItem = 'token'

@Effect()
loginComplete$: Observable<Action> = this.actions$
  .ofType(auth.ActionTypes.LOGIN_COMPLETE)
  .map(toPayload)
  .switchMap((payload: AuthInfo) => {

    localStorage.setItem(this.tokenItem, JSON.stringify(payload));

    return of(new auth.SetAuthInfoAction({
      authInfo: payload,
      updated: Math.floor(Date.now() / 1000) // To know when token expires
    }))
  })
```

## Summary

There are few more things we need to do for auth to be finished. AuthGuard to protect routes, logout to clean redux state + LocalStorage items, refresh token and minor tweaks, updates.
