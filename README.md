# clj-cljs-starter

A skeleton Clojure web app including an SPA front-end and HTTP API backend.

Includes starter code that implements a very basic task list.


## Getting Started

Hava java and [leiningen](http://leiningen.org/) installed, then...

 - clone
 - `lein repl`
 - `(start)`
 - open browser `http://localhost:8080/`


## Libraries Used

(bolded links you are likely to want to reference, the others are mostly implentation details)

Front-End:

| [*reagent*](https://github.com/reagent-project/reagent) | ui framework (~React)  |
| [*re-frame*](https://github.com/day8/re-frame)          | state management (~Redux) |
| [*tailwind css*](https://tailwindcss.com/)              | css / styling |
| [fetch](https://github.com/lambdaisland/fetch)        | fetch wrapper |
| [accountant](https://github.com/venantius/accountant) | client-side url shenanigans w/ pushstate |

Back-End:

| [*reitit*](https://github.com/metosin/reitit)           | http routing |
| [*hugsql*](https://github.com/layerware/hugsql)         | db fn generation with plain SQL |
| [clip](https://github.com/juxt/clip)                  | system state management |
| [http-kit](https://github.com/http-kit/http-kit)      | http server  |
| [h2](http://h2database.com/html/main.html)            | sql database |
| [girouette](https://github.com/green-coder/girouette) | actual library implementing tailwind css |


## Suggested First Tasks

  - add a button to filter showing completed/incomplete tasks
