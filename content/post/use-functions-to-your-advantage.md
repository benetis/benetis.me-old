
+++
date = "2016-11-12"
draft = false
title = "Use functions to your advantage"
slug = "use-functions-to-your-advantage"
tags = []
banner = ""
aliases = ['/use-functions-to-your-advantage/']
+++

##### Intro 
Looking on how to write better code? One way to do that would be to use functions more than variables. You want to define some variable which returns some data? Define it as a function!

##### Example
Let's say we columns array:

```typescript
  public columns: Array<Column> = [
      {
        title: 'Name',
        name: 'name',
        sort: 'desc',
        type: 'text'
      },
      {
        title: 'Surname',
        name: 'surname',
        type: 'text'
      },
      {
        title: 'Section',
        name: 'section',
        type: 'select',
        data: this.sections
      },
      {
        title: 'Role',
        name: 'role',
        type: 'select',
        data: this.roles
      }
  ];
```

Note on `data` property. It binds to this.roles. But when does it do that? On initialisation. What if this.roles fills after some time (Promise is resolved) - well that would leave us with empty hands. (Or with `cannot read property name of undefined` or similar result)

Let's do a quick upgrade!

```typescript
  public columns(): Array<Column> {
    return [
      {
        title: 'Name',
        name: 'name',
        sort: 'desc',
        type: 'text'
      },
      {
        title: 'Surname',
        name: 'surname',
        type: 'text'
      },
      {
        title: 'Section',
        name: 'section',
        type: 'select',
        data: this.sections
      },
      {
        title: 'Role',
        name: 'role',
        type: 'select',
        data: this.roles
      }
    ];
  }
```

What actually changed? We just declared a function which returns an array. Same as before? You will be surprised.

Now if we call our function `columns()` we get same thing we got before. It's just that `data` property will be assigned on call - so our snippet has already became _lazy_ - which is good enough for us.

Now while this is not the best example since we are using `this.` calls - there is a nice way to avoid that - but for sake of short post - let's skip it for now.


##### Summary
Pros of using functions more

* We calculate what we need on fly (lazy)
* Easier to refactor function to add new data transformations (explicit transformations)



