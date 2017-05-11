
+++
date = "2017-05-10T23:00:24+02:00"
draft = false
share = true
title = "Let's code: Advanced CRUD in Angular with unit tests"
slug = "angular-advanced-crud"
tags = ['Angular', 'Frontend', 'Side-project', "Let's code"]
banner = ""
aliases = ['/angular-advanced-crud/']
+++

## Introduction

What does word advanced mean? It's just that the CRUD application we are going to implement will have some additional complexity you might expect:

- import/export data
- save/load chunks data
- lazy data processing
- pagination
- tests

This project's source code is in github - [https://github.com/benetis/angular-advanced-crud](https://github.com/benetis/angular-advanced-crud)

## Requirements & Task itself

Aside of the stuff we mentioned above - we obviously will be implementing CRUD operations. Our model on which are going to operate is going to be points (coordinate points `{x, y}`).

- Input points by hand
- Import points from file
- Export points to file
- Clear all points
- Points should be paginated, pagination size can change
- How many squares can be formed from these squares (lazy processing)
- Remove loaded points
- Save chunk of points with specific name (if name exists - overwrite)
- Load chunk of points from saved list
- If something happens (duplicate points ignored...) user needs to be informed
- Delete list of points

We will be using Angular 4.1 with CLI 1.0.1

## Planning UI

Quick mockup on how I imagine this will look like. Probably best starting place for any application which can be componetized.

![](/images/2017/05/points-mock.png)

Clearly we will need a good grid/table component. I have implemented tables so many times in Angular that this time I am just gonna skip it. There is even a post on Angular table by me - [[https://benetis.me/post/angular-table/](/post/angular-table/)

Tabs component to switch between points and squares. (Since squares need pagination) OR just put squares table below.

Action menu for actions to perform on selected rows of table

Validations for inputs to add to the table. We have limits

Research into square finding algorithm - but I guess we can leave that for later.

Import file and upload to table can be plugged anywhere

After retro - I have updated mockup with another possible solution:

![](/images/2017/05/updated-mockup.png)

It doesn't really matter for us - we can decide this later.

## Architecture of code

We have a mockup, requirements. Now we can look at what components, models we will need.

I like to start from data since everything revolves around it. Starting from the bottom.

### Main types

Point and Square.

```typescript
interface Point {x: number, y: number}
// Corner 1, Corner 2
interface Square {c1: Point, c2: Point, c3: Point, c4: Point}

// Self explanatory, just for planning
type ListOfPoints = Point[]
type ListOfSquares = Square[]
```

### Modules, components, services

We can live with one module - app module. No reason to complicate ourselves when task is simple

**Services:**

- import/export data
- IO data transformer
- square-finder
- favorite-points

**Components:**

- table
    - pagination
- action-menu
- delete button
- save-list-button
- replace-list-button
- inputs
- points-table
- squares-table

We might have forgotten something - but for now it seems like everything. Stuff can be added later.

### Notorious table

Certainly table is one of the most popular components (everywhere?). I had a "pleasure" to create two tables (different API) in Angular and this time we will use [http://swimlane.github.io/ngx-datatable](http://swimlane.github.io/ngx-datatable). We will not wrap it inside of our component just for the sake simplicity, however I certainly recommend for you to do so.

To install:
`npm i @swimlane/ngx-datatable --save`

Creating skeleton for points-table using Angular CLI. Everything under `/src/app`

`ng g component points-table`

Compile application. Run tests.

We will need some dummy data, columns. We should think in terms of observables since we will be fetching data from this component directly.

points-table.component.ts
```typescript
public points$: Observable<Point[]> = of([
    {x: 1, y: 2},
    {x: -12, y: 22},
    {x: -2222, y: 4999},
]);
```
points-table.component.html
```html
<h2>Points table</h2>
<ngx-datatable
  [rows]="points$ | async"
  [columns]="[{name:'X'},{name:'Y'}]"
  [limit]="10"
>
</ngx-datatable>
```
And we can already see something:

![](/images/2017/05/table-initial.png)


### Adding a point

Since we will be adding a point - we need a service to handle this for us. We want to subscribe to that service for points to be updated.

Few tests TDD style and we should have basic service for getting points data.

```typescript

@Injectable()
export class PointsServiceService {

    private points: Point[] = [];

    constructor() {
    }

    public getPoints(): Observable<Point[]> {
        return new BehaviorSubject(this.points);
    }

    public addPoints(pointsToAdd): Observable<Point[]> {
        this.points = this.points.concat(pointsToAdd)
        return this.getPoints()
    }

}
```

and their tests:

```typescript
it('should return empty observable when called getPoints', done => {
    service.getPoints().subscribe(p => {
        expect(p).toEqual([]);
        done();
    })
})

it('should add points and return all points', done => {
    const pointsToAdd = [
        {x: 1, y: 1},
        {x: 0, y: 1},
    ]

    service.addPoints(pointsToAdd).subscribe(p => {
        expect(p).toEqual(pointsToAdd);
        done();
    })
})

it('should add points twice and return all points', done => {
    const pointsToAdd = [
        {x: 1, y: 1},
        {x: 0, y: 1},
    ]

    service.addPoints(pointsToAdd)
    service.addPoints(pointsToAdd)

    service.getPoints().subscribe(p => {
        expect(p).toEqual([...pointsToAdd, ...pointsToAdd]);
        done();
    })
})
```

You can find these files in github - [https://github.com/benetis/angular-advanced-crud/blob/master/src/app/points-service.service.ts](https://github.com/benetis/angular-advanced-crud/blob/master/src/app/points-service.service.ts)
